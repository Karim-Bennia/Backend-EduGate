package com.edugate.admin.dao;

import com.edugate.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Long> {
              User findByEmail(String email);
}
