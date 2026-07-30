package com.company.stockflow.controller;

import com.company.stockflow.dto.request.OrderItemRequest;
import com.company.stockflow.dto.request.OrderRequest;
import com.company.stockflow.enums.OrderStatus;
import com.company.stockflow.service.CustomerService;
import com.company.stockflow.service.OrderService;
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
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;

    public OrderController(OrderService orderService, CustomerService customerService, ProductService productService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("statuses", OrderStatus.values());
        return "orders/list";
    }

    @GetMapping("/new")
    public String newForm(Model model, RedirectAttributes redirectAttributes) {
        if (customerService.findAll().isEmpty() || productService.findAll().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Create at least one customer and one product before creating an order.");
            return "redirect:/orders";
        }
        OrderRequest request = new OrderRequest();
        request.getItems().add(new OrderItemRequest());
        setupForm(model, request);
        return "orders/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("order") OrderRequest request, BindingResult errors,
                         Model model, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            setupForm(model, request);
            return "orders/form";
        }
        try {
            orderService.create(request);
            redirectAttributes.addFlashAttribute("message", "Order created successfully.");
            return "redirect:/orders";
        } catch (IllegalArgumentException ex) {
            setupForm(model, request);
            model.addAttribute("errorMessage", ex.getMessage());
            return "orders/form";
        }
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, OrderStatus status, RedirectAttributes redirectAttributes) {
        try {
            orderService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("message", "Order status updated.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/orders";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Order deleted successfully.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/orders";
    }

    private void setupForm(Model model, OrderRequest order) {
        model.addAttribute("order", order);
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("statuses", OrderStatus.values());
    }
}
