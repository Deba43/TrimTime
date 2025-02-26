package com.debadatta.TrimTime.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BarbersDto {

    @NotNull(message = "Name can't be null")
    private String name;

    @NotNull(message = "Mobile Number can't be null")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile Number must be 10 digits")
    private String phone_no;

    @NotNull(message = "Password can't be null")
    @Size(min = 6, message = "Your Password must be at least 6 character long")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$", message = "Password must contain at least one letter, one number, and one special character")
    private String password;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", message = "Invalid email format")
    private String email;

    @NotNull(message = "Barber Shop Name can't be null")
    private String barberShopName;

    @NotNull(message = "location can't be null")
    private String location;

    private Integer experience;

    private String role;

    private String bio;

}
