package com.example.springrestgeo.service;

import com.example.springrestgeo.entity.Category;
import com.example.springrestgeo.entity.Place;
import com.example.springrestgeo.exceptions.ResourceNotFoundException;
import com.example.springrestgeo.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        Category category = optionalCategory.orElseThrow(() ->
                new ResourceNotFoundException("Category not found with ID: " + id));

        // Trigger lazy loading of places
        List<Place> places = category.getPlaces().stream().toList();

        category.setPlaces(places);

        return category;
    }
    @Transactional
    public void saveCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category with that name already exists");
        }

        // Continue with saving the new category if it doesn't exist
        categoryRepository.save(category);
    }


}