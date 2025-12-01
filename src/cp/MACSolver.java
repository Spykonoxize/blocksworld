package cp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import modelling.Constraint;
import modelling.Variable;

public class MACSolver extends AbstractSolver {

    private ArcConsistency arcConsistency;

    public MACSolver(Set<Variable> variables, Set<Constraint> constraints) {
        super(variables, constraints);
        this.arcConsistency = new ArcConsistency(constraints);
    }

    @Override
    public Map<Variable, Object> solve() {
        return mac(new HashMap<>(), new LinkedList<>(variables));
    }

    private Map<Variable, Object> mac(Map<Variable, Object> instantiation, LinkedList<Variable> variables) {
        if (variables.isEmpty()) {
            return instantiation;
        }
        Map<Variable, Set<Object>> map = new HashMap<>();
        for (Variable variable : this.variables) {
            map.put(variable, new HashSet<>(variable.getDomain()));
        }
        if (!arcConsistency.ac1(map)) {
            return null;
        }
        Variable var = variables.removeLast();
        for (Object value : new HashSet<>(var.getDomain())) {
            Map<Variable, Object> n = new HashMap<>(instantiation);
            n.put(var, value);
            if (isConsistent(n)) {
                Map<Variable, Object> r = mac(n, variables);
                if (r != null) {
                    return r;
                }
            }
        }
        variables.addLast(var);
        return null;
    }
}
