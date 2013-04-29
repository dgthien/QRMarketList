package com.qrmarketlist.core.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.qrmarketlist.core.AbstractTenantEntity;

/**
 * Classe que representa a entidade {@link User}
 */
@Entity
@Table(name = "USER")
@PrimaryKeyJoinColumn(name = "ID")
public class User extends AbstractTenantEntity {

	private static final long serialVersionUID = 1L;
	private String name;
	private String username;
	private String password;
	private String passwordConfirmation;
	private Boolean administrator;
	private UserEnum status;
	private String email;
	private String emailConfirmation;

	@Column(name = "NAME", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retorna o login de acesso do usuário
	 * 
	 * @return String
	 */
	@Column(name = "USERNAME", length = 50, nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Retorna a senha de acesso do usuário
	 * 
	 * @return String
	 */
	@Column(name = "PASSWORD", length = 50, nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Retorna a confirmação de senha do cadastro de usuário (apenas para
	 * confirmação no cadastro de usuário)
	 * 
	 * @return String
	 */
	@Transient
	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	/**
	 * Retorna se o usuário é administrador do sistema
	 * 
	 * @return Boolean
	 */
	@Column(name = "ADMINISTRATOR", nullable = false)
	public Boolean getAdministrator() {
		return administrator;
	}

	public void setAdministrator(Boolean administrator) {
		this.administrator = administrator;
	}

	/**
	 * Retorna o status do usuário
	 * 
	 * @return UserEnum
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 50, nullable = false)
	public UserEnum getStatus() {
		return status;
	}

	public void setStatus(UserEnum status) {
		this.status = status;
	}
	
	/**
	 * Retorna o email do usuário
	 * @return String
	 */
	@Size(max = 50)
	@Column(name = "EMAIL", length = 50, nullable = true)
	public String getEmail() {
		return email;
	}
	
	/**
	 * {@link User#getEmail()}
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Retorna a confirmação de email do cadastro de usuário 
	 * (apenas para confirmação no cadastro de usuário)
	 * @return String
	 */
	@Transient
	public String getEmailConfirmation() {
		return emailConfirmation;
	}

	/**
	 * {@link User#getEmailConfirmation()}
	 */
	public void setEmailConfirmation(String emailConfirmation) {
		this.emailConfirmation = emailConfirmation;
	}
	
}