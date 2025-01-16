package com.onlineStore.onlineStoreCoursework.controller;


import com.onlineStore.onlineStoreCoursework.model.User;
import com.onlineStore.onlineStoreCoursework.model.Order;
import com.onlineStore.onlineStoreCoursework.model.Product;
import com.onlineStore.onlineStoreCoursework.model.Category;
import com.onlineStore.onlineStoreCoursework.model.OrderProductViewModel;
import com.onlineStore.onlineStoreCoursework.service.OrderService;
import com.onlineStore.onlineStoreCoursework.service.ProductService;
import com.onlineStore.onlineStoreCoursework.service.UserService;
import com.onlineStore.onlineStoreCoursework.repository.ProductRepository;
import com.onlineStore.onlineStoreCoursework.repository.CategoryRepository;
import com.onlineStore.onlineStoreCoursework.repository.OrderRepository;
import com.onlineStore.onlineStoreCoursework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;


    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // 4. Получение всех товаров
    @GetMapping("/products")
    public String getAllСategory(Model model) {
        Iterable<Category> category = categoryRepository.findAll();
        category.forEach(ca -> System.out.println(ca.getName()));
        model.addAttribute("category", category);
        return "client/products";
    }
    // 7. Личный кабинет
    @GetMapping("/account")
    public String getAccountPage(Model model) {
        // Здесь можно добавить атрибуты в модель, если необходимо
        User user = userService.getCurrentUser();
        double balance = user.getBalance();
        List<OrderProductViewModel> orders = orderService.getOrdersWithProductsAndUsers(user.getId());
        orders.forEach(ca -> System.out.println(ca.getProductName()));
        model.addAttribute("balance", balance);
        model.addAttribute("order", orders);
        return "client/account"; // Имя представления (например, account.html)
    }

    // 5. Получение товаров определенной категории
    @GetMapping("/products/{categoryId}")
    @ResponseBody
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> productList = productRepository.findByCategory_CategoryId(categoryId);
        productList.forEach(ca -> System.out.println(ca.getName()));
        // Фильтруем продукты, у которых количество больше 0
        List<Product> filteredProducts = productList.stream()
                .filter(product -> product.getStockQuantity() > 0)
                .collect(Collectors.toList());
        // Выводим имена отфильтрованных товаров в консоль (для отладки)
        filteredProducts.forEach(ca -> System.out.println(ca.getName()));

        // Возвращаем отфильтрованный список продуктов
        return filteredProducts;
    }

    // 5. Получение товаров определенной категории
    @GetMapping("/products1/{categoryId}")
    @ResponseBody
    public List<Product> getProductsByAllCategory(@PathVariable Long categoryId) {
        List<Product> productList = productRepository.findByCategory_CategoryId(categoryId);
        // Возвращаем отфильтрованный список продуктов
        return productList;
    }

    // 1. Покупка товаров
    @PostMapping("/buy/{productId}")
    public ResponseEntity<String> buyProduct(@PathVariable Long productId) {
        User user = userService.getCurrentUser();
        Order success = orderService.createOrder(user.getId(), productId);
        if (success != null) {
            productService.decrementProductStock(productId); // уменьшить кол-во на складе
            return ResponseEntity.ok("Товар успешно куплен!");
        }
        return ResponseEntity.badRequest().body("Недостаточно средств");
    }

    // 2. Пополнение баланса
    @PostMapping("/balance")
    public ResponseEntity<Map<String, Object>> addBalance(@RequestParam double amount) {
        User user = userService.getCurrentUser();
        User updatedUser = userService.addBalance(user.getId(), amount);
        Map<String, Object> response = new HashMap<>();
        if (updatedUser != null) {
            response.put("message", "Баланс успешно пополнен.");
            response.put("balance", updatedUser.getBalance());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Ошибка при пополнении баланса.");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
