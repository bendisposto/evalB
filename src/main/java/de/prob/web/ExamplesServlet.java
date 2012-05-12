package de.prob.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class ExamplesServlet extends HttpServlet {

	private static final Map<String, String> examples = new HashMap<String, String>();

	static {
		examples.put(
				"Golumb Ruler",
				"n=7 & length = 25 & \n a:1..n --> 0..length & \n !i.(i:2..n => a(i-1) < a(i)) & \n !(i1,j1,i2,j2).(( i1>0 & i2>0 & j1<=n & j2 <= n & \n                   i1<j1 & i2<j2 & (i1,j1) /= (i2,j2) &  \n                   i1<=i2 & (i1=i2 => j1<j2) \n                 ) => (a(j1)-a(i1) /= a(j2)-a(i2)))");
		examples.put("Simple Expression", "2 - 6");
		examples.put("Simple Predicate", "2 - 6 < 0");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		String parameter = req.getParameter("example");
		if (parameter == null) {
			Set<String> keys = examples.keySet();
			out.println("  <option value=\"\" selected=\"selected\"  />");
			for (String string : keys) {
				out.println("<option value=\"" + string + "\">" + string
						+ "</option>");
			}
		} else {
			out.println(examples.get(parameter));
		}
		out.close();
	}

}
