package com.quanlytuyensinh.project.ViewModel;

public class NguyenVongView {
    private final Integer thuTuNV;
    private final String majorName;
    private final String toHopLabel;
    private final String methodLabel;
    private final Double diemXetTuyen;
    private final String ketQua;
    private final boolean admitted;

    public NguyenVongView(
            Integer thuTuNV,
            String majorName,
            String toHopLabel,
            String methodLabel,
            Double diemXetTuyen,
            String ketQua,
            boolean admitted
    ) {
        this.thuTuNV = thuTuNV;
        this.majorName = majorName;
        this.toHopLabel = toHopLabel;
        this.methodLabel = methodLabel;
        this.diemXetTuyen = diemXetTuyen;
        this.ketQua = ketQua;
        this.admitted = admitted;
    }

    public Integer getThuTuNV() {
        return thuTuNV;
    }

    public String getMajorName() {
        return majorName;
    }

    public String getToHopLabel() {
        return toHopLabel;
    }

    public String getMethodLabel() {
        return methodLabel;
    }

    public Double getDiemXetTuyen() {
        return diemXetTuyen;
    }

    public String getKetQua() {
        return ketQua;
    }

    public boolean isAdmitted() {
        return admitted;
    }
}
