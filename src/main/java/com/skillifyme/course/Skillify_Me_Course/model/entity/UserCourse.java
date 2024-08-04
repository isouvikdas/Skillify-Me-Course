package com.skillifyme.course.Skillify_Me_Course.model.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document("UserCourse")
public class UserCourse {
    private ObjectId id;
    private String userEmail;
    private List<ObjectId> courseId = new ArrayList<>();
}
