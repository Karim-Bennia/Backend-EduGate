package com.edugate.admin.service.impl;

import com.edugate.admin.dao.RoleDao;
import com.edugate.admin.dao.UserDao;
import com.edugate.admin.entity.Role;
import com.edugate.admin.entity.User;
import com.edugate.admin.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RoleDao roleDao;
    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public User loadUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User createUser(String email, String password) {
        return userDao.save(new User(email,password));
    }

    @Override
    public void assignRoleToUser(String email, String roleName) {
        User user=loadUserByEmail(email);
        Role role=roleDao.findByName(roleName);
        user.assignRoleToUser(role);
    }
}
