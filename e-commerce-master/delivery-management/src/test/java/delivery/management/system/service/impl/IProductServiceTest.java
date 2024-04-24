package delivery.management.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import common.exception.model.exception.ApplicationException;
import common.exception.model.service.ExceptionService;
import delivery.management.system.mapper.ProductMapper;
import delivery.management.system.model.dto.request.ProductRequestDto;
import delivery.management.system.model.entity.CartItem;
import delivery.management.system.model.entity.Product;
import delivery.management.system.model.enums.Exceptions;
import delivery.management.system.repository.ProductRepository;
import delivery.management.system.service.CategoryService;
import delivery.management.system.service.FileService;

import java.math.BigDecimal;
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

@ContextConfiguration(classes = {IProductService.class})
@ExtendWith(SpringExtension.class)
class IProductServiceTest {
    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ExceptionService exceptionService;

    @Autowired
    private IProductService iProductService;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private ProductRepository productRepository;


    @Test
    void testAddProduct() {
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
        when(productRepository.save(Mockito.<Product>any())).thenReturn(product);

        Product product2 = new Product();
        product2.setCategories(new ArrayList<>());
        product2.setCount(3L);
        product2.setDescription("The characteristics of someone or something");
        product2.setId(1L);
        product2.setImages(new ArrayList<>());
        product2.setName("Name");
        product2.setPrice(new BigDecimal("2.3"));
        product2.setStatus(true);
        when(productMapper.map(Mockito.<ProductRequestDto>any())).thenReturn(product2);
        when(categoryService.getByIds(Mockito.<List<Long>>any())).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<Void> actualAddProductResult = iProductService.addProduct(new ProductRequestDto());

        // Assert
        verify(productMapper).map(Mockito.<ProductRequestDto>any());
        verify(categoryService).getByIds(Mockito.<List<Long>>any());
        verify(productRepository).save(Mockito.<Product>any());
        assertNull(actualAddProductResult.getBody());
        assertEquals(204, actualAddProductResult.getStatusCodeValue());
        assertTrue(actualAddProductResult.getHeaders().isEmpty());
    }
    @Test
    void testGetById_Success() {
        Product product = new Product(); // Assume a valid product object
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        Product result = iProductService.getById(1L);
        assertEquals(product, result);
    }

    @Test
    void testGetById_NotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(exceptionService.exceptionResponse(anyString(), any())).thenReturn(new ApplicationException(exceptionService.exceptionResponse("Order not found",HttpStatus.NOT_FOUND)).getExceptionResponse());
        assertThrows(ApplicationException.class, () -> iProductService.getById(1L));
    }





}
