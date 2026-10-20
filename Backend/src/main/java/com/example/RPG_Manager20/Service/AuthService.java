package com.example.RPG_Manager20.Service;

import com.example.RPG_Manager20.Model.DTO.Auth.LoginRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Auth.RegisterRequestDTO;
import com.example.RPG_Manager20.Model.DTO.Auth.TokenResponseDTO;
import com.example.RPG_Manager20.Model.DTO.Response.UsuarioResponseDTO;
import com.example.RPG_Manager20.Model.Entities.Usuario;
import com.example.RPG_Manager20.Repository.UsuarioRepository;
import com.example.RPG_Manager20.Security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenResponseDTO registrar(RegisterRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new BusinessException(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmailUsuario(dto.email());
        usuario.setSenhaUsuario(passwordEncoder.encode(dto.senha()));
        usuario.setNomeUsuario(dto.nome());

        usuario = usuarioRepository.save(usuario);

        String token = jwtService.gerarToken(usuario.getId(), usuario.getEmailUsuario());

        return new TokenResponseDTO(token, usuario.getId(), usuario.getEmailUsuario(), usuario.getNomeUsuario());
    }

    public TokenResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos"));

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenhaUsuario())) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos");
        }

        String token = jwtService.gerarToken(usuario.getId(), usuario.getEmailUsuario());

        return new TokenResponseDTO(token, usuario.getId(), usuario.getEmailUsuario(), usuario.getNomeUsuario());
    }
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::from)
                .toList();
    }
}
