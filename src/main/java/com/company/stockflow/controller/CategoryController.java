package com.company.stockflow.controller;

import com.company.stockflow.dto.request.CategoryRequest;
import com.company.stockflow.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "categories/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new CategoryRequest());
        model.addAttribute("formAction", "/categories");
        model.addAttribute("pageTitle", "Create Category");
        model.addAttribute("buttonLabel", "Save Category");
        return "categories/form";
    }

    @PostMapping
    public String createCategory(
            @Valid @ModelAttribute("category") CategoryRequest category,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/categories");
            model.addAttribute("pageTitle", "Create Category");
            model.addAttribute("buttonLabel", "Save Category");
            return "categories/form";
        }

        try {
            categoryService.create(category);
            redirectAttributes.addFlashAttribute("message", "Category created successfully.");
            return "redirect:/categories";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("formAction", "/categories");
            model.addAttribute("pageTitle", "Create Category");
            model.addAttribute("buttonLabel", "Save Category");
            return "categories/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var category = categoryService.findById(id);
            CategoryRequest request = CategoryRequest.builder()
                    .name(category.getName())
                    .description(category.getDescription())
                    .build();
            model.addAttribute("category", request);
            model.addAttribute("categoryId", id);
            model.addAttribute("formAction", "/categories/" + id);
            model.addAttribute("pageTitle", "Edit Category");
            model.addAttribute("buttonLabel", "Update Category");
            return "categories/form";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/categories";
        }
    }

    @PostMapping("/{id}")
    public String updateCategory(
            @PathVariable Long id,
            @Valid @ModelAttribute("category") CategoryRequest category,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryId", id);
            model.addAttribute("formAction", "/categories/" + id);
            model.addAttribute("pageTitle", "Edit Category");
            model.addAttribute("buttonLabel", "Update Category");
            return "categories/form";
        }

        try {
            categoryService.update(id, category);
            redirectAttributes.addFlashAttribute("message", "Category updated successfully.");
            return "redirect:/categories";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("categoryId", id);
            model.addAttribute("formAction", "/categories/" + id);
            model.addAttribute("pageTitle", "Edit Category");
            model.addAttribute("buttonLabel", "Update Category");
            model.addAttribute("errorMessage", ex.getMessage());
            return "categories/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Category deleted successfully.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/categories";
    }
}