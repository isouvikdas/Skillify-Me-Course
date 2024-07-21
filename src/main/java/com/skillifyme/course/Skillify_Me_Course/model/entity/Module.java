package com.skillifyme.course.Skillify_Me_Course.model.entity;

import lombok.Data;
import java.util.List;

@Data
public class Module {
    private String title;
    private String content;
    private List<Lesson> lessons;
}
