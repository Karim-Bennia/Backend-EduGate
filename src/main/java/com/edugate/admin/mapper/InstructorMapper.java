package com.edugate.admin.mapper;

import com.edugate.admin.dto.InstructorDTO;
import com.edugate.admin.entity.Instructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
@Service
public class InstructorMapper {

    //map from entity to dto for response :fetch data from database
    public InstructorDTO fromInstructor(Instructor instructor) {
        InstructorDTO instructorDTO = new InstructorDTO();
        BeanUtils.copyProperties(instructor, instructorDTO);
        return instructorDTO;
    }


    //map from dto to entity for request :post and put request
    public Instructor fromInstructorDTO(InstructorDTO instructorDTO) {
        Instructor instructor = new Instructor();
        BeanUtils.copyProperties(instructorDTO, instructor);
        return instructor;
    }
}
