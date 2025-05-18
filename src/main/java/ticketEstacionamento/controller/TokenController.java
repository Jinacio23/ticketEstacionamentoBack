package ticketEstacionamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;
import ticketEstacionamento.controller.dto.LoginRequest;
import ticketEstacionamento.controller.dto.LoginResponse;
import ticketEstacionamento.entity.Role;
import ticketEstacionamento.entity.Usuario;
import ticketEstacionamento.repository.UsuarioRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TokenController {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //Executa o login, e retorna token de acesso e de refresh
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){

        Optional<Usuario> usuario = usuarioRepository.findByNome(loginRequest.nome());
        if(usuario.isEmpty() || !usuario.get().loginCorreto(loginRequest, passwordEncoder)){
            throw new BadCredentialsException("Usuário ou senha inválido!");
        }
        Instant now = Instant.now();
        long expireJWTAccess = 300L;//5 min
        long expireJWTRefresh = 86400L;//1 dia

        var scopes = usuario.get().getRoles()
                .stream()
                .map(Role::getNome)
                .collect(Collectors.joining(" "));

        //JWT de acesso
        var claims = JwtClaimsSet.builder()
                .issuer("backend")
                .subject(usuario.get().getUsuarioId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expireJWTAccess))
                .claim("scope", scopes)
                .claim("username", usuario.get().getNome())
                .build();
        var jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        //JWT de refresh
        var refreshTokenClaims = JwtClaimsSet.builder()
                .issuer("backend")
                .subject(usuario.get().getUsuarioId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expireJWTRefresh))
                .claim("type", "refresh_token")
                .claim("scope", scopes)
                .claim("username", usuario.get().getNome())
                .build();

        String jwtRefreshToken = jwtEncoder.encode(JwtEncoderParameters.from(refreshTokenClaims)).getTokenValue();
        return ResponseEntity.ok(new LoginResponse(jwtAccessToken, jwtRefreshToken, expireJWTAccess));
    }

    //Renova o tempo de expiração do token
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        Jwt decoded = jwtDecoder.decode(token);

        // Verifica se é mesmo um refresh token
        if (!"refresh_token".equals(decoded.getClaimAsString("type"))) {
            return ResponseEntity.status(403).build(); // Forbidden
        }
        String username = decoded.getClaimAsString("username");
        if (username == ""){
            return ResponseEntity.status(403).build(); // Forbidden
        }

        // Gera novo access token
        Instant now = Instant.now();
        long accessTokenValidity = 300L;

        var claims = JwtClaimsSet.builder()
                .issuer("backend")
                .subject(decoded.getSubject())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenValidity))
                .claim("scope", decoded.getClaimAsString("scope"))
                .claim("username", username)
                .build();

        String newAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(newAccessToken, token, accessTokenValidity));
    }


}
