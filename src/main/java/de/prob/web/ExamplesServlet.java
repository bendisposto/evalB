package de.prob.web;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@SuppressWarnings("serial")
public class ExamplesServlet extends HttpServlet {

	private final String path;

	@Inject
	public ExamplesServlet(String path) {
		this.path = path;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		File f = new File(path);
		File[] files = f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				return !arg1.startsWith(".");
			}
		});

		HashMap<String, String> hashMap = new HashMap<String, String>();
		for (File file : files) {
			hashMap.put(prettyName(file.getName()), readFile(file));
		}
		Gson gson = new Gson();
		String json = gson.toJson(hashMap);
		out.println(json);
	}

	private String readFile(File file) {
		int c;
		StringBuffer sb = new StringBuffer();
		try {
			FileReader in = new FileReader(file);
			while ((c = in.read()) != -1)
				sb.append((char) c);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String prettyName(String name) {
		String r1 = name.replaceAll("_", " ");
		StringBuffer sb = new StringBuffer();
		boolean upperCaseAllowed = true;
		for (int i = 0; i < r1.length(); i++) {
			char c = r1.charAt(i);
			if (!Character.isLowerCase(c)) {
				if (!upperCaseAllowed) {
					sb.append(" "); 
				}
				upperCaseAllowed = true;
			} else
				upperCaseAllowed = false;
			sb.append(c);
		}
		return sb.toString();
	}
}
