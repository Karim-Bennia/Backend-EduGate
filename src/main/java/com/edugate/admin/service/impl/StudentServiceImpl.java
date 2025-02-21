package com.edugate.admin.service.impl;

import com.edugate.admin.dao.StudentDao;
import com.edugate.admin.dto.StudentDTO;
import com.edugate.admin.entity.Course;
import com.edugate.admin.entity.Student;
import com.edugate.admin.entity.User;
import com.edugate.admin.mapper.StudentMapper;
import com.edugate.admin.service.StudentService;
import com.edugate.admin.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentDao  studentDao;
    private final  StudentMapper  studentMapper;
    private final UserService  userService;

    public StudentServiceImpl(StudentDao studentDao, StudentMapper studentMapper, UserService userService) {
        this.studentDao = studentDao;
        this.studentMapper = studentMapper;
        this.userService = userService;
    }

    @Override
    public Student loadStudentById(Long studentId) {
        return studentDao.findById(studentId).orElseThrow(()-> new EntityNotFoundException("Student With ID"+studentId+"Not Found"));
    }


    @Override
    public Page<StudentDTO> loadStudentByName(String name, int page, int size) {
        PageRequest pageRequest=PageRequest.of(page,size);
        Page<Student> studentPage=studentDao.findStudentByName(name,pageRequest);
        return new PageImpl<>(studentPage.getContent().stream().map(student->studentMapper.fromStudent(student)).collect(Collectors.toList()),pageRequest,studentPage.getTotalElements());
    }

    @Override
    public StudentDTO loadStudentByEmail(String email) {
        return studentMapper.fromStudent(studentDao.findStudentByEmail(email));
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        User user=userService.createUser(studentDTO.getUser().getEmail(),studentDTO.getUser().getPassword());
        userService.assignRoleToUser(user.getEmail(),"Student");
        Student student=studentMapper.fromStudentDTO(studentDTO);
        student.setUser(user);
        Student savedStudent=studentDao.save(student);
        return studentMapper.fromStudent(savedStudent);
    }

    @Override
    public StudentDTO updateStudent(StudentDTO studentDTO) {
        Student loadedStudent=loadStudentById(studentDTO.getStudentId());
        Student student=studentMapper.fromStudentDTO(studentDTO);
        student.setUser(loadedStudent.getUser());
        student.setCourses(loadedStudent.getCourses());
        Student updatedStudent=studentDao.save(student);
        return studentMapper.fromStudent(updatedStudent);

    }

    @Override
    public void removeStudent(Long studentId) {
        Student student=loadStudentById(studentId);
        Iterator<Course> courseIterator=student.getCourses().iterator();
        if(courseIterator.hasNext()){
            Course course=courseIterator.next();
            course.removeStudentFromCourse(student);
        }
        studentDao.deleteById(studentId);
    }
}
