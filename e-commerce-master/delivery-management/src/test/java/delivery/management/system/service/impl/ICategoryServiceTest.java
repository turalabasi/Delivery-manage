package delivery.management.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import common.exception.model.service.ExceptionService;
import delivery.management.system.mapper.CategoryMapper;
import delivery.management.system.model.dto.request.CategoryRequestDto;
import delivery.management.system.model.dto.response.CategoryResponseDto;
import delivery.management.system.model.entity.Category;
import delivery.management.system.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ICategoryService.class})
@ExtendWith(SpringExtension.class)
class ICategoryServiceTest {
    @MockBean
    private CategoryMapper categoryMapper;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ExceptionService exceptionService;

    @Autowired
    private ICategoryService iCategoryService;


    @Test
    void testGetByIds() {
        // Arrange
        ArrayList<Category> categoryList = new ArrayList<>();
        when(categoryRepository.findAllById(Mockito.<Iterable<Long>>any())).thenReturn(categoryList);

        // Act
        List<Category> actualByIds = iCategoryService.getByIds(new ArrayList<>());

        // Assert
        verify(categoryRepository).findAllById(Mockito.<Iterable<Long>>any());
        assertTrue(actualByIds.isEmpty());
        assertSame(categoryList, actualByIds);
    }


    @Test
    void testFindAll() {
        // Arrange
        when(categoryRepository.findAllByStatusTrue()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<CategoryResponseDto>> actualFindAllResult = iCategoryService.findAll();

        // Assert
        verify(categoryRepository).findAllByStatusTrue();
        assertEquals(200, actualFindAllResult.getStatusCodeValue());
        assertTrue(actualFindAllResult.hasBody());
        assertTrue(actualFindAllResult.getHeaders().isEmpty());
    }


    @Test
    void testFindById() {
        // Arrange
        Category category = new Category();
        category.setDescription("The characteristics of someone or something");
        category.setId(1L);
        category.setName("Name");
        category.setProducts(new ArrayList<>());
        category.setStatus(true);
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findByIdAndStatusTrue(anyLong())).thenReturn(ofResult);
        CategoryResponseDto buildResult = CategoryResponseDto.builder()
                .description("The characteristics of someone or something")
                .id(1L)
                .name("Name")
                .build();
        when(categoryMapper.map(Mockito.<Category>any())).thenReturn(buildResult);

        // Act
        ResponseEntity<CategoryResponseDto> actualFindByIdResult = iCategoryService.findById(1L);

        // Assert
        verify(categoryMapper).map(Mockito.<Category>any());
        verify(categoryRepository).findByIdAndStatusTrue(eq(1L));
        assertEquals(200, actualFindByIdResult.getStatusCodeValue());
        assertTrue(actualFindByIdResult.hasBody());
        assertTrue(actualFindByIdResult.getHeaders().isEmpty());
    }


    @Test
    void testAddCategory() {
        // Arrange
        Category category = new Category();
        category.setDescription("The characteristics of someone or something");
        category.setId(1L);
        category.setName("Name");
        category.setProducts(new ArrayList<>());
        category.setStatus(true);
        when(categoryRepository.save(Mockito.<Category>any())).thenReturn(category);

        Category category2 = new Category();
        category2.setDescription("The characteristics of someone or something");
        category2.setId(1L);
        category2.setName("Name");
        category2.setProducts(new ArrayList<>());
        category2.setStatus(true);
        when(categoryMapper.map(Mockito.<CategoryRequestDto>any())).thenReturn(category2);

        // Act
        ResponseEntity<Void> actualAddCategoryResult = iCategoryService
                .addCategory(new CategoryRequestDto("Name", "The characteristics of someone or something"));

        // Assert
        verify(categoryMapper).map(Mockito.<CategoryRequestDto>any());
        verify(categoryRepository).save(Mockito.<Category>any());
        assertNull(actualAddCategoryResult.getBody());
        assertEquals(204, actualAddCategoryResult.getStatusCodeValue());
        assertTrue(actualAddCategoryResult.getHeaders().isEmpty());
    }


    @Test
    void testUpdate() {
        // Arrange
        Category category = new Category();
        category.setDescription("The characteristics of someone or something");
        category.setId(1L);
        category.setName("Name");
        category.setProducts(new ArrayList<>());
        category.setStatus(true);
        Optional<Category> ofResult = Optional.of(category);

        Category category2 = new Category();
        category2.setDescription("The characteristics of someone or something");
        category2.setId(1L);
        category2.setName("Name");
        category2.setProducts(new ArrayList<>());
        category2.setStatus(true);
        when(categoryRepository.save(Mockito.<Category>any())).thenReturn(category2);
        when(categoryRepository.findByIdAndStatusTrue(anyLong())).thenReturn(ofResult);

        Category category3 = new Category();
        category3.setDescription("The characteristics of someone or something");
        category3.setId(1L);
        category3.setName("Name");
        category3.setProducts(new ArrayList<>());
        category3.setStatus(true);
        when(categoryMapper.map(Mockito.<Category>any(), Mockito.<CategoryRequestDto>any())).thenReturn(category3);

        // Act
        ResponseEntity<Void> actualUpdateResult = iCategoryService.update(1L,
                new CategoryRequestDto("Name", "The characteristics of someone or something"));

        // Assert
        verify(categoryMapper).map(Mockito.<Category>any(), Mockito.<CategoryRequestDto>any());
        verify(categoryRepository).findByIdAndStatusTrue(eq(1L));
        verify(categoryRepository).save(Mockito.<Category>any());
        assertNull(actualUpdateResult.getBody());
        assertEquals(204, actualUpdateResult.getStatusCodeValue());
        assertTrue(actualUpdateResult.getHeaders().isEmpty());
    }


    @Test
    void testDelete() {
        // Arrange
        Category category = new Category();
        category.setDescription("The characteristics of someone or something");
        category.setId(1L);
        category.setName("Name");
        category.setProducts(new ArrayList<>());
        category.setStatus(true);
        Optional<Category> ofResult = Optional.of(category);
        when(categoryRepository.findByIdAndStatusTrue(anyLong())).thenReturn(ofResult);

        // Act
        ResponseEntity<Void> actualDeleteResult = iCategoryService.delete(1L);

        // Assert
        verify(categoryRepository).findByIdAndStatusTrue(eq(1L));
        assertNull(actualDeleteResult.getBody());
        assertEquals(204, actualDeleteResult.getStatusCodeValue());
        assertTrue(actualDeleteResult.getHeaders().isEmpty());
    }

}