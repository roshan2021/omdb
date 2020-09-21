package com.roshan.moviesearch;

import com.roshan.moviesearch.Repository.UserRepository;
import com.roshan.moviesearch.Models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String welcome() {
        return "index";
    }
}
