package cp;

import java.util.Map;
import java.util.Set;
import modelling.Variable;

public class DomainSizeVariableHeuristic implements VariableHeuristic {

    private boolean bool;

    public DomainSizeVariableHeuristic(boolean bool) {
        this.bool = bool;
    }

    @Override
    public Variable best(Set<Variable> variables, Map<Variable, Set<Object>> domains) {
        Variable best = null;
        int count = bool ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Variable variable : variables) {
            int sizeDomainOfVariable = domains.get(variable).size();
            if (bool) {
                if (sizeDomainOfVariable > count) {
                    best = variable;
                    count = sizeDomainOfVariable;
                }
            } else {
                if (sizeDomainOfVariable < count) {
                    best = variable;
                    count = sizeDomainOfVariable;
                }
            }
        }
        return best;
    }

}
