package com.skillifyme.course.Skillify_Me_Course.controller;

import com.skillifyme.course.Skillify_Me_Course.model.LessonMapper;
import com.skillifyme.course.Skillify_Me_Course.model.ModuleMapper;
import com.skillifyme.course.Skillify_Me_Course.model.dto.LessonDTO;
import com.skillifyme.course.Skillify_Me_Course.model.dto.ModuleDTO;
import com.skillifyme.course.Skillify_Me_Course.service.LessonService;
import com.skillifyme.course.Skillify_Me_Course.service.ModuleService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("module")
public class ModuleController {

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private ModuleService moduleService;

    @PostMapping("{courseId}")
    public ResponseEntity<?> createModule(
            @RequestHeader("Authorization") String token,
            @PathVariable ObjectId courseId,
            @RequestBody ModuleDTO moduleDTO) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            ModuleDTO createdModule = moduleService.createModule(jwt, courseId, moduleDTO);
            return new ResponseEntity<>(createdModule, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("{courseId}/{moduleId}")
    public ResponseEntity<?> deleteModule(
            @PathVariable ObjectId courseId,
            @PathVariable ObjectId moduleId,
            @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            moduleService.deleteModule(jwt, courseId, moduleId);
            return ResponseEntity.ok("Course deleted successfully");
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
