package com.company.stockflow.service.impl;

import com.company.stockflow.dto.request.CustomerRequest;
import com.company.stockflow.entity.Customer;
import com.company.stockflow.repository.CustomerRepository;
import com.company.stockflow.service.CustomerService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }

    @Override
    public Customer create(CustomerRequest request) {
        String email = normalize(request.getEmail());
        if (customerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("A customer with that email already exists");
        }
        Customer customer = new Customer();
        apply(customer, request, email);
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, CustomerRequest request) {
        Customer customer = findById(id);
        String email = normalize(request.getEmail());
        customerRepository.findByEmail(email)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> { throw new IllegalArgumentException("A customer with that email already exists"); });
        apply(customer, request, email);
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Long id) {
        Customer customer = findById(id);
        try {
            customerRepository.delete(customer);
            customerRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("This customer has orders and cannot be deleted");
        }
    }

    private void apply(Customer customer, CustomerRequest request, String email) {
        customer.setFirstName(normalize(request.getFirstName()));
        customer.setLastName(normalize(request.getLastName()));
        customer.setEmail(email);
        customer.setPhone(normalize(request.getPhone()));
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
