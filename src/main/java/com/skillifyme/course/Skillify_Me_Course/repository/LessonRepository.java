package com.skillifyme.course.Skillify_Me_Course.repository;

import com.skillifyme.course.Skillify_Me_Course.model.entity.Lesson;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Module;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends MongoRepository<Lesson, ObjectId> {
    List<Lesson> findByInstructorEmail(String email);

    @Override
    List<Lesson> findAllById(Iterable<ObjectId> ids);
}
