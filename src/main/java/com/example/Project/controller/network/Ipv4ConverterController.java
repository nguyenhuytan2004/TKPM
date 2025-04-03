package com.example.Project.controller.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Project.service.ICategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.model.Category;
import com.example.Project.service.impl.CategoryService;

@Controller
@RequestMapping("/network")
public class Ipv4ConverterController {

    @Autowired
    private ICategoryService _categoryService;

    @GetMapping("/ipv4-converter")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "IPv4 Address Converter");
        model.addAttribute("body", "ipv4-converter"); // Load template ipv4-converter.hbs
        return "layout"; // Trả về layout.hbs
    }

    @RestController
    @RequestMapping("/api/network/ipv4-converter")
    public static class Ipv4ConverterApiController {

        @GetMapping
        public Map<String, String> convertIpv4(@RequestParam String ip) {
            System.out.println(ip);
            Map<String, String> info = new HashMap<>();
            try {
                InetAddress inet = InetAddress.getByName(ip);
                byte[] ipBytes = inet.getAddress();

                // Convert IPv4 to Decimal
                long decimalValue = ipToLong(ipBytes);
                info.put("decimal", String.valueOf(decimalValue));

                // Convert IPv4 to Hexadecimal
                String hexValue = String.format("%02X%02X%02X%02X", ipBytes[0] & 0xFF, ipBytes[1] & 0xFF, ipBytes[2] & 0xFF, ipBytes[3] & 0xFF);
                info.put("hexadecimal", hexValue);

                // Convert IPv4 to Binary
                String binaryValue = String.format("%8s.%8s.%8s.%8s",
                                Integer.toBinaryString(ipBytes[0] & 0xFF),
                                Integer.toBinaryString(ipBytes[1] & 0xFF),
                                Integer.toBinaryString(ipBytes[2] & 0xFF),
                                Integer.toBinaryString(ipBytes[3] & 0xFF))
                        .replace(' ', '0'); // Đảm bảo đủ 8 bit mỗi nhóm
                info.put("binary", binaryValue.replace(".", ""));

                // Convert IPv4 to IPv6
                String ipv6Full = String.format("0000:0000:0000:0000:0000:ffff:%02x%02x:%02x%02x",
                        ipBytes[0] & 0xFF, ipBytes[1] & 0xFF, ipBytes[2] & 0xFF, ipBytes[3] & 0xFF);
                info.put("ipv6", ipv6Full);

                // Shorten IPv6
                String ipv6Short = "::ffff:" + hexValue.substring(0, 4) + ":" + hexValue.substring(4);
                info.put("ipv6Short", ipv6Short);

            } catch (UnknownHostException e) {
                info.put("error", "Invalid IPv4 address");
            }
            return info;
        }

        private long ipToLong(byte[] bytes) {
            return ((bytes[0] & 0xFFL) << 24) |
                    ((bytes[1] & 0xFFL) << 16) |
                    ((bytes[2] & 0xFFL) << 8) |
                    (bytes[3] & 0xFFL);
        }
    }
}
