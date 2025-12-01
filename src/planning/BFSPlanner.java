package planning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import modelling.Variable;

public class BFSPlanner implements Planner {

    private Map<Variable, Object> etatInit;
    private Set<Action> actions;
    private Goal but;
    private boolean activate = false;
    private int nodeCount = 0;

    public BFSPlanner(Map<Variable, Object> etatInit, Set<Action> actions, Goal but) {
        this.etatInit = etatInit;
        this.actions = actions;
        this.but = but;
    }

    @Override
    public List<Action> plan() {
        Map<Map<Variable, Object>, Map<Variable, Object>> father = new HashMap<>(); // This variable contains the parents of explored nodes
        Map<Map<Variable, Object>, Action> plan = new HashMap<>(); // This one will contain the stack of actions to reach the goal
        ArrayList<Map<Variable, Object>> closed = new ArrayList<>(); // This one will contain the list of closed states
        closed.add(etatInit); // We add the initial state to the list of closed states
        LinkedList<Map<Variable, Object>> open = new LinkedList<>(); // This one will contain the states that we will explore once the current state is explored
        open.add(etatInit); // We add the initial state because here it is the next one we will explore
        father.put(etatInit, null); // We add the initial state whose parent is null because it is the starting state
        // If the initial state is the goal then we return an empty plan
        if (but.isSatisfiedBy(etatInit)) {
            return new Stack<>();
        }
        // While there are nodes to explore
        while (!open.isEmpty()) {
            // We instantiate by taking the element at the head of the queue and add it to the closed states because we are going to explore it
            Map<Variable, Object> instantiation = open.poll();
            closed.add(instantiation);
            if (activate == true) {
                nodeCount++;
            }
            // For each action we will check if we can perform it
            for (Action action : actions) {
                if (action.isApplicable(instantiation)) {
                    // If yes then we put the next node in the variable next
                    Map<Variable, Object> next = action.successor(instantiation);
                    // If this node is not part of the closed states and open states then we add it to the father map with instantiation as the parent and add the action to the plan
                    if (!(closed.contains(next)) && !(open.contains(next))) {
                        father.put(next, instantiation);
                        plan.put(next, action);
                        // If this next state is the goal then we return the creation of the plan
                        if (but.isSatisfiedBy(next)) {
                            return getBFSPlan(father, plan, next);
                            // Otherwise we add it to the list of open states
                        } else {
                            open.add(next);
                        }
                    }
                }
            }
        }
        // If there is no goal then no plan
        return null;
    }

    private List<Action> getBFSPlan(Map<Map<Variable, Object>, Map<Variable, Object>> father,
            Map<Map<Variable, Object>, Action> plan, Map<Variable, Object> goal) {
        LinkedList<Action> bfsPlan = new LinkedList<>();
        // While the father of the goal is not null
        while (father.get(goal) != null) {
            // We add to the plan the action that led to the goal
            bfsPlan.add(plan.get(goal));
            // Finally the father becomes the son
            goal = father.get(goal);
        }
        // We return the plan that we traced back via the loop above
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