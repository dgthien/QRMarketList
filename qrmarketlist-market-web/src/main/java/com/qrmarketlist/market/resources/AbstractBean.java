package com.qrmarketlist.market.resources;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;

import com.qrmarketlist.market.core.AbstractEntity;
import com.qrmarketlist.market.core.AbstractPersistence;
import com.qrmarketlist.market.core.BusinessException;

public abstract class AbstractBean<E extends AbstractEntity> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private E entity;
	private E search;
	private List<E> listDataModel;
	private E selectedEntity;	
	private FacesContext facesContext;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public AbstractBean() {
		setFacesContext(FacesContext.getCurrentInstance());
		setRequest((HttpServletRequest) getFacesContext().getExternalContext().getRequest());
		setResponse((HttpServletResponse) getFacesContext().getExternalContext().getResponse());
	}
	
	public E getEntity() {
		if (entity == null)
			entity = newInstance();
		return entity;
	}
	
	public void setEntity(E entity) {
		this.entity = entity;
	}
	
	public E getSearch() {
		if (search == null) {
			search = newInstance();
		}
		return search;
	}
	
	public void setSearch(E search) {
		this.search = search;
	}
	
	public List<E> getListDataModel() {
		if (listDataModel == null) {
			listDataModel = (List<E>) getBusiness().retrieveByFilter(getSearch());
		}
		return listDataModel;
	}
	
	public void setListDataModel(List<E> listDataModel) {
		this.listDataModel = listDataModel;
	}
	
	public E getSelectedEntity() {
		return selectedEntity;
	}
	
	public void setSelectedEntity(E selectedEntity) {
		this.selectedEntity = selectedEntity;
	}
	
	public static void addMessage(Severity icon, String title, String msg){
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(icon, title, msg));
	}
	
	@SuppressWarnings("unchecked")
	public E newInstance() {		
		try {
			Type type = this.getClass().getGenericSuperclass();
			Class<E> clazz = (Class<E>) ((ParameterizedType) type).getActualTypeArguments()[0];
			return (E) clazz.newInstance();
		} catch (InstantiationException e) {
			LoggerFactory.getLogger(AbstractBean.class).warn("error_instantiating_class", e);
		} catch (IllegalAccessException e) {
			LoggerFactory.getLogger(AbstractBean.class).warn("error_instantiating_class", e);
		}
		return null;
	}
	
	public abstract AbstractPersistence<E> getBusiness();
	public abstract String goToPage();
	public abstract String goToList();
	
	public String create() {
		setEntity(null);
		return goToPage();
	}
	
	public String edit() {
		if (getSelectedEntity() == null) {
			addMessage(FacesMessage.SEVERITY_INFO, "", "select_a_record_to_edit");
			return goToList();
		}
		setEntity(getBusiness().retrieveById(getSelectedEntity().getId()));
		return goToPage();
	}
	
//	public String save() {
//		try {
//			setEntity(getBusiness().createOrUpdate(getEntity()));
//			addMessage(FacesMessage.SEVERITY_INFO, "", "successfully_stored");
//			setListDataModel(null);
//		} catch (BusinessException e) {
//			LoggerFactory.getLogger(AbstractBean.class).warn("error_saving_entity" + ": " + getEntity(), e);
//			addMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage());
//			return goToPage();
//		}
//		return goToList();
//	}
	
	public String delete() {
		if (getSelectedEntity() == null) {
			addMessage(FacesMessage.SEVERITY_INFO, "", "select_a_record_to_remove");
		}
		else {
			E loadedEntity = getBusiness().retrieveById(getSelectedEntity().getId());
			getBusiness().delete(loadedEntity);
			addMessage(FacesMessage.SEVERITY_INFO, "", "successfully_removed");
			setListDataModel(null);
		}
		return goToList();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T findBean(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return (T) context.getApplication().evaluateExpressionGet(context,
				"#{" + beanName + "}", Object.class);
	}
	
	public String cancel() {
		return goToList();
	}
	
	public void clearFilter() {
		setSearch(null);
		setListDataModel(null);
	}
	
	public void filter() {
		setListDataModel(null);
	}
	
	public FacesContext getFacesContext() {
		return facesContext;
	}
	
	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}
	
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
}