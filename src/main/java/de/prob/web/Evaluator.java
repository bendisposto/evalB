package de.prob.web;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import de.be4.classicalb.core.parser.exceptions.BException;
import de.be4.classicalb.core.parser.exceptions.BParseException;
import de.prob.ProBException;
import de.prob.animator.command.RemoteEvaluateCommand;
import de.prob.animator.command.RemoteEvaluateCommand.EEvaluationStrategy;
import de.prob.animator.domainobjects.EvaluationResult;
import de.prob.statespace.StateSpace;

public class Evaluator {
	private static final int TIMEOUT = 3;
	private static final TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;
	private static final String TIMEOUT_MESSAGE = "Could not complete calculation within the time constraints. ("
			+ TIMEOUT + " " + TIMEOUT_UNIT + ")";

	private final static Logger logger = LoggerFactory
			.getLogger(Evaluator.class);

	public final StateSpace space;
	private volatile boolean busy;

	public Evaluator(StateSpace space) {
		this.space = space;
	}

	public String eval(final String formula) throws BParseException {
		EEvaluationStrategy strategy = EEvaluationStrategy.EXISTENTIAL;
		return exec(new RemoteEvaluateCommand(formula, strategy), formula,
				strategy);
	}

	public String check(final String formula) throws BParseException {
		EEvaluationStrategy strategy = EEvaluationStrategy.UNIVERSAL;
		return exec(new RemoteEvaluateCommand(formula, strategy), formula,
				strategy);
	}

	public String exec(final RemoteEvaluateCommand command,
			final String formula, final EEvaluationStrategy strategy)
			throws BParseException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<String> future = executor.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				try {
					space.execute(command);
					EvaluationResult first = command.getValue();

					String value = first.value;
					String resultType = first.getResultType();
					String solution = first.solution;
					String warn = "";
					if (first.hasEnumerationWarnings()) {
						warn = "\n\n*** Warning: the predicate contains infinite sets. ProB might have missed solutions/counterexamples.";
					}

					if (!solution.trim().isEmpty()) {
						solution = "\n" + first.explanation + "\n     "
								+ solution;
					}

					if ("expression".equals(resultType)) {
						return "Expression value is " + value;
					}
					if ("predicate".equals(resultType)) {
						return "Predicate is " + value + warn;
					}
					String vars = Joiner.on(",")
							.join(first.getQuantifiedVars());
					if ("exists".equals(resultType)) {
						return "Existentially quantified predicate over "
								+ vars + " is " + value + solution + warn;
					}
					if ("forall".equals(resultType)) {
						return "Universally quantified predicate over " + vars
								+ " is " + value + solution + warn;
					}

					return first.toString();

				} catch (ProBException e) {
					Throwable cause = e.getCause();
					if (cause != null)
						return "ERROR: " + cause.getMessage();
					return "ERROR: " + e.getMessage();
				}
			}
		});

		try {
			this.busy = true;
			String value = future.get(TIMEOUT, TIMEOUT_UNIT);
			logger.trace("Result {} ", value);
			return value;
		} catch (TimeoutException e) {
			space.sendInterrupt();
			logger.debug("Timeout while calculating '{}'.", formula);
			return TIMEOUT_MESSAGE;
		} catch (InterruptedException e) {
			logger.error("Interrupt Exception ", e);
			return "ERROR: " + e.getMessage();
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if (cause instanceof BException) {
				throw (BParseException) cause.getCause();
			}
			return "EXECUTION ERROR";
		}

		finally {
			executor.shutdownNow();
			this.busy = false;
		}
	}

	public boolean isBusy() {
		return busy;
	}

}
