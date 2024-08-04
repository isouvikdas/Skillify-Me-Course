package com.skillifyme.course.Skillify_Me_Course.model.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "Courses")
@Data
public class Course {
    @Id
    private ObjectId id;
    private String price;
    private String title;
    private String description;
    private List<Module> modules;
    private String instructorEmail;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
