package com.company.stockflow.service.impl;

import com.company.stockflow.dto.request.OrderItemRequest;
import com.company.stockflow.dto.request.OrderRequest;
import com.company.stockflow.entity.Customer;
import com.company.stockflow.entity.CustomerOrder;
import com.company.stockflow.entity.OrderItem;
import com.company.stockflow.entity.Product;
import com.company.stockflow.enums.OrderStatus;
import com.company.stockflow.repository.CustomerOrderRepository;
import com.company.stockflow.repository.CustomerRepository;
import com.company.stockflow.repository.ProductRepository;
import com.company.stockflow.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final CustomerOrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(CustomerOrderRepository orderRepository, CustomerRepository customerRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerOrder> findAll() {
        return orderRepository.findAllWithDetails();
    }

    @Override
    public CustomerOrder create(OrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        CustomerOrder order = CustomerOrder.builder()
                .customer(customer)
                .orderDate(LocalDateTime.now())
                .status(request.getStatus())
                .totalAmount(BigDecimal.ZERO)
                .build();

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("A selected product no longer exists"));
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            items.add(OrderItem.builder().order(order).product(product).quantity(itemRequest.getQuantity())
                    .unitPrice(product.getPrice()).subtotal(subtotal).build());
            total = total.add(subtotal);
        }
        order.setOrderItems(items);
        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    @Override
    public void updateStatus(Long id, OrderStatus status) {
        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(status);
    }

    @Override
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException("Order not found");
        }
        orderRepository.deleteById(id);
    }
}
