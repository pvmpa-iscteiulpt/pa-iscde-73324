package pt.iscte.pidesco.conventions.problems.conventions;

import pt.iscte.pidesco.conventions.problems.ProblemType;

/**
 * Lists out the types of Convention Violations.
 * @author grovy
 *
 */
public enum ConventionViolationType implements ProblemType {
	NON_STATIC_FINAL_CASE_VIOLATION {
		@Override
		public String getProperName() {
			return "camelCase for vars and procs";
		}
	},
	STATIC_FINAL_CASE_VIOLATION {
		@Override
		public String getProperName() {
			return "SNAKE_CASE for static final consts";
		}
	},
	CLASS_NAME_CASE_VIOLATION {
		@Override
		public String getProperName() {
			return "CamelCase for class declarations";
		}
	}
}
