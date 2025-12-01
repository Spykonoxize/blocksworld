package planning;

import java.util.HashMap;
import java.util.Map;

import modelling.Variable;

public class BasicAction implements Action {

    Map<Variable, Object> precondition;
    Map<Variable, Object> effet;
    int cout;

    public BasicAction(Map<Variable, Object> precondition, Map<Variable, Object> effet, int cout) {
        this.precondition = precondition;
        this.effet = effet;
        this.cout = cout;
    }

    @Override
    public boolean isApplicable(Map<Variable, Object> etat) {
        // On vérifie si l'ensemble des clés de la précondition sont présente dans
        // l'ensemble des clés de l'état
        if (!etat.keySet().containsAll(precondition.keySet())) {
            return false;
        }

        // On vérifie si les valeurs associées aux clés de la précondition correspondent
        for (Map.Entry<Variable, Object> entry : precondition.entrySet()) {
            Variable key = entry.getKey();
            Object expectedValue = entry.getValue();
            // récupération de la valeur associée à la même clé dans l'état
            Object actualValue = etat.get(key);

            if (!expectedValue.equals(actualValue)) {
                return false;
            }
        }
        // Sinon l'action est applicable
        return true;
    }

    @Override
    public Map<Variable, Object> successor(Map<Variable, Object> etat) {
        Map<Variable, Object> nouvelEtat = new HashMap<>(etat);
        for (Map.Entry<Variable, Object> entree : effet.entrySet()) {
            Variable variable = entree.getKey();
            Object valeur = entree.getValue();
            nouvelEtat.put(variable, valeur);
        }
        return nouvelEtat;
    }

    @Override
    public int getCost() {
        return cout;
    }

    @Override
    public String toString(){
        return "precondition: [" + precondition + "] => effets: [" + effet + "]\n";
    }

}
