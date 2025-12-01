package cp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import modelling.Constraint;
import modelling.Variable;

public class ArcConsistency {

    private Set<Constraint> constraints;

    public ArcConsistency(Set<Constraint> constraints) {
        for (Constraint constraint : constraints) {
            Set<Variable> scope = constraint.getScope();
            if (scope.size() != 1 && scope.size() != 2) {
                throw new IllegalArgumentException("Constraint scope size must be 1 or 2");
            }
        }
        this.constraints = constraints;
    }

    public boolean enforceNodeConsistency(Map<Variable, Set<Object>> domains) {
        for (Constraint constraint : constraints) {
            if (constraint.getScope().size() == 1) {
                for (Variable var : constraint.getScope()) {
                    for (Object value : new HashSet<>(domains.get(var))) {
                        Map<Variable, Object> map = new HashMap<>();
                        map.put(var, value);
                        if (!constraint.isSatisfiedBy(map)) {
                            domains.get(var).remove(value);
                        }
                    }
                }
            }
        }
        for (Variable var : domains.keySet()) {
            if (domains.get(var).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean myEnforceNodeConsistency(Map<Variable, Set<Object>> domains) {
        boolean empty = false;
        for (Constraint constraint : constraints) {
            if (constraint.getScope().size() == 1) {
                Set<Variable> scope = constraint.getScope();
                Variable var = scope.iterator().next();
                if (domains.get(var).isEmpty()) {
                    empty = true;
                }
                for (Object value : new HashSet<>(domains.get(var))) {
                    Map<Variable, Object> map = new HashMap<>();
                    map.put(var, value);
                    if (!constraint.isSatisfiedBy(map)) {
                        domains.get(var).remove(value);
                        if (domains.get(var).isEmpty()) {
                            empty = true;
                        }
                    }
                }
            }
        }
        return !empty;
    }

    public boolean revise(Variable v1, Set<Object> d1, Variable v2, Set<Object> d2) {
        boolean deleted = false;
        for (Object value1 : new HashSet<>(d1)) {
            boolean viable = false;
            for (Object value2 : new HashSet<>(d2)) {
                boolean toutSatisfait = true;
                for (Constraint constraint : constraints) {
                    if (constraint.getScope().size() == 2 && constraint.getScope().contains(v1)
                            && constraint.getScope().contains(v2)) {
                        Map<Variable, Object> map = new HashMap<>();
                        map.put(v1, value1);
                        map.put(v2, value2);
                        if (!constraint.isSatisfiedBy(map)) {
                            toutSatisfait = false;
                            break;
                        }
                    }
                }
                if (toutSatisfait) {
                    viable = true;
                    break;
                }
            }
            if (!viable) {
                d1.remove(value1);
                deleted = true;
            }
        }
        return deleted;
    }

    public boolean ac1(Map<Variable, Set<Object>> domains) {
        if (!enforceNodeConsistency(domains)) {
            return false;
        }
        boolean change = true;
        while (change == true) {
            change = false;
            for (Variable var1 : domains.keySet()) {
                for (Variable var2 : domains.keySet()) {
                    if (!var1.equals(var2)) {
                        if (revise(var1, domains.get(var1), var2, domains.get(var2))) {
                            change = true;
                        }
                    }
                }
            }
        }
        for (Variable var : domains.keySet()) {
            if (domains.get(var).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}