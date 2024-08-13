package com.skillifyme.course.Skillify_Me_Course.model.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class CourseDTO {
    private ObjectId id;
    private String price;
    private String title;
    private String description;
    private List<ObjectId> moduleIds;
    private List<ModuleDTO> moduleDTOS;
    private String instructorEmail;
}
