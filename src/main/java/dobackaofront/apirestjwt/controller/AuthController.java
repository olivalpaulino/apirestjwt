package dobackaofront.apirestjwt.controller;

import dobackaofront.apirestjwt.dominio.Usuario;
import dobackaofront.apirestjwt.repository.UsuarioRepository;
import dobackaofront.apirestjwt.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    // DTO interno para receber credenciais
    public static class LoginRequest {
        public String username;
        public String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestBody LoginRequest loginReq) {
        // 1. Verifica usuário e senha
        Usuario usuario = usuarioRepo.findByUsername(loginReq.username).orElse(null);
        if(usuario == null || !passwordEncoder.matches(loginReq.password, usuario.getPassword())) {
            // credenciais inválidas
            return ResponseEntity.status(401).body("Usuário ou senha incorretos");
        }

        // 2. Gera token JWT
        String token = jwtUtil.gerarToken(usuario.getUsername(), usuario.getRole());

        // 3. Retorna o token (e informações do usuário se quiser)
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("username", usuario.getUsername());
        resposta.put("role", usuario.getRole());
        resposta.put("token", token);
        return ResponseEntity.ok(resposta);
    }
}
