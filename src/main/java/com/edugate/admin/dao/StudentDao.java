package com.edugate.admin.dao;

import com.edugate.admin.entity.Instructor;
import com.edugate.admin.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface StudentDao extends JpaRepository<Student, Long> {
    @Query(value = "select s from Student as s  where s.firstName like %:name% or s.lastName like %:name%")
    Page<Student> findStudentByName(@Param("name") String name, PageRequest pageRequest);

    @Query(value = "select s from Student as s  where s.user.email=:email")
    Student findStudentByEmail(@Param("email") String email);

}
