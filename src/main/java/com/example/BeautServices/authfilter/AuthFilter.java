package com.example.BeautServices.authfilter;

import com.example.BeautServices.services.JwtService;
import com.example.BeautServices.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class AuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public AuthFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        //Check the auth token from request
        String authToken = request.getHeader("Authorization");
        //check if no header
        if(authToken == null || !authToken.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        String jwtToken = authToken.substring(7);
        String username = jwtService.extractUsername(jwtToken);

        //Check if user exists and not null otherwise fetch to database
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails fetchedUserDetails = userService.userDetailsService().loadUserByUsername(username);
            //verify loaded user with the token
            if(jwtService.isTokenValid(jwtToken, fetchedUserDetails)){
                //Create a empty security context holder
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authenticationToken = new
                        UsernamePasswordAuthenticationToken(fetchedUserDetails, null, fetchedUserDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.setContext(securityContext);

                //update the last active time
                userService.updateLastActiveUser(username);
            }
        }
      filterChain.doFilter(request,response);
    }
}
