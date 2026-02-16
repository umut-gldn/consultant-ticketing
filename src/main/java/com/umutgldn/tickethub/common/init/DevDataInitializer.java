package com.umutgldn.tickethub.common.init;

import com.umutgldn.tickethub.auth.repository.UserRepository;
import com.umutgldn.tickethub.company.Company;
import com.umutgldn.tickethub.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Profile("local")
@RequiredArgsConstructor
public class DevDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.existsByEmail("ahmet@techconsult.com")) {
            log.info("Test data already exists, skipping...");
            return;
        }

        // Test firma
        Company company = new Company("TechConsult", "info@techconsult.com",
                "+90 555 111 2233", Company.CompanyType.CONSULTANT);
        entityManager.persist(company);

        // Test kullanıcı (şifre: password123)
        User user = new User(company, "ahmet@techconsult.com",
                passwordEncoder.encode("password123"), "Ahmet", "Yilmaz");
        entityManager.persist(user);

        log.info("===========================================");
        log.info("  DEV TEST DATA CREATED");
        log.info("  Email:    ahmet@techconsult.com");
        log.info("  Password: password123");
        log.info("===========================================");
    }
}
