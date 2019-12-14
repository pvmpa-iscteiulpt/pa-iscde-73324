package pt.iscte.pidesco.conventions.problems.smells;

import pt.iscte.pidesco.conventions.problems.ProblemType;

public enum CodeSmellType implements ProblemType {
	LONG_METHOD {
		@Override
		public String getProperName() {
			return "Long Method";
		}
	},
	GOD_CLASS {
		@Override
		public String getProperName() {
			return "God Class";
		}
	},
	FEATURE_ENVY {
		@Override
		public String getProperName() {
			return "Feature Envy";
		}
	}
}
