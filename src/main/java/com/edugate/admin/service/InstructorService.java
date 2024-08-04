package com.edugate.admin.service;

import com.edugate.admin.dto.InstructorDTO;
import com.edugate.admin.entity.Instructor;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InstructorService {

    Instructor loadInstructorById(Long instructorId);

    Page<InstructorDTO> findInstructorByName(String name,int page,int size);

    InstructorDTO loadInstructorByEmail(String email);

    InstructorDTO createInstructor(InstructorDTO instructorDTO);

    InstructorDTO updateInstructor(InstructorDTO instructorDTO);

    List<InstructorDTO> fetchInstructor();

    void removeInstructor(Long instructorId);
}
