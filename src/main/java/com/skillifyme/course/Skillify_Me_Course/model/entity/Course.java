package com.skillifyme.course.Skillify_Me_Course.model.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "Courses")
@Data
public class Course {
    @Id
    private ObjectId id;
    private String title;
    private String description;
    private List<Module> modules;
    private String createdBy;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
