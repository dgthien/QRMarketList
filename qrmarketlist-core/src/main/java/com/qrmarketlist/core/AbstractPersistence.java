package com.qrmarketlist.core;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



/**
 * <p>Abstract class that implements all the persistence
 * operation for the product.</br>
 * All methods that call the persistence logic will get
 * validated by the validation chain.</p>
 * 
 * <p>{@link AbstractPersistence}</p>
 *
 * @param <E>
 */

@Repository
@Transactional
public abstract class AbstractPersistence<E extends AbstractEntity> {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	/**
	 * <p>Inserts a new register in the database. All
	 * the validators in the validation chain for the
	 * {@code AbstractPersistence.insert()} method
	 * are called before the database operation.</p>
	 * 
	 * @param entity
	 * @return
	 * @throws BusinessException
	 */
	protected E create(E entity) throws BusinessException {
		return createOrUpdate(entity);
	}

	/**
	 * <p>Updates a register in the database. All the 
	 * validators in the validation chain for the
	 * {@code AbstractPersistence.update()} method 
	 * are called before the database operation.</p>
	 * 
	 * @param entity
	 * @return
	 * @throws BusinessException
	 */
	protected E update(E entity) throws BusinessException {
		return this.createOrUpdate(entity);
	}
	
	/**
	 * * <p>Automatically decides to insert or update
	 * a register in the database. All the validators
	 * in the validation chain for the 
	 * {@code AbstractPersistence.insert()} or 
	 * {@code AbstractPersistence.update()} methods 
	 * are called before the database operation.</p>
	 * 
	 * @param entity
	 * @return
	 * @throws BusinessException
	 */
	public E createOrUpdate(E entity) throws BusinessException {
		try {
			E returnObj = entityManager.merge(entity);
			return returnObj;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	/**
	 * <p>Physically removes a register in the database.
	 * All the validators in the validation chain for the
	 * {@code AbstractPersistence.physicalDelete()} 
	 * method are called before the database operation.</p>
	 * 
	 * @param entity
	 * @throws BusinessException
	 */
	public void delete(E entity) {
		try {
			AbstractEntity abstractEntity = (AbstractEntity) entityManager.find(
					getGenericClass(), entity.getId());
			entityManager.remove(abstractEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>Search and returns the entity with the
	 * corresponding <b>id</b>.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param id
	 * @return
	 */
	public E retrieveById(Long id) {
		List<E> entityList = this.retrieve(eq("id", id));
		
		if(entityList == null || entityList.size() == 0) {
			return null;
		}

		return entityList.get(0);
	}
	
	/**
	 * <p>Search and returns a list of all entities.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @return
	 */
	public List<E> retrieve() {
		return retrieve(null, null, 0, 0);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * The {@code limit} parameter sets the maximum 
	 * row number, while the {@code startAt} sets the 
	 * starting row number for the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param limit
	 * @param startAt
	 * @return
	 */
	public List<E> retrieve(Integer limit, Integer startAt) {
		return retrieve(null, null, limit, startAt);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Criterion} objects.
	 * Useful for making filters.
	 * </p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param criterions
	 * @return
	 */
	public List<E> retrieve(Criterion... criterions) {
		return retrieve(criterions, null, 0, 0);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Order} objects.
	 * Useful to sort the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param orders
	 * @return
	 */
	public List<E> retrieve(Order... orders) {
		return retrieve(null, orders, 0, 0);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Criterion} objects.
	 * Useful for making filters. The {@code limit}
	 * parameter sets the maximum row number for the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param criterions
	 * @param limit
	 * @return
	 */
	public List<E> retrieve(Criterion[] criterions, Integer limit) {
		return retrieve (criterions, null, limit, 0);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Criterion} and an
	 * array of {@link Order} objects. Useful to 
	 * filter and sort the query. The {@code limit}
	 * parameter sets the maximum row number for the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param criterions
	 * @param orders
	 * @param limit
	 * @return
	 */
	public List<E> retrieve(Criterion[] criterions, Order[] orders,  Integer limit) {
		return retrieve (criterions, orders, limit, 0);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Order} objects.
	 * Useful to sort the query. The {@code limit}
	 * parameter sets the maximum row number for the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param orders
	 * @param limit
	 * @return
	 */
	public List<E> retrieve(Order[] orders,  Integer limit) {
		return retrieve (null, orders, limit, 0);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Order} objects.
	 * Useful for making filters.</br>
	 * The {@code limit} parameter sets the maximum 
	 * row number, while the {@code startAt} sets the 
	 * starting row number for the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param orders
	 * @param limit
	 * @param startAt
	 * @return
	 */
	public List<E> retrieve(Order[] orders,  Integer limit, Integer startAt) {
		return retrieve (null, orders, limit, startAt);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Criterion} objects.
	 * Useful to sort the query.<br>
	 * The {@code limit} parameter sets the maximum 
	 * row number, while the {@code startAt} sets the 
	 * starting row number for the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param criterions
	 * @param limit
	 * @param startAt
	 * @return
	 */
	public List<E> retrieve(Criterion[] criterions, Integer limit, Integer startAt) {
		return retrieve (criterions, null, limit, startAt);
	}

	/**
	 * <p>Search and returns a list of all entities.</br>
	 * Receives an array of {@link Criterion} and an
	 * array of {@link Order} objects. Useful to 
	 * filter and sort the query.
	 * The {@code limit} parameter sets the maximum 
	 * row number, while the {@code startAt} sets the 
	 * starting row number for the query.</p>
	 * 
	 * <p><b>Note:</b> Versions that are not active
	 * or deleted registers will not be retrieved.</p>
	 * 
	 * @param criterions
	 * @param orders
	 * @param limit
	 * @param startAt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<E> retrieve(Criterion[] criterions, Order[] orders, Integer limit, Integer startAt) {
		//FIXME - consultas deveriam ser read-only
		Session session = (Session) this.getEntityManager().getDelegate();

		Criteria criteria = session.createCriteria(this.getGenericClass());

		if (criterions != null) {
			for (Criterion criterion : criterions) {
				if (criterion != null) {
					criteria.add(criterion);
				}
			}
		}

		if (orders != null) {
			for (Order order : orders) {
				criteria.addOrder(order);
			}
		}

		if (!(limit == null) && limit > 0) {
			criteria.setMaxResults(limit);
		}

		if (!(startAt == null) && startAt > 0) {
			criteria.setFirstResult(startAt);
		}

		return (List<E>) criteria.list();
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Equals To.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion eq(String propertyName, Object value) {
		return Restrictions.eq(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Equals To.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @param ignoreCase
	 * @return
	 */
	public static Criterion eq(String propertyName, Object value, boolean ignoreCase) {
		if(ignoreCase)
			return Restrictions.eq(propertyName, value).ignoreCase();
		else
			return eq(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Not Equals To.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion neq(String propertyName, Object value) {
		return Restrictions.ne(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Not Equals To.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @param ignoreCase
	 * @return
	 */
	public static Criterion neq(String propertyName, Object value, boolean ignoreCase) {
		if(ignoreCase)
			return Restrictions.ne(propertyName, value).ignoreCase();
		else
			return neq(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Or.
	 * Receives two {@link Criterion} objects.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static Criterion or(Criterion first, Criterion second) {
		return Restrictions.or(first, second);
	}

	/**
	 * <p>Enclosures the {@link Restrictions} Greater Than.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion gt(String propertyName, Object value) {
		return Restrictions.gt(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Greater Than.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @param ignoreCase
	 * @return
	 */
	public static Criterion gt(String propertyName, Object value, boolean ignoreCase) {
		if(ignoreCase)
			return Restrictions.gt(propertyName, value).ignoreCase();
		else
			return gt(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Lesser Than.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion lt(String propertyName, Object value) {
		return Restrictions.lt(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Greater Than.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @param ignoreCase
	 * @return
	 */
	public static Criterion lt(String propertyName, Object value, boolean ignoreCase) {
		if(ignoreCase)
			return Restrictions.lt(propertyName, value).ignoreCase();
		else
			return lt(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Greater Than or Equals To.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion ge(String propertyName, Object value) {
		return Restrictions.ge(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Greater Than.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @param ignoreCase
	 * @return
	 */
	public static Criterion ge(String propertyName, Object value, boolean ignoreCase) {
		if(ignoreCase)
			return Restrictions.ge(propertyName, value).ignoreCase();
		else
			return ge(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Lesser Than or Equals To.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion le(String propertyName, Object value) {
		return Restrictions.le(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Greater Than.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @param ignoreCase
	 * @return
	 */
	public static Criterion le(String propertyName, Object value, boolean ignoreCase) {
		if(ignoreCase)
			return Restrictions.le(propertyName, value).ignoreCase();
		else
			return le(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Like.
	 * This method will search anywhere in the field.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion contains(String propertyName, String value) {
		return Restrictions.like(propertyName, value, MatchMode.ANYWHERE);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Like.
	 * This method will search anywhere in the field.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @param ignoreCase
	 * @return
	 */
	public static Criterion contains(String propertyName, String value, boolean ignoreCase) {
		if(ignoreCase)
			return Restrictions.like(propertyName, value, MatchMode.ANYWHERE).ignoreCase();
		else
			return contains(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Like.
	 * This method will search only at the beginning
	 * of the field.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion beginsWith(String propertyName, String value) {
		return Restrictions.like(propertyName, value, MatchMode.START);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Like.
	 * This method will search anywhere in the field.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @param ignoreCase
	 * @return
	 */
	public static Criterion beginsWith(String propertyName, String value, boolean ignoreCase) {
		if(ignoreCase)
			return Restrictions.like(propertyName, value, MatchMode.START).ignoreCase();
		else
			return beginsWith(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Like.
	 * This method will search only at the end of
	 * the field.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public static Criterion endsWith(String propertyName, String value) {
		return Restrictions.like(propertyName, value, MatchMode.END);
	}
	
	/**
	 * <p>Enclosures the {@link Restrictions} Like.
	 * This method will search anywhere in the field.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @param value
	 * @param ignoreCase
	 * @return
	 */
	public static Criterion endsWith(String propertyName, String value, boolean ignoreCase) {
		if(ignoreCase)
			return Restrictions.like(propertyName, value, MatchMode.END).ignoreCase();
		else
			return endsWith(propertyName, value);
	}
	
	/**
	 * <p>Enclosures the {@link Criterion}.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param criterion
	 * @return
	 */
	public static Criterion[] where(Criterion... criterion) {
		return criterion;
	}

	/**
	 * <p>See {@code AbstractPersistence.asc()} or 
	 * {@code AbstractPersistence.desc()}.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param fieldName
	 * @return
	 */
	public static Order[] orderby(String fieldName) {
		return orderby(asc(fieldName));
	}

	/**
	 * <p>See {@code AbstractPersistence.asc()} or 
	 * {@code AbstractPersistence.desc()}.</br>
	 * Use this to simplify the query.</p>
	 * 
	 * @param fieldName
	 * @return
	 */
	public static Order[] orderby(Order... order) {
		return order;
	}

	/**
	 * <p>Enclosures the {@link Order}. This orders
	 * the query in an ascending order.
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @return
	 */
	public static Order asc(String propertyName) {
		return Order.asc(propertyName);
	}

	/**
	 * <p>Enclosures the {@link Order}. This orders
	 * the query in an descending order.
	 * Use this to simplify the query.</p>
	 * 
	 * @param propertyName
	 * @return
	 */
	public static Order desc(String propertyName) {
		return Order.desc(propertyName);
	}

	/**
	 * <p>Retrieves the type of the generic super class.</p>
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<E> getGenericClass() {
		return (Class<E>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	* <p>The {@code entity} parameter defines the values 
	* that should be included in the filter criterion</p>
	*  
	* @param entity
	* @return
	*/
	public List<E> retrieveByFilter(E entity) {
		return this.retrieveByFilter(entity, true);
	}
	

	/***
	* Search the database looking for entities that matches the values in the
	* template object
	* @param entity The template Object used as example to find entities. All non-null properties (getXX) will be used.
	* @param useEquals True if the search should use EQUALS operator and false to CONTAINS
	* @return List of entities found
	*/
	public List<E> retrieveByFilter(E entity, boolean useEquals) {
		final HashSet<String> ignoredProps = new HashSet<String>();
		
		for(Method m : AbstractEntity.class.getMethods()) {
			ignoredProps.add(m.getName());
		}
		
		if(entity == null) {
			return null;
		}
		
		ArrayList<Criterion> criteria = new ArrayList<Criterion>();
		
		for(Method m : entity.getClass().getMethods()) {
			if(!m.getName().startsWith("get") || m.getParameterTypes().length > 0) {
				continue;
			}
			
			if(ignoredProps.contains(m.getName())) {
				continue;
			}
			
			String fieldName = m.getName().substring(3,4).toLowerCase() + m.getName().substring(4);
			
			try {
				Object value = m.invoke(entity);
				if(value == null) {
					continue;
				}
				
				Criterion c = null;
				if(value instanceof Collection) {
					// TODO: Contains deveria permitir um campo que é collection
					continue;
				}
				else if(m.getReturnType() == String.class) {
					if(useEquals) {
						c = AbstractPersistence.eq(fieldName, value);
					}
					else {
						c = AbstractPersistence.contains(fieldName, value.toString());
					}
				}
				else {
					c = AbstractPersistence.eq(fieldName, value);
				}
				
				criteria.add(c);
			} catch (Exception e) {
				// Se o método não pôde ser invocado, então apenas desconsidera
				LoggerFactory.getLogger(AbstractPersistence.class).debug("Could not invoke: " + m, e);
			}
		}
		
		Criterion crits[] = new Criterion[criteria.size()];
		return this.retrieve(criteria.toArray(crits));
	}
}

