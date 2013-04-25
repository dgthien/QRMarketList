package com.qrmarketlist.core.product;

/**
 * Enum que representa os estados do {@link User}
 * 
 * <p>{@link ProductEnum}</p>
 */
public enum ProductEnum {
	/**
	 * Identifica se o usuário está ativo no sistema. Usuários ativos podem fazer login no sistema.
	 */
	ACTIVE, 
	
	/**
	 * Identifica se o usuário está Inativo no sistema. Usuários inativos não podem logar no sistema.
	 */
	INACTIVE;
}
