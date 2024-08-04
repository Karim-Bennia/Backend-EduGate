package com.edugate.admin.service.impl;

import com.edugate.admin.dao.InstructorDao;
import com.edugate.admin.dto.InstructorDTO;
import com.edugate.admin.entity.Course;
import com.edugate.admin.entity.Instructor;
import com.edugate.admin.entity.User;
import com.edugate.admin.mapper.InstructorMapper;
import com.edugate.admin.service.InstructorService;
import com.edugate.admin.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InstructorServiceImpl implements InstructorService {
    private final InstructorDao instructorDao;
    private final CourseServiceImpl courseService;
    private final UserService userService;
    private final InstructorMapper instructorMapper;

    public InstructorServiceImpl(InstructorDao instructorDao, CourseServiceImpl courseService, UserService userService, InstructorMapper instructorMapper) {
        this.instructorDao = instructorDao;
        this.courseService = courseService;
        this.userService = userService;
        this.instructorMapper = instructorMapper;
    }

    @Override
    public Instructor loadInstructorById(Long instructorId) {
        return instructorDao.findById(instructorId).orElseThrow(()-> new EntityNotFoundException("Instructor With ID"+instructorId+"Not Found"));
    }

    @Override
    public Page<InstructorDTO> findInstructorByName(String name,int page,int size) {
        PageRequest pageRequest=PageRequest.of(page,size);
        Page<Instructor> InstructorPage=instructorDao.findInstructorsByName(name,pageRequest);
        return new PageImpl<>(InstructorPage.getContent().stream().map(instructorMapper::fromInstructor).collect(Collectors.toList()),pageRequest,InstructorPage.getTotalElements());
    }

    @Override
    public InstructorDTO loadInstructorByEmail(String email) {
        return instructorMapper.fromInstructor(instructorDao.findInstructorByEmail(email));
    }

    @Override
    public InstructorDTO createInstructor(InstructorDTO instructorDTO) {
        User user = userService.createUser(instructorDTO.getUser().getEmail(), instructorDTO.getUser().getPassword());
        userService.assignRoleToUser(user.getEmail(), "Instructor");

        Instructor instructor = instructorMapper.fromInstructorDTO(instructorDTO);
        instructor.setUser(user);

        Instructor savedInstructor = instructorDao.save(instructor);
       return instructorMapper.fromInstructor(savedInstructor);
    }

    @Override
    public InstructorDTO updateInstructor(InstructorDTO instructorDTO) {
        Instructor loadInstructor=loadInstructorById(instructorDTO.getInstructorId());
        Instructor instructor=instructorMapper.fromInstructorDTO(instructorDTO);
        instructor.setUser(loadInstructor.getUser());
        instructor.setCourses(loadInstructor.getCourses());
        Instructor updatedInstructor=instructorDao.save(instructor);
        return instructorMapper.fromInstructor(updatedInstructor);
    }

    @Override
    public List<InstructorDTO> fetchInstructor() {
        return instructorDao.findAll().stream().map(instructorMapper::fromInstructor).collect(Collectors.toList());
    }

    @Override
    public void removeInstructor(Long instructorId) {
        Instructor instructor=loadInstructorById(instructorId);
        for(Course course:instructor.getCourses()){
            courseService.removeCourse(course.getCourseId());
        }
        instructorDao.deleteById(instructorId);
    }

}
