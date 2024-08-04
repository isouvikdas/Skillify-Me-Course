package com.skillifyme.course.Skillify_Me_Course.repository;

import com.skillifyme.course.Skillify_Me_Course.model.entity.Course;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

@Repository
public interface CourseRepository extends MongoRepository<Course, ObjectId> {
    List<Course> findByInstructorEmail(String email);

    List<Course> findAllById(Iterable<ObjectId> ids);
}
