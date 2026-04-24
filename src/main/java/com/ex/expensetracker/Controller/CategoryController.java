package com.ex.expensetracker.Controller;

import com.ex.expensetracker.Exception.CategoryExistsException;
import com.ex.expensetracker.Exception.CategoryNotFoundException;
import com.ex.expensetracker.Service.CategoryService;
import com.ex.expensetracker.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public List<Category> getCategoryByIdOrName(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name) {
        List<Category> result = new ArrayList<>();
        try {
            if ((id == null) && (name == null)) {
                log.info("Getting list of all categories...");
                result = categoryService.getCategories();
            }
            else if (name == null) {
                log.info("Getting category by id: {}", id);
                result.add(categoryService.getCategoryById(id));
            }
            else {
                log.info("Getting category by name: {}", name);
                result.add(categoryService.getCategoryByName(name));
            }
        } catch (CategoryNotFoundException e) {
            log.error("Failed to fetch category information...");
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        return result;
    }

    @PostMapping
    public void addNewCategory(@RequestBody Category category) {
        try {
            log.info("Adding new category: {}", category.getName());
            categoryService.addNewCategory(category);
        } catch (CategoryExistsException e) {
            log.error("Failed to add new category...");
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }


    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteCategoryByName(@RequestBody Map<String, List<String>> categoryList) {
        if (categoryList == null || !categoryList.containsKey("category")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body. Missing 'category' key.");
        }

        List<String> categories = categoryList.get("category");
        List<String> failedCategories = new ArrayList<>();

        for (String category : categories) {
            try {
                log.info("Deleting category: {}", category);
                categoryService.deleteCategoryByName(category);
            } catch (CategoryNotFoundException e) {
                log.warn("Category not found: {}", category);
                failedCategories.add(category);
            }
        }

        if (!failedCategories.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Some categories could not be deleted.");
            response.put("failedCategories", failedCategories);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "All categories deleted successfully."));
    }


    @DeleteMapping(path = "{categoryId}")
    public void deleteCategoryById(
            @PathVariable("categoryId") Long categoryId
    ) {
        try {
            log.info("Deleting category id: {}", categoryId);
            categoryService.deleteCategoryById(categoryId);
        } catch (CategoryNotFoundException e) {
            log.error("Failed to delete category...");
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping(path = "{oldCategoryName}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable("oldCategoryName") String oldCategoryName,
            @RequestBody Category category
    ) {
        try {
            log.info("Updating category name: {}", oldCategoryName);
            return categoryService.updateCategory(oldCategoryName, category);
        } catch (CategoryNotFoundException e) {
            log.error("Failed to update category...");
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
