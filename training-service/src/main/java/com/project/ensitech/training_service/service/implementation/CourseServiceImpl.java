package com.project.ensitech.training_service.service.implementation;

import com.project.ensitech.training_service.client.TeacherClient;
import com.project.ensitech.training_service.exception.ResourceNotFoundException;
import com.project.ensitech.training_service.model.dto.UserDto;
import com.project.ensitech.training_service.model.dto.courseDto.CourseDto;
import com.project.ensitech.training_service.model.dto.courseDto.CreateCourseDto;
import com.project.ensitech.training_service.model.entity.Course;
import com.project.ensitech.training_service.repository.CourseRepository;
import com.project.ensitech.training_service.service.common.ICourseService;
import com.project.ensitech.training_service.service.mapper.CourseMapper;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {
    private static final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper; // MapStruct mapper
    private final TeacherClient teacherClient;



    @Override
    public CourseDto createCourse(CreateCourseDto createCourseDto) {

        // assertTeacherExists(createCourseDto.getTeacherId());
        Long teacherId = createCourseDto.getTeacherId();
        UserDto teacher = null;

        // Only check teacher if teacherId is not null
        if (teacherId != null) {
            teacher = teacherClient.getTeacher(teacherId); // throws if not found
            if (teacher == null) {
                throw new ResourceNotFoundException("Teacher", "id", teacherId);
            }
        }
        Course course = courseMapper.toEntity(createCourseDto);
        Course saved = courseRepository.save(course);
        // UserDto teacher = teacherClient.getTeacher(saved.getTeacherId());
        saved.setTeacher(teacher);
        return courseMapper.toDto(saved);
    }

    @Override
    public CourseDto getCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
       //  return courseMapper.toDto(course);
        CourseDto courseDto = courseMapper.toDto(course);
        // Fetch teacher details from user-service
        UserDto teacherDto = null;
        if(course.getTeacherId()!=null) {
            teacherDto = teacherClient.getTeacher(course.getTeacherId());
        }
        courseDto.setTeacher(teacherDto);
        return courseDto;
    }

    @Override
    public CourseDto updateCourse( CourseDto courseDto) {
        Course existing = courseRepository.findById(courseDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course inexistant ", "id", courseDto.getId()));

        // Apply updates
        existing.setTitle(courseDto.getTitle());
        existing.setCoefficient(courseDto.getCoefficient());
        existing.setHours(courseDto.getHours());
        existing.setTeacherId(courseDto.getTeacherId());

        Course updated = courseRepository.save(existing);
        UserDto teacher = null;
        if(courseDto.getTeacherId()!=null) {
            teacher = teacherClient.getTeacher(updated.getTeacherId());
        }
        updated.setTeacher(teacher);
        return courseMapper.toDto(updated);
    }

    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course", "id", id);
        }
        courseRepository.deleteById(id);
    }

    @Override
    public List<CourseDto> searchByTitle(String title) {
        /*return courseRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(courseMapper::toDto)
                .toList();*/
        log.info("searchByTitle: ",title);
        return courseRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getAllCourses() {


        /* return courseRepository.findAll().stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());*/

        return courseRepository.findAll().stream()
                .map(course -> {
                    CourseDto dto = courseMapper.toDto(course);
                    UserDto teacherDto = null;
                    // Fetch teacher details from User Service
                    if(course.getTeacherId()!= null) {
                         teacherDto = teacherClient.getTeacher(course.getTeacherId());
                    }
                    dto.setTeacher(teacherDto);

                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<CourseDto> getAllCoursesByTeacher(Long id) {


        return courseRepository.findByTeacherId(id).stream()
                .map(course -> {
                    CourseDto dto = courseMapper.toDto(course);
                    UserDto teacherDto = null;
                    // Fetch teacher details from User Service
                    if(course.getTeacherId()!= null) {
                        teacherDto = teacherClient.getTeacher(course.getTeacherId());
                    }
                    dto.setTeacher(teacherDto);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<Course> byTeacher(Long teacherId) { return courseRepository.findByTeacherId(teacherId); }
    private void assertTeacherExists(Long id) {
        try { teacherClient.getTeacher(id); }
        catch (FeignException.NotFound e) { throw new IllegalArgumentException("Teacher "+id+" not found"); }
    }
}