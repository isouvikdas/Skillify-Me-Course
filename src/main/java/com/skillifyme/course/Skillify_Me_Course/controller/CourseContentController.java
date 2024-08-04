package com.skillifyme.course.Skillify_Me_Course.controller;

import com.skillifyme.course.Skillify_Me_Course.exception.UnauthorizedException;
import com.skillifyme.course.Skillify_Me_Course.model.dto.CourseDTO;
import com.skillifyme.course.Skillify_Me_Course.model.dto.ValidateTokenResponse;
import com.skillifyme.course.Skillify_Me_Course.model.entity.UserCourse;
import com.skillifyme.course.Skillify_Me_Course.repository.UserCourseRepository;
import com.skillifyme.course.Skillify_Me_Course.service.CourseService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class CourseContentController {

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private CourseService courseService;

    @GetMapping("course/{courseId}")
    public ResponseEntity<?> getCourseContent(
            @PathVariable ObjectId courseId,
            @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            ValidateTokenResponse response = courseService.validateUser(jwt);
            if (!response.getValid()) {
                throw new UnauthorizedException("Invalid or unauthorized token");
            }
            String userEmail = response.getEmail();
            userCourseRepository.findByUserEmailAndCourseId(userEmail, courseId)
                    .orElseThrow(() -> new UnauthorizedException("User has not purchased this course"));
            CourseDTO content = courseService.getCourseContent(courseId);
            return ResponseEntity.ok(content);
        } else {
            return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("course/all")
    public ResponseEntity<?> getCoursesOfUser(
            @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            ValidateTokenResponse response = courseService.validateUser(jwt);
            if (!response.getValid()) {
                throw new UnauthorizedException("Invalid or unauthorized token");
            }
            String userEmail = response.getEmail();

            List<CourseDTO> courseDTOS = courseService.getAllCoursesOfUser(userEmail);
            return ResponseEntity.ok(courseDTOS);
        } else {
            return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("courseId/{courseId}")
    public ResponseEntity<?> checkCourseBuyingDetails(
            @PathVariable ObjectId courseId,
            @RequestBody Map<String, String> payload) {
        String userEmail = payload.get("userEmail");
        UserCourse course = userCourseRepository.findByUserEmailAndCourseId(userEmail, courseId)
                .orElseThrow(() -> new UnauthorizedException("User has not purchased this course"));
        return new ResponseEntity<>(course, HttpStatus.OK);
    }
}
