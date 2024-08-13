package com.skillifyme.course.Skillify_Me_Course.model;

import com.skillifyme.course.Skillify_Me_Course.model.dto.LessonDTO;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Lesson;

public class LessonMapper {

    public LessonDTO toDto(Lesson lesson) {
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setLessonId(lesson.getLessonId());
        lessonDTO.setTitle(lesson.getTitle());
        lessonDTO.setContent(lesson.getContent());
        lessonDTO.setVideoId(lesson.getVideoId());
        lessonDTO.setVideoTitle(lesson.getVideoTitle());
        lessonDTO.setVideoDescription(lesson.getVideoDescription());
        return lessonDTO;
    }

    public Lesson toEntity(LessonDTO lessonDTO) {
        Lesson lesson = new Lesson();
        lesson.setLessonId(lessonDTO.getLessonId());
        lesson.setTitle(lessonDTO.getTitle());
        lesson.setContent(lessonDTO.getContent());
        lesson.setVideoId(lessonDTO.getVideoId());
        lesson.setVideoTitle(lessonDTO.getVideoTitle());
        lesson.setVideoDescription(lessonDTO.getVideoDescription());
        return lesson;
    }
}
