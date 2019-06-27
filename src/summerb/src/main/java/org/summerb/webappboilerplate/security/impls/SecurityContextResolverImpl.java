package org.summerb.webappboilerplate.security.impls;

import org.springframework.security.core.userdetails.UserDetails;
import org.summerb.approaches.security.api.CurrentUserNotFoundException;
import org.summerb.approaches.spring.security.impl.SecurityContextResolverAbstract;
import org.summerb.microservices.users.api.dto.User;

import com.google.common.base.Preconditions;

public class SecurityContextResolverImpl extends SecurityContextResolverAbstract<User> {

	@Override
	public String getUserUuid() throws CurrentUserNotFoundException {
		return getUser().getUuid();
	}

	@Override
	protected User getUserFromUserDetails(UserDetails principal) {
		Preconditions.checkArgument(principal instanceof UserDetailsImpl);
		return ((UserDetailsImpl) principal).getUser();
	}

}
