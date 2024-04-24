package delivery.management.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import common.exception.model.dto.response.ExceptionResponse;
import common.exception.model.exception.ApplicationException;
import common.exception.model.service.ExceptionService;
import delivery.management.system.mapper.CartMapper;
import delivery.management.system.model.dto.response.CartResponseDto;
import delivery.management.system.model.entity.Cart;
import delivery.management.system.model.entity.CartItem;
import delivery.management.system.model.entity.Product;
import delivery.management.system.model.entity.Role;
import delivery.management.system.model.entity.User;
import delivery.management.system.repository.CartItemRepository;
import delivery.management.system.repository.CartRepository;
import delivery.management.system.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ICartService.class})
@ExtendWith(SpringExtension.class)
class ICartServiceTest {
    @MockBean
    private CartItemRepository cartItemRepository;

    @MockBean
    private CartMapper cartMapper;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private ExceptionService exceptionService;

    @Autowired
    private ICartService iCartService;

    @MockBean
    private ProductService productService;


    @Test
    void testAddProductToCart() {
        // Arrange
        Product product = new Product();
        product.setCategories(new ArrayList<>());
        product.setCount(3L);
        product.setDescription("The characteristics of someone or something");
        product.setId(1L);
        product.setImages(new ArrayList<>());
        product.setName("Name");
        product.setPrice(new BigDecimal("2.3"));
        product.setStatus(true);
        when(productService.getById(anyLong())).thenReturn(product);

        Role role = new Role();
        role.setId(1L);
        role.setName("Name");
        role.setPermission(new ArrayList<>());

        User user = new User();
        user.setBirthdate(LocalDate.of(1970, 1, 1));
        user.setEmail("jane.doe@example.org");
        user.setEnable(true);
        user.setId(1L);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPhoneNumber("6625550144");
        user.setRole(role);
        user.setSurname("Doe");

        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        cart.setCount(3);
        cart.setId(1L);
        cart.setTotalAmount(new BigDecimal("2.3"));
        cart.setUser(user);
        Optional<Cart> ofResult = Optional.of(cart);
        when(cartRepository.findByUser(Mockito.<User>any())).thenReturn(ofResult);

        Role role2 = new Role();
        role2.setId(1L);
        role2.setName("Name");
        role2.setPermission(new ArrayList<>());

        User user2 = new User();
        user2.setBirthdate(LocalDate.of(1970, 1, 1));
        user2.setEmail("jane.doe@example.org");
        user2.setEnable(true);
        user2.setId(1L);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setPhoneNumber("6625550144");
        user2.setRole(role2);
        user2.setSurname("Doe");

        Cart cart2 = new Cart();
        cart2.setCartItems(new ArrayList<>());
        cart2.setCount(3);
        cart2.setId(1L);
        cart2.setTotalAmount(new BigDecimal("2.3"));
        cart2.setUser(user2);

        Product product2 = new Product();
        product2.setCategories(new ArrayList<>());
        product2.setCount(3L);
        product2.setDescription("The characteristics of someone or something");
        product2.setId(1L);
        product2.setImages(new ArrayList<>());
        product2.setName("Name");
        product2.setPrice(new BigDecimal("2.3"));
        product2.setStatus(true);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart2);
        cartItem.setCount(3);
        cartItem.setId(1L);
        cartItem.setProduct(product2);
        cartItem.setTotalAmount(new BigDecimal("2.3"));
        when(cartItemRepository.save(Mockito.<CartItem>any())).thenReturn(cartItem);

        Role role3 = new Role();
        role3.setId(1L);
        role3.setName("Name");
        role3.setPermission(new ArrayList<>());

        User user3 = new User();
        user3.setBirthdate(LocalDate.of(1970, 1, 1));
        user3.setEmail("jane.doe@example.org");
        user3.setEnable(true);
        user3.setId(1L);
        user3.setName("Name");
        user3.setPassword("iloveyou");
        user3.setPhoneNumber("6625550144");
        user3.setRole(role3);
        user3.setSurname("Doe");

        // Act
        iCartService.addProductToCart(1L, user3, 3);

