package delivery.management.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import common.exception.model.exception.ApplicationException;
import common.exception.model.service.ExceptionService;
import delivery.management.system.model.dao.DaoDetailsAuthenticationProvider;
import delivery.management.system.model.dto.request.AuthRequestDto;
import delivery.management.system.model.entity.RefreshToken;
import delivery.management.system.model.entity.Role;
import delivery.management.system.model.entity.TableDetails;
import delivery.management.system.model.entity.User;
import delivery.management.system.repository.RefreshTokenRepository;
import delivery.management.system.repository.UserRepository;
import delivery.management.system.service.JwtService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {IAuthService.class})
@ExtendWith(SpringExtension.class)
class IAuthServiceTest {
    @MockBean
    private ExceptionService exceptionService;

    @Autowired
    private IAuthService iAuthService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @MockBean
    private UserRepository userRepository;


    @Test
    @Disabled("TODO: Complete this test")
    void testAuthentication() {



        iAuthService.authentication(new AuthRequestDto("janedoe", "iloveyou"));
    }

    @Test
    void testAccessTokensByRefreshToken() {


        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new DaoDetailsAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);

        TableDetails tableDetails = new TableDetails();
        tableDetails.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tableDetails.setId(1L);
        tableDetails.setStatus(true);
        tableDetails.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

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

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(1L);
        refreshToken.setLifeTime(1L);
        refreshToken.setName("Name");
        refreshToken.setRevoked(true);
        refreshToken.setTableDetails(tableDetails);
        refreshToken.setUser(user);
        Optional<RefreshToken> ofResult = Optional.of(refreshToken);
        RefreshTokenRepository refreshTokenRepository = mock(RefreshTokenRepository.class);
        when(refreshTokenRepository.findByNameAndRevoked(Mockito.<String>any(), anyBoolean())).thenReturn(ofResult);
        IJwtService jwtService = new IJwtService();
        UserRepository userRepository = mock(UserRepository.class);


        assertThrows(ApplicationException.class, () -> (new IAuthService(authenticationManager, jwtService, userRepository,
                refreshTokenRepository, new ExceptionService())).accessTokensByRefreshToken("ABC123"));
        verify(refreshTokenRepository).findByNameAndRevoked(eq("ABC123"), eq(false));
    }




    @Test
    void testIfNotExistCreate() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new DaoDetailsAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);

        TableDetails tableDetails = new TableDetails();
        tableDetails.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tableDetails.setId(1L);
        tableDetails.setStatus(true);
        tableDetails.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

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

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(1L);
        refreshToken.setLifeTime(1L);
        refreshToken.setName("Name");
        refreshToken.setRevoked(true);
        refreshToken.setTableDetails(tableDetails);
        refreshToken.setUser(user);
        RefreshTokenRepository refreshTokenRepository = mock(RefreshTokenRepository.class);
        when(refreshTokenRepository.save(Mockito.<RefreshToken>any())).thenReturn(refreshToken);
        IJwtService jwtService = new IJwtService();
        UserRepository userRepository = mock(UserRepository.class);
        IAuthService iAuthService = new IAuthService(authenticationManager, jwtService, userRepository,
                refreshTokenRepository, new ExceptionService());

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
        iAuthService.ifNotExistCreate(user2, "01234567-89AB-CDEF-FEDC-BA9876543210");

        // Assert
        verify(refreshTokenRepository).save(Mockito.<RefreshToken>any());
    }


    /**
     * Method under test:
     * {@link IAuthService#ifHasExistUpdate(User, RefreshToken, String)}
     */
    @Test
    void testIfHasExistUpdate() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new DaoDetailsAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);

        TableDetails tableDetails = new TableDetails();
        tableDetails.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tableDetails.setId(1L);
        tableDetails.setStatus(true);
        tableDetails.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

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

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(1L);
        refreshToken.setLifeTime(1L);
        refreshToken.setName("Name");
        refreshToken.setRevoked(true);
        refreshToken.setTableDetails(tableDetails);
        refreshToken.setUser(user);
        RefreshTokenRepository refreshTokenRepository = mock(RefreshTokenRepository.class);
        when(refreshTokenRepository.save(Mockito.<RefreshToken>any())).thenReturn(refreshToken);
        IJwtService jwtService = new IJwtService();
        UserRepository userRepository = mock(UserRepository.class);
        IAuthService iAuthService = new IAuthService(authenticationManager, jwtService, userRepository,
                refreshTokenRepository, new ExceptionService());

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

        TableDetails tableDetails2 = new TableDetails();
        tableDetails2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tableDetails2.setId(1L);
        tableDetails2.setStatus(true);
        tableDetails2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

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

        RefreshToken refreshToken2 = new RefreshToken();
        refreshToken2.setId(1L);
        refreshToken2.setLifeTime(1L);
        refreshToken2.setName("Name");
        refreshToken2.setRevoked(true);
        refreshToken2.setTableDetails(tableDetails2);
        refreshToken2.setUser(user3);

        iAuthService.ifHasExistUpdate(user2, refreshToken2, "01234567-89AB-CDEF-FEDC-BA9876543210");

        verify(refreshTokenRepository).save(Mockito.<RefreshToken>any());
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", refreshToken2.getName());
    }



    /**
     * Method under test: {@link IAuthService#getAuthenticatedUser()}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetAuthenticatedUser() {


        iAuthService.getAuthenticatedUser();
    }
}
