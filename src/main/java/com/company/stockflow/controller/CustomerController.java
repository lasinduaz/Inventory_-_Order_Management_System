package com.company.stockflow.controller;

import com.company.stockflow.dto.request.CustomerRequest;
import com.company.stockflow.entity.Customer;
import com.company.stockflow.service.CustomerService;
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
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "customers/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        setupForm(model, new CustomerRequest(), "/customers", "Add Customer", "Save Customer");
        return "customers/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("customer") CustomerRequest request, BindingResult errors,
                         Model model, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            setupForm(model, request, "/customers", "Add Customer", "Save Customer");
            return "customers/form";
        }
        try {
            customerService.create(request);
            redirectAttributes.addFlashAttribute("message", "Customer created successfully.");
            return "redirect:/customers";
        } catch (IllegalArgumentException ex) {
            setupForm(model, request, "/customers", "Add Customer", "Save Customer");
            model.addAttribute("errorMessage", ex.getMessage());
            return "customers/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Customer customer = customerService.findById(id);
            CustomerRequest request = new CustomerRequest();
            request.setFirstName(customer.getFirstName());
            request.setLastName(customer.getLastName());
            request.setEmail(customer.getEmail());
            request.setPhone(customer.getPhone());
            setupForm(model, request, "/customers/" + id, "Edit Customer", "Update Customer");
            return "customers/form";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/customers";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("customer") CustomerRequest request,
                         BindingResult errors, Model model, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            setupForm(model, request, "/customers/" + id, "Edit Customer", "Update Customer");
            return "customers/form";
        }
        try {
            customerService.update(id, request);
            redirectAttributes.addFlashAttribute("message", "Customer updated successfully.");
            return "redirect:/customers";
        } catch (IllegalArgumentException ex) {
            setupForm(model, request, "/customers/" + id, "Edit Customer", "Update Customer");
            model.addAttribute("errorMessage", ex.getMessage());
            return "customers/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Customer deleted successfully.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/customers";
    }

    private void setupForm(Model model, CustomerRequest customer, String formAction, String pageTitle, String buttonLabel) {
        model.addAttribute("customer", customer);
        model.addAttribute("formAction", formAction);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("buttonLabel", buttonLabel);
    }
}
