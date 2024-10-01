package hrms.rnt.login.security;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import hrms.rnt.login.projection.EmployeeMasterProjection;
import hrms.rnt.login.repo.EmployeeMasterRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtHelper {

	
    private static final String secretKey = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
	public static final long JWT_TOKEN_VALIDITY =2000 * 60 * 60 ;

	@Autowired
	EmployeeMasterRepo emplyeeMasterService;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	public Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(UserDetails userDetails) throws Exception {
		Map<String, Object> claims = new HashMap<>();
		EmployeeMasterProjection employeeByUserId = emplyeeMasterService.findByUserName(userDetails.getUsername());
//		String profilePicture = emplyeeMasterService.getProfilePicture(employeeByUserId.getStaffId());
		byte[] profilePicture = emplyeeMasterService.getProfilePicture(employeeByUserId.getStaffId());
		if(Objects.nonNull(profilePicture)) {
			String profilePictureBase64 = Base64.getEncoder().encodeToString(profilePicture);
			claims.put("profilePicture", profilePictureBase64);
		}

		claims.put("staffId", employeeByUserId.getStaffId());
 		claims.put("userId", employeeByUserId.getUserName());
 		claims.put("userName", employeeByUserId.getFirstName()+" "+employeeByUserId.getLastName());
 		claims.put("emailId", employeeByUserId.getEmailId());
 		claims.put("createdTime", LocalDateTime.now().plusHours(2).toString());
 		claims.put("roles", emplyeeMasterService.getEmployeeRoles(employeeByUserId.getStaffId()));
 		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	 

}
