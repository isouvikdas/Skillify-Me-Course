package com.skillifyme.course.Skillify_Me_Course.service;

import com.skillifyme.course.Skillify_Me_Course.model.dto.PaymentRequest;
import com.skillifyme.course.Skillify_Me_Course.model.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@FeignClient(name = "skillifymepayment")
public interface PaymentServiceClient {

    @PostMapping("payments/create")
    PaymentResponse processPayment(@RequestBody PaymentRequest paymentRequest);
}
