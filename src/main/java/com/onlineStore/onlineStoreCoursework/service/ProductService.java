package com.onlineStore.onlineStoreCoursework.service;

import com.onlineStore.onlineStoreCoursework.model.Product;
import com.onlineStore.onlineStoreCoursework.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Метод для создания нового продукта
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Метод для создания нового продукта
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    // Метод для обновления информации о продукте
    public Product updateProduct(Long productId, Product updatedProduct) {
        if (productRepository.existsById(productId)) {
            updatedProduct.setProductId(productId);
            return productRepository.save(updatedProduct);
        }
        return null; // Или выбросить исключение, если продукт не найден
    }

    // Метод для уменьшения количества товара на 1
    public Product decrementProductStock(Long productId) {
        // Получаем продукт по ID
        Product product = productRepository.findById(productId).orElse(null);

        // Если продукт существует и количество товара больше 0
        if (product != null && product.getStockQuantity() > 0) {
            product.setStockQuantity(product.getStockQuantity() - 1);
            return productRepository.save(product); // Сохраняем обновлённый продукт
        }
        return null; // Если продукт не найден или количество товара 0, возвращаем null
    }
    // Метод для поиска продукта по ID
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }


    // Метод для удаления продукта
    public boolean deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }
}
