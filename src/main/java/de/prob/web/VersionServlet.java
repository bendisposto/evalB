package de.prob.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.prob.ProBException;
import de.prob.animator.command.GetVersionCommand;
import de.prob.statespace.StateSpace;

@SuppressWarnings("serial")
@Singleton
public class VersionServlet extends HttpServlet {

	private GetVersionCommand command;
	
	
	@Inject
	public VersionServlet(StateSpace s) {
		GetVersionCommand command = new GetVersionCommand();
		try {
			s.execute(command);
		} catch (ProBException e) {
			e.printStackTrace();
		}

		this.command = command;
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		out.print(command.getVersionString());
		out.close();
	}
}