package com.example.RPG_Manager20.Security;

import com.example.RPG_Manager20.Model.Entities.Usuario;
import com.example.RPG_Manager20.Repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("🔍 [JWT] " + request.getMethod() + " " + request.getRequestURI());

        String header = request.getHeader("Authorization");
        System.out.println("🔍 [JWT] Header Authorization: " + header);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("🔍 [JWT] Token (início): " + token.substring(0, Math.min(30, token.length())));

            boolean valido = jwtService.tokenValido(token);
            System.out.println("🔍 [JWT] Token válido? " + valido);

            if (valido) {
                String email = jwtService.getEmail(token);
                Long idUsuario = jwtService.getUsuarioId(token);
                System.out.println("🔍 [JWT] Email: " + email + " | ID: " + idUsuario);

                Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
                System.out.println("🔍 [JWT] Usuário existe? " + usuarioOpt.isPresent());

                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    usuario,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("✅ [JWT] Autenticado: " + usuario.getEmailUsuario());
                }
            }
        } else {
            System.out.println("⚠️ [JWT] Sem header Authorization");
        }

        filterChain.doFilter(request, response);
    }
}