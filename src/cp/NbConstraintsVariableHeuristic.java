package cp;

import java.util.Map;
import java.util.Set;
import modelling.Constraint;
import modelling.Variable;

public class NbConstraintsVariableHeuristic implements VariableHeuristic {

    private Set<Constraint> constraints;
    private boolean bool;

    public NbConstraintsVariableHeuristic(Set<Constraint> constraints, boolean bool) {
        this.constraints = constraints;
        this.bool = bool;
    }

    @Override
    public Variable best(Set<Variable> variables, Map<Variable, Set<Object>> domains) {
        Variable best = null;
        int count = bool ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Variable variable : variables) {
            int countOfVariable = 0;
            for (Constraint constraint : constraints) {
                if (constraint.getScope().contains(variable)) {
                    countOfVariable++;
                }
            }
            if (bool) {
                if (countOfVariable > count) {
                    best = variable;
                    count = countOfVariable;
                }
            } else {
                if (countOfVariable < count) {
                    best = variable;
                    count = countOfVariable;
                }
            }
        }
        return best;
    }

}
