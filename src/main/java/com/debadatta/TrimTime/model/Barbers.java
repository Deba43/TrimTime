package com.debadatta.TrimTime.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Scope("prototype")
@DynamoDBTable(tableName = "Barbers")
public class Barbers {

    @DynamoDBAutoGeneratedKey
    @DynamoDBHashKey(attributeName = "barber_id")
    private String barber_id;

    @DynamoDBAttribute(attributeName = "name")
    @NotNull(message = "Name can't be null")
    private String name;

    @DynamoDBAttribute(attributeName = "mobileNumber")
    @NotNull(message = "Mobile Number can't be null")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile Number must be 10 digits")
    private String mobileNumber;

    @DynamoDBAttribute(attributeName = "password")
    @NotNull(message = "Password can't be null")
    @Size(min = 6, message = "Your Password must be at least 6 character long")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$", message = "Password must contain at least one letter, one number, and one special character")
    private String password;

    @DynamoDBAttribute(attributeName = "barberShopName")
    @NotNull(message = "Barber Shop Name can't be null")
    private String barberShopName;

    @DynamoDBAttribute(attributeName = "location")
    @NotNull(message = "location can't be null")
    private String location;

    @DynamoDBAttribute(attributeName = "isAvailable")
    @NotNull(message = "Availability status cannot be null")
    private String isAvailable;

    @DynamoDBAttribute(attributeName = "experience")
    private Integer experience;

    @DynamoDBAttribute(attributeName = "role")
    private String role;

    @DynamoDBAttribute(attributeName = "bio")
    private String bio;

    @DynamoDBAttribute(attributeName = "profilePictureUrl")
    private String profilePictureUrl;

}
