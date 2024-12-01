package com.debadatta.TrimTime.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;

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

    public CognitoService(AWSCognitoIdentityProvider cognitoIdentityProvider) {
        this.cognitoIdentityProvider = cognitoIdentityProvider;
    }

    public User loginUser(String mobile_number, String otp) {

        InitiateAuthRequest authRequest = new InitiateAuthRequest()
                .withAuthFlow("CUSTOM_AUTH")
                .withClientId(clientId)
                .withAuthParameters(Map.of("USERNAME", mobile_number, "ANSWER", otp)); // Cognito OTP flow

        try {
            InitiateAuthResult authResult = cognitoIdentityProvider.initiateAuth(authRequest);
            AuthenticationResultType authResponse = authResult.getAuthenticationResult();

            String accessToken = authResponse.getAccessToken();
            String idToken = authResponse.getIdToken();
            String refreshToken = authResponse.getRefreshToken();

            // Create User object and set tokens
            User user = new User();
            user.setMobile_number(mobile_number);
            user.setAccessToken(accessToken);
            user.setAccessToken(idToken);
            user.setRefreshToken(refreshToken);

            return user;
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }

    public String refreshToken(String refreshToken) {
        InitiateAuthRequest refreshAuthRequest = new InitiateAuthRequest()
                .withAuthFlow("REFRESH_TOKEN_AUTH")
                .withClientId(clientId)
                .withAuthParameters(Map.of("REFRESH_TOKEN", refreshToken));

        try {
            InitiateAuthResult refreshResult = cognitoIdentityProvider.initiateAuth(refreshAuthRequest);
            return refreshResult.getAuthenticationResult().getAccessToken();
        } catch (Exception e) {
            throw new RuntimeException("Token refresh failed: " + e.getMessage(), e);
        }
    }

    private boolean verifyOtp(String mobileNumber, String otp) {
        InitiateAuthRequest authRequest = new InitiateAuthRequest()
                .withAuthFlow(AuthFlowType.CUSTOM_AUTH.toString()) // Use predefined AuthFlowType enum
                .withClientId(clientId) // Client ID for Cognito user pool
                .withAuthParameters(Map.of(
                        "USERNAME", mobileNumber, // Cognito expects "USERNAME" for the user identifier
                        "ANSWER", otp // "ANSWER" is the expected key for OTP in custom auth
                ));

        try {

            InitiateAuthResult authResult = cognitoIdentityProvider.initiateAuth(authRequest);

            return true;

        } catch (com.amazonaws.services.cognitoidp.model.NotAuthorizedException e) {

            System.err.println("Invalid OTP for mobile number: " + mobileNumber);
            return false;

        } catch (com.amazonaws.services.cognitoidp.model.ResourceNotFoundException e) {

            System.err.println("User not found: " + mobileNumber);
            return false;

        } catch (Exception e) {

            System.err.println("An error occurred while verifying OTP: " + e.getMessage());
            return false;
        }
    }

}
