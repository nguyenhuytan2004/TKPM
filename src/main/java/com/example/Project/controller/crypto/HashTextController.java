package com.example.Project.controller.crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.Project.service.IToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("crypto")
public class HashTextController {

    @Autowired
    private IToolService _toolService;

    @GetMapping("/hash-text")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_USER"));

        if (isUser && _toolService.isPremiumToolByName("Hash Text")) {
            return "404";
        }

        model.addAttribute("title", "Hash Text Tool"); // Thêm tiêu đề
        model.addAttribute("body", "hash-text"); // Load trang con
        return "layout"; // Load vào layout.hbs
    }

    @RestController
    @RequestMapping("api/crypto/hash-text")
    public class HashTextApiController {
        private static final List<String> ALGORITHMS = List.of(
                "MD5", "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512", "SHA3-256", "SHA3-512");

        @GetMapping("")
        public Map<String, String> generateHashes(
                @RequestParam String format, @RequestParam String text) {
            Map<String, String> result = new LinkedHashMap<>();

            for (String algorithm : ALGORITHMS) {
                try {
                    MessageDigest digest = MessageDigest.getInstance(algorithm);
                    byte[] hashBytes = digest.digest(text.getBytes(StandardCharsets.UTF_8));

                    String hash = switch (format) {
                        case "Binary (base 2)" -> new BigInteger(1, hashBytes).toString(2);
                        case "Hexadecimal (base 16)" -> new BigInteger(1, hashBytes).toString(16);
                        case "Base64 (base 64)" -> Base64.getEncoder().encodeToString(hashBytes);
                        case "Base64url (base 64 with URL safe chars)" ->
                            Base64.getUrlEncoder().encodeToString(hashBytes);
                        default -> "";
                    };

                    result.put(algorithm, hash);
                } catch (NoSuchAlgorithmException e) {
                    result.put(algorithm, "Unsupported");
                }
            }
            return result;
        }
    }

}
