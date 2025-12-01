package planning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import modelling.Variable;

public class DijkstraPlanner implements Planner {

    private Map<Variable, Object> etatInit;
    private Set<Action> actions;
    private Goal but;
    private boolean activate = false;
    private int nodeCount = 0;

    public DijkstraPlanner(Map<Variable, Object> etatInit, Set<Action> actions, Goal but) {
        this.etatInit = etatInit;
        this.actions = actions;
        this.but = but;
    }

    @Override
    public List<Action> plan() {
        Map<Map<Variable, Object>, Action> plan = new HashMap<>(); // This variable will contain the stack of actions to reach the goal
        Map<Map<Variable, Object>, Integer> distance = new HashMap<>(); // This one will contain the distance for each state
        Map<Map<Variable, Object>, Map<Variable, Object>> father = new HashMap<>(); // This variable contains the parents of explored nodes
        ArrayList<Map<Variable, Object>> goals = new ArrayList<>(); // This one is the list of our goals
        ArrayList<Map<Variable, Object>> open = new ArrayList<>(); // This one is the list of open states, i.e., states to be explored explorer
        father.put(etatInit, null); // We add the initial state whose parent is null because it is the starting state
        distance.put(etatInit, 0); // We add the initial state whose distance is 0 because it is our starting point
        open.add(etatInit); // We add the initial state because here it is the next one we will explore
        // While there are nodes to explore
        while (!open.isEmpty()) {
            // We instantiate by taking the element that is at the shortest distance and remove it from the list of open states
            Map<Variable, Object> instantiation = argmin(open, distance);
            open.remove(instantiation);
            if (activate == true) {
                nodeCount++;
            }
            // If we reach a goal we add it to the list
            if (but.isSatisfiedBy(instantiation)) {
                goals.add(instantiation);
            }
            // For each action applicable to the current state
            for (Action action : actions) {
                if (action.isApplicable(instantiation)) {
                    // We record the next state in next
                    Map<Variable, Object> next = action.successor(instantiation);
                    // If the next state was not known then we initialize its distance to
                    // +∞
                    if (!distance.containsKey(next)) {
                        distance.put(next, Integer.MAX_VALUE);
                    }
                    // If its distance was already known and if it is smaller then we update it in distance.
                    // The distance is calculated from the starting point to the current point, so the traversal costs accumulate
                    if (distance.get(next) > distance.get(instantiation) + action.getCost()) {
                        distance.put(next, distance.get(instantiation) + action.getCost());
                        // We update father, plan and open by adding the next state
                        father.put(next, instantiation);
                        plan.put(next, action);
                        open.add(next);
                    }
                }
            }
        }
        // If there is no goal then no plan
        if (goals.isEmpty()) {
            return null;
        } else {
            // Otherwise we build it
            return getDijkstraPlan(father, plan, goals, distance);
        }
    }

    private List<Action> getDijkstraPlan(Map<Map<Variable, Object>, Map<Variable, Object>> father,
            Map<Map<Variable, Object>, Action> plan, ArrayList<Map<Variable, Object>> goals,
            Map<Map<Variable, Object>, Integer> distance) {
        LinkedList<Action> dijPlan = new LinkedList<>();
        // We look for our goal which is at the shortest distance thanks to the argmin method
        Map<Variable, Object> goal = argmin(goals, distance);
        // As long as we have a father
        while (father.get(goal) != null) {
            // We add to the plan the action from the father to the son
            dijPlan.add(plan.get(goal));
            // Finally the father becomes the son
            goal = father.get(goal);
        }
        // We return the plan that we traced back via the loop above
        return dijPlan;
    }

    private Map<Variable, Object> argmin(ArrayList<Map<Variable, Object>> open,
            Map<Map<Variable, Object>, Integer> distance) {
        // We initialize the variable min which is meant to contain the closest state with the first state of the open list
        Map<Variable, Object> min = open.get(0);
        // We will go through this list and compare the distances between the nodes to get the closest
        for (int i = 1; i < open.size(); i++) {
            if (distance.get(open.get(i)) < distance.get(min)) {
                min = open.get(i);
            }
        }
        // We return the closest node
        return min;
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
