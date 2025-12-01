package planning;

import java.util.Map;
import modelling.Variable;

public class BasicGoal implements Goal {

    private Map<Variable, Object> variables;

    public BasicGoal(Map<Variable, Object> variables) {
        this.variables = variables;
    }

    public Map<Variable, Object> getVariables() {
        return variables;
    }

    @Override
    public boolean isSatisfiedBy(Map<Variable, Object> etat) {
        if (!etat.keySet().containsAll(variables.keySet())) {
            return false;
        }
        for (Map.Entry<Variable, Object> entry : variables.entrySet()) {
            Variable key = entry.getKey();
            Object expectedValue = entry.getValue();
            Object actualValue = etat.get(key);
            if (!expectedValue.equals(actualValue)) {
                return false;
            }
        }
        return true;
    }

}
