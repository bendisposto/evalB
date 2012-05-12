package de.prob.web;

import com.google.inject.servlet.ServletModule;

public class WebModule extends ServletModule {

	@Override
	protected void configureServlets() {
		super.configureServlets();
		serve("/evaluate*").with(EvaluationServlet.class);
		bind(VersionInfo.class).asEagerSingleton();
	}
}
