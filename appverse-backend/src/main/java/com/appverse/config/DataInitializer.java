package com.appverse.config;

import com.appverse.entity.Category;
import com.appverse.entity.User;
import com.appverse.enums.Role;
import com.appverse.repository.CategoryRepository;
import com.appverse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UserRepository userRepository, CategoryRepository categoryRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin user
            if (!userRepository.existsByUsername("admin")) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@appverse.com")
                        .password(passwordEncoder.encode("admin123"))
                        .fullName("Admin User")
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(admin);
            }

            // Create developer user
            if (!userRepository.existsByUsername("developer")) {
                User developer = User.builder()
                        .username("developer")
                        .email("dev@appverse.com")
                        .password(passwordEncoder.encode("dev123"))
                        .fullName("Developer User")
                        .role(Role.DEVELOPER)
                        .build();
                userRepository.save(developer);
            }

            // Create default user
            if (!userRepository.existsByUsername("user")) {
                User user = User.builder()
                        .username("user")
                        .email("user@appverse.com")
                        .password(passwordEncoder.encode("user123"))
                        .fullName("Regular User")
                        .role(Role.USER)
                        .build();
                userRepository.save(user);
            }

            // Create categories
            String[] categories = {"Productivity", "Games", "Education", "Social", "Entertainment", "Health & Fitness", "Finance", "Utilities"};
            for (String catName : categories) {
                if (!categoryRepository.existsByName(catName)) {
                    Category category = Category.builder()
                            .name(catName)
                            .description(catName + " applications")
                            .build();
                    categoryRepository.save(category);
                }
            }
        };
    }
}
