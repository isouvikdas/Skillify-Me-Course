package com.skillifyme.course.Skillify_Me_Course.repository;

import com.skillifyme.course.Skillify_Me_Course.model.entity.Course;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Module;
import org.bouncycastle.math.raw.Mod;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends MongoRepository<Module, ObjectId> {
    List<Module> findByInstructorEmail(String email);

    @Override
    List<Module> findAllById(Iterable<ObjectId> ids);
}
