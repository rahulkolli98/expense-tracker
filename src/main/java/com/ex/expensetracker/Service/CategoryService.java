package com.ex.expensetracker.Service;

import com.ex.expensetracker.Exception.CategoryExistsException;
import com.ex.expensetracker.Exception.CategoryNotFoundException;
import com.ex.expensetracker.Repository.CategoryRepository;
import com.ex.expensetracker.model.Category;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public void addNewCategory(Category category) throws CategoryExistsException {
        if (categoryRepository.findCategoryByName(category.getName()).isPresent()) {
            log.error("Category {} already exists...", category.getName());
            throw new CategoryExistsException("Category exists...");
        }
        categoryRepository.save(category);
    }

    public void deleteCategoryByName(String categoryName) throws CategoryNotFoundException {
        Category category = categoryRepository.findCategoryByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Category does not exist..."));
        log.info("Deleting category: {}", categoryName);
        categoryRepository.deleteById(category.getId());
    }

    public void deleteCategoryById(Long categoryId) throws CategoryNotFoundException {
        if (!categoryRepository.existsById(categoryId)) {
            log.warn("Attempted to delete non-existent category with id: {}", categoryId);
            throw new CategoryNotFoundException("Category does not exist...");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Transactional
    public ResponseEntity<Category> updateCategory(String oldCategoryName, Category category) throws CategoryNotFoundException {
        Category existingCategory = categoryRepository.findCategoryByName(oldCategoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Category does not exist..."));

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return ResponseEntity.ok(updatedCategory);
    }

    public Category getCategoryById(Long categoryId) throws CategoryNotFoundException {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category does not exist..."));
    }

    public Category getCategoryByName(String categoryName) throws CategoryNotFoundException {
        return categoryRepository.findCategoryByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Category does not exist..."));
    }
}