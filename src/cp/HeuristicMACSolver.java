package cp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import modelling.Constraint;
import modelling.Variable;

public class HeuristicMACSolver extends AbstractSolver {

    private VariableHeuristic variableHeuristic;
    private ValueHeuristic valueHeuristic;
    private ArcConsistency arcConsistency;

    public HeuristicMACSolver(Set<Variable> variables, Set<Constraint> constraints, VariableHeuristic variableHeuristic,
            ValueHeuristic valueHeuristic) {
        super(variables, constraints);
        this.variableHeuristic = variableHeuristic;
        this.valueHeuristic = valueHeuristic;
        this.arcConsistency = new ArcConsistency(constraints);
    }

    @Override
    public Map<Variable, Object> solve() {
        return heuristicMac(new HashMap<>(), new LinkedList<>(variables));
    }

    private Map<Variable, Object> heuristicMac(Map<Variable, Object> instantiation, LinkedList<Variable> variables) {
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
        Variable var = variableHeuristic.best(this.variables, map);
        variables.remove(var);
        ArrayList<Object> domain = (ArrayList<Object>) valueHeuristic.ordering(var, new HashSet<>(var.getDomain()));
        for (Object value : domain) {
            Map<Variable, Object> n = new HashMap<>(instantiation);
            n.put(var, value);
            if (isConsistent(n)) {
                Map<Variable, Object> r = heuristicMac(n, variables);
                if (r != null) {
                    return r;
                }
            }
        }
        variables.add(var);
        return null;
    }

}
