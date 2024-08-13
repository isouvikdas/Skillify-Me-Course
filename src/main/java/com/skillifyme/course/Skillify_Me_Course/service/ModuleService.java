package com.skillifyme.course.Skillify_Me_Course.service;

import com.skillifyme.course.Skillify_Me_Course.exception.ResourceNotFoundException;
import com.skillifyme.course.Skillify_Me_Course.exception.UnauthorizedException;
import com.skillifyme.course.Skillify_Me_Course.model.CourseMapper;
import com.skillifyme.course.Skillify_Me_Course.model.ModuleMapper;
import com.skillifyme.course.Skillify_Me_Course.model.dto.ModuleDTO;
import com.skillifyme.course.Skillify_Me_Course.model.dto.ValidateTokenResponse;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Course;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Lesson;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Module;
import com.skillifyme.course.Skillify_Me_Course.repository.CourseRepository;
import com.skillifyme.course.Skillify_Me_Course.repository.LessonRepository;
import com.skillifyme.course.Skillify_Me_Course.repository.ModuleRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModuleService {

    @Autowired
    private CourseService courseService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private CourseRepository courseRepository;

    private final String moduleNotFound = "No module found with the given id: ";
    private final String courseNotFound = "No course found with the given id: ";

    public ModuleDTO createModule(String token, ObjectId courseId, ModuleDTO moduleDTO) {
        ValidateTokenResponse response = courseService.validateInstructor(token);
        if (!response.getValid()) {
            throw new UnauthorizedException("Invalid token or user not authorized to create module.");
        }

        Module module = moduleMapper.toEntity(moduleDTO);
        module = moduleRepository.save(module);

        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.getModuleIds().add(module.getModuleId());
            courseRepository.save(course);
        } else {
            throw new ResourceNotFoundException(courseNotFound + courseId);
        }

        return moduleMapper.toDto(module);
    }

    public void deleteModule(String token, ObjectId courseId, ObjectId moduleId) {
        ValidateTokenResponse response = courseService.validateInstructor(token);
        if (!response.getValid()) {
            throw new UnauthorizedException("Invalid token or user not authorized to delete module.");
        }

        Optional<Module> moduleOptional = moduleRepository.findById(moduleId);
        if (moduleOptional.isPresent()) {
            moduleRepository.deleteById(moduleId);

            Optional<Course> courseOptional = courseRepository.findById(courseId);
            if (courseOptional.isPresent()) {
                Course course = courseOptional.get();
                course.getModuleIds().remove(moduleId);
                courseRepository.save(course);
            } else {
                throw new ResourceNotFoundException(courseNotFound + courseId);
            }
        } else {
            throw new ResourceNotFoundException(moduleNotFound + moduleId);
        }
    }

    public ModuleDTO getModuleById(ObjectId moduleId) {
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        if (optionalModule.isPresent()) {
            Module module = optionalModule.get();
            List<Lesson> lessons = module.getLessonIds().stream()
                    .map(lessonService::findLessonById)
                    .collect(Collectors.toList());

            module.setLessons(lessons);
            return moduleMapper.toDto(module);
        } else {
            throw new ResourceNotFoundException(moduleNotFound + moduleId);
        }
    }
}
