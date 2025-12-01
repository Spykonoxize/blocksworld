package modelling;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Implication implements Constraint {

    public Variable var1;
    public Variable var2;
    public Set<Object> s1;
    public Set<Object> s2;

    public Implication(Variable var1, Set<Object> s1, Variable var2, Set<Object> s2) {
        this.var1 = var1;
        this.var2 = var2;
        this.s1 = s1;
        this.s2 = s2;
    }

    // Adds the two variables involved in the implication to the scope set and returns it
    @Override
    public Set<Variable> getScope() {
        Set<Variable> scope = new HashSet<Variable>();
        scope.add(var1);
        scope.add(var2);
        return scope;
    }

    // Returns false if the value of the first variable in the state is contained in the first set and the value of the second variable in the state is not contained in the second set, otherwise returns true
    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> map) {
        Object valeur1 = map.get(var1);
        Object valeur2 = map.get(var2);
        if (valeur1 == null || valeur2 == null) {
            throw new IllegalArgumentException("Variables from the scope must have a value");
        }
        if (s1.contains(valeur1)) {
            return s2.contains(valeur2);
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + var1 + " = " + s1 + ") => (" + var2 + " = " + s2 + ")";
    }
}