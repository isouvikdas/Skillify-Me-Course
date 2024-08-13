package com.skillifyme.course.Skillify_Me_Course.service;

import com.skillifyme.course.Skillify_Me_Course.exception.ResourceNotFoundException;
import com.skillifyme.course.Skillify_Me_Course.exception.UnauthorizedException;
import com.skillifyme.course.Skillify_Me_Course.model.CourseMapper;
import com.skillifyme.course.Skillify_Me_Course.model.ModuleMapper;
import com.skillifyme.course.Skillify_Me_Course.model.dto.CourseDTO;
import com.skillifyme.course.Skillify_Me_Course.model.dto.ModuleDTO;
import com.skillifyme.course.Skillify_Me_Course.model.dto.ValidateTokenResponse;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Course;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Module;
import com.skillifyme.course.Skillify_Me_Course.model.entity.UserCourse;
import com.skillifyme.course.Skillify_Me_Course.repository.CourseRepository;
import com.skillifyme.course.Skillify_Me_Course.repository.ModuleRepository;
import com.skillifyme.course.Skillify_Me_Course.repository.UserCourseRepository;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ModuleMapper moduleMapper;

    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        for (Course course : courses) {
            List<Module> modules = course.getModuleIds().stream()
                    .map(moduleService::getModuleById)
                    .map(moduleMapper::toEntity)
                    .collect(Collectors.toList());
            course.setModules(modules);
        }

        return courses.stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }


    public List<CourseDTO> getCoursesByInstructor(String token) throws UnauthorizedException {
        ValidateTokenResponse response = validateInstructor(token);

        if (response.getValid()) {
            String instructorEmail = response.getEmail();
            List<Course> courses = courseRepository.findByInstructorEmail(instructorEmail);
            for (Course course : courses) {
                List<ModuleDTO> moduleDTOs = course.getModuleIds().stream()
                        .map(moduleService::getModuleById)
                        .toList();

                List<Module> modules = moduleDTOs.stream()
                        .map(moduleMapper::toEntity)
                        .collect(Collectors.toList());
                course.setModules(modules);
            }
            return courses.stream().
                    map(courseMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            throw new UnauthorizedException("Invalid or Unauthorized token");
        }
    }


    public ValidateTokenResponse validateInstructor(String token) {
        Map<String, String> payload = new HashMap<>();
        payload.put("token", token);
        ResponseEntity<ValidateTokenResponse> responseEntity = authServiceClient.validateInstructor(payload);
        ValidateTokenResponse response = new ValidateTokenResponse();
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            response.setValid(Objects.requireNonNull(responseEntity.getBody()).getValid());
            response.setEmail(responseEntity.getBody().getEmail());
        } else {
            response.setValid(false);
        }

        return response;
    }

    public ValidateTokenResponse validateUser(String token) {
        Map<String, String> payload = new HashMap<>();
        payload.put("token", token);
        ResponseEntity<ValidateTokenResponse> responseEntity = authServiceClient.validateUser(payload);
        ValidateTokenResponse response = new ValidateTokenResponse();
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            response.setValid(Objects.requireNonNull(responseEntity.getBody()).getValid());
            response.setEmail(responseEntity.getBody().getEmail());
        } else {
            response.setValid(false);
        }
        return response;
    }

    public CourseDTO createCourse(String token, CourseDTO courseDTO) throws UnauthorizedException {
        ValidateTokenResponse response = validateInstructor(token);
        if (!response.getValid()) {
            throw new UnauthorizedException("Invalid token or user not authorized to create course.");
        }
        String email = response.getEmail();
        Course course = courseMapper.toEntity(courseDTO);
        course.setInstructorEmail(email);
        course.setCreatedDate(LocalDateTime.now());
        course.setLastModifiedDate(LocalDateTime.now());
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDto(savedCourse);
    }

    public CourseDTO updateCourse(ObjectId id, CourseDTO courseDTO, String token) {
        ValidateTokenResponse response = validateInstructor(token);

        if (!response.getValid()) {
            throw new UnauthorizedException("Invalid or unauthorized token");
        }

        String instructorEmailFromToken = response.getEmail();

        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();

            if (!existingCourse.getInstructorEmail().equals(instructorEmailFromToken)) {
                throw new UnauthorizedException("You are not authorized to update this course");
            }

            existingCourse.setTitle(courseDTO.getTitle());
            existingCourse.setDescription(courseDTO.getDescription());
            existingCourse.setModuleIds(courseMapper.toEntity(courseDTO).getModuleIds());
            existingCourse.setLastModifiedDate(LocalDateTime.now());
            Course savedCourse = courseRepository.save(existingCourse);
            return courseMapper.toDto(savedCourse);
        } else {
            throw new ResourceNotFoundException("Course not found with id " + id);
        }
    }

    public void deleteCourse(ObjectId id, String token) {
        ValidateTokenResponse response = validateInstructor(token);

        if (!response.getValid()) {
            throw new UnauthorizedException("Invalid or unauthorized token");
        }

        String instructorEmailFromToken = response.getEmail();

        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();

            if (!existingCourse.getInstructorEmail().equals(instructorEmailFromToken)) {
                throw new UnauthorizedException("You are not authorized to update this course");
            }
            courseRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Course not found with id " + id);
        }
    }

    public CourseDTO getCourseContent(ObjectId courseId) throws ResourceNotFoundException {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();

            List<ModuleDTO> moduleDTOs = course.getModuleIds().stream()
                    .map(moduleService::getModuleById)
                    .toList();

            List<Module> modules = moduleDTOs.stream()
                    .map(moduleMapper::toEntity)
                    .collect(Collectors.toList());

            course.setModules(modules);

            return courseMapper.toDto(course);
        } else {
            throw new ResourceNotFoundException("Course not found with id " + courseId);
        }
    }

    public List<CourseDTO> getAllCoursesOfUser(String userEmail) throws ResourceNotFoundException {
        UserCourse userCourse = userCourseRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UnauthorizedException("User has not purchased any course"));
        List<ObjectId> courseIds = userCourse.getCourseId();
        List<Course> courses = courseRepository.findAllById(courseIds);

        for (Course course : courses) {
            List<Module> modules = course.getModuleIds().stream()
                    .map(moduleService::getModuleById)
                    .map(moduleMapper::toEntity)
                    .collect(Collectors.toList());
            course.setModules(modules);
        }

        return courses.stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }
}
