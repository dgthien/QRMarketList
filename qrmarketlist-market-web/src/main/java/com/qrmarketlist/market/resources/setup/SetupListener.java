package com.qrmarketlist.market.resources.setup;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.qrmarketlist.market.core.user.UserBusiness;
import com.qrmarketlist.market.framework.SpringUtils;

/**
 * <p>
 * Esta classe tem por finalidade exibir página de "setup" do produto caso seja
 * a primeira utilização do mesmo, o usuário será direcionado para um formulário
 * onde irá preencher as informações necessárias e após concluir será
 * direcionado para uma página de finalização do Setup.
 * </p>
 * <p>
 * As páginas referentes ao Setup não estarão mais disponíveis para acesso após
 * o usuário ter finalizado o processo de Setup.
 * </p>
 * 
 */
public class SetupListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	public void afterPhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		String viewId = facesContext.getViewRoot().getViewId();

		boolean isLoginPage = viewId.lastIndexOf("login.xhtml") > -1;
		boolean isSetupPage = viewId.lastIndexOf("setup.xhtml") > -1;
		boolean isSetupFormPage = viewId.lastIndexOf("setupForm.xhtml") > -1;
		boolean isSetupEndPage = viewId.lastIndexOf("setupEnd.xhtml") > -1;

		if (isLoginPage) {
			if (!existsGlobalAdministrator()) {
				handleNavigation(facesContext, "setup.xhtml");
				return;
			}
		}
		if (isSetupPage || isSetupFormPage || isSetupEndPage) {
			boolean existsGlobalAdministrator = existsGlobalAdministrator();
			if (existsGlobalAdministrator) {
				handleNavigation(facesContext, "login.xhtml");
				return;
			} 
		}
	}

	/**
	 * <p>
	 * Realiza navegação entre as páginas
	 * </p>
	 * 
	 * @param facesContext
	 * @param path
	 */
	public void handleNavigation(FacesContext facesContext, String path) {
		NavigationHandler nh = facesContext.getApplication()
				.getNavigationHandler();
		nh.handleNavigation(facesContext, null, path + "?faces-redirect=true");
	}

	/**
	 * <p>
	 * Verifica se já existe o Tenant Global
	 * </p>
	 * 
	 * @return boolean
	 */
	public boolean existsGlobalAdministrator() {
		UserBusiness userBusiness = (UserBusiness) SpringUtils
				.getBean(UserBusiness.class);
		return userBusiness.retrieveGlobalAdministrator() != null;
	}

	public void beforePhase(PhaseEvent event) {
	}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}