package com.company.stockflow.dto.request;

import com.company.stockflow.enums.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    @NotNull(message = "Choose a customer")
    private Long customerId;

    @NotNull(message = "Choose an order status")
    private OrderStatus status = OrderStatus.PENDING;

    @Valid
    @NotEmpty(message = "Add at least one order item")
    private List<OrderItemRequest> items = new ArrayList<>();
}
