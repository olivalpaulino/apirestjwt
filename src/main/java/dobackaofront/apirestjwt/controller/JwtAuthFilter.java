package dobackaofront.apirestjwt.controller;

import dobackaofront.apirestjwt.dominio.Usuario;
import dobackaofront.apirestjwt.repository.UsuarioRepository;
import dobackaofront.apirestjwt.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UsuarioRepository usuarioRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // remove "Bearer " prefix, obtendo só o token
            try {
                username = jwtUtil.getUsernameFromToken(token);
            } catch (JwtException e) {
                // Token inválido ou expirado
                // Podemos registrar log, mas simplesmente não autenticaremos o usuário
            }
        }

        // Se obtivemos um username válido do token e ainda não há usuário autenticado no contexto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carrega usuário do banco (poderíamos confiar só no token,
            // mas é bom verificar se usuário ainda existe e pegar detalhes/roles atualizados)
            Usuario usuario = usuarioRepo.findByUsername(username).orElse(null);
            if (usuario != null) {
                // Valida token novamente com segurança (incluindo expiração)
                try {
                    jwtUtil.validarToken(token); // lança exceção se inválido
                } catch (JwtException e) {
                    filterChain.doFilter(request, response);
                    return; // Token inválido: aborta filtro sem setar autenticação
                }
                // Cria objeto de autenticação para o Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                usuario.getUsername(), null,
                                // Cria lista de authorities a partir da role do usuário
                                org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_" + usuario.getRole())
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Coloca a autenticação no contexto do Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}
