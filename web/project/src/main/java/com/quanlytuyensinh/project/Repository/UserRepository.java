package com.quanlytuyensinh.project.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.quanlytuyensinh.project.Model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
