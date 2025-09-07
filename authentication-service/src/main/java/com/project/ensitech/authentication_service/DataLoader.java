package com.project.ensitech.authentication_service;

import com.project.ensitech.authentication_service.model.entity.User;
import com.project.ensitech.authentication_service.model.enumeration.Role;
import com.project.ensitech.authentication_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@ensitech.com").isEmpty()) {
            User user = new User();
            // user.setUsername("admin");
            user.setEmail("admin@ensitech.com");
            user.setPassword(encoder.encode("admin123"));
            user.setRole(Role.SUPER_ADMIN);
            userRepository.save(user);
            System.out.println("SUPER_ADMIN créé avec succès");
        }

       if (userRepository.findByEmail("directeur@ensitech.com").isEmpty()) {
            User user = new User();
            user.setEmail("directeur@ensitech.com");
            user.setPassword(encoder.encode("1234567"));
            user.setRole(Role.DIRECTEUR);
            userRepository.save(user);
            System.out.println("DIRECTEUR créé avec succès");
        }

        if (userRepository.findByEmail("resp-etude@ensitech.com").isEmpty()) {
            User user = new User();
            user.setEmail("resp-etude@ensitech.com");
            user.setPassword(encoder.encode("1234567"));
            user.setRole(Role.RESPONSABLE_ETUDE);
            userRepository.save(user);
            System.out.println("RESPONSABLE_ETUDES créé avec succès");
        }
    }
}
