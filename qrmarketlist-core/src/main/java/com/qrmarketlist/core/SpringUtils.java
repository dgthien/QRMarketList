package com.qrmarketlist.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("singleton")
@Component
public class SpringUtils implements ApplicationContextAware {

   private static ApplicationContext context;

   @Override
   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      initializeApplicationContext(applicationContext);
   }

   private static void initializeApplicationContext(ApplicationContext applicationContext) {
      context = applicationContext;
   }

   public static ApplicationContext getApplicationContext() {
      return context;
   }
   
   public static Object getBean(String arg0) {
	   if (getApplicationContext() != null)
		   return getApplicationContext().getBean(arg0);
	   return null;
   }
   
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public static Object getBean(Class clazz) {
	   if (getApplicationContext() != null)
		   return getApplicationContext().getBean(clazz);
	   return null;
   }

}