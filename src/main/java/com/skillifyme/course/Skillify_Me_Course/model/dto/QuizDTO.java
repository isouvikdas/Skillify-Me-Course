package com.skillifyme.course.Skillify_Me_Course.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuizDTO {
    private String questions;
    private List<String> options;
    private String answers;
}
