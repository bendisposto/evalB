package de.prob.config;

import java.util.Set;

import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import de.prob.Main;
import de.prob.MainModule;
import de.prob.web.WebModule;

public class ServletContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new WebModule(), new MainModule());
	}

	public void contextDestroyed(ServletContextEvent event) {
		Set<Process> keySet = Main.processes.keySet();
		for (Process process : keySet) {
			process.destroy();
		}
	}

}