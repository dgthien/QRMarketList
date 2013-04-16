package com.qrmarketlist.market.resources.setup;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import com.qrmarketlist.market.core.user.User;
import com.qrmarketlist.market.core.user.UserBusiness;
import com.qrmarketlist.market.core.user.UserEnum;

@ManagedBean
@RequestScoped
public class SetupBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private User user;

	@ManagedProperty(value = "#{userBusiness}")
	private transient UserBusiness userBusiness;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUserBusiness(UserBusiness userBusiness) {
		this.userBusiness = userBusiness;
	}

	public String form() {
		User formUser = new User();
		formUser.setName("Administrator");
		formUser.setUsername("admin");
		formUser.setPassword("admin");
		formUser.setPasswordConfirmation("admin");
		formUser.setAdministrator(true);
		formUser.setStatus(UserEnum.ACTIVE);
		setUser(formUser);
		return "setupForm.xhtml";
	}

	public String save() {
		this.userBusiness.saveOrUpdate(user);
		return "setupEnd.xhtml";
	}

	public String finish() {
		return "login.xhtml?faces-redirect=true";
	}
	
	
}