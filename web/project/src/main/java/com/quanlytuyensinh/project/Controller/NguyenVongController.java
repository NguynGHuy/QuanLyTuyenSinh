package com.quanlytuyensinh.project.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quanlytuyensinh.project.Model.NguyenVong;
import com.quanlytuyensinh.project.Service.NguyenVongService;

@RestController
@RequestMapping("/api/nguyen-vong")
public class NguyenVongController {
    @Autowired
    private NguyenVongService nguyenVongService;

    @GetMapping
    public List<NguyenVong> getAllNguyenVongs() {
        return nguyenVongService.getAllNguyenVongs();
    }

    @GetMapping("/keys/{nvKeys}")
    public ResponseEntity<NguyenVong> getNguyenVongByNvKeys(@PathVariable String nvKeys) {
        NguyenVong nguyenVong = nguyenVongService.getNguyenVongByNvKeys(nvKeys);
        if (nguyenVong == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(nguyenVong);
    }

    @GetMapping("/cccd/{cccd}")
    public List<NguyenVong> getNguyenVongsByCccd(@PathVariable("cccd") String cccd) {
        return nguyenVongService.getNguyenVongsByTsCccd(cccd);
    }

    @GetMapping("/ma-nganh/{maNganh}")
    public List<NguyenVong> getNguyenVongsByMaNganh(@PathVariable String maNganh) {
        return nguyenVongService.getNguyenVongsByMaNganh(maNganh);
    }
}
