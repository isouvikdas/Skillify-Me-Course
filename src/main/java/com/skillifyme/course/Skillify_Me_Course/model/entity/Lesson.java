package com.skillifyme.course.Skillify_Me_Course.model.entity;

import lombok.Data;
import java.util.List;

@Data
public class Lesson {
    private String title;
    private String content;
    private String videoUrl;
    private String videoTitle;
    private String videoDescription;
    private List<Quiz> quizzes;
}
