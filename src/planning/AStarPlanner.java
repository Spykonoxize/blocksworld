package planning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import modelling.Variable;

public class AStarPlanner implements Planner {

    private Map<Variable, Object> etatInit;
    private Set<Action> actions;
    private Goal but;
    private Heuristic heuristic;
    private boolean activate = false;
    private int nodeCount = 0;

    public AStarPlanner(Map<Variable, Object> etatInit, Set<Action> actions, Goal but, Heuristic heuristic) {
        this.etatInit = etatInit;
        this.actions = actions;
        this.but = but;
        this.heuristic = heuristic;
    }

    @Override
    public List<Action> plan() {
        Map<Map<Variable, Object>, Action> plan = new HashMap<>(); // This variable will contain the stack of actions to reach the goal
        Map<Map<Variable, Object>, Map<Variable, Object>> father = new HashMap<>(); // This variable contains the parents of explored nodes
        Map<Map<Variable, Object>, Float> distance = new HashMap<>(); // This one will contain the distance for each state
        Map<Map<Variable, Object>, Float> value = new HashMap<>(); // This one the list of the cost of actions to reach the associated state
        ArrayList<Map<Variable, Object>> open = new ArrayList<>(); // This one the list of open nodes, i.e., states to explore
        open.add(etatInit); // We add the initial state because here it is the next one we will explore as long as there are nodes to explore
        father.put(etatInit, null); // We add the initial state whose parent is null because it is the starting state
        distance.put(etatInit, 0f); // We add the initial state whose distance is 0 because it is our starting point
        value.put(etatInit, heuristic.estimate(etatInit)); // We estimate the shortest path from the initial state.
        while (!open.isEmpty()) {
            // We instantiate by taking the element with the shortest estimated distance and remove it from the list of open nodes
            Map<Variable, Object> instantiation = argmin(open, value);
            if (activate == true) {
                nodeCount++;
            }
            // If we reach a goal we add it to the list
            if (but.isSatisfiedBy(instantiation)) {
                return getBFSPlan(father, plan, instantiation);
            } else {
                // Otherwise we remove the instantiation from the list of open nodes
                open.remove(instantiation);
                // For each action applicable to the current state
                for (Action action : actions) {
                    if (action.isApplicable(instantiation)) {
                        // We record the next state in suivant
                        Map<Variable, Object> suivant = action.successor(instantiation);
                        // If the next state was not known then we initialize its distance to +∞
                        if (!distance.containsKey(suivant)) {
                            distance.put(suivant, Float.MAX_VALUE);
                        }
                        // If its distance was already known and if it is smaller then we update it in distance.
                        // The distance is calculated from the starting point to the point where we are, so the traversal costs accumulate
                        if (distance.get(suivant) > distance.get(instantiation) + action.getCost()) {
                            // We update the distance of suivant by adding the distance to instantiation + the cost of the action.
                            distance.put(suivant, distance.get(instantiation) + action.getCost());
                            // We add the value of the path to suivant + the "shortest" estimated h to the goal
                            value.put(suivant, distance.get(suivant) + heuristic.estimate(suivant));
                            // We update father, plan, and open by adding the next state
                            father.put(suivant, instantiation);
                            plan.put(suivant, action);
                            open.add(suivant);
                        }
                    }
                }
            }
        }
        return null;
    }

    private Map<Variable, Object> argmin(ArrayList<Map<Variable, Object>> open,
            Map<Map<Variable, Object>, Float> distance) {
        Map<Variable, Object> min = null;
        for (int i = 0; i < open.size(); i++) {
            if (distance.containsKey(open.get(i))) {
                if (min == null || distance.get(open.get(i)) < distance.get(min)) {
                    min = open.get(i);
                }
            }
        }
        return min;
    }

    private List<Action> getBFSPlan(Map<Map<Variable, Object>, Map<Variable, Object>> father,
            Map<Map<Variable, Object>, Action> plan, Map<Variable, Object> goal) {
        LinkedList<Action> bfsPlan = new LinkedList<>();
        while (father.get(goal) != null) {
            bfsPlan.add(plan.get(goal));
            goal = father.get(goal);
        }
        Collections.reverse(bfsPlan);
        return bfsPlan;
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
