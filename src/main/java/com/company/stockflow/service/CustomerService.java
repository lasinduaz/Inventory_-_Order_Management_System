package com.company.stockflow.service;

import com.company.stockflow.dto.request.CustomerRequest;
import com.company.stockflow.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll();
    Customer findById(Long id);
    Customer create(CustomerRequest request);
    Customer update(Long id, CustomerRequest request);
    void delete(Long id);
}
