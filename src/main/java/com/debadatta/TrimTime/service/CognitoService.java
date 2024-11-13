package com.debadatta.TrimTime.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.debadatta.TrimTime.dto.User;
import com.debadatta.TrimTime.repo.UserRepo;

@Service
public class CognitoService {

    @Value("${spring.security.oauth2.client.registration.cognito.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.cognito.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.cognito.scope}")
    private String scope;

    @Value("${spring.security.oauth2.client.provider.cognito.issuer-uri}")
    private String issuerUri;

    @Autowired
    private AWSCognitoIdentityProvider cognitoIdentityProvider;

    @Autowired
    private UserRepo userRepo;

    public User registerUser(String username, String email, String password) {
        // Set up the AWS Cognito registration request
        SignUpRequest signUpRequest = new SignUpRequest()
                .withClientId(clientId)
                .withUsername(username)
                .withPassword(password)
                .withUserAttributes(
                        new AttributeType().withName("email").withValue(email));

        // Register the user with Amazon Cognito
        try {
            SignUpResult signUpResponse = cognitoIdentityProvider.signUp(signUpRequest);

            User registeredUser = new User();
            registeredUser.setUsername(username);
            registeredUser.setEmail(email);
            registeredUser.setPassword(password);

            return userRepo.save(registeredUser);

        } catch (Exception e) {
            throw new RuntimeException("User registration failed: " + e.getMessage(), e);
        }
    }

    public User loginUser(String username, String password) {
        // Set up the authentication request
        InitiateAuthRequest authRequest = new InitiateAuthRequest()
                .withAuthFlow("USER_PASSWORD_AUTH")
                .withClientId(clientId)
                .withAuthParameters(
                        Map.of(
                                "USERNAME", username, // Use email as the username
                                "PASSWORD", password));

        try {
            InitiateAuthResult authResult = cognitoIdentityProvider.initiateAuth(authRequest);
            System.out.println(authResult);
            AuthenticationResultType authResponse = authResult.getAuthenticationResult();

            // At this point, the user is successfully authenticated, and you can access JWT
            // tokens:
            String accessToken = authResponse.getAccessToken();
            String idToken = authResponse.getIdToken();
            String refreshToken = authResponse.getRefreshToken();

            // You can decode and verify the JWT tokens for user information

            User loggedInUser = new User();
            loggedInUser.setUsername(username);
            loggedInUser.setAccessToken(accessToken); // Store the token for future requests

            return loggedInUser;

        } catch (Exception e) {
            throw new RuntimeException("User login failed: " + e.getMessage(), e);
        }
    }

}
