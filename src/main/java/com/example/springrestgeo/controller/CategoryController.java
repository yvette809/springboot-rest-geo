package com.example.springrestgeo.controller;

import com.example.springrestgeo.entity.Category;
import com.example.springrestgeo.exceptions.ResourceNotFoundException;
import com.example.springrestgeo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class CategoryController {


    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.findAllCategories();

        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(categories);
        } else {
            return ResponseEntity.ok(categories);
        }
    }


    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/categories")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> saveCategory(@RequestBody Category category) {
        try {
            categoryService.saveCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body("Category created successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}