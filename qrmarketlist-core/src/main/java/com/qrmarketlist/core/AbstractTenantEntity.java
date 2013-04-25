package com.qrmarketlist.core;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.qrmarketlist.core.tenant.Tenant;

/**
 * Classe que representa a entidade {@link AbstractMultiTenantEntity} 
 */
@MappedSuperclass
public abstract class AbstractTenantEntity extends AbstractEntity {
    
    private static final long serialVersionUID = 1L;
    private Tenant tenant;

	/**
	 * {@link AbstractMultiTenantEntity#getTenant()}
	 */
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
	
	/**
     * @return {@link Tenant}
     */
	@ManyToOne
    @JoinColumn(name="ID_TENANT", referencedColumnName="ID", insertable = true, updatable = true)
	public Tenant getTenant() {
		return tenant;
	}
	
}