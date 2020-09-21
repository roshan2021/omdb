package com.roshan.moviesearch;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.roshan.moviesearch.Models.User;
import com.roshan.moviesearch.Repository.UserRepository;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody User user) {
        User storedUser = userRepository.findByUserName(user.getUserName());
        if (storedUser == null) {
            return "User does not exist";
        } else {
            boolean checkedPassword = BCrypt.checkpw(user.getPassword(), storedUser.getPassword());
            if (checkedPassword) {
                Algorithm algorithm = Algorithm.HMAC256("secret");
                String token = JWT.create().withIssuer("Roshan")
                        .withExpiresAt(DateUtils.addMinutes(new Date(), 60 * 24)).withClaim("id", storedUser.getId())
                        .sign(algorithm);
                return token;
            } else {
                return "Incorrect password";
            }
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody User user) {
        
        int count = userRepository.countByUserName(user.getUserName());
        if (count == 0) {
            String password = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(password);
            userRepository.save(user);
            return "User saved";
        } else if (count > 0) {
            return "User Already exists";
        }

        return "Unspecified Error" + count;
    }
}