package hexlet.code.controllers;

import hexlet.code.dto.AuthRequest;
import hexlet.code.utils.JWTUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthenticationController {
    private JWTUtils jwtUtils;
    private AuthenticationManager authenticationManager;

    @PostMapping("${base-url}" + "/login")
    public String create(@RequestBody AuthRequest authRequest) {
        var authentication = new UsernamePasswordAuthenticationToken(
                authRequest.email(), authRequest.password());

        authenticationManager.authenticate(authentication);

        var token = jwtUtils.generateToken(authRequest.email());
        return token;
    }
}
