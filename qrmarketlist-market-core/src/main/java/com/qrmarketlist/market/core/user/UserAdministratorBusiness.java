package com.qrmarketlist.market.core.user;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qrmarketlist.market.core.AbstractPersistence;
import com.qrmarketlist.market.core.Util;
import com.qrmarketlist.market.core.tenant.Tenant;
import com.qrmarketlist.market.framework.BusinessException;
/**
 * Classe que representa as regras de negócios de {@link User} com papél de administrador 
 */
@Service
public class UserAdministratorBusiness extends AbstractPersistence<User> {
	
	/**
	 * Salva ou atualiza usuário
	 * @param user
	 * @param tenant
	 * @throws BusinessException
	 */
	public void saveOrUpdate(User user, Tenant tenant) throws BusinessException {
		
		if (user.getId() != null) {
			if (user.getPassword().equals("")) {
				String oldPass = retrieveUserByIdAndTenant(user.getId(), tenant).getPassword();
				user.setPassword(oldPass);
			} else {
				user.setPassword(Util.encrypt(user.getPassword()));
			}
			if (user.getPasswordConfirmation() == null || user.getPasswordConfirmation().equals("")) {
				String oldPass = retrieveUserByIdAndTenant(user.getId(), tenant).getPassword();
				user.setPasswordConfirmation(oldPass);
			} else {
				user.setPasswordConfirmation(Util.encrypt(user.getPasswordConfirmation()));
			}
			createOrUpdate(user);
		} else {
			user.setTenant(tenant);
			user.setStatus(UserEnum.ACTIVE);
			user.setAdministrator(true);
			user.setPassword(Util.encrypt(user.getPassword()));
			user.setPasswordConfirmation(Util.encrypt(user.getPasswordConfirmation()));
			createOrUpdate(user);
		}
	}
	
	/**
	 * Recupera usuário por id e {@link Tenant}
	 * @param id
	 * @param tenant
	 * @return User
	 */
	public User retrieveUserByIdAndTenant(Long id, Tenant tenant) {
	    if (!super.retrieve(eq("id", id), eq("tenant", tenant)).isEmpty()) {
	        return super.retrieve(eq("id", id), eq("tenant", tenant)).iterator().next();
	    }
		return null;
	}
	
	/**
	 * Recupera usuário por email e {@link Tenant}
	 * @param email
	 * @param tenant
	 * @return User
	 */
	public User retrieveUserByEmailAndTenant(String email, Tenant tenant) {
		List<User> list = super.retrieve(eq("email", email), eq("tenant", tenant));
		if (list == null || list.size() == 0)
			return null;
		else
			return list.get(0);
	}
	
	/**
	 * Recupera usuário por nome e {@link Tenant}
	 * @param username
	 * @param tenant
	 * @return User
	 */
	public User retrieveUserByUsernameAndTenant(String username, Tenant tenant) {
		List<User> list = super.retrieve(eq("username", username), eq("tenant", tenant));
		if (list == null || list.size() == 0)
			return null;
		else
			return list.get(0);
	}
	
}