package com.skillifyme.course.Skillify_Me_Course.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class ModuleDTO {
    private String title;
    private String content;
    private List<LessonDTO> lessons;
}
