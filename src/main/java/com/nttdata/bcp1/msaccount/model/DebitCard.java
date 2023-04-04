package com.nttdata.bcp1.msaccount.model;

import lombok.Data;

@Data
public class DebitCard {

    private String id;
    private String idCustomer;
    private String cardNumber;
    private String idProduct;
}
