package com.example.Project.controller.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
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
import com.example.Project.service.impl.CategoryService;

@Controller
@RequestMapping("/network")
public class Ipv4RangeExpanderController {

    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/ipv4-range-expander")
    public String showExpanderPage(Model model) {
        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "IPv4 Range Expander");
        model.addAttribute("body", "ipv4-range-expander"); // Load template ipv4-range-expander.hbs
        return "layout"; // Trả về layout.hbs
    }

    @RestController
    @RequestMapping("/api/network/ipv4-range-expander")
    public static class Ipv4RangeExpanderApiController {

        @GetMapping
        public Map<String, String> expandRange(@RequestParam String start, @RequestParam String end) {
            Map<String, String> info = new HashMap<>();
            try {
                // Chuyển đổi IP thành số nguyên không âm
                long startIp = ipToLong(InetAddress.getByName(start).getAddress());
                long endIp = ipToLong(InetAddress.getByName(end).getAddress());

                if (startIp > endIp) {
                    info.put("error", "Start IP must be less than or equal to End IP");
                    return info;
                }

                // Tìm subnet nhỏ nhất chứa cả dải IP
                int prefix = findCidrPrefix(startIp, endIp);
                long networkIp = startIp & subnetMask(prefix);
                long broadcastIp = networkIp | ~subnetMask(prefix);

                // Ghi kết quả
                info.put("oldStart", start);
                info.put("newStart", longToIp(networkIp));

                info.put("oldEnd", end);
                info.put("newEnd", longToIp(broadcastIp));

                info.put("oldRange", String.valueOf(endIp - startIp + 1));
                info.put("newRange", String.valueOf((long) Math.pow(2, (32 - prefix))));

                info.put("cidr", longToIp(networkIp) + "/" + prefix);

            } catch (UnknownHostException e) {
                info.put("error", "Invalid IP address");
            }
            return info;
        }

        // Chuyển IP từ byte[] sang số nguyên không âm
        private long ipToLong(byte[] bytes) {
            return ((bytes[0] & 0xFFL) << 24) |
                    ((bytes[1] & 0xFFL) << 16) |
                    ((bytes[2] & 0xFFL) << 8) |
                    (bytes[3] & 0xFFL);
        }

        // Chuyển số nguyên về IPv4
        private String longToIp(long ip) {
            return String.format("%d.%d.%d.%d",
                    (ip >> 24) & 0xFF,
                    (ip >> 16) & 0xFF,
                    (ip >> 8) & 0xFF,
                    ip & 0xFF);
        }

        // Tạo subnet mask từ prefix
        private long subnetMask(int prefix) {
            return 0xFFFFFFFFL << (32 - prefix);
        }

        // Tìm prefix CIDR phù hợp nhất
        private int findCidrPrefix(long startIp, long endIp) {
            long range = endIp - startIp + 1; // Thêm 1 để bao gồm cả IP cuối
            int prefix = 32;

            while (prefix > 0 && (1L << (32 - prefix)) < range) {
                prefix--;
            }
            return prefix;
        }
    }
}
