package com.skillifyme.course.Skillify_Me_Course.repository;

import com.skillifyme.course.Skillify_Me_Course.model.entity.UserCourse;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseRepository extends MongoRepository<UserCourse, ObjectId> {
    Optional<UserCourse> findByUserEmailAndCourseId(String userEmail, ObjectId courseId);
    Optional<UserCourse> findByUserEmail(String userEmail);
}
