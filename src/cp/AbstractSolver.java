package cp;

import java.util.Map;
import java.util.Set;
import modelling.Constraint;
import modelling.Variable;

public abstract class AbstractSolver implements Solver {

    protected Set<Variable> variables;
    protected Set<Constraint> constraints;

    public AbstractSolver(Set<Variable> variables, Set<Constraint> constraints) {
        this.constraints = constraints;
        this.variables = variables;
    }

    public boolean isConsistent(Map<Variable, Object> affectation) {
        for (Constraint constraint : constraints) {
            if (affectation.keySet().containsAll(constraint.getScope())) {
                if (!constraint.isSatisfiedBy(affectation)) {
                    return false;
                }
            }
        }
        return true;
    }
}