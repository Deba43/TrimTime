package com.debadatta.TrimTime.repo;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.debadatta.TrimTime.dto.User;

public class UserRepo {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public User save(User registeredUser) {
        dynamoDBMapper.save(registeredUser);
        return registeredUser;
    }

    public Object findByEmail(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    }

}
