package com.quanlytuyensinh.project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quanlytuyensinh.project.Model.DiemThi;

public interface DiemThiRepository extends JpaRepository<DiemThi, Integer> {
    DiemThi findByCccd(String cccd);
    List<DiemThi> findBySobaodanh(String sobaodanh);
}
