package delivery.management.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import common.exception.model.dto.response.ExceptionResponse;
import common.exception.model.exception.ApplicationException;
import common.exception.model.service.ExceptionService;
import delivery.management.system.mapper.UserMapper;
import delivery.management.system.model.dto.request.PasswordRequestDto;
import delivery.management.system.model.dto.request.UserRequestDto;
import delivery.management.system.model.entity.Otp;
import delivery.management.system.model.entity.Role;
import delivery.management.system.model.entity.User;
import delivery.management.system.repository.UserRepository;
import delivery.management.system.service.CartService;
import delivery.management.system.service.OtpService;
import delivery.management.system.service.RoleService;
import delivery.management.system.service.TokenService;
import delivery.management.system.util.MessageUtil;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {IUserService.class})
@ExtendWith(SpringExtension.class)
class IUserServiceTest {
    @MockBean
    private CartService cartService;

    @MockBean
    private ExceptionService exceptionService;

    @Autowired
    private IUserService iUserService;

    @MockBean
    private MessageUtil messageUtil;

    @MockBean
    private OtpService otpService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RoleService roleService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserRepository userRepository;


    @Test
    void testRegistration() {
        // Arrange
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

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
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);

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
        when(userMapper.map(Mockito.<UserRequestDto>any())).thenReturn(user2);
        doNothing().when(tokenService).confirm(Mockito.<User>any());

        Role role3 = new Role();
        role3.setId(1L);
        role3.setName("Name");
        role3.setPermission(new ArrayList<>());
        when(roleService.findByRole(Mockito.<String>any())).thenReturn(role3);
        doNothing().when(cartService).saveCart(Mockito.<User>any());

        // Act
        ResponseEntity<Void> actualRegistrationResult = iUserService.registration(new UserRequestDto());

        // Assert
        verify(userMapper).map(Mockito.<UserRequestDto>any());
        verify(cartService).saveCart(Mockito.<User>any());
        verify(roleService).findByRole(eq("CUSTOMER"));
        verify(tokenService).confirm(Mockito.<User>any());
        verify(userRepository).save(Mockito.<User>any());
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
        assertNull(actualRegistrationResult.getBody());
        assertEquals(204, actualRegistrationResult.getStatusCodeValue());
        assertTrue(actualRegistrationResult.getHeaders().isEmpty());
    }

    @Test
    void testConfirmation() {
        // Arrange
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .httpStatus(HttpStatus.CONTINUE)
                .message("An error occurred")
                .build();
        when(tokenService.getByToken(Mockito.<String>any())).thenThrow(new ApplicationException(exceptionResponse));

        // Act and Assert
        assertThrows(ApplicationException.class, () -> iUserService.confirmation("ABC123"));
        verify(tokenService).getByToken(eq("ABC123"));
    }


    @Test
    void testUserRenewPassword() {
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
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        doNothing().when(otpService).createOtp(Mockito.<User>any());
        when(messageUtil.getMessages(Mockito.<String>any(), isA(Object[].class))).thenReturn("Messages");

        // Act
        ResponseEntity<String> actualUserRenewPasswordResult = iUserService.userRenewPassword("janedoe");

        // Assert
        verify(userRepository).findByEmail(eq("janedoe"));
        verify(otpService).createOtp(Mockito.<User>any());
        verify(messageUtil).getMessages(eq("SEND_EMAIL_SUCCESSFULLY"), isA(Object[].class));
        assertEquals("Messages", actualUserRenewPasswordResult.getBody());
        assertEquals(200, actualUserRenewPasswordResult.getStatusCodeValue());
        assertTrue(actualUserRenewPasswordResult.getHeaders().isEmpty());
    }


    @Test
    void testResetPassword() {
        // Arrange
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

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

        Otp otp = new Otp();
        otp.setConfirm(true);
        otp.setExpired(LocalDate.of(1970, 1, 1).atStartOfDay());
        otp.setId(1L);
        otp.setOtp(1);
        otp.setUser(user);
        when(otpService.findByCheckOtp(Mockito.<String>any(), anyInt())).thenReturn(otp);
        when(messageUtil.getMessages(Mockito.<String>any(), isA(Object[].class))).thenReturn("Messages");

        // Act
        ResponseEntity<String> actualResetPasswordResult = iUserService.resetPassword("janedoe", 1,
                new PasswordRequestDto("iloveyou", "iloveyou"));

        // Assert
        verify(otpService).findByCheckOtp(eq("janedoe"), eq(1));
        verify(messageUtil).getMessages(eq("RESET_PASSWORD_SUCCESSFULLY"), isA(Object[].class));
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
        assertEquals("Messages", actualResetPasswordResult.getBody());
        assertEquals(200, actualResetPasswordResult.getStatusCodeValue());
        assertTrue(actualResetPasswordResult.getHeaders().isEmpty());
    }


    @Test
    void testFindByUsername() {
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
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        ResponseEntity<User> actualFindByUsernameResult = iUserService.findByUsername("janedoe");

        // Assert
        verify(userRepository).findByEmail(eq("janedoe"));
        assertEquals(200, actualFindByUsernameResult.getStatusCodeValue());
        assertTrue(actualFindByUsernameResult.hasBody());
        assertTrue(actualFindByUsernameResult.getHeaders().isEmpty());
    }


    @Test
    void testCustomerCount() {
        // Arrange
        when(userRepository.findUsersByRole_Name(Mockito.<String>any())).thenReturn(new ArrayList<>());

        // Act
        int actualCustomerCountResult = iUserService.customerCount();

        // Assert
        verify(userRepository).findUsersByRole_Name(eq("CUSTOMER"));
        assertEquals(0, actualCustomerCountResult);
    }

    @Test
    void testDriverCount() {
        // Arrange
        when(userRepository.findUsersByRole_Name(Mockito.<String>any())).thenReturn(new ArrayList<>());

        // Act
        int actualDriverCountResult = iUserService.driverCount();

        // Assert
        verify(userRepository).findUsersByRole_Name(eq("DRIVER"));
        assertEquals(0, actualDriverCountResult);
    }

}
