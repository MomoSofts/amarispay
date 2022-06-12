package com.amarispay.mobilemoneyservice.web.dto.om;


import lombok.Data;

@Data
public class OMPaymentFinalizeResponseDto {
    private String status;
    private String accesslayer;
    private String txnid;
    private String message;
    private String reason;
}
