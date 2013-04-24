/**
 * Copyright 2011 Miere Liniel Teixeira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qrmarketlist.market.core;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qrmarketlist.market.core.user.User;
import com.qrmarketlist.market.framework.SpringUtils;

/**
 * Classe Util do sistema.
 */
public class Util {

	private static Logger logger = LoggerFactory.getLogger(Util.class);
	
	/**
	 * Método responsável por retornar se o objeto é nulo.
	 * @param object
	 * @return boolean
	 */
	public static boolean isNull(java.lang.Object object) {
		return object == null;
	}
	
	/**
	 * Método responsável por retornar se o String é numero.
	 * @param String
	 * @return boolean
	 */
	public static boolean isEmpty(java.lang.String string) {
		return string.trim().isEmpty();
	}
	
	public static boolean isNumber(java.lang.String string) {
		return string.matches("[0-9]*");
	}

	/**
	 * Método responsável por juntar strings por por um separador.
	 * @param strings
	 * @param separator
	 * @return String
	 */
	public static String join(String[] strings, String separator) {
		StringBuffer buffer = new StringBuffer();
		for (String string : strings)
			buffer.append(String.format("%s%s", string, separator));
		buffer = buffer.replace(buffer.length()-1, buffer.length(), "");
		return buffer.toString();
	}
	
