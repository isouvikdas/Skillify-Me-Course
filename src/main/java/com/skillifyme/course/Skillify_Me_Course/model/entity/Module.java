package com.skillifyme.course.Skillify_Me_Course.model.entity;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class Module {
    private ObjectId moduleId;
    private String title;
    private String content;
    private List<ObjectId> lessonIds;
    private List<Lesson> lessons;
}
