package com.quanlytuyensinh.project.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "xt_nguyenvongxettuyen")
public class NguyenVong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idnv;

    @Column(name = "nn_cccd")
    private String tsCccd;

    @Column(name = "nv_manganh")
    private String maNganh;

    @Column(name = "nv_tt")
    private Integer thuTuNV;

    @Column(name = "tt_phuongthuc")
    private String phuongThuc;

    @Column(name = "tt_thm")
    private String maToHop;

    @Column(name = "diem_thxt")
    private Double diemThxt;

    @Column(name = "diem_utqd")
    private Double diemUtqd;

    @Column(name = "diem_cong")
    private Double diemCong;

    @Column(name = "diem_xettuyen")
    private Double diemXetTuyen;

    @Column(name = "nv_ketqua")
    private String ketQua;

    @Column(name = "nv_keys", unique = true)
    private String nvKeys;

    public int getIdnv() {
        return idnv;
    }

    public void setIdnv(int idnv) {
        this.idnv = idnv;
    }

    public String getTsCccd() {
        return tsCccd;
    }

    public void setTsCccd(String tsCccd) {
        this.tsCccd = tsCccd;
    }

    public String getMaNganh() {
        return maNganh;
    }

    public void setMaNganh(String maNganh) {
        this.maNganh = maNganh;
    }

    public Integer getThuTuNV() {
        return thuTuNV;
    }

    public void setThuTuNV(Integer thuTuNV) {
        this.thuTuNV = thuTuNV;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public String getMaToHop() {
        return maToHop;
    }

    public void setMaToHop(String maToHop) {
        this.maToHop = maToHop;
    }

    public Double getDiemThxt() {
        return diemThxt;
    }

    public void setDiemThxt(Double diemThxt) {
        this.diemThxt = diemThxt;
    }

    public Double getDiemUtqd() {
        return diemUtqd;
    }

    public void setDiemUtqd(Double diemUtqd) {
        this.diemUtqd = diemUtqd;
    }

    public Double getDiemCong() {
        return diemCong;
    }

    public void setDiemCong(Double diemCong) {
        this.diemCong = diemCong;
    }

    public Double getDiemXetTuyen() {
        return diemXetTuyen;
    }

    public void setDiemXetTuyen(Double diemXetTuyen) {
        this.diemXetTuyen = diemXetTuyen;
    }

    public String getKetQua() {
        return ketQua;
    }

    public void setKetQua(String ketQua) {
        this.ketQua = ketQua;
    }

    public String getNvKeys() {
        return nvKeys;
    }

    public void setNvKeys(String nvKeys) {
        this.nvKeys = nvKeys;
    }
}