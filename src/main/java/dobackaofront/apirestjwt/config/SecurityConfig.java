package dobackaofront.apirestjwt.config;

import dobackaofront.apirestjwt.controller.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Desabilita CSRF para uso de tokens JWT
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // ^ Não guardar estado de sessão, stateless
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()   // libera endpoints de autenticação (login)
                        .anyRequest().authenticated()              // demais requisições precisam de autenticação
                )
                .logout(logout -> logout.disable())            // desabilita logout (opcional, já que sem sessão não há logout)
                .formLogin(form -> form.disable())             // não usamos formulário de login
                .httpBasic(basic -> basic.disable());          // desabilita auth básica
        // Registrando o filtro JWT antes da cadeia de filtro padrão do UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

