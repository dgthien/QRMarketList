package com.qrmarketlist.market.core.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.qrmarketlist.market.core.AbstractEntity;
/**
 * Classe que representa a entidade {@link Tenant}  
 */
@Entity
@Table(name = "ELE_TENANT")
public class Tenant extends AbstractEntity {
    
    private static final long serialVersionUID = 1L;
    private String name;
	private String domain;
	private TenantEnum status;
	private boolean globalAdministrator;
	private boolean undeterminedLicences;
	private int avaliableLicenses;
	
	/**
	 * {@link Tenant#getName()} 
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Retorna nome
	 * @return String
	 */
	@Size(max = 50)
	@Column(name = "NAME", length = 50, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * {@link Tenant#getDomain()} 
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	/**
	 * Retorna domínio
	 * @return String
	 */
	@Size(max = 100)
	@Column(name = "DOMAIN", length = 100, nullable = false)
	public String getDomain() {
		return domain;
	}
	
	/**
	 * Retorna status
	 * @return TenantEnum
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 50, nullable = false)
	public TenantEnum getStatus() {
		return status;
	}
	
	/**
	 * {@link Tenant#getStatus()} 
	 */
	public void setStatus(TenantEnum status) {
		this.status = status;
	}
	
	/**
	 * Retorna true | false
	 * @return boolean
	 */
	@Column(name = "GLOBAL_ADMINISTRATOR", nullable = true)
	public boolean isGlobalAdministrator() {
		return globalAdministrator;
	}
	
	/**
	 * {@link Tenant#isGlobalAdministrator()} 
	 */
	public void setGlobalAdministrator(boolean globalAdministrator) {
		this.globalAdministrator = globalAdministrator;
	}
	
	/**
	 * {@link Tenant#isUndeterminedLicences()} 
	 */
	public void setUndeterminedLicences(boolean undeterminedLicences) {
		this.undeterminedLicences = undeterminedLicences;
	}
	
	/**
	 * Retorna "true" caso a quantidade de licenças possua um valor indeterminado e 
	 * falso, caso contrário.
	 * @return boolean
	 */
	@Column(name = "UNDETERMINED_LICENSES", nullable = true)
	public boolean isUndeterminedLicences() {
		return undeterminedLicences;
	}
	
	/**
	 * Tem por objetivo armazenar o número de licenças disponíveis para o próprio Tenant.
	 * @return Integer
	 */
	@Column(name = "AVALIABLE_LICENSES", nullable = true)
	public int getAvaliableLicenses() {
		return avaliableLicenses;
	}
	
	/**
	 * {@link Tenant#getAvaliableLicenses()} 
	 * @param avaliableLicenses
	 */
	public void setAvaliableLicenses(int avaliableLicenses) {
		this.avaliableLicenses = avaliableLicenses;
	}
}