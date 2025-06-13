package dobackaofront.apirestjwt.config;

import dobackaofront.apirestjwt.dominio.Usuario;
import dobackaofront.apirestjwt.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {
    @Bean
    CommandLineRunner loadInitialData(UsuarioRepository usuarioRepo) {
        return args -> {
            // Dentro do CommandLineRunner em DataLoader:
            if(usuarioRepo.count() == 0) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole("ADMIN");
                usuarioRepo.save(admin);

                Usuario comum = new Usuario();
                comum.setUsername("joao");
                comum.setPassword(encoder.encode("12345"));
                comum.setRole("USER");
                usuarioRepo.save(comum);
            }
        };
    }
}
