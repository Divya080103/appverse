package com.appverse.service;

import com.appverse.dto.CategoryDTO;
import com.appverse.entity.Category;
import com.appverse.exception.BadRequestException;
import com.appverse.exception.ResourceNotFoundException;
import com.appverse.repository.CategoryRepository;
import com.appverse.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category testCategory;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(1L)
                .name("Productivity")
                .description("Productivity apps")
                .iconUrl("http://icon.url")
                .apps(Collections.emptyList())
                .build();

        categoryDTO = CategoryDTO.builder()
                .name("Productivity")
                .description("Productivity apps")
                .iconUrl("http://icon.url")
                .build();
    }

    @Test
    void testCreateCategory_Success() {
        when(categoryRepository.existsByName("Productivity")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        CategoryDTO result = categoryService.createCategory(categoryDTO);

        assertNotNull(result);
        assertEquals("Productivity", result.getName());
        assertEquals("Productivity apps", result.getDescription());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testCreateCategory_DuplicateName() {
        when(categoryRepository.existsByName("Productivity")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> categoryService.createCategory(categoryDTO));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testUpdateCategory_Success() {
        CategoryDTO updateDTO = CategoryDTO.builder()
                .name("Updated Name")
                .description("Updated Description")
                .iconUrl("http://new-icon.url")
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        CategoryDTO result = categoryService.updateCategory(1L, updateDTO);

        assertNotNull(result);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategory(99L, categoryDTO));
    }

    @Test
    void testDeleteCategory_Success() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryService.deleteCategory(1L);

        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(99L));
    }

    @Test
    void testGetCategoryById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        CategoryDTO result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals("Productivity", result.getName());
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(99L));
    }

    @Test
    void testGetAllCategories() {
        Category cat2 = Category.builder()
                .id(2L).name("Games").description("Gaming apps")
                .apps(Collections.emptyList()).build();
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(testCategory, cat2));

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
    }

    @Test
    void testGetAllCategories_Empty() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertTrue(result.isEmpty());
    }
}
