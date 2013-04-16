package com.qrmarketlist.market.core.login;

import java.util.ArrayList;
import java.util.Collection;

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
import com.qrmarketlist.market.core.user.User;
import com.qrmarketlist.market.core.user.UserBusiness;
import com.qrmarketlist.market.core.user.UserEnum;

/**
 * Classe responsável por efetar login ao sistema
 */
@Component
public class LoginModule implements AuthenticationProvider {

	@Autowired
	private AuthenticationContext authenticationContext;
	@Autowired
	private UserBusiness userBusiness;
	private User user;
	private Collection<GrantedAuthority> authorities;
	
	@Override
	public Authentication authenticate(Authentication authentication) {
		String password = (String) (authentication.isAuthenticated() ? 
						  (String) authentication.getCredentials() : 
							  authentication.getCredentials());
		user = userBusiness.retrieveUserByNameAndPassword((String) authentication.getPrincipal(), password);
		if (user == null) {
			throw new BadCredentialsException("invalid_user_or_password");
		} else {
			if (user.getStatus() == UserEnum.INACTIVE) {
				throw new BadCredentialsException("invalid_user_status");
			}
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
