package com.debadatta.TrimTime.repo;

import java.util.*;
//import java.util.List;
//import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.debadatta.TrimTime.model.Appointments;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class AppointmentsRepo {

    @Autowired
    final private DynamoDBMapper dynamoDBMapper;

    public List<Appointments> fetchAllAppointments() {
        return dynamoDBMapper.scan(Appointments.class, new DynamoDBScanExpression());
    }

    public List<Appointments> fetchAppointmentsForBarber(String barber_id) {

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":barber_id", new AttributeValue().withS(barber_id));

        DynamoDBQueryExpression<Appointments> queryExpression = new DynamoDBQueryExpression<Appointments>()
                .withKeyConditionExpression("barber_id = :barber_id")
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.query(Appointments.class, queryExpression);

    }

}
