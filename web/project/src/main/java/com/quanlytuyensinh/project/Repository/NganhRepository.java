package com.quanlytuyensinh.project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quanlytuyensinh.project.Model.Nganh;

public interface NganhRepository extends JpaRepository<Nganh, Integer> {
    Nganh findByManganh(String manganh);
    List<Nganh> findByTennganh(String tennganh);
}
