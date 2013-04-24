package com.qrmarketlist.market.core.tenant;

/**
 * Enum que representa os estados do {@link Tenant}
 * 
 * <p>{@link TenantEnum}</p>
 */
public enum TenantEnum {
	
	/**
	 * O tenant esta ativo na instancia e pode realizar qualquer operação.
	 */
	ACTIVE,
	
	/**
	 * O tenant esta inativo na instancia, impossibilitado de algumas ações.
	 */
	INACTIVE,
	
	/**
	 * O tenant esta suspenso da instancia
	 */
	SUSPENDED;
}
