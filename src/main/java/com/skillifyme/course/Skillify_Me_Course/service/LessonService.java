package com.skillifyme.course.Skillify_Me_Course.service;

import com.skillifyme.course.Skillify_Me_Course.exception.ResourceNotFoundException;
import com.skillifyme.course.Skillify_Me_Course.exception.UnauthorizedException;
import com.skillifyme.course.Skillify_Me_Course.model.LessonMapper;
import com.skillifyme.course.Skillify_Me_Course.model.dto.LessonDTO;
import com.skillifyme.course.Skillify_Me_Course.model.dto.ValidateTokenResponse;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Lesson;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Module;
import com.skillifyme.course.Skillify_Me_Course.repository.LessonRepository;
import com.skillifyme.course.Skillify_Me_Course.repository.ModuleRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Service
public class LessonService {

    private final String lessonNotFound = "No lesson found with the given id: ";
    private final String moduleNotFound = "No Module found with the given id: ";

    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseService courseService;

    public LessonDTO createLesson(String token, ObjectId moduleId, LessonDTO lessonDTO) {
        ValidateTokenResponse response = courseService.validateInstructor(token);
        if (!response.getValid()) {
            throw new UnauthorizedException("Invalid token or user not authorized to create course.");
        }
        Optional<Module> optionalModule = moduleRepository.findById(moduleId);
        if (optionalModule.isPresent()) {
            Module module = optionalModule.get();
            Lesson lesson = lessonMapper.toEntity(lessonDTO);
            lesson.setVideoId(null);
            lesson = lessonRepository.save(lesson);
            module.getLessonIds().add(lesson.getLessonId());
            Lesson savedModule = lessonRepository.save(lesson);
            return lessonMapper.toDto(savedModule);
        } else {
            throw new ResourceNotFoundException(moduleNotFound + moduleId);
        }
    }

    public void deleteLesson(String token, ObjectId moduleId, ObjectId lessonId) {
        ValidateTokenResponse response = courseService.validateInstructor(token);
        if (!response.getValid()) {
            throw new UnauthorizedException("Invalid token or user not authorized to create course.");
        }
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonId);
        if (lessonOptional.isPresent()) {
            lessonRepository.deleteById(lessonId);

            Optional<Module> moduleOptional = moduleRepository.findById(moduleId);
            if (moduleOptional.isPresent()) {
                Module module = moduleOptional.get();
                module.getLessonIds().remove(lessonId);
                moduleRepository.save(module);
            } else {
                throw new ResourceNotFoundException(moduleNotFound + moduleId);
            }
        } else {
            throw new ResourceNotFoundException(lessonNotFound + lessonId);
        }
    }

    @PostMapping("/lessons/{lessonId}/video")
    public boolean updateLessonVideo(@PathVariable ObjectId lessonId, @RequestParam ObjectId videoId) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonId);
        if (lessonOptional.isPresent()) {
            Lesson lesson = lessonOptional.get();
            lesson.setVideoId(videoId);
            lessonRepository.save(lesson);
            return true;
        } else {
            throw new ResourceNotFoundException(lessonNotFound + lessonId);
        }
    }

    public LessonDTO getLessonById(ObjectId lessonId) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonId);
        if (lessonOptional.isPresent()) {
            Lesson lesson = lessonOptional.get();
            return lessonMapper.toDto(lesson);
        } else {
            throw new ResourceNotFoundException(lessonNotFound + lessonId);
        }
    }

    public Lesson findLessonById(ObjectId lessonId) {
        Optional<Lesson> lessonOptional = lessonRepository.findById(lessonId);
        if (lessonOptional.isPresent()) {
            return lessonOptional.get();
        } else {
            throw new ResourceNotFoundException(lessonNotFound + lessonId);
        }
    }
}
