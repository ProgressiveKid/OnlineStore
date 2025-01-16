package com.onlineStore.onlineStoreCoursework.service;

import com.onlineStore.onlineStoreCoursework.model.Order;
import com.onlineStore.onlineStoreCoursework.model.Product;
import com.onlineStore.onlineStoreCoursework.model.User;
import com.onlineStore.onlineStoreCoursework.model.OrderProductViewModel;
import com.onlineStore.onlineStoreCoursework.repository.OrderRepository;
import com.onlineStore.onlineStoreCoursework.repository.ProductRepository;
import com.onlineStore.onlineStoreCoursework.repository.UserRepository;
import com.onlineStore.onlineStoreCoursework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    // Метод для создания нового заказа
    public Order createOrder(Long userId, Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        BigDecimal price = product.getPrice();
        User user = userService.getCurrentUser();
        BigDecimal userBalance = BigDecimal.valueOf(user.getBalance());
        if (userBalance.compareTo(price) < 0)
        {
            return null;
        }
        double currentBalanceDouble = userBalance.subtract(price).doubleValue(); // Преобразуем в double
        user.setBalance(currentBalanceDouble);
        userRepository.save(user);

        Order order = new Order();
        order.setUserId(userId);
        order.setProductId(productId);
        // добавить кол-во вещей
        order.setOrderDate(java.time.LocalDateTime.now());
        return orderRepository.save(order);
    }

    // Метод для обновления существующего заказа
    public boolean updateOrder(Long orderId, Order updatedOrder) {
        if (orderRepository.existsById(orderId)) {
            updatedOrder.setOrderId(orderId);
            orderRepository.save(updatedOrder);
            return true;
        }
        return false;
    }

    // Метод для получения заказа по ID
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    //public List<Order> getOrdersByUserId(Long userId) {
    //    return orderRepository.findByUserId(userId);
   // }

    public List<OrderProductViewModel> getOrdersWithProductsAndUsers(Long userId) {
        return orderRepository.findOrdersWithProductAndUser(userId);
    }
    // Метод для удаления заказа
    public boolean deleteOrder(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
            return true;
        }
        return false;
    }
}