        // Assert
        verify(cartRepository).findByUser(Mockito.<User>any());
        verify(productService).getById(eq(1L));
        verify(cartItemRepository).save(Mockito.<CartItem>any());
    }

    @Test
    void testGetProductToCart() {
        // Arrange
        Role role = new Role();
        role.setId(1L);
        role.setName("Name");
        role.setPermission(new ArrayList<>());

        User user = new User();
        user.setBirthdate(LocalDate.of(1970, 1, 1));
        user.setEmail("jane.doe@example.org");
        user.setEnable(true);
        user.setId(1L);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPhoneNumber("6625550144");
        user.setRole(role);
        user.setSurname("Doe");

        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        cart.setCount(3);
        cart.setId(1L);
        cart.setTotalAmount(new BigDecimal("2.3"));
        cart.setUser(user);
        Optional<Cart> ofResult = Optional.of(cart);
        when(cartRepository.findByUser(Mockito.<User>any())).thenReturn(ofResult);
        CartResponseDto.CartResponseDtoBuilder builderResult = CartResponseDto.builder();
        CartResponseDto.CartResponseDtoBuilder idResult = builderResult.cartItems(new ArrayList<>()).count(3).id(1L);
        CartResponseDto buildResult = idResult.totalAmount(new BigDecimal("2.3")).build();
        when(cartMapper.map(Mockito.<Cart>any())).thenReturn(buildResult);

        Role role2 = new Role();
        role2.setId(1L);
        role2.setName("Name");
        role2.setPermission(new ArrayList<>());

        User user2 = new User();
        user2.setBirthdate(LocalDate.of(1970, 1, 1));
        user2.setEmail("jane.doe@example.org");
        user2.setEnable(true);
        user2.setId(1L);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setPhoneNumber("6625550144");
        user2.setRole(role2);
        user2.setSurname("Doe");

        // Act
        ResponseEntity<CartResponseDto> actualProductToCart = iCartService.getProductToCart(user2);

        // Assert
        verify(cartMapper).map(Mockito.<Cart>any());
        verify(cartRepository).findByUser(Mockito.<User>any());
        assertEquals(200, actualProductToCart.getStatusCodeValue());
        assertTrue(actualProductToCart.hasBody());
        assertTrue(actualProductToCart.getHeaders().isEmpty());
    }


    @Test
    void testDeleteProductToCart() {
        // Arrange
        Role role = new Role();
        role.setId(1L);
        role.setName("Name");
        role.setPermission(new ArrayList<>());

        User user = new User();
        user.setBirthdate(LocalDate.of(1970, 1, 1));
        user.setEmail("jane.doe@example.org");
        user.setEnable(true);
        user.setId(1L);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPhoneNumber("6625550144");
        user.setRole(role);
        user.setSurname("Doe");

        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        cart.setCount(3);
        cart.setId(1L);
        cart.setTotalAmount(new BigDecimal("2.3"));
        cart.setUser(user);
        Optional<Cart> ofResult = Optional.of(cart);
        when(cartRepository.findByUser(Mockito.<User>any())).thenReturn(ofResult);
        ExceptionResponse buildResult = ExceptionResponse.builder()
                .httpStatus(HttpStatus.CONTINUE)
                .message("An error occurred")
                .build();
        when(exceptionService.exceptionResponse(Mockito.<String>any(), Mockito.<HttpStatus>any())).thenReturn(buildResult);

        Role role2 = new Role();
        role2.setId(1L);
        role2.setName("Name");
        role2.setPermission(new ArrayList<>());

        User user2 = new User();
        user2.setBirthdate(LocalDate.of(1970, 1, 1));
        user2.setEmail("jane.doe@example.org");
        user2.setEnable(true);
        user2.setId(1L);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setPhoneNumber("6625550144");
        user2.setRole(role2);
        user2.setSurname("Doe");

        // Act and Assert
        assertThrows(ApplicationException.class, () -> iCartService.deleteProductToCart(user2, 1L));
        verify(exceptionService).exceptionResponse(eq("exception.not.found"), eq(HttpStatus.NOT_FOUND));
        verify(cartRepository).findByUser(Mockito.<User>any());
    }



    @Test
    void testGetCartByUser() {

        Role role = new Role();
        role.setId(1L);
        role.setName("Name");
        role.setPermission(new ArrayList<>());

        User user = new User();
        user.setBirthdate(LocalDate.of(1970, 1, 1));
        user.setEmail("jane.doe@example.org");
        user.setEnable(true);
        user.setId(1L);
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setPhoneNumber("6625550144");
        user.setRole(role);
        user.setSurname("Doe");

        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        cart.setCount(3);
        cart.setId(1L);
        cart.setTotalAmount(new BigDecimal("2.3"));
        cart.setUser(user);
        Optional<Cart> ofResult = Optional.of(cart);
        when(cartRepository.findByUser(Mockito.<User>any())).thenReturn(ofResult);

        Role role2 = new Role();
        role2.setId(1L);
        role2.setName("Name");
        role2.setPermission(new ArrayList<>());

        User user2 = new User();
        user2.setBirthdate(LocalDate.of(1970, 1, 1));
        user2.setEmail("jane.doe@example.org");
        user2.setEnable(true);
        user2.setId(1L);
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setPhoneNumber("6625550144");
        user2.setRole(role2);
        user2.setSurname("Doe");

        Cart actualCartByUser = iCartService.getCartByUser(user2);

        verify(cartRepository).findByUser(Mockito.<User>any());
        assertSame(cart, actualCartByUser);
    }



}
