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

    // Initiates the depth-first search
    @Override
    public List<Action> plan() {
        Stack<Action> plan = new Stack<>(); // This variable will contain the stack of actions to reach the goal
        ArrayList<Map<Variable, Object>> closed = new ArrayList<>(); // This one will contain the list of closed states
        closed.add(etatInit);// We can already add the initial state to it
        return dfs(etatInit, plan, closed); // We return the result of the recursive depth-first search method
    }

    private List<Action> dfs(Map<Variable, Object> etatInit, Stack<Action> plan,
        ArrayList<Map<Variable, Object>> closed) {
        if (activate == true) {
            nodeCount++;
        }
        // If the current state is the goal then we return the plan
        if (but.isSatisfiedBy(etatInit)) {
            return plan;
        } else {
            for (Action action : actions) {
                // For each action we check if we can apply it to the current state
                if (action.isApplicable(etatInit)) {
                    // In this case we add the next state to the variable next
                    Map<Variable, Object> next = action.successor(etatInit);
                    // If this next state has not been explored
                    if (!closed.contains(next)) {
                        plan.push(action); // We add the action to the plan
                        closed.add(next); // We add the next state to the list of closed states
                        // Recursive call to perform depth-first search
                        List<Action> subplan = dfs(next, plan, closed);
                        // If the subplan is not null we return it otherwise we remove the action at the top of the plan stack
                        if (!(subplan == null)) {
                            return subplan;
                        } else {
                            plan.pop();
                        }
                    }
                }
            }
            // If there is no goal then no plan
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
