package com.onlineStore.onlineStoreCoursework.service;

import com.onlineStore.onlineStoreCoursework.model.Category;
import com.onlineStore.onlineStoreCoursework.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Метод для создания новой категории
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Метод для обновления существующей категории
    public Category updateCategory(Long categoryId, Category updatedCategory) {
        if (categoryRepository.existsById(categoryId)) {
            updatedCategory.setCategoryId(categoryId);
            return categoryRepository.save(updatedCategory);
        }
        return null; // Или выбросить исключение
    }

    // Метод для получения категории по ID
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    // Метод для удаления категории
    public boolean deleteCategory(Long categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
            return true;
        }
        return false;
    }

    // Метод для сохранения категории в базе данных
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }
}
