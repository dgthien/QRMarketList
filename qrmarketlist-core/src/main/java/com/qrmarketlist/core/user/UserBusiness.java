package com.qrmarketlist.core.user;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qrmarketlist.core.AbstractPersistence;
import com.qrmarketlist.core.AuthenticationContext;
import com.qrmarketlist.core.BusinessException;
import com.qrmarketlist.core.tenant.Tenant;

@Service
public class UserBusiness extends AbstractPersistence<User> {
	
	@Autowired
	private AuthenticationContext authenticationContext;
	
	/**
	 * Recupera usuários por nome e senha
	 * @param username
	 * @param password
	 * @return {@link User}
	 */
	public User retrieveUserByNameAndPasswordAndTenant(String username, String password, Tenant tenant) {
		List<User> user = retrieve(eq("username", username), eq("password", password), eq("tenant", tenant));
		if (user == null || user.size() == 0) {
			return null;
		}
		return user.get(0);
	}
	
	/**
	 * Recupera usuário por email
	 * @param email
	 * @return {@link User}
	 */
	public User retrieveUserByEmail(String email) {
		List<User> user = retrieve(eq("email", email));
		if (user == null || user.size() == 0) {
			return null;
		}
		return user.get(0);
	}
	
	/**
	 * Recupera usuário por nome
	 * @param username
	 * @return {@link User}
	 */
	public User retrieveUserByUsername(String username) {
		List<User> user = retrieve(eq("username", username));
		if (user == null || user.size() == 0) {
			return null;
		}
		return user.get(0);
	}
	
	public User retrieveGlobalAdministrator() {
		List<User> users = retrieve(eq("administrator", true));
		if (users == null || users.size() == 0)
			return null;
		return users.get(0);
	}
	
	/**
	 * Salva ou atualiza usuário
	 * @param user
	 * @param tenant
	 * @throws BusinessException
	 */
	public User saveOrUpdate(User user)  {
		try {
			if (user.getId() != null) {
				if (user.getPassword().equals("")) {
					String oldPass = retrieveById(user.getId()).getPassword();
					user.setPassword(oldPass);
				} else {
					user.setPassword(user.getPassword());
				}
				if (user.getPasswordConfirmation() == null || user.getPasswordConfirmation().equals("")) {
					String oldPass = retrieveById(user.getId()).getPassword();
					user.setPasswordConfirmation(oldPass);
				} else {
					user.setPasswordConfirmation(user.getPasswordConfirmation());
				}
				user = createOrUpdate(user);
			} else {
				user.setStatus(UserEnum.ACTIVE);
				user.setAdministrator(true);
				user.setPassword(user.getPassword());
				user.setPasswordConfirmation(user.getPasswordConfirmation());
				user.setTenant(authenticationContext.getTenant());
				user = createOrUpdate(user);
			}
		} catch (BusinessException e) {
			LoggerFactory.getLogger(this.getClass()).error(e.getMessage());
		}
		return user;
	}
	
	/**
	 * <p>
	 * Inativa o {@link User}, atualizando o campo {@link User#getStatus()}
	 * para o valor {@link UserEnum#INACTIVE}. 
	 * Ao inativar o usuário é verificado se este está na lista de espera, 
	 * caso esteja, o mesmo é eliminado
	 * </p>
	 * @param user
	 * @throws BusinessException
	 */
	public void inactive(User user) throws BusinessException {
		user.setPasswordConfirmation(user.getPassword());
		user.setStatus(UserEnum.INACTIVE);
		super.createOrUpdate(user);
	}
	
	/**
	 * <p>
	 * Reativa o {@link User}, atualizando o campo {@link User#getStatus()}
	 * para o valor {@link UserEnum#ACTIVE}. 
	 * </p>
	 * @param user
	 * @throws BusinessException
	 */
	public void reactivate(User user) throws BusinessException {
		user.setPasswordConfirmation(user.getPassword());
		user.setStatus(UserEnum.ACTIVE);
		super.createOrUpdate(user);
	}

}
