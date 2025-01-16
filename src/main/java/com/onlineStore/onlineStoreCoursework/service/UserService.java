package com.onlineStore.onlineStoreCoursework.service;

import com.onlineStore.onlineStoreCoursework.model.User;
import com.onlineStore.onlineStoreCoursework.model.User.Role;
import com.onlineStore.onlineStoreCoursework.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Автоматически внедряется бин

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void registerUser(String email, String username, String password) {
        // Проверяем, существует ли уже пользователь с таким именем
        if (userRepository.findByUsername(username) != null) {
            logger.error("User with username '{}' already exists!", username);
            throw new RuntimeException("User already exists");
        }
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("Email already registered");
        }

        System.out.println("Registering new user with username: " + username);

        try {
            // Хэшируем пароль
            String encodedPassword = passwordEncoder.encode(password);

            // Создаем нового пользователя с ролью ROLE_CLIENT
            User user = new User();
            user.setUsername(username);
            user.setPassword(encodedPassword);
            user.setRole(Role.CLIENT);
            user.setEmail(email);  // Возможно, стоит заменить на фактическую информацию
            user.setCreatedAt(LocalDateTime.now());

            // Сохраняем пользователя в базе данных
            userRepository.save(user);

            System.out.println("User with username "+username+" and pass "+password+" successfully registered.");

        } catch (Exception exception) {
            logger.error("Error while registering user: {}", exception.getMessage());
            throw new RuntimeException("Registration failed: " + exception.getMessage());
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);

        if (user.getId() == null) {
            throw new UsernameNotFoundException("Пользователь " + username + " не найден");
        }

        return user;
    }

    // Метод для добавления баланса пользователю
    public User addBalance(Long userId, double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        user.addBalance(amount);
        return userRepository.save(user);
    }

    public List<User> findAllByRole(User.Role role) {
        return userRepository.findAllByRole(role);
    }

    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(() ->
                new IllegalArgumentException("Пользователь не найден"));

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());
        existingUser.setBalance(user.getBalance());

        userRepository.save(existingUser);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}

