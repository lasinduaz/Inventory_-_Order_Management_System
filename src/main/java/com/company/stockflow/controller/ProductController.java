package com.company.stockflow.controller;

import com.company.stockflow.dto.request.ProductRequest;
import com.company.stockflow.service.CategoryService;
import com.company.stockflow.service.ProductService;
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
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductRequest());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("formAction", "/products");
        model.addAttribute("pageTitle", "Create Product");
        model.addAttribute("buttonLabel", "Save Product");
        return "products/form";
    }

    @PostMapping
    public String createProduct(
            @Valid @ModelAttribute("product") ProductRequest product,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("formAction", "/products");
            model.addAttribute("pageTitle", "Create Product");
            model.addAttribute("buttonLabel", "Save Product");
            return "products/form";
        }

        try {
            productService.create(product);
            redirectAttributes.addFlashAttribute("message", "Product created successfully.");
            return "redirect:/products";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("formAction", "/products");
            model.addAttribute("pageTitle", "Create Product");
            model.addAttribute("buttonLabel", "Save Product");
            return "products/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var product = productService.findById(id);
            ProductRequest request = ProductRequest.builder()
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stockQuantity(product.getStockQuantity())
                    .sku(product.getSku())
                    .categoryId(product.getCategoryId())
                    .build();
            model.addAttribute("product", request);
            model.addAttribute("productId", id);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("formAction", "/products/" + id);
            model.addAttribute("pageTitle", "Edit Product");
            model.addAttribute("buttonLabel", "Update Product");
            return "products/form";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/products";
        }
    }

    @PostMapping("/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute("product") ProductRequest product,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("formAction", "/products/" + id);
            model.addAttribute("pageTitle", "Edit Product");
            model.addAttribute("buttonLabel", "Update Product");
            return "products/form";
        }

        try {
            productService.update(id, product);
            redirectAttributes.addFlashAttribute("message", "Product updated successfully.");
            return "redirect:/products";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("productId", id);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("formAction", "/products/" + id);
            model.addAttribute("pageTitle", "Edit Product");
            model.addAttribute("buttonLabel", "Update Product");
            return "products/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/products";
    }
}