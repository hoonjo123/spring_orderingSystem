package com.encore.ordering.securities;

import com.encore.ordering.common.ErrorResponseDto;
import com.encore.ordering.member.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.security.sasl.AuthenticationException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthFilter extends GenericFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String bearerToken = ((HttpServletRequest) request).getHeader("Authorization");
            if(bearerToken != null) {
                if (!bearerToken.substring(0, 7).equals("Bearer ")) {
                    throw new AuthenticationServiceException("token의 형식이 맞지 않습니다.");
                }
                //bearer토큰에서 토큰값만 추출
                String token = bearerToken.substring(7);
                /*"mysecret"
                 * 추출된 토큰을 검증 후 Authentication객체 생성 */
                //토큰 검증 및 claims 추출
                Claims claims = Jwts.parser().setSigningKey("mysecret").parseClaimsJws(token).getBody();

                //Authentication 객체를 생성하기 위한 UserDetail생성
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_"+claims.get("role")));
                UserDetails userDetails = new User(claims.getSubject(), "", authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "",userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            //filterchain에서 그다음 filtering으로 넘어가도록 하는 메서드
            chain.doFilter(request, response);
        } catch (AuthenticationServiceException e) {
            HttpServletResponse httpServletResponse = (HttpServletResponse)response;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("applicaion/json");
            httpServletResponse.getWriter().write(ErrorResponseDto.makeMessage(HttpStatus.UNAUTHORIZED,e.getMessage()).toString());
        }
    }

}
