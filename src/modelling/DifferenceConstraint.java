package modelling;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DifferenceConstraint implements Constraint {

    public Variable var1;
    public Variable var2;

    public DifferenceConstraint(Variable var1, Variable var2) {
        this.var1 = var1;
        this.var2 = var2;
    }

    // Adds the two variables involved in the difference constraint to the scope set and returns it
    @Override
    public Set<Variable> getScope() {
        Set<Variable> scope = new HashSet<Variable>();
        scope.add(var1);
        scope.add(var2);
        return scope;
    }

    // Returns true if the two variables in the state are different, otherwise returns false
    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> map) {
        Object valeur1 = map.get(var1);
        Object valeur2 = map.get(var2);
        if (valeur1 == null || valeur2 == null) {
            throw new IllegalArgumentException("Variables from the scope must have a value");
        }
        return !valeur1.equals(valeur2);
    }

    @Override
    public String toString() {
        return "(" + var1 + " != " + var2 + ")\n";
    }
}