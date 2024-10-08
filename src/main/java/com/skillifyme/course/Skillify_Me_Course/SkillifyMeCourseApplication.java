package com.skillifyme.course.Skillify_Me_Course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SkillifyMeCourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillifyMeCourseApplication.class, args);
	}

}
