package com.qrmarketlist.resources.product;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.slf4j.LoggerFactory;

import com.qrmarketlist.core.AbstractPersistence;
import com.qrmarketlist.core.BusinessException;
import com.qrmarketlist.core.QRCodeUtil;
import com.qrmarketlist.core.Util;
import com.qrmarketlist.core.product.Product;
import com.qrmarketlist.core.product.ProductBusiness;
import com.qrmarketlist.resources.AbstractBean;

/**
 * @author geny.herrera
 * 
 */
@RequestScoped
@ManagedBean
public class ProductBean extends AbstractBean<Product> {
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{productBusiness}")
	private transient ProductBusiness productBusiness;

	public void setProductBusiness(ProductBusiness productBusiness) {
		this.productBusiness = productBusiness;
	}

	public String save() {
		try {
			productBusiness.saveOrUpdate(getEntity());
			addMessage(FacesMessage.SEVERITY_INFO, "", "Salvo com sucesso");
			return goToList();
		} catch (BusinessException e) {
			LoggerFactory.getLogger(ProductBean.class).error("Error saving Product " + getEntity().getName(), e);
			addMessage(FacesMessage.SEVERITY_ERROR, "", Util.getProperty(e.getMessage()));
			return goToPage();
		}
	}

	public String inactivate() {
		if (getSelectedEntity() == null) {
			addMessage(FacesMessage.SEVERITY_INFO, "", "Selecione um usuário para inativar");
			return goToList();
		}
		try {
			productBusiness.inactive(getSelectedEntity());
			addMessage(FacesMessage.SEVERITY_INFO, "", "Inativado com sucesso");
		} catch (BusinessException e) {
			LoggerFactory.getLogger(ProductBean.class).error("Error inactivating Product " + getSelectedEntity().getName(), e);
			addMessage(FacesMessage.SEVERITY_INFO, "", e.getMessage());
		} finally {
			setListDataModel(null);
		}
		return goToList();
	}



	public String reactivate() {
		if (getSelectedEntity() == null) {
			addMessage(FacesMessage.SEVERITY_INFO, "", "Selecione um usuário para reativar");
			return goToList();
		}
		try {
			productBusiness.reactivate(getSelectedEntity());
			addMessage(FacesMessage.SEVERITY_INFO, "", "Reativado com sucesso");
		} catch (BusinessException e) {
			LoggerFactory.getLogger(ProductBean.class).error("Error reactivating Product " + getSelectedEntity().getName(), e);
			addMessage(FacesMessage.SEVERITY_INFO, "", e.getMessage());
		} finally {
			setListDataModel(null);
		}
		return goToList();
	}

	public String printQRCode() {
		if (getSelectedEntity() == null) {
			addMessage(FacesMessage.SEVERITY_INFO, "", "Selecione um produto para imprimir");
			return goToList();
		}
		try {
			String filePath = QRCodeUtil.printQRCode(getSelectedEntity(), "");
			productBusiness.printQRCode(getSelectedEntity());
			addMessage(FacesMessage.SEVERITY_INFO, "", "Arquivo "+filePath + " criado com sucesso");
		} catch (BusinessException e) {
			LoggerFactory.getLogger(ProductBean.class).error("Error creating QRCode Product " + getSelectedEntity().getName(), e);
			addMessage(FacesMessage.SEVERITY_INFO, "", e.getMessage());
		} finally {
			setListDataModel(null);
		}
		return goToList();
	}

	@Override
	public AbstractPersistence<Product> getBusiness() {
		return productBusiness;
	}

	@Override
	public String goToPage() {
		return "product.xhtml";
	}

	@Override
	public String goToList() {
		return "products.xhtml";
	}
}