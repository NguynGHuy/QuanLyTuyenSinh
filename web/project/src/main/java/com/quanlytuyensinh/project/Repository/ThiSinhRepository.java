package com.quanlytuyensinh.project.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.quanlytuyensinh.project.Model.ThiSinh;

public interface ThiSinhRepository extends JpaRepository<ThiSinh, Integer> {
    ThiSinh findByCccd(String cccd);
    ThiSinh findBySobaodanh(String sobaodanh);
    
}
