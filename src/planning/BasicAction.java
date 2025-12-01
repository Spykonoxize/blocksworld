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
        // We check if the set of keys in the precondition is present in the set of keys in the state
        if (!etat.keySet().containsAll(precondition.keySet())) {
            return false;
        }

        // We check if the values associated with the keys in the precondition match
        for (Map.Entry<Variable, Object> entry : precondition.entrySet()) {
            Variable key = entry.getKey();
            Object expectedValue = entry.getValue();
            // Retrieval of the value associated with the same key in the state
            Object actualValue = etat.get(key);

            if (!expectedValue.equals(actualValue)) {
                return false;
            }
        }
        // Otherwise the action is applicable
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
        return "precondition: [" + precondition + "] => effects: [" + effet + "]\n";
    }

}
