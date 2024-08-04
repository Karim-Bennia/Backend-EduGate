package com.edugate.admin.service;

import com.edugate.admin.dto.StudentDTO;
import com.edugate.admin.entity.Student;
import org.springframework.data.domain.Page;

public interface StudentService {

    Student loadStudentById(Long studentId);

    Page<StudentDTO> loadStudentByName(String name, int page, int size);

    StudentDTO loadStudentByEmail(String email);

    StudentDTO createStudent(StudentDTO studentDTO);

    StudentDTO updateStudent(StudentDTO studentDTO);

     void removeStudent(Long studentId);
}
