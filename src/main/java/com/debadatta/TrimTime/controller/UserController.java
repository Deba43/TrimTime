package com.debadatta.TrimTime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.bind.annotation.*;

import com.debadatta.TrimTime.dto.User;
import com.debadatta.TrimTime.dto.UserLoginRequest;
import com.debadatta.TrimTime.dto.UserRegistrationRequest;
import com.debadatta.TrimTime.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        System.out.println(userRegistrationRequest);

        User registeredUser = userService.registerUser(userRegistrationRequest);
        if (registeredUser != null) {
            return ResponseEntity.ok(registeredUser);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        User loggedInUser = userService.loginUser(userLoginRequest.getMobileNumber());

        if (loggedInUser != null) {

            return ResponseEntity.ok(loggedInUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok("User logged out successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not logged in.");
        }
    }

    @GetMapping("/redirect")
    public void redirectUser(HttpServletRequest request, HttpServletResponse response, Authentication auth)
            throws Exception {
        if (auth != null) {
            String role = auth.getAuthorities().iterator().next().getAuthority();
            String username = (String) auth.getPrincipal();
            System.out.println(username + " has role: " + role);

            switch (role) {
                case "ADMIN":
                    response.sendRedirect("/Admin");
                    break;
                case "CUSTOMER":
                    response.sendRedirect("/Customers");
                    break;
                case "BARBER":
                    response.sendRedirect("/Barbers");
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized role");
                    break;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access");
        }
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
