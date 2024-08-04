package com.skillifyme.course.Skillify_Me_Course.model.dto;

import lombok.Data;

@Data
public class PaymentResponse {
    private boolean success;
    private String paymentId;
    private String payerId;
    private String message;
}
