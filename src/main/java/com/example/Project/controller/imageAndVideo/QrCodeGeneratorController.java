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
public class QrCodeGeneratorController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/qr-code-generator")
    public String show(Model model) {
        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "QR Code Generator");  // Thêm tiêu đề
        model.addAttribute("body", "qr-code-generator");  // Load trang con
        return "layout";  // Load vào layout.hbs
    }
}

@RestController
@RequestMapping("/api/images-videos/qr-code-generator")
class QrCodeGeneratorApiController {
    @GetMapping("/generate")
    public Map<String, Object> generateQrCode(
            @RequestParam String text,
            @RequestParam(defaultValue = "medium") String errorResistance,
            @RequestParam(defaultValue = "#000000") String fgColor,
            @RequestParam(defaultValue = "#FFFFFF") String bgColor) {

        Map<String, Object> response = new HashMap<>();

        if (text.isEmpty()) {
            response.put("isSuccess", false);
            return response;
        }

        try {
            int width = 300, height = 300;
            ErrorCorrectionLevel level = getErrorCorrectionLevel(errorResistance);

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, level);

            BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix, new MatrixToImageConfig(
                    hexToColor(fgColor), hexToColor(bgColor)));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            response.put("qrCodeImage", "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes));
            response.put("isSuccess", true);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("isSuccess", false);
        }
        return response;
    }

    // Chuyển đổi mức độ chịu lỗi
    private ErrorCorrectionLevel getErrorCorrectionLevel(String level) {
        switch (level.toLowerCase()) {
            case "low":
                return ErrorCorrectionLevel.L;
            case "medium":
                return ErrorCorrectionLevel.M;
            case "quartile":
                return ErrorCorrectionLevel.Q;
            case "high":
                return ErrorCorrectionLevel.H;
            default:
                return ErrorCorrectionLevel.M;
        }
    }

    // Chuyển mã HEX thành màu
    private int hexToColor(String hex) {
        return Integer.parseInt(hex.substring(1), 16) | 0xFF000000;
    }
}
