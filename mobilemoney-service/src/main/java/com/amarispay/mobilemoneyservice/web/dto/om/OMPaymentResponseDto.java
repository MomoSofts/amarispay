package com.amarispay.mobilemoneyservice.web.dto.om;

import lombok.Data;



@Data
public class OMPaymentResponseDto  {
    private int status;
    private String message;
    private String pay_token;
    private String payment_url;
    private String notif_token;



}
