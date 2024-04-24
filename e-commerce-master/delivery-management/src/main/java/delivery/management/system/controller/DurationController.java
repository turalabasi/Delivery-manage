package delivery.management.system.controller;

import delivery.management.system.model.dto.response.OrderResponseDto;
import delivery.management.system.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/duration")
public class DurationController {

    private final OrderService orderService;

    public DurationController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/calculate/{orderId}")
    public ResponseEntity<String> trackDuration(@PathVariable Long orderId) {
        OrderResponseDto orderDto = orderService.findById(orderId).getBody();
        if (orderDto == null) {
            return ResponseEntity.notFound().build();
        }

        LocalDateTime deliveryTime = orderDto.getDeliveryTime();
        LocalDateTime startTime = LocalDateTime.now();


        if (startTime.isAfter(deliveryTime)) {
            return ResponseEntity.ok("The courier has reached your location.");
        }


        Duration duration = Duration.between(startTime, deliveryTime);
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        String formattedDuration = hours + " hours " + minutes + " minutes";

        return ResponseEntity.ok(formattedDuration);
    }
}
