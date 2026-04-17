package com.example.web;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dao.NganhDAO;
import com.example.dao.NguyenVongDAO;
import com.example.dao.ThiSinhDAO;
import com.example.dao.UserDAO;
import com.example.entity.Nganh;
import com.example.entity.ThiSinh;
import com.example.entity.User;
import com.example.service.XetTuyenService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    private final UserDAO userDAO = new UserDAO();
    private final ThiSinhDAO thiSinhDAO = new ThiSinhDAO();
    private final NganhDAO nganhDAO = new NganhDAO();
    private final NguyenVongDAO nguyenVongDAO = new NguyenVongDAO();
    private final XetTuyenService xetTuyenService = new XetTuyenService();

    @GetMapping("/health")
    public MessageResponse health() {
        return new MessageResponse(true, "OK");
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String username = request.username() == null ? "" : request.username().trim();
        String password = request.password() == null ? "" : request.password().trim();

        if (username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse(false, null, null, null, null, null, null, null,
                            "Thiếu username hoặc password"));
        }

        Object result = userDAO.authenticateUser(username, password);
        if (result instanceof User user) {
            return ResponseEntity.ok(new LoginResponse(true, "ADMIN", user.getId(), user.getUsername(),
                    user.getRole(), null, null, null, "Đăng nhập thành công"));
        }

        if (result instanceof ThiSinh thiSinh) {
            return ResponseEntity.ok(new LoginResponse(true, "THI_SINH", thiSinh.getIdthisinh(), thiSinh.getCccd(),
                    null, fullName(thiSinh.getHo(), thiSinh.getTen()), thiSinh.getCccd(), thiSinh.getSobaodanh(),
                    "Đăng nhập thành công"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse(false, null, null, null, null, null, null, null,
                        "Tài khoản hoặc mật khẩu không chính xác"));
    }

    @GetMapping("/thisinhs")
    public ResponseEntity<PageResponse<ThiSinhResponse>> getThiSinhs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "") String keyword) {

        int safePage = Math.max(page, 1);
        int safePageSize = Math.max(pageSize, 1);
        String safeKeyword = keyword == null ? "" : keyword.trim();

        List<ThiSinh> thiSinhs = thiSinhDAO.getThiSinhs(safePage, safePageSize, safeKeyword);
        if (thiSinhs == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        long totalElements = thiSinhDAO.getTotalThiSinhs(safeKeyword);
        int totalPages = totalElements == 0 ? 0 : (int) Math.ceil(totalElements / (double) safePageSize);

        List<ThiSinhResponse> items = thiSinhs.stream()
                .filter(Objects::nonNull)
                .map(this::toThiSinhResponse)
                .toList();

        return ResponseEntity.ok(new PageResponse<>(items, totalElements, safePage, safePageSize, totalPages));
    }

    @GetMapping("/thisinhs/{id}")
    public ResponseEntity<ThiSinhResponse> getThiSinhById(@PathVariable int id) {
        ThiSinh thiSinh = thiSinhDAO.getThiSinhById(id);
        if (thiSinh == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toThiSinhResponse(thiSinh));
    }

    @GetMapping("/nganhs")
    public ResponseEntity<List<NganhResponse>> getNganhs() {
        List<Nganh> nganhs = nganhDAO.getAllNganh();
        if (nganhs == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(nganhs.stream().filter(Objects::nonNull).map(this::toNganhResponse).toList());
    }

    @GetMapping("/nguyenvongs/tra-cuu")
    public ResponseEntity<List<NguyenVongTraCuuResponse>> traCuu(@RequestParam(defaultValue = "") String keyword) {
        List<Object[]> rows = nguyenVongDAO.traCuuTheoCccdHoacSbd(keyword == null ? "" : keyword.trim());
        if (rows == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(rows.stream().map(this::toNguyenVongTraCuuResponse).toList());
    }

    @PostMapping("/xettuyen/run")
    public ResponseEntity<MessageResponse> runXetTuyen() {
        try {
            xetTuyenService.chayThuatToanXetTuyen();
            return ResponseEntity.ok(new MessageResponse(true, "Đã chạy xét tuyển thành công"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(false, "Chạy xét tuyển thất bại: " + ex.getMessage()));
        }
    }

    private ThiSinhResponse toThiSinhResponse(ThiSinh thiSinh) {
        return new ThiSinhResponse(
                thiSinh.getIdthisinh(),
                thiSinh.getCccd(),
                thiSinh.getSobaodanh(),
                thiSinh.getHo(),
                thiSinh.getTen(),
                thiSinh.getDienThoai(),
                thiSinh.getGioiTinh(),
                thiSinh.getEmail(),
                thiSinh.getNoiSinh(),
                thiSinh.getDoiTuong(),
                thiSinh.getKhuVuc());
    }

    private NganhResponse toNganhResponse(Nganh nganh) {
        return new NganhResponse(
                nganh.getIdnganh(),
                nganh.getManganh(),
                nganh.getTennganh(),
                nganh.getnChitieu(),
                nganh.getnDiemsan(),
                nganh.getnDiemtrungtuyen());
    }

    private NguyenVongTraCuuResponse toNguyenVongTraCuuResponse(Object[] row) {
        return new NguyenVongTraCuuResponse(
                asString(row, 0),
                asString(row, 1),
                asString(row, 2),
                asString(row, 3),
                asInteger(row, 4),
                asString(row, 5),
                asString(row, 6),
                asString(row, 7),
                asDouble(row, 8),
                asString(row, 9));
    }

    private String fullName(String ho, String ten) {
        String safeHo = ho == null ? "" : ho.trim();
        String safeTen = ten == null ? "" : ten.trim();
        String joined = (safeHo + " " + safeTen).trim();
        return joined.isEmpty() ? null : joined;
    }

    private String asString(Object[] row, int index) {
        if (row == null || index < 0 || index >= row.length || row[index] == null) {
            return null;
        }
        return row[index].toString();
    }

    private Integer asInteger(Object[] row, int index) {
        if (row == null || index < 0 || index >= row.length || row[index] == null) {
            return null;
        }
        return ((Number) row[index]).intValue();
    }

    private Double asDouble(Object[] row, int index) {
        if (row == null || index < 0 || index >= row.length || row[index] == null) {
            return null;
        }
        return ((Number) row[index]).doubleValue();
    }

    public record LoginRequest(String username, String password) {
    }

    public record LoginResponse(boolean success, String accountType, Integer id, String username, String role,
                                String fullName, String cccd, String soBaoDanh, String message) {
    }

    public record ThiSinhResponse(int idthisinh, String cccd, String soBaoDanh, String ho, String ten,
                                  String dienThoai, String gioiTinh, String email, String noiSinh,
                                  String doiTuong, String khuVuc) {
    }

    public record PageResponse<T>(List<T> items, long totalElements, int page, int pageSize, int totalPages) {
    }

    public record NganhResponse(int idnganh, String manganh, String tennganh, Integer nChitieu,
                                Double nDiemsan, Double nDiemtrungtuyen) {
    }

    public record NguyenVongTraCuuResponse(String cccd, String soBaoDanh, String ho, String ten,
                                           Integer thuTuNV, String maNganh, String tenNganh,
                                           String maToHop, Double diemXetTuyen, String ketQua) {
    }

    public record MessageResponse(boolean success, String message) {
    }
}