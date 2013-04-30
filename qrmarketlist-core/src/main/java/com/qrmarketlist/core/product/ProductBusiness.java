package com.qrmarketlist.core.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qrmarketlist.core.AbstractPersistence;
import com.qrmarketlist.core.AuthenticationContext;
import com.qrmarketlist.core.BusinessException;
import com.qrmarketlist.core.tenant.Tenant;

@Service
public class ProductBusiness extends AbstractPersistence<Product> {

	@Autowired
	private AuthenticationContext authenticationContext;

	/**
	 * Recupera produto por ean e tenant
	 * @param ean
	 * @param tenant
	 * @return {@link Product}
	 */
	public Product retrieveProductByEANAndTenant(String ean, Tenant tenant) {
		List<Product> product = retrieve(eq("ean", ean), eq("tenant", tenant));
		if (product == null || product.size() == 0) {
			return null;
		}
		return product.get(0);
	}
	
	/**
	 * Recupera usuários ean
	 * @param ean
	 * @return {@link Product}
	 */
	public List<Product> retrieveProductByEAN(String ean) {
		List<Product> product = retrieve(eq("ean",ean));
		if (product == null || product.size() == 0) {
			return null;
		}
		return product;
	}
	
	/**
	 * Recupera produto por ean e tenant
	 * @param name
	 * @param tenant
	 * @return {@link Product}
	 */
	public Product retrieveProductByNameAndTenant(String name, Tenant tenant) {
		List<Product> product = retrieve(eq("name", name), eq("tenant", tenant));
		if (product == null || product.size() == 0) {
			return null;
		}
		return product.get(0);
	}
	
	/**
	 * Recupera produto por nome
	 * @param name
	 * @return {@link Product}
	 */
	public List<Product> retrieveProductByName(String name) {
		List<Product> product = retrieve(eq("name", name));
		if (product == null || product.size() == 0) {
			return null;
		}
		return product;
	}
	
	/**
	 * Recupera produto por tenant
	 * @param tenant
	 * @return {@link Product}
	 */
	public List<Product> retrieveProductByTenant(Tenant tenant) {
		List<Product> product = retrieve(eq("tenant", tenant));
		if (product == null || product.size() == 0) {
			return null;
		}
		return product;
	}


	/**
	 * Salva ou atualiza usuário
	 * @param user
	 * @param tenant
	 * @throws BusinessException
	 */
	public Product saveOrUpdate(Product product)  throws BusinessException {
		if (product.getId() != null) {
			product.setQrCodePrinted(false);
			product = createOrUpdate(product);
		} else {
			product.setStatus(ProductEnum.ACTIVE);
			product.setQrCodePrinted(false);
			product.setTenant(authenticationContext.getTenant());
			product = createOrUpdate(product);
		}
		return product;
	}

	/**
	 * <p>
	 * Inativa o {@link User}, atualizando o campo {@link User#getStatus()}
	 * para o valor {@link UserEnum#INACTIVE}. 
	 * Ao inativar o usuário é verificado se este está na lista de espera, 
	 * caso esteja, o mesmo é eliminado
	 * </p>
	 * @param user
	 * @throws BusinessException
	 */
	public void inactive(Product product) throws BusinessException {
		product.setStatus(ProductEnum.INACTIVE);
		super.createOrUpdate(product);
	}

	/**
	 * <p>
	 * Reativa o {@link User}, atualizando o campo {@link User#getStatus()}
	 * para o valor {@link UserEnum#ACTIVE}. 
	 * </p>
	 * @param user
	 * @throws BusinessException
	 */
	public void reactivate(Product product) throws BusinessException {
		product.setStatus(ProductEnum.ACTIVE);
		product.setQrCodePrinted(false);
		super.createOrUpdate(product);
	}

}
