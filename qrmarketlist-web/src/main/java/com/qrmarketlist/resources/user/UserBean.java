package com.qrmarketlist.resources.user;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.slf4j.LoggerFactory;

import com.qrmarketlist.core.AbstractPersistence;
import com.qrmarketlist.core.AuthenticationContext;
import com.qrmarketlist.core.BusinessException;
import com.qrmarketlist.core.SpringUtils;
import com.qrmarketlist.core.user.User;
import com.qrmarketlist.core.user.UserBusiness;
import com.qrmarketlist.resources.AbstractBean;

/**
 * @author geny.herrera
 * 
 */
@RequestScoped
@ManagedBean
public class UserBean extends AbstractBean<User> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{userBusiness}")
	private transient UserBusiness userBusiness;
	@ManagedProperty(value = "#{authenticationContext}")
	private AuthenticationContext authenticationContext;

	public UserBean() {
		userBusiness = (UserBusiness) SpringUtils.getBean("userBusiness");
		authenticationContext = (AuthenticationContext) SpringUtils
				.getBean("authenticationContext");
	}

	@Override
	public AbstractPersistence<User> getBusiness() {
		return userBusiness;
	}

	@Override
	public String goToPage() {
		return "user.xhtml";
	}

	@Override
	public String goToList() {
		return "users.xhtml";
	}

	public void setUserBusiness(UserBusiness userBusiness) {
		this.userBusiness = userBusiness;
	}

	public void setAuthenticationContext(
			AuthenticationContext authenticationContext) {
		this.authenticationContext = authenticationContext;
	}

	@Override
	public String create() {
		User newUser = new User();
		setEntity(newUser);
		return "user.xhtml";
	}

	public String save() {
		userBusiness.saveOrUpdate(getEntity());
		addMessage(FacesMessage.SEVERITY_INFO, "", "Salvo com sucesso");
		return goToList();
	}
	
	public String inactivate() {
		if (getSelectedEntity() == null) {
			addMessage(FacesMessage.SEVERITY_INFO, "", "Selecione um usuário para inativar");
			return goToList();
		}
		try {
			userBusiness.inactive(getSelectedEntity());
			addMessage(FacesMessage.SEVERITY_INFO, "", "Inativado com sucesso");
		} catch (BusinessException e) {
			LoggerFactory.getLogger(UserBean.class).error("Error inactivating User " + getSelectedEntity().getUsername(), e);
			addMessage(FacesMessage.SEVERITY_INFO, "", e.getMessage());
		} finally {
			setListDataModel(null);
		}
		return goToList();
	}
	
	@Override
	public String edit() {
		String strReturn = "";
		if (getSelectedEntity() == null) {
			addMessage(FacesMessage.SEVERITY_INFO, "", "Selecione um registro para editar");
		} else  {
			setEntity(userBusiness.retrieveById(getSelectedEntity().getId()));
			strReturn = "user.xhtml";
		}
		return strReturn;
	}
	
	public String reactivate() {
		if (getSelectedEntity() == null) {
			addMessage(FacesMessage.SEVERITY_INFO, "", "Selecione um usuário para reativar");
			return goToList();
		}
		try {
			userBusiness.reactivate(getSelectedEntity());
			addMessage(FacesMessage.SEVERITY_INFO, "", "Reativado com sucesso");
		} catch (BusinessException e) {
			LoggerFactory.getLogger(UserBean.class).error("Error reactivating User " + getSelectedEntity().getUsername(), e);
			addMessage(FacesMessage.SEVERITY_INFO, "", e.getMessage());
		} finally {
			setListDataModel(null);
		}
		return goToList();
	}
}