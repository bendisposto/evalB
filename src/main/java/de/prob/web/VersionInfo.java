package de.prob.web;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.prob.animator.command.GetVersionCommand;

@Singleton
public class VersionInfo {

	private static String version = "";

	@SuppressWarnings("static-access")
	@Inject
	public VersionInfo(EvaluatorPool pool) {
		GetVersionCommand command = new GetVersionCommand();
		Evaluator evaluator = pool.get();
		evaluator.space.execute(command);
		this.version = "probcli "
				+ command.getVersionString().replaceAll("\n", "<br />");
	}

	public static String getVersion() {
		return version;
	}

}
