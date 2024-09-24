package com.app79.crudKafka.controller;

import com.app79.crudKafka.model.Customer;
import com.app79.crudKafka.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "Invalid request";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> list() {
        try {
            return ResponseEntity.ok(customerService.list());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Customer> createUser(@Validated @RequestBody Customer customer) {
        try {
            Customer createdCustomer = customerService.create(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }

    @GetMapping("/message")
    public ResponseEntity<String> getReceivedMessage() {
        String receivedMessage = customerService.getReceivedMessage();
        if (receivedMessage != null) {
            return ResponseEntity.ok(receivedMessage);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No message received yet.");
        }
    }
}