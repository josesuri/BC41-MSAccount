package com.nttdata.bcp1.msaccount.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("account")
public class Account {
    @Id
    private String id;
    private String idCustomer;
    private String idCard;
    private String accountNumber;
    private Float balance;
    //private Float maintenance;
    private Integer maxTransactions;
    private String accountType;
    private Double commission;
}
