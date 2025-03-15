package com.example.Project.controller.crypto;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.model.Category;
import com.example.Project.service.CategoryService;

@Controller
@RequestMapping("")
public class HashTextController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/crypto/hash-text")
    public String show(Model model) {
        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        return "hash-text";
    }

    @RestController
    @RequestMapping("")
    public class HashController {

        private static final List<String> ALGORITHMS = List.of(
                "MD5", "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512", "SHA3-256", "SHA3-512");

        @GetMapping("/api/crypto/hash-text")
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
