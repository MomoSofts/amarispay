package com.amarispay.mobilemoneyservice.web.dto.om;

import lombok.Data;

@Data
public class OMPaymentRequestDto {
    private String currency;
    private String orderId;
    private double amount;
    private String returnUrl;
    private String cancelUrl;
    private String notificationUrl;
    private String language;
    private String reference;
}
