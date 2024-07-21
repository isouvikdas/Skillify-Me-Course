package com.skillifyme.course.Skillify_Me_Course.service;

import com.skillifyme.course.Skillify_Me_Course.exception.ResourceNotFoundException;
import com.skillifyme.course.Skillify_Me_Course.model.CourseMapper;
import com.skillifyme.course.Skillify_Me_Course.model.dto.CourseDTO;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Course;
import com.skillifyme.course.Skillify_Me_Course.repository.CourseRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(courseMapper::toDto).collect(Collectors.toList());
    }

    public Optional<CourseDTO> getCourseById(ObjectId id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.map(courseMapper::toDto);
    }

    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = courseMapper.toEntity(courseDTO);
        course.setCreatedDate(LocalDateTime.now());
        course.setLastModifiedDate(LocalDateTime.now());
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDto(savedCourse);
    }

    public CourseDTO updateCourse(ObjectId id, CourseDTO courseDTO) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();
            existingCourse.setCreatedBy(courseDTO.getCreatedBy());
            existingCourse.setTitle(courseDTO.getTitle());
            existingCourse.setDescription(courseDTO.getDescription());
            existingCourse.setModules(courseMapper.toEntity(courseDTO).getModules());
            existingCourse.setLastModifiedDate(LocalDateTime.now());
            Course savedCourse = courseRepository.save(existingCourse);
            return courseMapper.toDto(savedCourse);
        } else {
            throw new ResourceNotFoundException("Course not found with id " + id);
        }
    }

    public void deleteCourse(ObjectId id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("course not found with id"+ id);
        }
    }
}
