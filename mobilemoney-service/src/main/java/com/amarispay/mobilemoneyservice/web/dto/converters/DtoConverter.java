package com.amarispay.mobilemoneyservice.web.dto.converters;

import com.amarispay.mobilemoneyservice.entities.OMPaymentRequest;
import com.amarispay.mobilemoneyservice.web.dto.om.OMPaymentRequestDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DtoConverter {
    private final ModelMapper modelMapper;



    public DtoConverter(){
        this.modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        initializeModelMapper(modelMapper);
    }
    private void initializeModelMapper(ModelMapper modelMapper) {
        {
            final TypeMap<OMPaymentRequestDto, OMPaymentRequest> typeMap =
                    modelMapper.createTypeMap(OMPaymentRequestDto.class, OMPaymentRequest.class);
            typeMap.addMapping(OMPaymentRequestDto::getCurrency, OMPaymentRequest::setCurrency);
            typeMap.addMappings(mapper -> mapper.using(ctx ->{return UUID.randomUUID().toString();})
                                                .map(OMPaymentRequestDto::getOrderId, OMPaymentRequest::setOrder_id));
            typeMap.addMapping(OMPaymentRequestDto::getAmount, OMPaymentRequest::setAmount);
            typeMap.addMapping(OMPaymentRequestDto::getReturnUrl, OMPaymentRequest::setReturn_url);
            typeMap.addMapping(OMPaymentRequestDto::getCancelUrl, OMPaymentRequest::setCancel_url);
            typeMap.addMapping(OMPaymentRequestDto::getNotificationUrl, OMPaymentRequest::setNotif_url);
            typeMap.addMapping(OMPaymentRequestDto::getLanguage, OMPaymentRequest::setLang);
            typeMap.addMapping(OMPaymentRequestDto::getReference, OMPaymentRequest::setReference);
        }
    }
    public <TFrom, TTo> TTo create(TFrom from, Class<TTo> toClass) {
        return modelMapper.map(from, toClass);
    }


    public <TFrom, TTo> List<TTo> createList(List<TFrom> source, Class<TTo> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
