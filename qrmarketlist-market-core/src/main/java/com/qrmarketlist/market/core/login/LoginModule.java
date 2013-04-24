package com.qrmarketlist.market.core.login;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.qrmarketlist.market.core.AuthenticationContext;
import com.qrmarketlist.market.core.RoleEnum;
import com.qrmarketlist.market.core.Util;
import com.qrmarketlist.market.core.tenant.Tenant;
import com.qrmarketlist.market.core.tenant.TenantBusiness;
import com.qrmarketlist.market.core.tenant.TenantEnum;
import com.qrmarketlist.market.core.user.User;
import com.qrmarketlist.market.core.user.UserBusiness;
import com.qrmarketlist.market.core.user.UserEnum;

/**
 * Classe responsável por efetar login ao sistema
 */
@Component
public class LoginModule implements AuthenticationProvider {

	@Autowired
	private TenantBusiness tenantBusiness;
	@Autowired
	private AuthenticationContext authenticationContext;
	@Autowired
	private UserBusiness userBusiness;
	@Autowired
	private HttpServletRequest request;
	private User user;
	private Collection<GrantedAuthority> authorities;
	private Tenant tenant;
	
	@Override
	public Authentication authenticate(Authentication authentication) {
		tenant = tenantBusiness.retrieveByDomain(request.getServerName());
		if (tenant == null) {
			throw new BadCredentialsException("invalid_tenant_see_your_system_administrator");
		}
		if (tenant.getStatus() == TenantEnum.INACTIVE) {
			throw new BadCredentialsException("inactive_tenant_see_the_system_administrator");
		}
		String password = (String) (authentication.isAuthenticated() ? 
						  (String) authentication.getCredentials() : 
							  authentication.getCredentials());
		user = userBusiness.retrieveUserByNameAndPasswordAndTenant((String) authentication.getPrincipal(), Util.encrypt(password), tenant);
		if (user == null) {
			throw new BadCredentialsException("invalid_user_or_password");
		}
		if (tenant.getStatus() == TenantEnum.SUSPENDED && !user.getAdministrator()) {
			throw new BadCredentialsException("tenant_suspended_see_your_system_administrator");
		}
		if (user.getStatus() != UserEnum.ACTIVE) {
			throw new BadCredentialsException("inactive_user_see_your_system_administrator");
		}
		populateUserAuthorities();
		populateAuthenticationContext();
		return new UsernamePasswordAuthenticationToken(authenticationContext, password, authorities);
	}
	
	/**
	 * Popula todas as autorização correspondentes ao usuário 
	 */
	public void populateUserAuthorities() {
		authorities = this.getUserAuthorities();
	}
	
	/**
	 * <p>
	 * Preenche a lista de permissões do usuário
	 * </p>
	 * 
	 * @return
	 */
	public Collection<GrantedAuthority> getUserAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(RoleEnum.USER_AUTHENTICATED.toString()));
		return authorities;
	}
	
	/**
	 * Configura contexto de autenticação
	 */
	public void populateAuthenticationContext() {
		authenticationContext.setUser(user);
		authenticationContext.setAuthorities(authorities);
		authenticationContext.setTenant(tenant);
	}
	
	/**
	 * Faz a autenticação do usuário
	 */
	public void forceAuthenticate() {
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				authenticationContext.getUser().getUsername(),
				authenticationContext.getUser().getPassword(),
				new ArrayList<GrantedAuthority>());
		authentication = this.authenticate(authentication);
		if (authentication.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(arg0));
	}
	
}