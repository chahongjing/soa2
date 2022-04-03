package com.zjy.securitycommon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 将用户的登录信息放到上下文中
 */
public class AuthenticationTokenFilter extends BasicAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

    public AuthenticationTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String token = "";
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(JwtTokenUtils.TOKEN_HEADER)) {
                    token = cookie.getValue();
                }
            }
        }
        if(token != null && !token.trim().equals("")) {
            String username = JwtTokenUtils.getUsername(token);
            LOGGER.info("checking username:{}", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                List<String> authorityAndRoleList = new ArrayList<>();
                authorityAndRoleList.add("myAuthority");
                authorityAndRoleList.add(ROLE_PREFIX + "myRole");
                List<SimpleGrantedAuthority> collect = authorityAndRoleList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                User user = new User(username, "abc", collect);
                // 获取用户权限，绑定到上下文中 AbstractSecurityInterceptor.attemptAuthorization
//                UserDetails user = this.userDetailsService.loadUserByUsername(username);
                // 验证token是否有效
//                if (JwtTokenUtils.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    // 注意，这里set后系统中都会按这个来操作，包括权限，因此这里的权限应该是真正的权限
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    LOGGER.info("authenticated user:{}", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
            }
        }
        chain.doFilter(request, response);
    }
}
