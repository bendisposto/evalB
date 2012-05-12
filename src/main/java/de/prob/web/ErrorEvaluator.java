package de.prob.web;

import de.prob.statespace.StateSpace;

public class ErrorEvaluator extends Evaluator {

	public ErrorEvaluator(StateSpace space) {
		super(null);
	}

	@Override
	public boolean isBusy() {
		return false;
	}

	@Override
	public String eval(String formula) {
		return "System is busy. Please try again later. If this error remains, please let us know.";
	}

}
