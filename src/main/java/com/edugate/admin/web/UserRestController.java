package com.edugate.admin.web;

import com.edugate.admin.service.UserService;
import com.edugate.admin.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {


    private UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users")
    public boolean checkIfEmailExist(@RequestParam(name="email",defaultValue = "") String email){
        return userService.loadUserByEmail(email)!=null;
    }
}
