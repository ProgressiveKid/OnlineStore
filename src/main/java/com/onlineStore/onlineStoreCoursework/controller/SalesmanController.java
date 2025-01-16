package com.onlineStore.onlineStoreCoursework.controller;

import com.onlineStore.onlineStoreCoursework.model.Category;
import com.onlineStore.onlineStoreCoursework.model.Product;
import com.onlineStore.onlineStoreCoursework.model.Order;
import com.onlineStore.onlineStoreCoursework.service.CategoryService;
import com.onlineStore.onlineStoreCoursework.service.OrderService;
import com.onlineStore.onlineStoreCoursework.repository.CategoryRepository;
import com.onlineStore.onlineStoreCoursework.repository.ProductRepository;
import com.onlineStore.onlineStoreCoursework.service.ProductService;
import com.onlineStore.onlineStoreCoursework.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.io.IOException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;

@Controller
@RequestMapping("/salesman")
public class SalesmanController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/menu")
    public String getMenuPage(Model model) {
        Iterable<Category> category = categoryRepository.findAll();
        category.forEach(ca -> System.out.println(ca.getName()));
        model.addAttribute("category", category);
        return "salesman/menu";
    }

    @PostMapping("/add-to-sclad/{productId}")
    public ResponseEntity<Map<String, Object>>addToSclad(@PathVariable Long productId,
                             @RequestParam int quantity,
                             Model model) {
        // Получаем продукт по ID
        // Создаём Map для ответа
        Map<String, Object> response = new HashMap<>();
        Product product = productService.getProductById(productId);

        if (product != null) {
            // Увеличиваем количество на складе
            product.setStockQuantity(product.getStockQuantity() + quantity);
            productRepository.save(product); // Обновляем продукт в базе данных
            response.put("success", true);
            response.put("message", "Продукт успешно пополнен на склад.");
        } else {
            response.put("success", false);
            response.put("message", "Продукт не найден.");
            ResponseEntity.badRequest().body("Ошибка при обновлении заказа.");
        }

        // Возвращаем страницу с сообщением
        return ResponseEntity.ok(response);  // Страница с подтверждением
    }

    @GetMapping("/getCategory")
    public ResponseEntity<Map<String, Object>>addToSclad() {
        Map<String, Object> response = new HashMap<>();

        // Преобразуем Iterable в List
        List<Category> categories = new ArrayList<>();
        categoryRepository.findAll().forEach(categories::add);
        System.out.println("Categories: " + categories);
        // Добавляем список категорий в ответ
        response.put("categories", categories);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/createProduct")
    public ResponseEntity<Map<String, Object>> createProduct(
            @ModelAttribute Product product,
            @RequestParam Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        Map<String, Object> response = new HashMap<>();

        // Валидация цены
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            response.put("success", false);
            response.put("message", "Цена товара должна быть больше 0.");
            return ResponseEntity.badRequest().body(response);
        }

        // Валидация количества
        if (Objects.isNull(product.getStockQuantity()) || product.getStockQuantity() <= 0) {
            response.put("success", false);
            response.put("message", "Количество на складе должно быть больше 0.");
            return ResponseEntity.badRequest().body(response);
        }


        // Проверка на наличие изображения
        if (image != null && !image.isEmpty()) {

            String imagePath = fileStorageService.saveFile(image); // Сохранение файла
            product.setImageUrl(imagePath); // Установка пути к картинке

        } else {
            response.put("message", "Ошибка при создании товара: изображение не передано.");
            return ResponseEntity.badRequest().body(response);
        }

        System.out.println("Categories: " + categoryId);

        // Поиск категории по ID
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Категория с данным ID не найдена"));

        if (category != null) {
            product.setCategory(category);  // Устанавливаем категорию для товара
            productRepository.save(product); // Сохраняем товар в базе данных
            response.put("message", "Продукт успешно создан!");
            return ResponseEntity.ok(response); // Страница успешного создания товара
        } else {
            response.put("message", "Ошибка! Категория не найдена.");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
