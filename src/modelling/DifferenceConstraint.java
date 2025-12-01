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

    // Ajoute les variables à notre ensemble et le renvoie
    @Override
    public Set<Variable> getScope() {
        Set<Variable> scope = new HashSet<Variable>();
        scope.add(var1);
        scope.add(var2);
        return scope;
    }

    // Retourne true si les deux varaibles de l'état sont différentes sinon renvoie
    // false
    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> map) {
        Object valeur1 = map.get(var1);
        Object valeur2 = map.get(var2);
        if (valeur1 == null || valeur2 == null) {
            throw new IllegalArgumentException("Toute les variables du scope doivent avoir une valeur");
        }
        return !valeur1.equals(valeur2);
    }

    @Override
    public String toString() {
        return "(" + var1 + " != " + var2 + ")\n";
    }
}