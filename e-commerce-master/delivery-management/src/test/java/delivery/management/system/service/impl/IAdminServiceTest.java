package delivery.management.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import delivery.management.system.helper.AdminServiceHelper;
import delivery.management.system.model.dto.response.DashboardResponseDto;
import delivery.management.system.model.dto.response.OrderDashboardDto;
import delivery.management.system.service.OrderService;
import delivery.management.system.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {IAdminService.class})
@ExtendWith(SpringExtension.class)
class IAdminServiceTest {
    @MockBean
    private AdminServiceHelper adminServiceHelper;

    @Autowired
    private IAdminService iAdminService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    @Test
    void testDashboard() {
        when(userService.customerCount()).thenReturn(3);
        when(userService.driverCount()).thenReturn(3);
        OrderDashboardDto.OrderDashboardDtoBuilder countResult = OrderDashboardDto.builder().count(3);
        OrderDashboardDto buildResult = countResult.sum(new BigDecimal("2.3")).build();
        when(orderService.profitByDate(Mockito.<LocalDateTime>any())).thenReturn(buildResult);
        DashboardResponseDto.DashboardResponseDtoBuilder dailyOrderCountResult = DashboardResponseDto.builder()
                .customerCount(3)
                .dailyOrderCount(3L);
        DashboardResponseDto.DashboardResponseDtoBuilder monthlyOrderCountResult = dailyOrderCountResult
                .dailyProfit(new BigDecimal("2.3"))
                .driverCount(3)
                .monthlyOrderCount(3L);
        DashboardResponseDto.DashboardResponseDtoBuilder yearlyOrderCountResult = monthlyOrderCountResult
                .monthlyProfit(new BigDecimal("2.3"))
                .yearlyOrderCount(3L);
        DashboardResponseDto buildResult2 = yearlyOrderCountResult.yearlyProfit(new BigDecimal("2.3")).build();
        when(adminServiceHelper.dashboardBuild(Mockito.<OrderDashboardDto>any(), Mockito.<OrderDashboardDto>any(),
                Mockito.<OrderDashboardDto>any(), anyInt(), anyInt())).thenReturn(buildResult2);

        ResponseEntity<DashboardResponseDto> actualDashboardResult = iAdminService.dashboard();

        verify(adminServiceHelper).dashboardBuild(Mockito.<OrderDashboardDto>any(), Mockito.<OrderDashboardDto>any(),
                Mockito.<OrderDashboardDto>any(), eq(3), eq(3));
        verify(orderService, atLeast(1)).profitByDate(Mockito.<LocalDateTime>any());
        verify(userService).customerCount();
        verify(userService).driverCount();
        assertEquals(200, actualDashboardResult.getStatusCodeValue());
        assertTrue(actualDashboardResult.hasBody());
        assertTrue(actualDashboardResult.getHeaders().isEmpty());
    }
}
