package com.onlineStore.onlineStoreCoursework.controller;


import com.onlineStore.onlineStoreCoursework.model.Order;
import com.onlineStore.onlineStoreCoursework.model.User;
import com.onlineStore.onlineStoreCoursework.model.Category;
import com.onlineStore.onlineStoreCoursework.service.OrderService;
import com.onlineStore.onlineStoreCoursework.service.UserService;
import com.onlineStore.onlineStoreCoursework.service.CategoryService;
import com.onlineStore.onlineStoreCoursework.service.UserService;
import com.onlineStore.onlineStoreCoursework.model.OrderProductViewModel;
import com.onlineStore.onlineStoreCoursework.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @GetMapping("/orders")
    public String getOrdersPage() {
        return "admin/orders";
    }
    // Обработка POST запроса на добавление новой категории
    @PostMapping("/category")
    public String addCategory(String name, String description, Model model) {
        // Создаем и сохраняем новую категорию
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        categoryService.saveCategory(category);

        // Добавляем сообщение об успехе на страницу
        model.addAttribute("message", "Категория успешно добавлена!");

        // Перенаправляем обратно на страницу с меню администратора
        return "admin/orders";
    }


    @GetMapping("/get-users")
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/get-user-data")
    @ResponseBody
    public User getUserData(@RequestParam Long userId) {
        System.out.println(userId);
        User aa = userService.getUserById(userId);
        System.out.println(aa.getUsername());
        return userService.getUserById(userId);
    }

    @PostMapping("/edit-user")
    @ResponseBody
    public ResponseEntity<String> editUser(@RequestParam Long id,
                           @RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String role,
                           @RequestParam Double balance) {

        try {
            User user = userService.getUserById(id);
            user.setUsername(username);
            user.setEmail(email);
            user.setRole(User.Role.valueOf(role));  // Преобразуем строку в роль
            //user.setBalance(balance);

            userService.updateUser(user);

            // Возвращаем успешный ответ с HTTP статусом 200
            return ResponseEntity.ok("User updated successfully");
        } catch (IllegalArgumentException e) {
            // В случае ошибки с ролью
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid role provided");
        } catch (Exception e) {
            // В случае других ошибок
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the user");
        }
    }
}
