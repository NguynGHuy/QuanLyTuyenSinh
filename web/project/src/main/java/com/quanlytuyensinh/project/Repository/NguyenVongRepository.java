package com.quanlytuyensinh.project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quanlytuyensinh.project.Model.NguyenVong;

public interface NguyenVongRepository extends JpaRepository<NguyenVong, Integer> {
    NguyenVong findByNvKeys(String nvKeys);
    List<NguyenVong> findByTsCccd(String tsCccd);
    List<NguyenVong> findByMaNganh(String maNganh);
}
