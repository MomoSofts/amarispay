package com.amarispay.mobilemoneyservice.entities;

import lombok.Data;

@Data
public class OMPaymentRequest {

    private String merchant_key;
    private String currency;
    private String order_id;
    private double amount;
    private String return_url;
    private String cancel_url;
    private String notif_url;
    private String lang;
    private String reference;
}
