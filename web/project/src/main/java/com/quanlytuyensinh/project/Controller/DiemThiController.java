package com.quanlytuyensinh.project.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quanlytuyensinh.project.Model.DiemThi;
import com.quanlytuyensinh.project.Service.DiemThiService;

@RestController
@RequestMapping("/api/diem-thi")
public class DiemThiController {
    @Autowired
    private DiemThiService diemThiService;

    @GetMapping
    public List<DiemThi> getAllDiemThis() {
        return diemThiService.getAllDiemThis();
    }

    @GetMapping("/cccd/{cccd}")
    public ResponseEntity<DiemThi> getDiemThiByCccd(@PathVariable String cccd) {
        DiemThi diemThi = diemThiService.getDiemThiByCccd(cccd);
        if (diemThi == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(diemThi);
    }

    @GetMapping("/sbd/{sobaodanh}")
    public List<DiemThi> getDiemThisBySobaodanh(@PathVariable String sobaodanh) {
        return diemThiService.getDiemThisBySobaodanh(sobaodanh);
    }
}
