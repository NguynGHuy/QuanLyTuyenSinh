package com.quanlytuyensinh.project.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlytuyensinh.project.Model.ThiSinh;
import com.quanlytuyensinh.project.Repository.ThiSinhRepository;

@Service
public class ThiSinhService {
    @Autowired
    private ThiSinhRepository thiSinhRepository;

    public ThiSinhRepository getThiSinhRepository() {
        return thiSinhRepository;
    }

    public List<ThiSinh> getAllThiSinhs() {
        return thiSinhRepository.findAll();
    }

    public ThiSinh getThiSinhByCccd(String cccd) {
        return thiSinhRepository.findByCccd(cccd);
    }

    public ThiSinh getThiSinhBySobaodanh(String sobaodanh) {
        return thiSinhRepository.findBySobaodanh(sobaodanh);
    }
}
