package com.example.expensetrackerapi.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.expensetrackerapi.constants.GlobalConstants;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class AuthFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpReqObject = (HttpServletRequest) req;
        HttpServletResponse httpRespObject = (HttpServletResponse) resp;

        String authHeader = httpReqObject.getHeader("Authorization");

        if (authHeader != null) {
            String[] authHeaderArray = authHeader.split("Bearer");

            if (authHeaderArray.length > 1 && authHeaderArray[1] != null) {
                try {
                    String token = authHeaderArray[1];
                    Claims claims = Jwts.parser().setSigningKey(GlobalConstants.API_SECRET_KEY)
                        .parseClaimsJws(token).getBody();

                    httpReqObject.setAttribute("userId", Integer.parseInt(claims.get("userId").toString()));

                } catch (Exception e) {
                    httpRespObject.sendError(HttpStatus.FORBIDDEN.value(), "Invalid/expired token");
                    return;
                }
            } else{

                httpRespObject.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be in , Bearer <token> format");
                return;
            }
        } else {
            httpRespObject.sendError(HttpStatus.FORBIDDEN.value(),"Authorization Token Must be provided");
            return;
        }
        filterChain.doFilter(req, resp);
    }

}
