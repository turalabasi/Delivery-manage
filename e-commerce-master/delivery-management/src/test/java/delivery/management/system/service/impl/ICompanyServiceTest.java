package delivery.management.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import common.exception.model.dto.response.ExceptionResponse;
import common.exception.model.exception.ApplicationException;
import common.exception.model.service.ExceptionService;
import delivery.management.system.repository.CompanyRepository;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ICompanyService.class})
@ExtendWith(SpringExtension.class)
class ICompanyServiceTest {
    @MockBean
    private CompanyRepository companyRepository;

    @MockBean
    private ExceptionService exceptionService;

    @Autowired
    private ICompanyService iCompanyService;


    @Test
    void testIncreaseCompanyBalance() {
        // Arrange
        when(companyRepository.findAll()).thenReturn(new ArrayList<>());
        ExceptionResponse buildResult = ExceptionResponse.builder()
                .httpStatus(HttpStatus.CONTINUE)
                .message("An error occurred")
                .build();
        when(exceptionService.exceptionResponse(Mockito.<String>any(), Mockito.<HttpStatus>any())).thenReturn(buildResult);

        // Act and Assert
        assertThrows(ApplicationException.class, () -> iCompanyService.increaseCompanyBalance(new BigDecimal("2.3")));
        verify(exceptionService).exceptionResponse(eq("Company not found"), eq(HttpStatus.NOT_FOUND));
        verify(companyRepository).findAll();
    }



}
