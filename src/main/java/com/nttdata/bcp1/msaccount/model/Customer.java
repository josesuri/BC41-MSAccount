package com.nttdata.bcp1.msaccount.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("customer")
public class Customer {
    @Id
    private String id;
    private String name;
    private String docType;
    private String docNumber;
    private String customerType;
}