	/**
	 * Método responsável por criptografar em uma string em MD5.
	 * @param password
	 * @return String
	 */
	public static String encrypt(String password) {     
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");  
			BigInteger hash = new BigInteger(1, md.digest(password.getBytes()));  
			String s = hash.toString(16);  
			if (s.length() %2 != 0) { 
				s = "0" + s;  
			}
			return s;
		} catch (NoSuchAlgorithmException ns) {
			logger.error("Error encripting password.", ns);
		}
		return password;
	}
	
	/**
	 * Método responsável por retornar o valor de uma chave bundle.
	 * @param key
	 * @return String
	 */
	public static String getProperty(String key) {
		String msg = key;
		if(key == null) {
			msg = "please_see_the_system_administrator";
			logger.warn("Bundle key is null. Please verify.");
		}
		try {
			AuthenticationContext authenticationContext = (AuthenticationContext) SpringUtils.getBean("authenticationContext");
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ResourceBundle bundle = null;
			User user = null;
			
			if(authenticationContext != null)
				user = authenticationContext.getUser();
//			if(user!= null){
//				if(user.getLanguage().getLocale().split("_").length > 1)
//					bundle = Util.getResourceBundle(new Locale(user.getLanguage().getLocale().split("_")[0], user.getLanguage().getLocale().split("_")[1]));
//				else
//					bundle = Util.getResourceBundle(new Locale(user.getLanguage().getLocale()));
//			} else {
//				bundle = Util.getResourceBundle(facesContext.getExternalContext().getRequestLocale());
//			}
			
			msg = bundle.getString(key);
		} catch(MissingResourceException e){
			logger.debug("Bundle message for the key \"" + key + "\" not found. Using default message.");
		}
		return msg;
	}
	
	/**
	 * Método responsável por retornar o valor de uma chave bundle aceitando parametros.
	 * @param key
	 * @return String
	 */
	public static String getProperty(String key, Object param) {
		String msg = key;
		if(key == null) {
			msg = "please_see_the_system_administrator";
			logger.warn("Bundle key is null. Please verify.");
		}
		try {
			AuthenticationContext authenticationContext = (AuthenticationContext) SpringUtils.getBean("authenticationContext");
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ResourceBundle bundle = null;
			User user = null;
			
			if(authenticationContext != null)
				user = authenticationContext.getUser();
//			if(user!= null){
//				if(user.getLanguage().getLocale().split("_").length > 1)
//					bundle = Util.getResourceBundle(new Locale(user.getLanguage().getLocale().split("_")[0], user.getLanguage().getLocale().split("_")[1]));
//				else
//					bundle = Util.getResourceBundle(new Locale(user.getLanguage().getLocale()));
//			} else {
//				bundle = Util.getResourceBundle(facesContext.getExternalContext().getRequestLocale());
//			}
			
			MessageFormat form = new MessageFormat(bundle.getString(key));
			
			if (param instanceof String || param instanceof StringBuffer || param instanceof StringBuilder) {
				msg = form.format(new Object[] { param });
            } else if (param instanceof Object[]) {
            	msg =  form.format(param);
            } else {
            	logger.error("Bundle param is not valid. Please verify.");
                return bundle.getString(msg);
            }
			return msg;
		} catch(MissingResourceException e){
			logger.debug("Bundle message for the key \"" + key + "\" not found. Using default message.");
		}
		return msg;
	}

	/**
	 * Método reponsável por retornar o ResourceBundle da session.
	 * @param locale
	 * @return ResourceBundle
	 */
	public static ResourceBundle getResourceBundle(Locale locale) {
		ResourceBundle rb = cacheResource.get(locale);
		if (rb == null) {
			rb = ResourceBundle.getBundle("com.totvs.elearning.core.bundle.BundleEPM", locale);
			ResourceBundle r = cacheResource.putIfAbsent(locale, rb);
			if (r != null) {
				rb = r;
			}
		}
		return rb;
	}

	/**
	 * Retorna o cache do ResourceBundle da session.
	 */
	public final static ConcurrentHashMap<Locale, ResourceBundle> cacheResource = new ConcurrentHashMap<Locale, ResourceBundle>();

	/**
	 * Método responsável por ordenar uma lista de items.
	 * @param selectItemList
	 */
	public static void orderListSelectedItems(List<SelectItem> selectItemList) {
        Collections.sort(selectItemList, new Comparator<SelectItem>(){
            public int compare(SelectItem item1, SelectItem item2) { 
                return item1.getLabel().compareTo(item2.getLabel());
            }
        });
    }
	
	/**
	 * Método responsável por retornar o Locale para a linguagem passada.
	 * @param language
	 * @return Locale
	 */
	public static Locale retrieveLocale(String language) {
		Locale[] locales = Locale.getAvailableLocales();
		for (Locale locale : locales) {
			if (locale.toString().equals(language)) {
				return locale;
			}
		}
		return null;
	}

	/**
	 * Método responsável por remover as tags html de uma String.
	 * @param str
	 * @return String
	 */
	public static String getStringNoHtml(String str) {
		String noHtmlString = "";
		if(str != null)
			noHtmlString = str.replaceAll("\\<.*?\\>", "");
		return noHtmlString;
	}
	
	/**
	 * Método responsável por converter uma string para UTF-8
	 * @param string
	 * @return String
	 */
	public static String convertToUTF8 (String string) {
		try {
			byte[] bytes = string.getBytes("ISO-8859-1");
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Error Convert String: "+string, e);
		} 
		return string;
	}
	
	/**
	 * <p>
	 * Retorna uma {@link String} com a URL da aplicação.<br>
	 * O método leva em consideração o protocolo, o domínio, a porta e o nome da
	 * aplicação.<br>
	 * Ex.: <code>http://dominio:porta/nome-da-aplicação</code>
	 * </p>
	 * 
	 * @param request
	 * @param authenticationContext
	 * @return uma string com a URL da aplicação.
	 */
	public static String getApplicationURL(HttpServletRequest request, AuthenticationContext authenticationContext) {
		return request.getScheme() + "://" + authenticationContext.getTenant().getDomain() + ":" + request.getServerPort() + request.getContextPath();
	}
	
	/**
	 * <p>
	 * Recupera o objeto real.
	 * </p>
	 * 
	 * @param model
	 * @return
	 */
	public static Object getUnproxyModel(Object model) {
	    if (HibernateProxy.class.isAssignableFrom(model.getClass())) {
	        return ((HibernateProxy)model).getHibernateLazyInitializer().getImplementation();
	    }
	    return model;
	}
	
	/**
	 * <p>
	 *  Retorna uma 'substring' de valor long a partir do índice inicial ao final. 
	 *  Se o substring está vazia, então -1 será retornado.
	 * </p>
	 * @param value
	 * @param beginIndex
	 * @param endIndex
	 * @return long
	 */
	public static long sublong(String value, int beginIndex, int endIndex) {
        String substring = value.substring(beginIndex, endIndex);
        return (substring.length() > 0) ? Long.parseLong(substring) : -1;
    }
	
	/**
	 * <p>
	 * Realiza uma busca no mapa de parametros de acordo com o parametro
	 * informado
	 * </p>
	 * 
	 * @param String
	 * @param FacesContext
	 * @return Object[]
	 */
	public static Object[] getParam(String value, FacesContext context) {
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		Map<String, String[]> params = Collections
				.synchronizedMap((Map<String, String[]>) new HashMap<String, String[]>(
						request.getParameterMap()));
		Object[] obj = null;
		for (Map.Entry<String, String[]> param : params.entrySet()) {
			if (param.getKey().contains(value)) {
				obj = param.getValue();
				break;
			}
		}
		return obj;
	}
	
	/**
	 * <p>
	 * Realiza uma busca no mapa de parametros de acordo com o parametro
	 * informado
	 * </p>
	 * 
	 * @param String
	 * @param HttpServletRequest
	 * @return Object[]
	 */
	public static Object[] getParam(String value, HttpServletRequest request) {
		Object[] obj = null;
		if (value != null && request != null) {
			Map<String, String[]> params = Collections
					.synchronizedMap((Map<String, String[]>) new HashMap<String, String[]>(
							request.getParameterMap()));
			for (Map.Entry<String, String[]> param : params.entrySet()) {
				if (param.getKey().contains(value)) {
					obj = param.getValue();
					break;
				}
			}
		}
		return obj;
	}
	
	/**
	 * <p>
	 * Realiza uma busca no mapa de parametros de acordo com o parametro
	 * informado
	 * </p>
	 * 
	 * @param String
	 * @param HttpServletRequest
	 * @return Object[]
	 */
	public static int compareStrings(String string1, String string2){
		int result = 0;
		int target = (string1.length() < string2.length()) ? string1.length() : string2.length();
		for(int index = 0; index < target; index++){
			result = string1.charAt(index) - string2.charAt(index);
			if(result != 0) break; 
			if((target == index + 1) && (result == 0)){
				if(string1.length() < string2.length()){
					result = -1;
				}else{
					result = 1;
				}
			}
		}
		return result;
	}
}
