package com.amarispay.mobilemoneyservice.web.dto.om;

import lombok.Data;

@Data
public class OMPaymentFinalizeRequestDto {
    private String Token;
    private String Msisdn;
    private String Otp;
}
