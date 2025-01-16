package com.onlineStore.onlineStoreCoursework.repository;

import com.onlineStore.onlineStoreCoursework.model.Order;
import com.onlineStore.onlineStoreCoursework.model.OrderProductViewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    //List<Order> findByUserId(Long userId);

    @Query("SELECT new com.onlineStore.onlineStoreCoursework.model.OrderProductViewModel(o.orderId, p.name, u.username, p.description, p.price, c.name, o.orderDate) " +
            "FROM Order o " +
            "JOIN Product p ON o.productId = p.productId " +
            "JOIN User u ON o.userId = u.id " +
            "JOIN Category c ON p.category.categoryId = c.categoryId " + // Добавление связи с категорией
            "WHERE u.id = :userId")
    List<OrderProductViewModel> findOrdersWithProductAndUser(@Param("userId") Long userId);

}
