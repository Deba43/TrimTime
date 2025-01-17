package com.debadatta.TrimTime.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.debadatta.TrimTime.dto.User;
import com.debadatta.TrimTime.dto.UserRegistrationRequest;
import com.debadatta.TrimTime.model.Customers;
import com.debadatta.TrimTime.service.AuthenticationService;
import com.debadatta.TrimTime.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String mobile_number, @RequestParam String otp) {
        Customers customer = authenticationService.authenticate(mobile_number, otp);
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestParam String refreshToken) {
        String newToken = authenticationService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(newToken);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        User registeredUser = userService.registerUser(userRegistrationRequest);

        if (registeredUser != null) {
            String role = userRegistrationRequest.getRole();
            if ("BARBER".equalsIgnoreCase(role)) {
                return ResponseEntity.ok("Redirect to /Barber/barber-registration");
            } else if ("CUSTOMER".equalsIgnoreCase(role)) {
                return ResponseEntity.ok("Redirect to /Customer/customer-registration");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/redirect")
    public void redirectUser(HttpServletRequest request, HttpServletResponse response, Authentication auth)
            throws IOException {
        if (auth == null || auth.getAuthorities().isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access");
            return;
        }

        String role = auth.getAuthorities().iterator().next().getAuthority();
        String username = (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User)
                ? ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername()
                : auth.getPrincipal().toString();

        System.out.println("User: " + username + " Role: " + role);

        String redirectUrl;
        switch (role) {
            case "ROLE_ADMIN":
                redirectUrl = "/Admin";
                break;
            case "ROLE_CUSTOMER":
                redirectUrl = "/Customers";
                break;
            case "ROLE_BARBER":
                redirectUrl = "/Barbers";
                break;
            default:
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized role");
                return;
        }

        response.sendRedirect(redirectUrl);
    }

    public static class CustomLogoutHandler extends SimpleUrlLogoutSuccessHandler {
        private final String logoutUrl;
        private final String logoutRedirectUrl;
        private final String clientId;

        public CustomLogoutHandler(String logoutUrl, String logoutRedirectUrl, String clientId) {
            this.logoutUrl = logoutUrl;
            this.logoutRedirectUrl = logoutRedirectUrl;
            this.clientId = clientId;
        }

        @Override
        protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                Authentication authentication) {
            return UriComponentsBuilder
                    .fromUri(URI.create(logoutUrl))
                    .queryParam("client_id", clientId)
                    .queryParam("logout_uri", logoutRedirectUrl)
                    .encode(StandardCharsets.UTF_8)
                    .build()
                    .toUriString();
        }
    }

}
