package com.app79.crudKafka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app79.crudKafka.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
