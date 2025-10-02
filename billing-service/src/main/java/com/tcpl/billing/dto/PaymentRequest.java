package com.tcpl.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data // getters, setters, toString, equals, hashCode
@NoArgsConstructor // default constructor
@AllArgsConstructor // all-args constructor
public class PaymentRequest {

    private double amount;
    private String mode;
    private Date paidOn;
    private String refNo;
    private String remarks;
}
