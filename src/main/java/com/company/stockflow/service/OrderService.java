package com.company.stockflow.service;

import com.company.stockflow.dto.request.OrderRequest;
import com.company.stockflow.entity.CustomerOrder;
import com.company.stockflow.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    List<CustomerOrder> findAll();
    CustomerOrder create(OrderRequest request);
    void updateStatus(Long id, OrderStatus status);
    void delete(Long id);
}
