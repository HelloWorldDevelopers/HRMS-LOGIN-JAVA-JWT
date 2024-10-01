package hrms.rnt.login.security;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Vishal Dabade
 *
 */
public class AuthenticationUtil {
	
	public static final String ASTERISK ="*";
	public static final String LOGIN_AUTHENTICATE="/api/v1";
	public static final String AUTHENTICATION="/authentication";
	public static final String HOME="/api/v1/home";
 	public static final String TOKEN="/token";
	public static final String PARSE="/parse";
	public static final String LOGIN="/login";
	public static final String ALL_MODULE="/allapps";
	 

	public static final Predicate<String> ALLOW_URL = s -> {
		 
		if (isNull(s))
			return false;
		if (AuthenticationUtil.PUBLIC_APIS.get().stream().anyMatch(s::startsWith))
			return true;
		return false;
	};
	
	public static final Supplier<List<String>> PUBLIC_APIS = () -> {
		List<String> api = new ArrayList<>();
		api.add(LOGIN_AUTHENTICATE+AUTHENTICATION+LOGIN); 
		api.add(LOGIN_AUTHENTICATE+AUTHENTICATION+PARSE);
		api.add(LOGIN_AUTHENTICATE+AUTHENTICATION+ALL_MODULE);
  		return api;
		
	};
	public static final String[] PUBLIC_URLS = { "/api/v1/**", };
}
