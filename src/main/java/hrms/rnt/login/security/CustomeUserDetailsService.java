package hrms.rnt.login.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hrms.rnt.login.model.EmployeeMaster;
import hrms.rnt.login.projection.EmployeeMasterProjection;
import hrms.rnt.login.repo.EmployeeMasterRepo;

/**
 * @author Vishal Dabade
 *
 */
@Service
public class CustomeUserDetailsService implements UserDetailsService {

	@Autowired
	EmployeeMasterRepo emplyeeMasterService;

	@Override
	public UserDetail loadUserByUsername(String userId) throws UsernameNotFoundException {
		try {
			EmployeeMasterProjection optionalUser = emplyeeMasterService.findByUserName(userId);
			if (Objects.nonNull(optionalUser))
				return new UserDetail(optionalUser.getUserName(), optionalUser.getPassword(), true, true, true, true,
						maGrantedAuthorities(), optionalUser.getStaffId());
		} catch (Exception e) {
		}
		return null;
	}

	private Collection<? extends GrantedAuthority> maGrantedAuthorities() {
		List<String> list = Arrays.asList("User", "Admin");
		return list.stream().map(e -> new SimpleGrantedAuthority(e)).collect(Collectors.toList());

	}
}
