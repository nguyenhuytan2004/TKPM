package com.example.Project.security;

import com.example.Project.model.Role;
import com.example.Project.model.User;
import com.example.Project.repository.IUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", "ROLE_" + user.getRole().name());

            // Cập nhật lại SecurityContextHolder
            List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_" + user.getRole().name());
            Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
            SecurityContextHolder.getContext().setAuthentication(newAuth);

        }


        if (user != null && user.getRole() == Role.ADMIN) {
            response.sendRedirect("/admin/tool/management");
        } else {
            response.sendRedirect("/");
        }
    }
}
