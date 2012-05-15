package de.prob.web;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.prob.ProBException;
import de.prob.animator.command.ICommand;
import de.prob.animator.command.LoadBProjectFromStringCommand;
import de.prob.animator.command.SetPreferenceCommand;
import de.prob.animator.command.notImplemented.StartAnimationCommand;
import de.prob.statespace.StateSpace;

@Singleton
public class EvaluatorPool implements Provider<Evaluator> {

	private final ICommand[] losCommandos = {
			new LoadBProjectFromStringCommand("MACHINE empty END"),
			new SetPreferenceCommand("CLPFD", "TRUE"),
			new SetPreferenceCommand("BOOL_AS_PREDICATE", "TRUE"),
			new SetPreferenceCommand("MAXINT", "127"),
			new SetPreferenceCommand("MININT", "-128"),
			new SetPreferenceCommand("TIME_OUT", "500"),
			new StartAnimationCommand() };

	private static final int POOLSIZE = 4;
	Evaluator[] pool = new Evaluator[POOLSIZE];
	int c = 0;
	int rounds = 0;

	@Inject
	public EvaluatorPool(Provider<StateSpace> ap) {
		for (int i = 0; i < pool.length; i++) {
			StateSpace stateSpace = ap.get();
			try {
				stateSpace.execute(losCommandos);
			} catch (ProBException e) {
				e.printStackTrace();
			}
			pool[i] = new Evaluator(stateSpace);
		}
	}

	@Override
	public Evaluator get() {
		do {
			Evaluator e = pool[c];
			c = (c + 1) % POOLSIZE;
			if (!e.isBusy()) {
				rounds = 0;
				return e;
			}
			rounds++;
		} while (rounds < 2 * POOLSIZE);
		return new ErrorEvaluator(null);
	}

}
