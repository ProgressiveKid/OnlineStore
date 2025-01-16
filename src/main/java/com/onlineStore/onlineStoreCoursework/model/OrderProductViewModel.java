package com.onlineStore.onlineStoreCoursework.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderProductViewModel {

    private Long orderId;
    private String productName;
    private String userName;
    private String description;
    private BigDecimal price;
    private String categoryName; // Добавлено новое поле для категории
    private LocalDateTime orderDate; // Добавлено новое поле для даты заказа

    // Конструктор с дополнительным параметром для даты заказа
    public OrderProductViewModel(Long orderId, String productName, String userName, String description, BigDecimal price, String categoryName, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.productName = productName;
        this.userName = userName;
        this.description = description;
        this.price = price;
        this.categoryName = categoryName;
        this.orderDate = orderDate;
    }

    // Геттеры и сеттеры
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
