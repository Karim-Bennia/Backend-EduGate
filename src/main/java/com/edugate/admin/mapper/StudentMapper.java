package com.edugate.admin.mapper;

import com.edugate.admin.dto.StudentDTO;
import com.edugate.admin.entity.Student;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class StudentMapper {
    public StudentDTO fromStudent(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        BeanUtils.copyProperties(student, studentDTO);
        return studentDTO;
    }

    public Student fromStudentDTO(StudentDTO studentDTO) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDTO, student);
        return student;
    }

}
