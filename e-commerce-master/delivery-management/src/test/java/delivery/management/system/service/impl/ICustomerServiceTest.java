package delivery.management.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import common.exception.model.dto.response.ExceptionResponse;
import common.exception.model.exception.ApplicationException;
import common.exception.model.service.ExceptionService;
import delivery.management.system.mapper.UserMapper;
import delivery.management.system.model.dto.request.PaymentRequestDto;
import delivery.management.system.model.dto.response.CartResponseDto;
import delivery.management.system.model.dto.response.UserResponseDto;
import delivery.management.system.model.entity.Role;
import delivery.management.system.model.entity.User;
import delivery.management.system.repository.UserRepository;
import delivery.management.system.service.AuthService;
import delivery.management.system.service.CartService;
import delivery.management.system.service.OrderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

@ContextConfiguration(classes = {ICustomerService.class})
@ExtendWith(SpringExtension.class)
class ICustomerServiceTest {
    @MockBean
    private AuthService authService;

    @MockBean
    private CartService cartService;

    @MockBean
    private ExceptionService exceptionService;

    @Autowired
    private ICustomerService iCustomerService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserRepository userRepository;


    @Test
    void testFindAll() {
        // Arrange
        when(userRepository.findUsersByRole_Name(Mockito.<String>any())).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<UserResponseDto>> actualFindAllResult = iCustomerService.findAll();

        // Assert
        verify(userRepository).findUsersByRole_Name(eq("CUSTOMER"));
        assertEquals(200, actualFindAllResult.getStatusCodeValue());
        assertTrue(actualFindAllResult.hasBody());
        assertTrue(actualFindAllResult.getHeaders().isEmpty());
    }


    @Test
    void testFindById() {
        // Arrange
        UserResponseDto.UserResponseDtoBuilder builderResult = UserResponseDto.builder();
        UserResponseDto buildResult = builderResult.birthdate(LocalDate.of(1970, 1, 1))
                .email("jane.doe@example.org")
                .id(1L)
                .name("Name")
                .password("iloveyou")
                .phoneNumber(1L)
                .surname("Doe")
                .build();
        when(userMapper.map(Mockito.<User>any())).thenReturn(buildResult);

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
        when(authService.getAuthenticatedUser()).thenReturn(user);

        // Act
        ResponseEntity<UserResponseDto> actualFindByIdResult = iCustomerService.findById();

        // Assert
        verify(userMapper).map(Mockito.<User>any());
        verify(authService).getAuthenticatedUser();
        assertEquals(200, actualFindByIdResult.getStatusCodeValue());
        assertTrue(actualFindByIdResult.hasBody());
        assertTrue(actualFindByIdResult.getHeaders().isEmpty());
    }


    @Test
    void testDelete() {
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
        Optional<User> ofResult = Optional.of(user);

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
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        ResponseEntity<Void> actualDeleteResult = iCustomerService.delete(1L);

        // Assert
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userRepository).save(Mockito.<User>any());
        assertNull(actualDeleteResult.getBody());
        assertEquals(204, actualDeleteResult.getStatusCodeValue());
        assertTrue(actualDeleteResult.getHeaders().isEmpty());
    }


    @Test
    void testAddProduct() {
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
        when(authService.getAuthenticatedUser()).thenReturn(user);
        doNothing().when(cartService).addProductToCart(anyLong(), Mockito.<User>any(), anyInt());

        // Act
        ResponseEntity<Void> actualAddProductResult = iCustomerService.addProduct(1L, 3);

        // Assert
        verify(authService).getAuthenticatedUser();
        verify(cartService).addProductToCart(eq(1L), Mockito.<User>any(), eq(3));
        assertNull(actualAddProductResult.getBody());
        assertEquals(204, actualAddProductResult.getStatusCodeValue());
        assertTrue(actualAddProductResult.getHeaders().isEmpty());
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
        when(authService.getAuthenticatedUser()).thenReturn(user);
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .httpStatus(HttpStatus.CONTINUE)
                .message("An error occurred")
                .build();
        when(cartService.getProductToCart(Mockito.<User>any())).thenThrow(new ApplicationException(exceptionResponse));

        // Act and Assert
        assertThrows(ApplicationException.class, () -> iCustomerService.getProductToCart());
        verify(authService).getAuthenticatedUser();
        verify(cartService).getProductToCart(Mockito.<User>any());
    }




    @Test
    void testBuyProductFromCart() {
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
        when(authService.getAuthenticatedUser()).thenReturn(user);
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .httpStatus(HttpStatus.CONTINUE)
                .message("An error occurred")
                .build();
        when(orderService.order(Mockito.<User>any(), Mockito.<PaymentRequestDto>any()))
                .thenThrow(new ApplicationException(exceptionResponse));

        // Act and Assert
        assertThrows(ApplicationException.class,
                () -> iCustomerService.buyProductFromCart(new PaymentRequestDto("Destination", "42", "Cvv")));
        verify(authService).getAuthenticatedUser();
        verify(orderService).order(Mockito.<User>any(), Mockito.<PaymentRequestDto>any());
    }
}
