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

@Controller
@RequestMapping("/network")
public class SubnetCalculatorController {
    @Autowired
    private ICategoryService _categoryService;

    @GetMapping("/ipv4-subnet")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "IPv4 Subnet Calculator");
        model.addAttribute("body", "subnet"); // Load template subnet.hbs
        return "layout"; // Trả về layout.hbs
    }

    @RestController
    @RequestMapping("/api/network/ipv4-subnet")
    public static class SubnetApiController {

        @GetMapping("/calculate")
        public Map<String, String> calculateSubnet(@RequestParam String ip) {
            Map<String, String> info = new HashMap<>();
            try {
                String[] parts = ip.split("/");
                String ipAddress = parts[0];
                int prefix = Integer.parseInt(parts[1]);

                InetAddress inet = InetAddress.getByName(ipAddress);
                byte[] ipBytes = inet.getAddress();
                int mask = 0xFFFFFFFF << (32 - prefix);

                // Tính Netmask
                String netmask = String.format("%d.%d.%d.%d",
                        (mask >> 24) & 0xFF,
                        (mask >> 16) & 0xFF,
                        (mask >> 8) & 0xFF,
                        mask & 0xFF);

                // Netmask dạng nhị phân
                String netmaskBinary = String.format("%32s", Integer.toBinaryString(mask)).replace(' ', '0')
                        .replaceAll("(.{8})", "$1."); // Chia nhóm 8-bit

                // Tính wildcard mask
                int wildcard = ~mask;
                String wildcardMask = String.format("%d.%d.%d.%d",
                        (wildcard >> 24) & 0xFF,
                        (wildcard >> 16) & 0xFF,
                        (wildcard >> 8) & 0xFF,
                        wildcard & 0xFF);

                // Địa chỉ mạng
                int network = ipToInt(ipBytes) & mask;
                String networkAddress = intToIp(network);

                // Địa chỉ broadcast
                int broadcast = network | ~mask;
                String broadcastAddress = intToIp(broadcast);

                // Địa chỉ host đầu tiên và cuối cùng
                String firstAddress = intToIp(network + 1);
                String lastAddress = intToIp(broadcast - 1);

                // CIDR Notation
                String cidrNotation = "/" + prefix;

                // Số lượng địa chỉ trong mạng (trừ 2 địa chỉ mạng và broadcast)
                int totalHosts = (1 << (32 - prefix));

                // Thêm thông tin vào Map trả về
                info.put("ip", ip);
                info.put("netmask", netmask);
                info.put("networkMask", netmask); // Trùng với netmask
                info.put("networkMaskBinary", netmaskBinary);
                info.put("networkAddress", networkAddress);
                info.put("wildcardMask", wildcardMask);
                info.put("networkSize", String.valueOf(totalHosts));
                info.put("firstAddress", firstAddress);
                info.put("lastAddress", lastAddress);
                info.put("broadcastAddress", broadcastAddress);
                info.put("ipClass", getIpClass(ipBytes[0]));
                info.put("cidrNotation", cidrNotation);
            } catch (UnknownHostException | NumberFormatException e) {
                info.put("error", "Invalid IP address");
            }
            return info;
        }

        private int ipToInt(byte[] bytes) {
            return ((bytes[0] & 0xFF) << 24) |
                    ((bytes[1] & 0xFF) << 16) |
                    ((bytes[2] & 0xFF) << 8) |
                    (bytes[3] & 0xFF);
        }

        private String intToIp(int ip) {
            return String.format("%d.%d.%d.%d",
                    (ip >> 24) & 0xFF,
                    (ip >> 16) & 0xFF,
                    (ip >> 8) & 0xFF,
                    ip & 0xFF);
        }

        private String getIpClass(int firstOctet) {
            firstOctet = firstOctet & 0xFF;
            if (firstOctet >= 1 && firstOctet <= 126)
                return "A";
            if (firstOctet >= 128 && firstOctet <= 191)
                return "B";
            if (firstOctet >= 192 && firstOctet <= 223)
                return "C";
            if (firstOctet >= 224 && firstOctet <= 239)
                return "D (Multicast)";
            if (firstOctet >= 240 && firstOctet <= 255)
                return "E (Experimental)";
            return "Unknown1";
        }
    }
}