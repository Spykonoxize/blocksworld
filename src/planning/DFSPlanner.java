package planning;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import modelling.Variable;

public class DFSPlanner implements Planner {

    private Map<Variable, Object> etatInit;
    private Set<Action> actions;
    private Goal but;
    private boolean activate = false;
    private int nodeCount = 0;

    public DFSPlanner(Map<Variable, Object> etatInit, Set<Action> actions, Goal but) {
        this.etatInit = etatInit;
        this.actions = actions;
        this.but = but;
    }

    // On initialise les différentes variables
    @Override
    public List<Action> plan() {
        Stack<Action> plan = new Stack<>(); // Cette variable contiendra la pile des actions pour arriver au but
        ArrayList<Map<Variable, Object>> closed = new ArrayList<>(); // Celle-ci contiendra la liste des états fermés
        closed.add(etatInit);// On peut déjà y ajouter l'état initial
        return dfs(etatInit, plan, closed); // On retorune le résultat de la méthode récursive du parcours en profondeur
    }

    private List<Action> dfs(Map<Variable, Object> etatInit, Stack<Action> plan,
        ArrayList<Map<Variable, Object>> closed) {
        if (activate == true) {
            nodeCount++;
        }
        // Si l'état actuel est le but alors on retourne plan
        if (but.isSatisfiedBy(etatInit)) {
            return plan;
        } else {
            for (Action action : actions) {
                // Pour chaque action on vérifie si on peut l'appliquer à l'état actuel
                if (action.isApplicable(etatInit)) {
                    // Dans ce cas on vient ajouter l'état suivant dans la variable suivant
                    Map<Variable, Object> suivant = action.successor(etatInit);
                    // Si cet état suivant n'avait pas été parcouru
                    if (!closed.contains(suivant)) {
                        plan.push(action); // On ajoute l'action dans plan
                        closed.add(suivant); // On ajoute l'état suivant à la liste d'états fermés
                        // Appel récursif pour parcourir en profondeur
                        List<Action> subplan = dfs(suivant, plan, closed);
                        // Si le subplan n'est pas null on le retourne sinon on enlève l'action en haut
                        // de la pile plan
                        if (!(subplan == null)) {
                            return subplan;
                        } else {
                            plan.pop();
                        }
                    }
                }
            }
            // S'il n'y a pas de but alors pas de plan
            return null;
        }
    }

    public void activateNodeCount(boolean activate) {
        this.activate = activate;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public Map<Variable, Object> getInitialState() {
        return etatInit;
    }

    @Override
    public Set<Action> getActions() {
        return actions;
    }

    @Override
    public Goal getGoal() {
        return but;
    }

}
