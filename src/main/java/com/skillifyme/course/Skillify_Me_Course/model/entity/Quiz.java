package com.skillifyme.course.Skillify_Me_Course.model.entity;

import lombok.Data;
import java.util.List;

@Data
public class Quiz {
    private String questions;
    private List<String> options;
    private String answers;
}
