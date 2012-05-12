package de.prob.web;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.prob.ProBException;
import de.prob.animator.command.GetVersionCommand;
import de.prob.statespace.StateSpace;

@Singleton
public class VersionInfo {

	private static String version = "";

	@SuppressWarnings("static-access")
	@Inject
	public VersionInfo(StateSpace space) {
		GetVersionCommand command = new GetVersionCommand();
		try {
			space.execute(command);
		} catch (ProBException e) {
			e.printStackTrace();
		}
		this.version = "probcli "
				+ command.getVersionString().replaceAll("\n", "<br />");
	}

	public static String getVersion() {
		return version;
	}

}
