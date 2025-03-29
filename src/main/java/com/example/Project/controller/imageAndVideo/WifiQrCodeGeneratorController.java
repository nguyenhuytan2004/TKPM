package com.example.Project.controller.imageAndVideo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("images-videos")
public class WifiQrCodeGeneratorController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/wifi-qr-code-generator")
    public String show(Model model) {
        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "WiFi QR Code Generator");
        model.addAttribute("body", "wifi-qr-code-generator");
        return "layout";
    }
}

@RestController
@RequestMapping("/api/images-videos/wifi-qr-code-generator")
class WifiQrCodeGeneratorApiController {
    @GetMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateWifiQrCode(
            @RequestParam String ssid,
            @RequestParam(required = false, defaultValue = "") String password,
            @RequestParam(defaultValue = "#000000") String fgColor,
            @RequestParam(defaultValue = "#FFFFFF") String bgColor,
            @RequestParam(defaultValue = "WPA") String encryption) {

        Map<String, Object> response = new HashMap<>();

        if (ssid.isEmpty()) {
            response.put("isSuccess", false);
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String wifiData = "WIFI:T:" + encryption + ";S:" + ssid + ";P:" + password + ";H:false;;";

            int width = 300, height = 300;
            ErrorCorrectionLevel level = ErrorCorrectionLevel.M;

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, level);

            BitMatrix matrix = new MultiFormatWriter().encode(
                    wifiData, BarcodeFormat.QR_CODE, width, height, hints);

            BufferedImage image = MatrixToImageWriter.toBufferedImage(
                    matrix, new MatrixToImageConfig(hexToColor(fgColor), hexToColor(bgColor)));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            response.put("qrCodeImage", "data:image/png;base64," + base64Image);
            response.put("isSuccess", true);
        } catch (Exception e) {
            response.put("isSuccess", false);
            response.put("error", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    private int hexToColor(String hex) {
        return (Integer.parseInt(hex.substring(1), 16) & 0xFFFFFF) | 0xFF000000;
    }
}
