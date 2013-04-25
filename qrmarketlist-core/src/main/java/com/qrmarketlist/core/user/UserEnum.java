package com.qrmarketlist.core.user;

/**
 * Enum que representa os estados do {@link User}
 * 
 * <p>{@link UserEnum}</p>
 */
public enum UserEnum {
	/**
	 * Identifica se o usuário está ativo no sistema. Usuários ativos podem fazer login no sistema.
	 */
	ACTIVE, 
	
	/**
	 * Identifica se o usuário está Inativo no sistema. Usuários inativos não podem logar no sistema.
	 */
	INACTIVE;
}
