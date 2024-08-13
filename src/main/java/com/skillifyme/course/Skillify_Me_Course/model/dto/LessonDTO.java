package com.skillifyme.course.Skillify_Me_Course.model.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class LessonDTO {
    private ObjectId lessonId;
    private String title;
    private String content;
    private String videoId;
    private String videoTitle;
    private String videoDescription;
    private List<QuizDTO> quizzes;
}
