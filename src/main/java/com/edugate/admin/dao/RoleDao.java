package com.edugate.admin.dao;

import com.edugate.admin.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDao  extends JpaRepository<Role,Long> {
       Role findByName(String name);
}
