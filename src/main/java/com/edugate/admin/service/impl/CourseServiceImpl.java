package com.edugate.admin.service.impl;

import com.edugate.admin.dao.CourseDao;
import com.edugate.admin.dao.InstructorDao;
import com.edugate.admin.dao.StudentDao;
import com.edugate.admin.dto.CourseDTO;
import com.edugate.admin.entity.Course;
import com.edugate.admin.entity.Instructor;
import com.edugate.admin.entity.Student;
import com.edugate.admin.mapper.CourseMapper;
import com.edugate.admin.service.CourseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao;
    private final CourseMapper courseMapper;
    private final InstructorDao instructorDao;
    private final StudentDao studentDao;

    public CourseServiceImpl(CourseDao courseDao, CourseMapper courseMapper, InstructorDao instructorDao, StudentDao studentDao) {
        this.courseDao = courseDao;
        this.courseMapper = courseMapper;
        this.instructorDao = instructorDao;
        this.studentDao = studentDao;
    }

    @Override
    public Course loadCourseById(Long courseId) {
        return courseDao.findById(courseId).orElseThrow(()-> new EntityNotFoundException("Course With ID"+courseId+"Not Found"));
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course=courseMapper.fromCourseDTO(courseDTO);
        Instructor instructor=instructorDao.findById(courseDTO.getInstructor().getInstructorId()).orElseThrow(()-> new EntityNotFoundException("Instructor With ID"+courseDTO.getInstructor().getInstructorId()+"Not Found"));
        course.setInstructor(instructor);
        Course savedCourse=courseDao.save(course) ;
        return courseMapper.fromCourse(savedCourse);
    }

    @Override
    public CourseDTO updateCourse(CourseDTO courseDTO) {
        Course loadedCourse=loadCourseById(courseDTO.getCourseId());
        Instructor instructor=instructorDao.findById(courseDTO.getInstructor().getInstructorId()).orElseThrow(()-> new EntityNotFoundException("Instructor With ID"+courseDTO.getInstructor().getInstructorId()+"Not Found"));
        Course course=courseMapper.fromCourseDTO(courseDTO);
        course.setInstructor(instructor);
        course.setStudents(loadedCourse.getStudents());
        Course updatedCourse=courseDao.save(course) ;
        return courseMapper.fromCourse(updatedCourse);
    }

    @Override
    public Page<CourseDTO> findCoursesByCourseName(String keyword, int page, int size) {
        PageRequest pageRequest=PageRequest.of(page,size);
        Page<Course> coursesPage=courseDao.findCourseByCourseNameContains(keyword,pageRequest);
        return new PageImpl<>(coursesPage.getContent().stream().map(courseMapper::fromCourse).collect(Collectors.toList()));
    }

    @Override
    public void assignStudentToCourse(Long courseId, Long studentId) {
        Student student=studentDao.findById(studentId).orElseThrow(()-> new EntityNotFoundException("Student With ID"+studentId+"Not Found"));
        Course course=loadCourseById(courseId);
        course.assignStudentToCourse(student);


    }

    @Override
    public Page<CourseDTO> fetchCoursesForStudent(Long studentId, int page, int size) {
        PageRequest pageRequest=PageRequest.of(page,size);
        Page<Course> StudentCoursesPage=courseDao.getCoursesByStudentId(studentId,pageRequest);

        return new PageImpl<>(StudentCoursesPage.getContent().stream().map(courseMapper::fromCourse).collect(Collectors.toList()),pageRequest,StudentCoursesPage.getTotalElements());
    }

    @Override
    public Page<CourseDTO> fetchNonEnrolledInCoursesForStudent(Long studentId, int page, int size) {
        PageRequest pageRequest=PageRequest.of(page,size);
        Page<Course> nonEnrolledInCoursesPage=courseDao.getNonEnrolledINCoursesByStudentId(studentId,pageRequest);

        return new PageImpl<>(nonEnrolledInCoursesPage.getContent().stream().map(courseMapper::fromCourse).collect(Collectors.toList()),pageRequest,nonEnrolledInCoursesPage.getTotalElements());
    }

    @Override
    public void removeCourse(Long courseId) {
         courseDao.deleteById(courseId);
    }

    @Override
    public Page<CourseDTO> fetchCoursesForInstructor(Long instructorId, int page, int size) {
        PageRequest pageRequest=PageRequest.of(page,size);
        Page<Course> instructorCoursesPage=courseDao.getCoursesByInstructorId(instructorId,pageRequest);
        return new PageImpl<>(instructorCoursesPage.getContent().stream().map(courseMapper::fromCourse).collect(Collectors.toList()),pageRequest,instructorCoursesPage.getTotalElements());

    }
}
