package com.company.stockflow.repository;

import com.company.stockflow.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    @Query("select distinct o from CustomerOrder o join fetch o.customer left join fetch o.orderItems item left join fetch item.product order by o.orderDate desc")
    List<CustomerOrder> findAllWithDetails();
}
