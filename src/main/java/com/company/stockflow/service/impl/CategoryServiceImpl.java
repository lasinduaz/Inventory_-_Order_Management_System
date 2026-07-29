package com.company.stockflow.service.impl;

import com.company.stockflow.dto.request.CategoryRequest;
import com.company.stockflow.dto.response.CategoryResponse;
import com.company.stockflow.entity.Category;
import com.company.stockflow.mapper.CategoryMapper;
import com.company.stockflow.repository.CategoryRepository;
import com.company.stockflow.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        String name = normalize(request.getName());
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("Category name already exists");
        }

        Category category = categoryMapper.toEntity(request);
        category.setName(name);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        String name = normalize(request.getName());
        categoryRepository.findByName(name)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Category name already exists");
                });

        categoryMapper.updateEntity(category, request);
        category.setName(name);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }
}