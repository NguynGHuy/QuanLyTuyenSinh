package com.quanlytuyensinh.project.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlytuyensinh.project.Model.DiemThi;
import com.quanlytuyensinh.project.Repository.DiemThiRepository;

@Service
public class DiemThiService {
    @Autowired
    private DiemThiRepository diemThiRepository;

    public DiemThiRepository getDiemThiRepository() {
        return diemThiRepository;
    }

    public List<DiemThi> getAllDiemThis() {
        return diemThiRepository.findAll();
    }

    public DiemThi getDiemThiByCccd(String cccd) {
        return diemThiRepository.findByCccd(cccd);
    }

    public List<DiemThi> getDiemThisBySobaodanh(String sobaodanh) {
        return diemThiRepository.findBySobaodanh(sobaodanh);
    }
}
