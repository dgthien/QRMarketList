package com.qrmarketlist.core;

import java.util.Collection;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.qrmarketlist.core.tenant.Tenant;
import com.qrmarketlist.core.user.User;

/**
 * Classe que representa o contexoto de autenticação de usuários 
 */
@Component(value = "authenticationContext")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthenticationContext implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private User user;
	private Tenant tenant;
	private transient Collection<? extends GrantedAuthority> authorities;
	
	public Tenant getTenant() {
		return tenant;
	}
	
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
	
	/**
	 * Retorna o usuário que esta acessando o sistema
	 * @return User
	 */
	public User getUser() {
		return user;
	}

	/**
	 * {@link AuthenticationContext#getUser()}
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Retorna as roles do usuário
	 * @return Collection<? extends GrantedAuthority>
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	/**
	 * {@link AuthenticationContext#getAuthorities()}
	 */
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	/**
	 * Retorna o "password" do usuário
	 * @return String
	 */
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	/**
	 * Retorna o "username" do usuário
	 * @return String
	 */
	@Override
	public String getUsername() {
		return user.getName();
	}

	/**
	 * Retorna se a conta não esta expirada
	 * @return boolean
	 */
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	/**
	 * Retorna se a conta não esta bloqueada
	 * @return boolean
	 */
	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	/**
	 * Retorna se a credenciais não expirarão
	 * @return boolean
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	/**
	 * Retorna se a conta esta habilitada
	 * @return boolean
	 */
	@Override
	public boolean isEnabled() {
		return false;
	}
}
