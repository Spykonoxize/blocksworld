package cp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import modelling.Constraint;
import modelling.Variable;

public class BacktrackSolver extends AbstractSolver {

    public BacktrackSolver(Set<Variable> variables, Set<Constraint> constraints) {
        super(variables, constraints);
    }

    @Override
    public Map<Variable, Object> solve() {
        return bt(new HashMap<>(), new LinkedList<>(variables));
    }

    private Map<Variable, Object> bt(Map<Variable, Object> instantiation, LinkedList<Variable> variables) {
        if (variables.isEmpty()) {
            return instantiation;
        }
        Variable x = variables.pollLast();
        for (Object v : x.getDomain()) {
            Map<Variable, Object> n = new HashMap<>(instantiation);
            n.put(x, v);
            if (isConsistent(n)) {
                Map<Variable, Object> r = bt(n, variables);
                if (r != null) {
                    return r;
                }
            }
        }
        variables.add(x);
        return null;
    }
}
