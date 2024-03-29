package com.qrmarketlist.core.tenant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qrmarketlist.core.AbstractPersistence;
import com.qrmarketlist.core.BusinessException;
import com.qrmarketlist.core.user.User;
import com.qrmarketlist.core.user.UserAdministratorBusiness;
/**
 * Regras de negócio da entidade {@link Tenant}  
 */
@Service
public class TenantBusiness extends AbstractPersistence<Tenant> {
	
	@Autowired
	private UserAdministratorBusiness userAdministratorBusiness;
	
	/**
	 * Recupera {@link Tenant} por domínio
	 * @param domain
	 * @return Tenant
	 */
	public Tenant retrieveByDomain(String domain) {
		List<Tenant> tenant = retrieve(eq("domain", domain));
		if (tenant == null || tenant.size() == 0)
			return null;
		return tenant.get(0);
	}
	
	/**
	 * <p>Recupera o Tenant Global</p>
	 * 
	 * @return Tenant
	 */
	public Tenant retrieveGlobalAdministrator() {
		List<Tenant> tenants = retrieve(eq("globalAdministrator", true));
		if (tenants == null || tenants.size() == 0)
			return null;
		return tenants.get(0);
	}
	
	/**
	 * Salva ou atualiza {@link Tenant}
	 * @param tenant
	 * @param user
	 * @param volume
	 * @return Tenant
	 * @throws BusinessException
	 */
	public Tenant saveOrUpdate(Tenant tenant, User user) throws BusinessException {
	    Tenant newTenant = null;
		if (tenant.getId() != null) {
		    newTenant = createOrUpdate(tenant);
		    userAdministratorBusiness.saveOrUpdate(user, newTenant);
		} else {
			tenant.setGlobalAdministrator(false);
			tenant.setStatus(TenantEnum.ACTIVE);
			newTenant = createOrUpdate(tenant);
			userAdministratorBusiness.saveOrUpdate(user, newTenant);
		}
		return newTenant;
	}
	
	/**
	 * Inativa {@link Tenant}
	 * @param tenant
	 * @throws BusinessException
	 */
	public void inactive(Tenant tenant) throws BusinessException {
		tenant.setStatus(TenantEnum.INACTIVE);
		createOrUpdate(tenant);
	}
	
	/**
	 * Suspende {@link Tenant} 
	 * @param tenant
	 * @throws BusinessException
	 */
	public void suspend(Tenant tenant) throws BusinessException {
		tenant.setStatus(TenantEnum.SUSPENDED);
		createOrUpdate(tenant);
	}
	
	/**
	 * Reativa {@link Tenant}
	 * @param tenant
	 * @throws BusinessException
	 */
	public void reactivate(Tenant tenant) throws BusinessException {
		tenant.setStatus(TenantEnum.ACTIVE);
		createOrUpdate(tenant);
	}
	
}