package com.skillifyme.course.Skillify_Me_Course.controller;

import com.skillifyme.course.Skillify_Me_Course.model.LessonMapper;
import com.skillifyme.course.Skillify_Me_Course.model.dto.LessonDTO;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Lesson;
import com.skillifyme.course.Skillify_Me_Course.service.LessonService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("lesson")
public class LessonController {

    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private LessonService lessonService;

    @PostMapping("{moduleId}")
    public ResponseEntity<?> createLesson(
            @RequestHeader("Authorization") String token,
            @PathVariable ObjectId moduleId,
            @RequestBody LessonDTO lessonDTO) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            LessonDTO createdLesson = lessonService.createLesson(jwt, moduleId, lessonDTO);
            return new ResponseEntity<>(createdLesson, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("{moduleId}/{lessonId}")
    public ResponseEntity<?> deleteLesson(
            @PathVariable ObjectId moduleId,
            @PathVariable ObjectId lessonId,
            @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            lessonService.deleteLesson(jwt, moduleId, lessonId);
            return ResponseEntity.ok("Course deleted successfully");
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



}
