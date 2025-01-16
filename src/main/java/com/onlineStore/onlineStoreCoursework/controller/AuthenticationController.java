package com.onlineStore.onlineStoreCoursework.controller;

import com.onlineStore.onlineStoreCoursework.model.User;
import com.onlineStore.onlineStoreCoursework.security.JWTUtil;
import com.onlineStore.onlineStoreCoursework.service.AuthService;
import com.onlineStore.onlineStoreCoursework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller; // Используем @Controller вместо @RestController
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity; // Для ResponseEntity
import org.springframework.web.bind.annotation.RequestBody; // Для @RequestBody
import org.springframework.web.bind.annotation.ResponseBody; // Для @ResponseBody
import java.util.Map; // Для Map
import org.springframework.http.HttpStatus; // Для HttpStatus

import java.util.Collection;

@Controller // Это аннотация для контроллеров, которые возвращают HTML
@RequestMapping("/auth") // Рекомендуется использовать префикс для аутентификации
public class AuthenticationController {

    private final UserService userService;
    private final AuthService authenticationService;

    private final JWTUtil jwtUtil;

    @Autowired
    public AuthenticationController(UserService userService, JWTUtil jwtUtil, AuthService authenticationService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationService = authenticationService;
    }

//    @GetMapping("/not-found")
//    public String notFoundPage() {
//        System.out.println("NOT FOUND PAGE");
//        return "notFound"; // Отображает страницу login.html из папки templates
//    }

    // Страница логина, которая будет отображаться при GET-запросе
    @GetMapping("/login")
    public String loginPage() {
        System.out.println("LOGIN PAGE GET");
        return "login"; // Отображает страницу login.html из папки templates
    }

    // Страница регистрации
    @GetMapping("/register")
    public String registerPage() {
        System.out.println("REGISTER PAGE GET");
        return "register"; // Отображает страницу register.html из папки templates
    }

    @PostMapping("/register")
    @ResponseBody // Указываем, что возвращаем данные в формате JSON
    public ResponseEntity<Map<String, String>> register(
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password) {
        try {
            System.out.println("NEW REGISTER");
            userService.registerUser(email, username, password);
            System.out.println("REGISTERED");

            // Успешный ответ
            return ResponseEntity.ok(Map.of("message", "Registration successful!"));
        } catch (RuntimeException e) {
            // Возвращаем ошибку
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        System.out.println("TRYING TO LOGIN");
        User user = userService.findByUsername(username);

        boolean authenticated = authenticationService.authenticate(username, password);

        if (!authenticated) {
            redirectAttributes.addAttribute("error", "true");
            return "redirect:/auth/login";
        }

        return "redirect:/";
    }


    public String getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            return authority.getAuthority();
        }

        return "ROLE_CLIENT";
    }

    // Разлогиниться
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        // Инвалидируем текущую сессию
        session.invalidate();
        // Удаляем данные аутентификации
        SecurityContextHolder.clearContext();
        // Дополнительно можно удалить JWT из куков, если используется
        return "redirect:/auth/login?logout=true"; // Перенаправляем на страницу логина
    }
}
