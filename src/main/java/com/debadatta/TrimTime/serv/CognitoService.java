package com.debadatta.TrimTime.serv;

import java.text.ParseException;
import java.util.Map;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.debadatta.TrimTime.dto.User;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;

@Service
public class CognitoService {

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Value("${aws.cognito.clientId}")
    private String clientId;

    private final AWSCognitoIdentityProvider cognitoClient;

    public CognitoService(AWSCognitoIdentityProvider cognitoClient) {
        this.cognitoClient = cognitoClient;
    }

    public boolean userExists(String username) {
        try {
            AdminGetUserRequest request = new AdminGetUserRequest()
                    .withUserPoolId(userPoolId)
                    .withUsername(username);
            cognitoClient.adminGetUser(request);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    public void createUser(String username, String role) {
        AdminCreateUserRequest createRequest = new AdminCreateUserRequest()
                .withUserPoolId(userPoolId)
                .withUsername(username)
                .withTemporaryPassword("TempPass123!")
                .withUserAttributes(
                        new AttributeType().withName("phone_number").withValue(username),
                        new AttributeType().withName("custom:role").withValue(role))
                .withMessageAction("SUPPRESS");
        cognitoClient.adminCreateUser(createRequest);
    }

    public User loginUser(String phone_no, String otp) {
        InitiateAuthRequest authRequest = new InitiateAuthRequest()
                .withAuthFlow("CUSTOM_AUTH")
                .withClientId(clientId)
                .withAuthParameters(Map.of(
                        "USERNAME", phone_no,
                        "ANSWER", otp));

        InitiateAuthResult authResult = cognitoClient.initiateAuth(authRequest);
        AuthenticationResultType authResultType = authResult.getAuthenticationResult();

        User user = new User();
        user.setAccessToken(authResultType.getAccessToken());
        user.setId(authResultType.getIdToken());
        user.setRefreshToken(authResultType.getRefreshToken());
        return user;
    }

    public String getUserIdFromToken(String idToken) {
        try {
            // Parse the JWT token
            JWT jwt = JWTParser.parse(idToken);

            return jwt.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new RuntimeException("Failed to decode JWT token", e);
        }
    }
}
