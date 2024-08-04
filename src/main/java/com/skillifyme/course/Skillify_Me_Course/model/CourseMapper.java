package com.skillifyme.course.Skillify_Me_Course.model;

import com.skillifyme.course.Skillify_Me_Course.model.dto.CourseDTO;
import com.skillifyme.course.Skillify_Me_Course.model.dto.LessonDTO;
import com.skillifyme.course.Skillify_Me_Course.model.dto.ModuleDTO;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Course;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Lesson;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Module;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    public CourseDTO toDto(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setPrice(course.getPrice());
        courseDTO.setTitle(course.getTitle());
        courseDTO.setDescription(course.getDescription());
        courseDTO.setInstructorEmail(course.getInstructorEmail());
        courseDTO.setModules(course.getModules().stream().map(this::toDto).collect(Collectors.toList()));
        return courseDTO;
    }

    public Course toEntity(CourseDTO courseDTO) {
        Course course = new Course();
        course.setId(courseDTO.getId());
        course.setPrice(courseDTO.getPrice());
        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setInstructorEmail(courseDTO.getInstructorEmail());
        course.setModules(courseDTO.getModules().stream().map(this::toEntity).collect(Collectors.toList()));
        return course;
    }

    private ModuleDTO toDto(Module module) {
        ModuleDTO moduleDTO = new ModuleDTO();
        moduleDTO.setTitle(module.getTitle());
        moduleDTO.setContent(module.getContent());
        moduleDTO.setLessons(module.getLessons().stream().map(this::toDto).collect(Collectors.toList()));
        return moduleDTO;
    }

    private Module toEntity(ModuleDTO moduleDTO) {
        Module module = new Module();
        module.setTitle(moduleDTO.getTitle());
        module.setContent(moduleDTO.getContent());
        module.setLessons(moduleDTO.getLessons().stream().map(this::toEntity).collect(Collectors.toList()));
        return module;
    }

    private LessonDTO toDto(Lesson lesson) {
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setTitle(lesson.getTitle());
        lessonDTO.setContent(lesson.getContent());
        lessonDTO.setVideoUrl(lesson.getVideoUrl());
        lessonDTO.setVideoTitle(lesson.getVideoTitle());
        lessonDTO.setVideoDescription(lesson.getVideoDescription());
        return lessonDTO;
    }

    private Lesson toEntity(LessonDTO lessonDTO) {
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDTO.getTitle());
        lesson.setContent(lessonDTO.getContent());
        lesson.setVideoUrl(lessonDTO.getVideoUrl());
        lesson.setVideoTitle(lessonDTO.getVideoTitle());
        lesson.setVideoDescription(lessonDTO.getVideoDescription());
        return lesson;
    }
}
