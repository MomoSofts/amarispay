package com.amarispay.mobilemoneyservice.web.controllers;

import com.amarispay.mobilemoneyservice.entities.OMPaymentRequest;
import com.amarispay.mobilemoneyservice.utilities.OkHttpUtility;
import com.amarispay.mobilemoneyservice.web.dto.converters.DtoConverter;
import com.amarispay.mobilemoneyservice.web.dto.om.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.HashMap;


@RestController
@RequestMapping(value= "api/${vAPI}/om")
public class OrangeMoneyController {
    @Value("${om.base.url}")
    private String omBaseUrl;
    @Value("${om.application.authorization.header}")
    private String omAuthorizationHeader;
    @Value("${om.merchant.key}")
    private String omMerchantKey;
    @Value("${om.application.access-token.url}")
    private String omAccessTokenUrl;
    @Value("${om.application.create-payment.url}")
    private String omCreatePaymentUrl;
    @Value("${return_url.prefix}")
    private String returnUrlPrefix;
    @Value("${om.merchant.reference}")
    private String omMerchantReference;
    @Value("${om.application.create-payment-charge.url}")
    private String omPaymentChargesUrl;
    @Value("${om.application.finalize-payment.url}")
    private String omFinalizePaymentUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DtoConverter dtoConverter = new DtoConverter();

   @GetMapping(value="oauth-token")
   @ResponseBody
   public ResponseEntity<OMAccessTokenResponseDto> getAccessToken() throws IOException {
       HashMap<String, String> headerParameters = new HashMap<>();
       headerParameters.put("Authorization", omAuthorizationHeader);
       Response response = postCallRequest(omAccessTokenUrl, headerParameters, new FormBody.Builder().add("grant_type","client_credentials").build());
     if(response.code() == 200)
      return ResponseEntity.status(response.code()).body(objectMapper.readValue(response.body().string(), OMAccessTokenResponseDto.class));
     return ResponseEntity.status(response.code()).body(null);
   }

   @PostMapping(value="create-payment/{accessToken}")
   @ResponseBody
   public ResponseEntity<OMPaymentResponseDto> createPayment(@PathVariable("accessToken") String accessToken,
                                          @RequestBody OMPaymentRequestDto omPaymentRequestDto) throws IOException {
        omPaymentRequestDto.setReference(omMerchantReference);
        OMPaymentRequest omPaymentRequest = dtoConverter.create(omPaymentRequestDto, OMPaymentRequest.class);
        omPaymentRequest.setMerchant_key(omMerchantKey);
        HashMap<String, String> headerParameters = new HashMap<>();
     headerParameters.put("Authorization", String.format("%s %s","Bearer", accessToken));
     headerParameters.put("Accept", "application/json");
     headerParameters.put("Content-type", "application/json");
     Response response = postCallRequest(omCreatePaymentUrl, headerParameters, paymentRequestBody(omPaymentRequestDto));
     if(response.code() == 201 || response.code() == 200 )
         return ResponseEntity.status(response.code()).body(objectMapper.readValue(response.body().string(), OMPaymentResponseDto.class));
     return ResponseEntity.status(response.code()).body(null);
    }

    @PostMapping(value="/create-payment-charge")
    @ResponseBody
    public ResponseEntity<?> createPaymentCharge
            (@RequestBody OMPaymentFinalizeRequestDto omPaymentFinalizeRequestDto)
            throws IOException {
        return refactoredFinalizePayment(omPaymentChargesUrl, omPaymentFinalizeRequestDto);
    }

    @PostMapping(value="/finalize-payment")
    @ResponseBody
    public ResponseEntity<OMPaymentFinalizeResponseDto>
    finalizePayment(@RequestBody OMPaymentFinalizeRequestDto omPaymentFinalizeRequestDto)
            throws IOException {
       return (ResponseEntity<OMPaymentFinalizeResponseDto>) refactoredFinalizePayment(omFinalizePaymentUrl, omPaymentFinalizeRequestDto);
    }

    private ResponseEntity<?> refactoredFinalizePayment
            (String url, OMPaymentFinalizeRequestDto omPaymentFinalizeRequestDto) throws IOException {
        HashMap<String, String> headerParameters = new HashMap<>();
        headerParameters.put("Content-type", "application/json");
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.Companion.create(
                objectMapper.writeValueAsString(omPaymentFinalizeRequestDto),
                MediaType.get("application/json"));
        Response response = OkHttpUtility.postCallRequest(url, headerParameters, requestBody);
        return ResponseEntity.status(response.code()).body(response.body());
    }

    private Response getCallRequest(String endpoint, HashMap<String, String> parameters)
            throws IOException {
        return OkHttpUtility.getCallRequest(String.format("%s/%s",omBaseUrl, endpoint), parameters);
    }

    private Response postCallRequest(String endpoint, HashMap<String,
            String> headerParameters, okhttp3.RequestBody requestBody) throws IOException {
       return OkHttpUtility.postCallRequest(String.format("%s/%s",omBaseUrl, endpoint), headerParameters, requestBody);
    }
    private okhttp3.RequestBody paymentRequestBody(OMPaymentRequestDto omPaymentRequestDto) throws JsonProcessingException {
       OMPaymentRequest omPaymentRequest = dtoConverter.create(omPaymentRequestDto, OMPaymentRequest.class);
       omPaymentRequest.setMerchant_key(omMerchantKey);
       omPaymentRequest.setReference(omMerchantReference);
       okhttp3.RequestBody requestBody = okhttp3.RequestBody.Companion.create(
                objectMapper.writeValueAsString(omPaymentRequest),
                MediaType.get("application/json"));
       return requestBody;
    }

}
