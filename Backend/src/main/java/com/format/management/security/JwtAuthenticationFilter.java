package com.format.management.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private TokenProvider tokenProvider;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {
		// 토큰 검증하는 필터링 구현
		
		try {
			//요청에서 토큰 가져오기... 
			String token = parseBearerToken(request);  // 밑에 만들었어요 
			log.info("Filter is running...");
			//토큰 검사하기. JWT이기 때문에 서버에 요청하지 않고 검증이 가능함. 
			if(token != null && !token.equalsIgnoreCase("null")) {
				//UserId 가져오기... 위조된 경우에는 예외처리가 됨.
				String userId = tokenProvider.validateAndGetUserId(token);
				log.info("Authenticated User ID : " + userId);
				//인증 완료; SecurityContextHolder에 등록해야 인증된 사용자라 생각하게됨.
				AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userId, //인증된 사용자 정보. 문자열 아니여됨. 
						null,	
						AuthorityUtils.NO_AUTHORITIES
				);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
				securityContext.setAuthentication(authentication);
				SecurityContextHolder.setContext(securityContext);
			}
		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context",ex);
		}
		
		filterChain.doFilter(request, response);

	}
	
	private String parseBearerToken(HttpServletRequest request) {
		//Http 요청의 헤더를 파싱하여 Bearer 토큰을 리턴하는 함수
		String bearerToken = request.getHeader("Authorization");
		
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		
		return null;
	}
	
	
}
