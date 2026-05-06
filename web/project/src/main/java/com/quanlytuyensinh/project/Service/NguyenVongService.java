package com.quanlytuyensinh.project.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlytuyensinh.project.Model.NguyenVong;
import com.quanlytuyensinh.project.Repository.NguyenVongRepository;

@Service
public class NguyenVongService {
    @Autowired
    private NguyenVongRepository nguyenVongRepository;

    public NguyenVongRepository getNguyenVongRepository() {
        return nguyenVongRepository;
    }

    public List<NguyenVong> getAllNguyenVongs() {
        return nguyenVongRepository.findAll();
    }

    public NguyenVong getNguyenVongByNvKeys(String nvKeys) {
        return nguyenVongRepository.findByNvKeys(nvKeys);
    }

    public List<NguyenVong> getNguyenVongsByTsCccd(String tsCccd) {
        return nguyenVongRepository.findByTsCccd(tsCccd);
    }

    public List<NguyenVong> getNguyenVongsByMaNganh(String maNganh) {
        return nguyenVongRepository.findByMaNganh(maNganh);
    }
}
