package com.edugate.admin.service;

import com.edugate.admin.entity.User;

public interface UserService {
    User loadUserByEmail(String email);
    User createUser(String email,String password);
    void assignRoleToUser(String email,String roleName);

}
