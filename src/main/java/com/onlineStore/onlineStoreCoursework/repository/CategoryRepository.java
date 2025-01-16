package com.onlineStore.onlineStoreCoursework.repository;

import com.onlineStore.onlineStoreCoursework.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
