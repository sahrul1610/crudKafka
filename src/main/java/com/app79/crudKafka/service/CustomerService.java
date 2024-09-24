package com.app79.crudKafka.service;

import com.app79.crudKafka.model.Customer;
import com.app79.crudKafka.repository.CustomerRepository;
import com.app79.crudKafka.DTO.CustomerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private KafkaTemplate<String, Customer> kafkaTemplate;

    private String receivedMessage;

    public List<CustomerResponse> list() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerResponse> response = new ArrayList<>();
        for (Customer data : customers) {
            response.add(CustomerResponse.builder()
                    .id(data.getId())
                    .name(data.getName())
                    .age(data.getAge())
                    .build());
        }
        return response;
    }

    public Customer create(Customer customer) {
        kafkaTemplate.send("data-customer", customer);
        return customerRepository.save(customer);
    }

    @Async
    @KafkaListener(topics = "data-customer")
    public void listen(String message) {
        receivedMessage = message;
        log.info(message);
    }

    public String getReceivedMessage() {
        return receivedMessage;
    }
}