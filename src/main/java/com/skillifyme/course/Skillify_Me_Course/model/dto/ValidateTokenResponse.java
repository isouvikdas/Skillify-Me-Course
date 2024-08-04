package com.skillifyme.course.Skillify_Me_Course.model.dto;

import lombok.Data;

@Data
public class ValidateTokenResponse {
    String email;
    Boolean valid;
}
