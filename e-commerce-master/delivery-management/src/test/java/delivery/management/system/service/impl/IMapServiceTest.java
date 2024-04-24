package delivery.management.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import delivery.management.system.config.GoogleMapsConfig;
import delivery.management.system.model.dto.response.DirectionsApiResponse;
import delivery.management.system.model.dto.response.RouteResponseDto;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ContextConfiguration(classes = {IMapService.class, GoogleMapsConfig.class})
@ExtendWith(SpringExtension.class)
class IMapServiceTest {
    @Autowired
    private GoogleMapsConfig googleMapsConfig;

    @Autowired
    private IMapService iMapService;

    @MockBean
    private RestTemplate restTemplate;


    @Test
    void testCalculateRoute() throws RestClientException {
        // Arrange
        DirectionsApiResponse directionsApiResponse = new DirectionsApiResponse();
        directionsApiResponse.setRoutes(new ArrayList<>());
        directionsApiResponse.setStatus("Status");
        when(restTemplate.getForObject(Mockito.<String>any(), Mockito.<Class<DirectionsApiResponse>>any(),
                isA(Object[].class))).thenReturn(directionsApiResponse);

        // Act
        iMapService.calculateRoute("Origin", "Destination", "Mode");

        // Assert
        verify(restTemplate).getForObject(
                eq("https://maps.googleapis.com/maps/api/directions/json?origin=Origin&destination=Destination&mode=Mode&key"),
                Mockito.<Class<DirectionsApiResponse>>any(), isA(Object[].class));
    }

}
