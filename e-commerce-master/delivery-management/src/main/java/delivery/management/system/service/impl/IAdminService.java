package delivery.management.system.service.impl;

import delivery.management.system.helper.AdminServiceHelper;
import delivery.management.system.model.dto.response.DashboardResponseDto;
import delivery.management.system.model.dto.response.OrderDashboardDto;
import delivery.management.system.service.AdminService;
import delivery.management.system.service.OrderService;
import delivery.management.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IAdminService implements AdminService {

    private final UserService userService;
    private final OrderService orderService;
    private final AdminServiceHelper adminServiceHelper;

    @Override
    public ResponseEntity<DashboardResponseDto> dashboard() {

        int customerCount = userService.customerCount();
        int driverCount = userService.driverCount();

        LocalDateTime localDateTime = LocalDateTime.now();
        OrderDashboardDto daily = orderService.profitByDate(localDateTime.minusDays(1));
        OrderDashboardDto monthly = orderService.profitByDate(localDateTime.minusMonths(1));
        OrderDashboardDto yearly = orderService.profitByDate(localDateTime.minusYears(1));

        return ResponseEntity.ok(adminServiceHelper.dashboardBuild(daily, monthly, yearly, customerCount, driverCount));

    }
}
