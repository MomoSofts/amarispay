package com.amarispay.mobilemoneyservice.web.dto.om;

import lombok.Data;

@Data
public class OMAccessTokenResponseDto {
    private String token_type;
    private String access_token;
    private String expires_in;
}
