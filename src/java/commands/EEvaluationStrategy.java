package commands;

public enum EEvaluationStrategy {
		EXISTENTIAL("evalb_evaluate_formula", true), UNIVERSAL("evalb_evaluate_tautology",
				false);
		public final String prolog;
		private final boolean existential;

		EEvaluationStrategy(final String prolog, final boolean existential) {
			this.prolog = prolog;
			this.existential = existential;
		}

		public boolean isExistential() {
			return existential;
		}

	}