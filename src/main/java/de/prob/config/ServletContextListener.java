package de.prob.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import de.prob.MainModule;
import de.prob.web.WebModule;

public class ServletContextListener extends GuiceServletContextListener {
	
	
	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new WebModule(), new MainModule());
	}

}