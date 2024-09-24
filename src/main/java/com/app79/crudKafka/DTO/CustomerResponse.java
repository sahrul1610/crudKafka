package com.app79.crudKafka.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponse {
    private Long id;
    private String name;
    private Integer age;
}