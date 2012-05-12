package de.prob.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class EvaluationServlet extends HttpServlet {
	
	private final EvaluatorPool pool;

	@Inject
	public EvaluationServlet(EvaluatorPool pool) {
		this.pool = pool;
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		String parameter = req.getParameter("input");
		Evaluator evaluator = pool.get();
		String result = evaluator.eval(parameter);
		out.println(result);
		out.close();
	}
}