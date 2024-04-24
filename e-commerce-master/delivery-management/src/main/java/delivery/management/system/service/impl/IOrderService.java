package delivery.management.system.service.impl;

import common.exception.model.exception.ApplicationException;
import common.exception.model.service.ExceptionService;
import delivery.management.system.helper.DriverServiceHelper;
import delivery.management.system.mapper.OrderMapper;
import delivery.management.system.model.dto.request.PaymentRequestDto;
import delivery.management.system.model.dto.response.OrderDashboardDto;
import delivery.management.system.model.dto.response.OrderResponseDto;
import delivery.management.system.model.dto.response.RouteResponseDto;
import delivery.management.system.model.entity.*;
import delivery.management.system.model.enums.Exceptions;
import delivery.management.system.model.enums.OrderStatusEnums;
import delivery.management.system.repository.CartItemRepository;
import delivery.management.system.repository.OrderRepository;
import delivery.management.system.repository.OrderStatusRepository;
import delivery.management.system.service.*;
import delivery.management.system.util.TimeCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IOrderService implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final ExceptionService exceptionService;
    private final CompanyService companyService;
    private final MapService mapService;
    private final DriverServiceHelper driverServiceHelper;
    private final CartItemRepository cartItemRepository;
    private final TimeCalculator timeCalculator;
    private final OrderMapper orderMapper;
    private final AuthService authService;
    private final OrderStatusRepository orderStatusRepository;

    @Override
    public OrderDashboardDto profitByDate(LocalDateTime localDateTime) {

        List<Order> orders = orderRepository.findOrdersByDetailsCreatedAtProfit(localDateTime);

        BigDecimal sum = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OrderDashboardDto.builder()
                .count(orders.size())
                .sum(sum)
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<Void> order(User user, PaymentRequestDto paymentRequest) {
        Cart cart = cartService.getCartByUser(user);
        log.error("{}", cart);
        List<CartItem> cartItems = cart.getCartItems();
        log.error("{}", cart);

        boolean paymentIsCheck = true;
        if (check(cartItems)) {
            if (paymentIsCheck) {
                Company company = companyService.increaseCompanyBalance(cart.getTotalAmount());
                log.error("{}", company);
                // Fixme
                RouteResponseDto time = mapService.calculateRoute(company.getLocation(), paymentRequest.getDestination(), "driving");
                log.error("{}", time);
                LocalDateTime deliveryTime = timeCalculator.durationTime(time.getDuration());
                log.error("{}", deliveryTime);
                List<OrderItem> orderItems = buildOrderItem(cartItems);
                TableDetails tableDetails = new TableDetails();
                Driver driver = driverServiceHelper.driverSelection();
                Order order = buildOrder(orderItems, tableDetails, cart, paymentRequest.getDestination(), deliveryTime, driver);
                productService.decreaseProductCount(cartItems);
                orderRepository.save(order);

                return ResponseEntity.noContent().build();
            } else {
                throw new ApplicationException(exceptionService.exceptionResponse(Exceptions.PAYMENT_INSUFFICIENT.getMessage(), HttpStatus.BAD_REQUEST));}
        } else {
            throw new ApplicationException(exceptionService.exceptionResponse(Exceptions.PRODUCT_QUANTITY_INSUFFICIENT.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }

    @Override
    public ResponseEntity<List<OrderResponseDto>> findALL() {
        List<OrderResponseDto> orders = orderRepository.findAll().stream()
                .map(orderMapper::map)
                .toList();
        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<OrderResponseDto> findById(long orderId) {
        OrderResponseDto orderResponse = orderMapper.map(getById(orderId));
        return ResponseEntity.ok(orderResponse);
    }

    @Override
    public ResponseEntity<List<OrderResponseDto>> findAllCustomerOrder() {
        User user = authService.getAuthenticatedUser();
        List<OrderResponseDto> orders = orderRepository.findOrdersByCart_User(user).stream()
                .map(orderMapper::map)
                .toList();
        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<OrderResponseDto> findOrder() {
        User user = authService.getAuthenticatedUser();
        return ResponseEntity.ok(orderMapper.map(getCurrentOrderByDriver(user)));
    }

    @Override
    @Transactional
    public void orderDelivered() {
        User user = authService.getAuthenticatedUser();
        Order order = getOrderByDriver(user);
        OrderStatus orderStatus = getOrderStatusByName(OrderStatusEnums.ORDER_DELIVERED);
        order.setOrderStatus(orderStatus);
    }

    private Order getOrderByDriver(User user) {
        OrderStatus orderStatusByName = getOrderStatusByName(OrderStatusEnums.DELIVERY);
//        return orderRepository.findOrdersByDriver_User(user)
//                .orElseThrow(() -> new ApplicationException(exceptionService.exceptionResponse("exception.not.found", HttpStatus.NOT_FOUND)));
        return orderRepository.findOrdersByDriver_User(user).stream()
                .filter(order -> order.getOrderStatus().equals(orderStatusByName))
                .findAny()
                .orElseThrow(() -> new ApplicationException(exceptionService.exceptionResponse(Exceptions.ORDER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND)));
    }


    private Order getCurrentOrderByDriver(User user) {
        return orderRepository.findOrdersByDriver_User(user)
                .stream()
                .max(Comparator.comparing(order -> order.getDetails().getCreatedAt()))
                .orElseThrow(() -> new ApplicationException(exceptionService.exceptionResponse(Exceptions.ORDER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND)));
    }



    private OrderStatus getOrderStatusByName(OrderStatusEnums orderStatusEnums) {
        return orderStatusRepository.findByName(orderStatusEnums)
                .orElseThrow(() -> new ApplicationException(exceptionService.exceptionResponse(Exceptions.NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND)));
    }


    private Order getById(long id) {
        return orderRepository.findById(id)
                .orElseThrow(()-> new ApplicationException(exceptionService.exceptionResponse(Exceptions.ORDER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND)));
    }


    private Order buildOrder(List<OrderItem> orderItems, TableDetails tableDetails, Cart cart, String destination, LocalDateTime deliveryTime, Driver driver) {

         OrderStatus orderStatus = orderStatusRepository.findByName(OrderStatusEnums.DELIVERY)
                 .orElseThrow(()-> new ApplicationException(exceptionService.exceptionResponse(Exceptions.NOT_FOUND.getMessage(),HttpStatus.NOT_FOUND)));
        return Order.builder()
                .totalAmount(cart.getTotalAmount())
                .orderItems(orderItems)
                .place(destination)
                .cart(cart)
                .deliveryTime(deliveryTime)
                .details(tableDetails)
                .driver(driver)
                .orderStatus(orderStatus)
                .build();
    }

    private boolean check(List<CartItem> cartItems) {
        boolean isTrue = false;

        for (CartItem cartItem : cartItems) {
            Product product = productService.getById(cartItem.getProduct().getId());
            if (cartItem.getCount() <= product.getCount()) {
                isTrue = true;
            } else {
                return false;
            }
        }
        return isTrue;
    }

    private List<OrderItem> buildOrderItem(List<CartItem> cartItems) {
        List<OrderItem> orderItems = new LinkedList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .count(cartItem.getCount())
                    .product(cartItem.getProduct())
                    .total_amount(cartItem.getTotalAmount())
                    .build();

            orderItems.add(orderItem);
            cartItemRepository.delete(cartItem);
        }
        return orderItems;
    }
}