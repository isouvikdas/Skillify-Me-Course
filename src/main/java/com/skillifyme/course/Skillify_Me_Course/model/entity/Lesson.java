package com.skillifyme.course.Skillify_Me_Course.model.entity;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class Lesson {
    private ObjectId lessonId;
    private String title;
    private String content;
    private ObjectId videoId;
    private String videoTitle;
    private String videoDescription;
    private List<Quiz> quizzes;
}
