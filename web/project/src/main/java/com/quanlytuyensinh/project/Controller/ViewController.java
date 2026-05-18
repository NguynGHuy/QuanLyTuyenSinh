package com.quanlytuyensinh.project.Controller;

import java.text.Normalizer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.quanlytuyensinh.project.Model.Nganh;
import com.quanlytuyensinh.project.Model.NguyenVong;
import com.quanlytuyensinh.project.Model.ThiSinh;
import com.quanlytuyensinh.project.Model.ToHopMon;
import com.quanlytuyensinh.project.Service.NganhService;
import com.quanlytuyensinh.project.Service.NguyenVongService;
import com.quanlytuyensinh.project.Service.ThiSinhService;
import com.quanlytuyensinh.project.Service.ToHopMonService;
import com.quanlytuyensinh.project.ViewModel.NguyenVongView;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViewController {
    private static final Pattern DIACRITICS = Pattern.compile("\\p{M}+");

    @Autowired
    private ThiSinhService thiSinhService;

    @Autowired
    private NguyenVongService nguyenVongService;

    @Autowired
    private NganhService nganhService;

    @Autowired
    private ToHopMonService toHopMonService;

    @ModelAttribute("candidate")
    public ThiSinh candidate(HttpSession session) {
        Object value = session.getAttribute("candidate");
        if (value instanceof ThiSinh) {
            return (ThiSinh) value;
        }
        return null;
    }

    @GetMapping("/")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "cccd", required = false) String cccd,
            Model model
    ) {
        model.addAttribute("error", error);
        model.addAttribute("cccd", cccd);
        return "index";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("cccd") String cccd,
            @RequestParam("dob") String dob,
            Model model,
            HttpSession session
    ) {
        String trimmedCccd = cccd == null ? "" : cccd.trim();
        String trimmedDob = dob == null ? "" : dob.replaceAll("[^0-9]", "");
        if (trimmedCccd.isEmpty() || trimmedDob.isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập đầy đủ CCCD và ngày sinh.");
            model.addAttribute("cccd", trimmedCccd);
            return "index";
        }

        ThiSinh candidate = thiSinhService.getThiSinhByCccd(trimmedCccd);
        if (candidate == null) {
            model.addAttribute("error", "Không tìm thấy thí sinh với CCCD đã nhập.");
            model.addAttribute("cccd", trimmedCccd);
            return "index";
        }

        String storedDob = normalizeDob(candidate.getNgaySinh());
        if (!storedDob.equals(trimmedDob)) {
            model.addAttribute("error", "Ngày sinh không đúng. Vui lòng kiểm tra lại.");
            model.addAttribute("cccd", trimmedCccd);
            return "index";
        }

        session.setAttribute("candidate", candidate);
        return "redirect:/ket-qua";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/ket-qua")
    public String result(Model model, HttpSession session) {
        ThiSinh candidate = (ThiSinh) session.getAttribute("candidate");
        if (candidate == null) {
            return "redirect:/";
        }

        List<NguyenVong> nguyenVongs = nguyenVongService.getNguyenVongsByTsCccd(candidate.getCccd());
        if (nguyenVongs == null) {
            nguyenVongs = Collections.emptyList();
        }

        List<Nganh> nganhList = nganhService.getAllNganhs();
        if (nganhList == null) {
            nganhList = Collections.emptyList();
        }

        List<ToHopMon> toHopList = toHopMonService.getAllToHopMons();
        if (toHopList == null) {
            toHopList = Collections.emptyList();
        }

        Map<String, String> majorNameByMa = nganhList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        Nganh::getManganh,
                        Nganh::getTennganh,
                        (left, right) -> left
                ));

        Map<String, ToHopMon> toHopByMa = toHopList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        ToHopMon::getMaToHop,
                        value -> value,
                        (left, right) -> left
                ));

        List<NguyenVongView> preferences = nguyenVongs.stream()
                .sorted(Comparator.comparing(NguyenVong::getThuTuNV, Comparator.nullsLast(Integer::compareTo)))
                .map(item -> {
                    String majorName = majorNameByMa.getOrDefault(item.getMaNganh(), item.getMaNganh());
                    String toHopLabel = buildToHopLabel(item.getMaToHop(), toHopByMa.get(item.getMaToHop()));
                    String methodLabel = mapMethodLabel(item.getPhuongThuc());
                    boolean admitted = isAdmitted(item.getKetQua());
                    return new NguyenVongView(
                            item.getThuTuNV(),
                            majorName,
                            toHopLabel,
                            methodLabel,
                            item.getDiemXetTuyen(),
                            item.getKetQua(),
                            admitted
                    );
                })
                .collect(Collectors.toList());

        NguyenVongView admittedPref = preferences.stream()
                .filter(NguyenVongView::isAdmitted)
                .findFirst()
                .orElse(null);

        model.addAttribute("preferences", preferences);
        model.addAttribute("admittedPref", admittedPref);
        model.addAttribute("priorityPoints", calculatePriorityPoints(candidate.getDoiTuong(), candidate.getKhuVuc()));
        return "result";
    }

    @GetMapping("/tinh-diem")
    public String calculator() {
        return "calculator";
    }

    private String normalizeDob(String ngaySinh) {
        if (ngaySinh == null) {
            return "";
        }
        return ngaySinh.replaceAll("[^0-9]", "");
    }

    private boolean isAdmitted(String ketQua) {
        if (ketQua == null) {
            return false;
        }
        String normalized = normalizeVietnamese(ketQua).replaceAll("\\s+", "");
        return normalized.contains("TRUNGTUYEN") || normalized.contains("DAT");
    }

    private String normalizeVietnamese(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        return DIACRITICS.matcher(normalized).replaceAll("").toUpperCase(Locale.ROOT);
    }

    private String mapMethodLabel(String phuongThuc) {
        if (phuongThuc == null) {
            return "-";
        }
        String key = phuongThuc.trim().toUpperCase(Locale.ROOT);
        switch (key) {
            case "PT1":
                return "THPT";
            case "PT2":
                return "DGNL";
            case "PT3":
                return "VSAT";
            default:
                return phuongThuc;
        }
    }

    private String buildToHopLabel(String maToHop, ToHopMon toHop) {
        if (toHop == null) {
            return maToHop == null ? "-" : maToHop;
        }
        String tenToHop = toHop.getTenToHop();
        if (tenToHop != null && !tenToHop.isBlank()) {
            return String.format("%s - %s", toHop.getMaToHop(), tenToHop);
        }
        return toHop.getMaToHop();
    }

    private double calculatePriorityPoints(String doiTuong, String khuVuc) {
        return priorityByGroup(doiTuong) + priorityByRegion(khuVuc);
    }

    private double priorityByGroup(String doiTuong) {
        if (doiTuong == null) {
            return 0.0;
        }
        switch (doiTuong.trim()) {
            case "01":
                return 2.0;
            case "02":
                return 1.5;
            case "03":
                return 1.0;
            case "04":
                return 0.5;
            default:
                return 0.0;
        }
    }

    private double priorityByRegion(String khuVuc) {
        if (khuVuc == null) {
            return 0.0;
        }
        String key = khuVuc.trim().toUpperCase(Locale.ROOT);
        switch (key) {
            case "KV1":
                return 0.75;
            case "KV2-NT":
                return 0.5;
            case "KV2":
                return 0.25;
            case "KV3":
                return 0.0;
            default:
                return 0.0;
        }
    }
}
