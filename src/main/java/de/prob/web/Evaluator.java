package de.prob.web;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.be4.classicalb.core.parser.exceptions.BException;
import de.be4.classicalb.core.parser.exceptions.BParseException;
import de.prob.ProBException;
import de.prob.animator.domainobjects.EvaluationResult;
import de.prob.statespace.StateSpace;

public class Evaluator {
	private static final int TIMEOUT = 3;
	private static final TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;
	private static final String TIMEOUT_MESSAGE = "Could not complete calculation within the time constraints. ("
			+ TIMEOUT + " " + TIMEOUT_UNIT + ")";

	private final static Logger logger = LoggerFactory
			.getLogger(Evaluator.class);

	private final StateSpace space;
	private volatile boolean busy;

	public Evaluator(StateSpace space) {
		this.space = space;
	}

	public String eval(final String formula) {

		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<String> future = executor.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				try {
					List<EvaluationResult> evaluate = space.evaluate(formula);
					EvaluationResult first = evaluate.get(0);
					return first.toString();
				} catch (ProBException e) {
					return "ERROR: " + e.getMessage();
				} catch (BException e) {
					BParseException cause = (BParseException) e.getCause();
					return produceErrorMessage(cause);
				}
			}

			private String produceErrorMessage(BParseException cause) {
				int line = cause.getToken().getLine();
				int pos = cause.getToken().getPos();
				pos = (line == 1) ? pos - 10 : pos; // remove #FORMULA
				return "PARSE ERROR at line " + line + " column " + pos
						+ "\n\n" + peeloff(formula, line) + "\n"
						+ Strings.repeat(" ", pos) + "^" + "\n\n"
						+ cause.getRealMsg();
			}

			private String peeloff(String formula, int line) {
				String[] split = (formula + " ").split("\n");
				return split[line - 1];
			}
		});

		try {
			this.busy = true;
			return future.get(TIMEOUT, TIMEOUT_UNIT);
		} catch (TimeoutException e) {
			space.sendInterrupt();
			logger.debug("Timeout while calculating '{}'.", formula);
			return TIMEOUT_MESSAGE;
		} catch (InterruptedException e) {
			logger.error("Interrupt Exception ", e);
			return "ERROR: " + e.getMessage();
		} catch (ExecutionException e) {
			logger.error("Execution Exception ", e);
			return "ERROR: " + e.getMessage();
		} finally {
			executor.shutdownNow();
			this.busy = false;
		}
	}

	public boolean isBusy() {
		return busy;
	}

}
