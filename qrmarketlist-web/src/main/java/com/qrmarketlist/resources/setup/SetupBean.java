package com.qrmarketlist.resources.setup;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.slf4j.LoggerFactory;

import com.qrmarketlist.core.BusinessException;
import com.qrmarketlist.core.tenant.Tenant;
import com.qrmarketlist.core.tenant.TenantBusiness;
import com.qrmarketlist.core.tenant.TenantEnum;
import com.qrmarketlist.core.user.User;
import com.qrmarketlist.core.user.UserAdministratorBusiness;
import com.qrmarketlist.core.user.UserBusiness;
import com.qrmarketlist.core.user.UserEnum;

@ManagedBean
@RequestScoped
public class SetupBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private User user;
	private Tenant tenant;
	private String domain;

	@ManagedProperty(value="#{tenantBusiness}")
	private transient TenantBusiness tenantBusiness;
	@ManagedProperty(value="#{userAdministratorBusiness}")
	private transient UserAdministratorBusiness userAdministratorBusiness;
	
	public void setTenantBusiness(TenantBusiness tenantBusiness) {
		this.tenantBusiness = tenantBusiness;
	}
	
	public void setUserAdministratorBusiness(
			UserAdministratorBusiness userAdministratorBusiness) {
		this.userAdministratorBusiness = userAdministratorBusiness;
	}

	public Tenant getTenant() {
		return tenant;
	}
	
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
		Tenant formTenant = new Tenant();
		formTenant.setDomain(getDomain());
		formTenant.setName("TOTVS S/A");
		setTenant(formTenant);
		return "setupForm.xhtml";
	}

	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String save() {
		try {
			tenant.setStatus(TenantEnum.ACTIVE);
			tenant = this.tenantBusiness.createOrUpdate(tenant);
			this.userAdministratorBusiness.saveOrUpdate(user, tenant);
		} catch (BusinessException e) {
			LoggerFactory.getLogger(this.getClass()).error(e.getMessage());
		}
		return "setupEnd.xhtml";
	}

	public String finish() {
		return "login.xhtml?faces-redirect=true";
	}
	
	
}