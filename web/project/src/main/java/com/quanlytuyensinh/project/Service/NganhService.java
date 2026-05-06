package com.quanlytuyensinh.project.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlytuyensinh.project.Model.Nganh;
import com.quanlytuyensinh.project.Repository.NganhRepository;

@Service
public class NganhService {
    @Autowired
    private NganhRepository nganhRepository;

    public NganhRepository getNganhRepository() {
        return nganhRepository;
    }

    public List<Nganh> getAllNganhs() {
        return nganhRepository.findAll();
    }

    public Nganh getNganhByManganh(String manganh) {
        return nganhRepository.findByManganh(manganh);
    }

    public List<Nganh> getNganhsByTennganh(String tennganh) {
        return nganhRepository.findByTennganh(tennganh);
    }
}
