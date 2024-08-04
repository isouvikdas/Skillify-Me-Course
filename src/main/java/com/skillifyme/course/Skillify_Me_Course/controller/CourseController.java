package com.skillifyme.course.Skillify_Me_Course.controller;

import com.skillifyme.course.Skillify_Me_Course.model.CourseMapper;
import com.skillifyme.course.Skillify_Me_Course.model.dto.CourseDTO;
import com.skillifyme.course.Skillify_Me_Course.service.CourseService;
import jakarta.ws.rs.Path;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("course")
public class CourseController {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("courseId/{courseId}")
    public ResponseEntity<?> getCourseById(@PathVariable ObjectId courseId) {
        return new ResponseEntity<>(courseService.getCourseContent(courseId), HttpStatus.OK);
    }

    @GetMapping("/instructor")
    public ResponseEntity<List<CourseDTO>> getCoursesByInstructor(
            @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            List<CourseDTO> courses = courseService.getCoursesByInstructor(jwt);
            return ResponseEntity.ok(courses);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> createCourse(
            @RequestHeader("Authorization") String token,
            @RequestBody CourseDTO courseDTO) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            CourseDTO createdCourse = courseService.createCourse(jwt, courseDTO);
            return new ResponseEntity<>(createdCourse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(
            @PathVariable ObjectId id,
            @RequestBody CourseDTO courseDTO,
            @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            CourseDTO updatedCourse = courseService.updateCourse(id, courseDTO, jwt);
            return ResponseEntity.ok(updatedCourse);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(
            @PathVariable ObjectId id,
            @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            courseService.deleteCourse(id, jwt);
            return ResponseEntity.ok("Course deleted successfully");
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
