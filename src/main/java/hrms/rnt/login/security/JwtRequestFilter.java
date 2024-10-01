package hrms.rnt.login.security;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * @author Vishal Dabade
 *
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	JwtHelper jwtUtil;

	@Autowired
	CustomeUserDetailsService customeUserDetailsService;
	
	 
	public final  String AUTHORIZATION="Authorization";
	public final  String BEARER="Bearer";

	@SuppressWarnings("unused")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
 		try { 
 			System.out.println(request.getServletPath());
  			if (true) {
				filterChain.doFilter(request, response);
 			} else {
 				String requestTokenHeader = request.getHeader(AUTHORIZATION);
				if (Objects.isNull(requestTokenHeader))
					throw new MissingServletRequestPartException("AUTHORIZATION Header is missing");
				String userName;
				if (requestTokenHeader.startsWith(BEARER) && Objects.nonNull(requestTokenHeader)) {
					requestTokenHeader = requestTokenHeader.substring(7);
 					
					userName = this.jwtUtil.extractUsername(requestTokenHeader);
					UserDetail loadUserByUsername = this.customeUserDetailsService.loadUserByUsername(userName);
					if (Boolean.TRUE.equals(this.jwtUtil.validateToken(requestTokenHeader, loadUserByUsername))) {
						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								loadUserByUsername, null, loadUserByUsername.getAuthorities());
						usernamePasswordAuthenticationToken.setDetails(loadUserByUsername);
						getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				}
				filterChain.doFilter(request, response);
 			}
		} catch (Exception e) {
 
		}

	}

}
