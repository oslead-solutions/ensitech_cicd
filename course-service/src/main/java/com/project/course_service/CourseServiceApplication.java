package com.project.course_service;

import com.project.course_service.repositories.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients//avec ceci au demaarrage spring va scanner les interface restClients
public class CourseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseServiceApplication.class, args);
	}

//CommandLineRunner commandLineRunner(CourseRepository courseRepository,){
//
//}

}
