package com.qrmarketlist.market.core;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;

import com.qrmarketlist.market.framework.BusinessException;

/**
 * Classe que representa a entidade {@link AbstractTenantPersistence} 
 */
public abstract class AbstractTenantPersistence<E extends AbstractTenantEntity> extends AbstractPersistence<E> {

	@Autowired
	private AuthenticationContext authenticationContext;
	
	@Override
	public List<E> retrieve(Criterion[] criterions, Order[] orders, Integer limit, Integer startAt) {
		Criterion[] criterion = criterions;
		if (criterion == null) {
			criterion = new Criterion[0];
		}
		Criterion[] multiTenantCriterions = new Criterion[criterion.length + 1];
		// For more about "arraycopy" method http://www.javapractices.com/topic/TopicAction.do?Id=3
		System.arraycopy(criterion, 0, multiTenantCriterions, 0, criterion.length);
		if (authenticationContext != null && authenticationContext.getTenant() != null) {
			multiTenantCriterions[multiTenantCriterions.length - 1] = eq(
					"tenant", authenticationContext.getTenant());
		}
		return super.retrieve(multiTenantCriterions, orders, limit, startAt);
	}
	
	/**
	 * Método que traz uma lista de acordo com os parâmetros.
	 * @param criterions
	 * @param orders
	 * @param limit
	 * @param startAt
	 * @return List<E>
	 */
	public List<E> retrieveGlobal(Criterion[] criterions, Order[] orders, Integer limit, Integer startAt) {
		return super.retrieve(criterions, orders, limit, startAt);
	}
	
	/**
	 * Vincula ao {@link Tenant}, persiste ou atualizar entidade
	 * @param entity
	 * @return
	 * @throws BusinessException 
	 */
	public E createOrUpdate(E entity) throws BusinessException {
		if (entity.getTenant() == null) {
			entity.setTenant(authenticationContext.getTenant());
		}
		return super.createOrUpdate(entity);
	}
}