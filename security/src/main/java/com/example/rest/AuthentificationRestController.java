package com.example.rest;

import com.example.dto.request.RegistrationRequestDto;
import com.example.dto.response.AuthenticationResponse;
import com.example.jwt.factory.TokenFactory;
import com.example.jwt.models.Token;
import com.example.jwt.models.Tokens;
import com.example.service.AuthentificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.response.UserResponseDto;
import com.example.jwt.converter.serializer.TokenSerializer;

@RestController
@RequestMapping(value="/api/auth/")
public class   AuthentificationRestController {

    private final AuthentificationService authentificationService;

    public final AuthenticationManager authenticationManager;

    @Qualifier("refreshTokenFactory")
    private final TokenFactory<Authentication> refreshTokenFactory;

    @Qualifier("accessTokenFactory")
    private final TokenFactory<Token> accessTokenFactory;

    @Qualifier("accessTokenSerializer")
    private final TokenSerializer accessTokenSerializer;

    @Qualifier("refreshTokenSerializer")
    private final TokenSerializer refreshTokenSerializer;

    public AuthentificationRestController(
            AuthentificationService authentificationService,
            AuthenticationManager authenticationManager,
            @Qualifier("refreshTokenFactory") TokenFactory<Authentication> refreshTokenFactory,
            @Qualifier("accessTokenFactory") TokenFactory<Token> accessTokenFactory,
            @Qualifier("accessTokenSerializer") TokenSerializer accessTokenSerializer,
            @Qualifier("refreshTokenSerializer") TokenSerializer refreshTokenSerializer
    ) {
        this.authentificationService = authentificationService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenFactory = refreshTokenFactory;
        this.accessTokenFactory = accessTokenFactory;
        this.accessTokenSerializer = accessTokenSerializer;
        this.refreshTokenSerializer = refreshTokenSerializer;
    }

    // Регистрация
    @PostMapping("/register")
    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь был зарегестрирован"),
            @ApiResponse(responseCode = "400", description = "Неверно переданы данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequestDto registrationRequestDto) {

        UserResponseDto response = authentificationService.register(registrationRequestDto);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                response.username(),
                null,
                response.authorities()
        );

        Token refreshToken = refreshTokenFactory.create(authentication);
        Token accessToken = accessTokenFactory.create(refreshToken);

        String refreshTokenString = refreshTokenSerializer.serializer(refreshToken);
        String accessTokenString = accessTokenSerializer.serializer(accessToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new AuthenticationResponse(
                        "Registration successfully!",
                        new Tokens(accessTokenString,
                                accessToken.expiresAt().toString(),
                                refreshTokenString,
                                refreshToken.expiresAt().toString()),
                        response
                )
        );
    }

    // Логин
    @PostMapping("/login")
    @Operation(summary = "Вход пользователя в систему")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь был авторизован"),
            @ApiResponse(responseCode = "400", description = "Неверно переданы данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<AuthenticationResponse> login() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String username = authentication.getName();

            UserResponseDto userResponse = authentificationService.login(username);

            Token refreshToken = refreshTokenFactory.create(authentication);
            Token accessToken = accessTokenFactory.create(refreshToken);

            return ResponseEntity.ok(new AuthenticationResponse(
                    "Login successfully!",
                    new Tokens(
                            accessTokenSerializer.serializer(accessToken),
                            accessToken.expiresAt().toString(),
                            refreshTokenSerializer.serializer(refreshToken),
                            refreshToken.expiresAt().toString()
                    ),
                    userResponse
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new AuthenticationResponse(
                            "User must be authenticated",
                            null,
                            null));
        }
    }
}


