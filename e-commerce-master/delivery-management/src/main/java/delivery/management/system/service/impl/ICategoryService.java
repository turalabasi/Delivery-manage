package delivery.management.system.service.impl;

import common.exception.model.exception.ApplicationException;
import common.exception.model.service.ExceptionService;
import delivery.management.system.mapper.CategoryMapper;
import delivery.management.system.model.dto.request.CategoryRequestDto;
import delivery.management.system.model.dto.response.CategoryResponseDto;
import delivery.management.system.model.entity.Category;
import delivery.management.system.model.enums.Exceptions;
import delivery.management.system.repository.CategoryRepository;
import delivery.management.system.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ICategoryService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ExceptionService exceptionService;

    @Override
    public List<Category> getByIds(List<Long> categoriesId) {
        if (categoriesId == null || categoriesId.isEmpty()) {
            throw new ApplicationException(exceptionService.exceptionResponse(Exceptions.INVALID_CATEGORY_IDS.getMessage(), HttpStatus.BAD_REQUEST));
        }
        return categoryRepository.findAllById(categoriesId);
    }

    @Override
    public ResponseEntity<List<CategoryResponseDto>> findAll() {
        List<CategoryResponseDto> categoryResponses = categoryRepository.findAllByStatusTrue().stream()
                .map(categoryMapper::map)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryResponses);
    }

    @Override
    public ResponseEntity<CategoryResponseDto> findById(long id) {
        Category category = getById(id);
        return ResponseEntity.ok(categoryMapper.map(category));
    }

    @Override
    public ResponseEntity<Void> addCategory(CategoryRequestDto categoryRequestDto) {
        Category category = categoryMapper.map(categoryRequestDto);
        categoryRepository.save(category);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> update(long id, CategoryRequestDto categoryRequestDto) {
        Category existingCategory = getById(id);
        Category updatedCategory = categoryMapper.map(existingCategory, categoryRequestDto);
        categoryRepository.save(updatedCategory);
        return ResponseEntity.noContent().build();
    }

    @Override
    @Transactional
    public ResponseEntity<Void> delete(long id) {
        Category category = getById(id);
        if (!category.isStatus()) {
            throw new ApplicationException(exceptionService.exceptionResponse(Exceptions.CATEGORY_ALREADY_INACTIVE.getMessage(), HttpStatus.BAD_REQUEST));
        }
        category.setStatus(false);
        categoryRepository.save(category);
        return ResponseEntity.noContent().build();
    }

    private Category getById(long id) {
        return categoryRepository.findByIdAndStatusTrue(id)
                .orElseThrow(() -> new ApplicationException(
                        exceptionService.exceptionResponse(Exceptions.CATEGORY_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND)));
    }
}


//@Service
//@RequiredArgsConstructor
//public class ICategoryService implements CategoryService {
//
//    private final CategoryRepository categoryRepository;
//    private final CategoryMapper categoryMapper;
//    private final ExceptionService exceptionService;
//
//    @Override
//    public List<Category> getByIds(List<Long> categoriesId) {
//        if (categoriesId != null) {
//            List<Category> categories = categoryRepository.findAllById(categoriesId);
//            return categories;
//        }
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<List<CategoryResponseDto>> findAll() {
//
//        List<CategoryResponseDto> categoryResponses = categoryRepository.findAllByStatusTrue().stream()
//                .map(categoryMapper::map)
//                .toList();
//
//        return ResponseEntity.ok(categoryResponses);
//    }
//
//    @Override
//    public ResponseEntity<CategoryResponseDto> findById(long id) {
//        return ResponseEntity.ok(categoryMapper.map(getById(id)));
//    }
//
//    @Override
//    public ResponseEntity<Void> addCategory(CategoryRequestDto categoryRequestDto) {
//        Category category = categoryMapper.map(categoryRequestDto);
//        categoryRepository.save(category);
//        return ResponseEntity.noContent().build();
//    }
//
//    @Override
//    public ResponseEntity<Void> update(long id, CategoryRequestDto categoryRequestDto) {
//        Category category = getById(id);
//        Category updateCategory = categoryMapper.map(category, categoryRequestDto);
//        categoryRepository.save(updateCategory);
//        return ResponseEntity.noContent().build();
//    }
//
//    @Override
//    @Transactional
//    public ResponseEntity<Void> delete(long id) {
//        Category category = getById(id);
//        category.setStatus(false);
//        return ResponseEntity.noContent().build();
//    }
//
//    private Category getById(long id) {
//
//        return categoryRepository.findByIdAndStatusTrue(id)
//                .orElseThrow(() -> new ApplicationException(exceptionService.exceptionResponse("not.found.exception", HttpStatus.NOT_FOUND)));
//    }
//}
