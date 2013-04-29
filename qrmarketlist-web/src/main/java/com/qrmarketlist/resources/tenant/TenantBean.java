package com.qrmarketlist.resources.tenant;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.slf4j.LoggerFactory;

import com.qrmarketlist.core.AbstractPersistence;
import com.qrmarketlist.core.BusinessException;
import com.qrmarketlist.core.Util;
import com.qrmarketlist.core.tenant.Tenant;
import com.qrmarketlist.core.tenant.TenantBusiness;
import com.qrmarketlist.core.user.User;
import com.qrmarketlist.core.user.UserAdministratorBusiness;
import com.qrmarketlist.resources.AbstractBean;

@RequestScoped
@ManagedBean
public class TenantBean extends AbstractBean<Tenant> {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value="#{tenantBusiness}")
	private transient TenantBusiness tenantBusiness;
	@ManagedProperty(value="#{userAdministratorBusiness}")
	private transient UserAdministratorBusiness userAdministratorBusiness;
	private User user;
		
	public User getUser() {
		if (user == null) {
			user = new User();
		}
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public String create() {
		user = null;
		return super.create();
	}
	
	@Override
	public String edit() {
		String page = super.edit();
		if(!Util.isNull(getSelectedEntity())) {
			user = userAdministratorBusiness.retrieveUserAdministrator((Tenant) getEntity());
		}
		return page;
	}
	
	public String inactivate() {
		if (getSelectedEntity() == null) {
			addMessage(FacesMessage.SEVERITY_INFO, "", Util.getProperty("select_a_tenant_to_inactivate"));
			return goToList();
		}
		try {
			tenantBusiness.inactive(getSelectedEntity());
		} catch (BusinessException e) {
			LoggerFactory.getLogger(TenantBean.class).error("Error inactivating Tenant " + getSelectedEntity().getDomain(), e);
			addMessage(FacesMessage.SEVERITY_INFO, "", Util.getProperty("can_not_inactivate_the_global_enterprise"));
		} finally {
			setListDataModel(null);
		}
		return goToList();
	}
	
	public void saveTenant() throws BusinessException {
		Tenant tenant =  tenantBusiness.saveOrUpdate((Tenant) getEntity(), user);
		setEntity(tenant);
	}
	
	public String save() {
		try {
			this.saveTenant();
			//addMessage(FacesMessage.SEVERITY_INFO, "", Util.getProperty("successfully_stored"));
			addMessage(FacesMessage.SEVERITY_INFO, "", "Salvo com sucesso");
		} catch (BusinessException e) {
			LoggerFactory.getLogger(TenantBean.class).error("Error saving Tenant " + getEntity().getDomain(), e);
			addMessage(FacesMessage.SEVERITY_ERROR, "", Util.getProperty(e.getMessage()));
			return goToPage();
		}
		return goToList();
	}
	
	public String suspended() {
		if (getSelectedEntity() == null) {
			addMessage(FacesMessage.SEVERITY_INFO, "", Util.getProperty("select_a_tenant_to_suspend"));
			return goToList();
		}
		try {
			tenantBusiness.suspend(getSelectedEntity());
		} catch (BusinessException e) {
			LoggerFactory.getLogger(TenantBean.class).error("Error suspending Tenant " + getSelectedEntity().getDomain(), e);
			addMessage(FacesMessage.SEVERITY_INFO, "", Util.getProperty("can_not_suspended_the_global_enterprise"));
		} finally {
			setListDataModel(null);
		}
		return goToList();
	}
	
	public String reactivate() {
		if (getSelectedEntity() == null) {
			addMessage(FacesMessage.SEVERITY_INFO, "", Util.getProperty("select_a_tenant_to_reactivate"));
			return goToList();
		}
		try {
			tenantBusiness.reactivate(getSelectedEntity());
		} catch (BusinessException e) {
			LoggerFactory.getLogger(TenantBean.class).error("Error reactivating Tenant " + getSelectedEntity().getDomain(), e);
			addMessage(FacesMessage.SEVERITY_INFO, "", Util.getProperty(e.getMessage()));
		} finally {
			setListDataModel(null);
		}
		return goToList();
	}

	public void setTenantBusiness(TenantBusiness tenantBusiness) {
		this.tenantBusiness = tenantBusiness;
	}

	public void setUserAdministratorBusiness(UserAdministratorBusiness userAdministratorBusiness) {
		this.userAdministratorBusiness = userAdministratorBusiness;
	}
	
	
	@Override
	public AbstractPersistence<Tenant> getBusiness() {
		return tenantBusiness;
	}

	@Override
	public String goToPage() {
		return "tenant.xhtml";
	}

	@Override
	public String goToList() {
		return "tenants.xhtml";
	}
	
	
}