package com.skillifyme.course.Skillify_Me_Course.repository;

import com.skillifyme.course.Skillify_Me_Course.model.entity.Course;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends MongoRepository<Course, ObjectId> {
}
