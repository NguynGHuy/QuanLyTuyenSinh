package com.quanlytuyensinh.project.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quanlytuyensinh.project.Model.ThiSinh;
import com.quanlytuyensinh.project.Service.ThiSinhService;

@RestController
@RequestMapping("/api/thi-sinh")
public class ThiSinhController {
    @Autowired
    private ThiSinhService thiSinhService;

    @GetMapping
    public List<ThiSinh> getAllThiSinhs() {
        return thiSinhService.getAllThiSinhs();
    }

    @GetMapping("/cccd/{cccd}")
    public ResponseEntity<ThiSinh> getThiSinhByCccd(@PathVariable String cccd) {
        ThiSinh thiSinh = thiSinhService.getThiSinhByCccd(cccd);
        if (thiSinh == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(thiSinh);
    }

    @GetMapping("/sbd/{sobaodanh}")
    public ResponseEntity<ThiSinh> getThiSinhBySobaodanh(@PathVariable String sobaodanh) {
        ThiSinh thiSinh = thiSinhService.getThiSinhBySobaodanh(sobaodanh);
        if (thiSinh == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(thiSinh);
    }
}
