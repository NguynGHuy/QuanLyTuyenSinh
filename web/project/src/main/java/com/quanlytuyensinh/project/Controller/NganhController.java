package com.quanlytuyensinh.project.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quanlytuyensinh.project.Model.Nganh;
import com.quanlytuyensinh.project.Service.NganhService;

@RestController
@RequestMapping("/api/nganh")
public class NganhController {
    @Autowired
    private NganhService nganhService;

    @GetMapping
    public List<Nganh> getAllNganhs() {
        return nganhService.getAllNganhs();
    }

    @GetMapping("/ma/{manganh}")
    public ResponseEntity<Nganh> getNganhByManganh(@PathVariable String manganh) {
        Nganh nganh = nganhService.getNganhByManganh(manganh);
        if (nganh == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(nganh);
    }


    @GetMapping("/ten/{tennganh}")
    public List<Nganh> getNganhsByTennganh(@PathVariable String tennganh) {
        return nganhService.getNganhsByTennganh(tennganh);
    }
}
