package com.edugate.admin.mapper;

import com.edugate.admin.dto.CourseDTO;
import com.edugate.admin.entity.Course;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class CourseMapper {

    private InstructorMapper instructorMapper;

    public CourseMapper(InstructorMapper instructorMapper) {
        this.instructorMapper = instructorMapper;
    }

    public CourseDTO fromCourse(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        BeanUtils.copyProperties(course, courseDTO);
        courseDTO.setInstructor(instructorMapper.fromInstructor(course.getInstructor()));
        return courseDTO;
    }

    public Course fromCourseDTO(CourseDTO courseDTO) {
        Course course = new Course();
        BeanUtils.copyProperties(courseDTO, course);
        return course;
    }

}
