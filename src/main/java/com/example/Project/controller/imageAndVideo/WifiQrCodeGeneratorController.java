package com.example.Project.controller.imageAndVideo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.model.Category;
import com.example.Project.service.CategoryService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@Controller
@RequestMapping("")
public class WifiQrCodeGeneratorController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/images-videos/wifi-qr-code-generator")
    public String show(Model model) {
        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        return "wifi-qr-code-generator";
    }

    @RestController
    @RequestMapping("/api/images-videos/wifi-qr-code-generator")
    public class WifiQrCodeGeneratorHandler {
        @GetMapping("")
        public Map<String, Object> generateWifiQrCode(
                @RequestParam String ssid,
                @RequestParam String password,
                @RequestParam String fgColor,
                @RequestParam String bgColor) {

            Map<String, Object> response = new HashMap<>();

            if (ssid.isEmpty()) {
                response.put("isSuccess", false);
                return response;
            }

            try {
                // Tạo nội dung mã QR theo định dạng WiFi
                String wifiData = "WIFI:T:WPA;S:" + ssid + ";P:" + password + ";H:false;;";

                int width = 300, height = 300;
                ErrorCorrectionLevel level = ErrorCorrectionLevel.M;

                // Cấu hình mã hóa
                Map<EncodeHintType, Object> hints = new HashMap<>();
                hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                hints.put(EncodeHintType.ERROR_CORRECTION, level);

                // Tạo mã QR
                BitMatrix matrix = new MultiFormatWriter().encode(
                        wifiData, BarcodeFormat.QR_CODE, width, height, hints);

                BufferedImage image = MatrixToImageWriter.toBufferedImage(
                        matrix, new MatrixToImageConfig(hexToColor(fgColor), hexToColor(bgColor)));

                // Chuyển ảnh QR thành chuỗi Base64
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(image, "png", outputStream);
                byte[] imageBytes = outputStream.toByteArray();

                response.put("qrCodeImage", "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes));
                response.put("isSuccess", true);
            } catch (Exception e) {
                response.put("isSuccess", false);
            }

            return response;
        }

        // Chuyển mã HEX thành màu
        private int hexToColor(String hex) {
            return Integer.parseInt(hex.substring(1), 16) | 0xFF000000;
        }
    }
}
