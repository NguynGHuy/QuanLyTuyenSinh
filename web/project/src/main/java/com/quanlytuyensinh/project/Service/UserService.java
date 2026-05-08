package com.quanlytuyensinh.project.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlytuyensinh.project.Model.User;
import com.quanlytuyensinh.project.Repository.UserRepository;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
