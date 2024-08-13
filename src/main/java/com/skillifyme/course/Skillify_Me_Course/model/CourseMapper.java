package com.skillifyme.course.Skillify_Me_Course.model;

import com.skillifyme.course.Skillify_Me_Course.model.dto.CourseDTO;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CourseMapper {

    @Autowired
    private ModuleMapper moduleMapper;

    public CourseDTO toDto(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setPrice(course.getPrice());
        courseDTO.setTitle(course.getTitle());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setInstructorEmail(course.getInstructorEmail());
        courseDTO.setModuleIds(course.getModuleIds());
        courseDTO.setModuleDTOS(course.getModules().stream()
                .map(moduleMapper::toDto)
                .collect(Collectors.toList()));
        return courseDTO;
    }

    public Course toEntity(CourseDTO courseDTO) {
        Course course = new Course();
        course.setId(courseDTO.getId());
        course.setPrice(courseDTO.getPrice());
        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setInstructorEmail(courseDTO.getInstructorEmail());
        course.setModuleIds(courseDTO.getModuleIds());
        course.setModules(courseDTO.getModuleDTOS().stream()
                .map(moduleMapper::toEntity)
                .collect(Collectors.toList()));
        return course;
    }

}
