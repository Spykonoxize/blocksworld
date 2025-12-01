package blocksworld.planning;

import java.util.*;
import javax.swing.JFrame;
import blocksworld.modelling.BlocksWorld;
import blocksworld.modelling.BlocksWorldConstraint;
import bwmodel.BWState;
import bwmodel.BWStateBuilder;
import bwui.BWComponent;
import bwui.BWIntegerGUI;
import modelling.Variable;
import planning.*;

public class Executable {
    
    private static final int NB_BLOCKS = 3;
    private static final int NB_PILES = 4;

    private static BWState<Integer> makeBWState(Map<Variable, Object> initialState) {
        BWStateBuilder<Integer> builder = BWStateBuilder.makeBuilder(NB_BLOCKS);
        for (Map.Entry<Variable, Object> entry : initialState.entrySet()) {
            if (entry.getKey().getClass() == Variable.class) {
                int under = (int) entry.getValue();
                if (under >= 0) {
                    builder.setOn(BlocksWorld.deleteB(entry.getKey().getName()), under);
                }
            }
        }
        return builder.getState();
    }
    
    public static void main(String[] args) {
        
        BlocksWorldConstraint blocksWorld = new BlocksWorldConstraint(NB_BLOCKS, NB_PILES);

        SortedSet<Variable> sortedSetOnb = new TreeSet<>(BlocksWorld.COMPARATOR);
        SortedSet<Variable> sortedSetFixedb = new TreeSet<>(BlocksWorld.COMPARATOR);
        SortedSet<Variable> sortedSetFreep = new TreeSet<>(BlocksWorld.COMPARATOR);
        sortedSetOnb.addAll(new HashSet<>(blocksWorld.getSetOnb()));
        sortedSetFixedb.addAll(new HashSet<>(blocksWorld.getSetFixedb()));
        sortedSetFreep.addAll(new HashSet<>(blocksWorld.getSetFreep()));
        ArrayList<Variable> listOnb = new ArrayList<>(sortedSetOnb);
        ArrayList<Variable> listFixedb = new ArrayList<>(sortedSetFixedb);
        ArrayList<Variable> listFreep = new ArrayList<>(sortedSetFreep);

        Map<Variable, Object> initialState = new HashMap<>();
        Map<Variable, Object> finalState = new HashMap<>();

        // Voici l'état initial
        //
        //          
        //          1
        //  2       0   
        // -1  -2  -3  -4
        initialState.put(listOnb.get(0), -3);
        initialState.put(listOnb.get(1), 0);
        initialState.put(listOnb.get(2), -1);

        initialState.put(listFixedb.get(0), true);
        initialState.put(listFixedb.get(1), false);
        initialState.put(listFixedb.get(2), false);

        initialState.put(listFreep.get(0), false);
        initialState.put(listFreep.get(1), true);
        initialState.put(listFreep.get(2), false);
        initialState.put(listFreep.get(3), true);

        // Voici l'état final (Le bloc 1 pourra être sur la pile -1 ou -2)
        //
        //           
        //              2
        //              0
        // -1  -2  -3  -4
        finalState.put(listOnb.get(0), -4);
        finalState.put(listOnb.get(2), 0);

        finalState.put(listFixedb.get(0), true);

        finalState.put(listFreep.get(2), true);
        finalState.put(listFreep.get(3), false);
        
        // Ensemble des actions possibles
        Set<Action> actions = new Actions(NB_BLOCKS, NB_PILES).getSetActions();

        // Notre but
        Goal goal = new BasicGoal(finalState); 

        // L'ensemble des planner
        DFSPlanner dfs = new DFSPlanner(initialState, actions, goal);
        dfs.activateNodeCount(true);
        double start = System.currentTimeMillis();
        List<Action> plan = dfs.plan();
        List<Action> copyPlan = plan;
        double end = System.currentTimeMillis();
        System.out.println("DFS =>");
        System.out.println(plan);
        System.out.println("Node count => " + dfs.getNodeCount());
        System.out.println("Time => " + (end - start) + " ms\n");
        
        BFSPlanner bfs = new BFSPlanner(initialState, actions, goal);
        bfs.activateNodeCount(true);
        System.out.println("BFS =>");
        start = System.currentTimeMillis();
        plan = bfs.plan();
        end = System.currentTimeMillis();
        System.out.println(plan);
        System.out.println("Node count => " + bfs.getNodeCount());
        System.out.println("Time => " + (end - start) + " ms\n");

        DijkstraPlanner dijkstra = new DijkstraPlanner(initialState, actions, goal);
        dijkstra.activateNodeCount(true);
        System.out.println("Dijkstra =>");
        start = System.currentTimeMillis();
        plan = dijkstra.plan();
        end = System.currentTimeMillis();
        System.out.println(plan);
        System.out.println("Node count => " + dijkstra.getNodeCount());
        System.out.println("Time => " + (end - start) + " ms\n");

        Heuristic H1 = new H1(finalState);

        Heuristic H2 = new H2(finalState, blocksWorld.getSetFixedb());

        AStarPlanner AStarH1 = new AStarPlanner(initialState, actions, goal, H1);
        AStarH1.activateNodeCount(true);
        System.out.println("A* with H1 =>");
        start = System.currentTimeMillis();
        plan = AStarH1.plan();
        end = System.currentTimeMillis();
        System.out.println(plan);
        System.out.println("Node count => " + AStarH1.getNodeCount());
        System.out.println("Time => " + (end - start) + " ms\n");

        AStarPlanner AStarH2 = new AStarPlanner(initialState, actions, goal, H2);
        AStarH2.activateNodeCount(true);
        System.out.println("A* with H2 =>");
        start = System.currentTimeMillis();
        plan = AStarH2.plan();
        end = System.currentTimeMillis();
        System.out.println(plan);
        System.out.println("Node count => " + AStarH2.getNodeCount());
        System.out.println("Time => " + (end - start) + " ms\n");

        /*
        BWStateBuilder<Integer> builder = BWStateBuilder.makeBuilder(NB_BLOCKS);
        for (int b = 0; b < NB_BLOCKS; b++){
            Variable onB = listOnb.get(b);
            int under = (int) initialState.get(onB);
            if (under >= 0) {
                builder.setOn(b, under);
            }
        }

        
        BWState<Integer> state = builder.getState();
        System.out.println(state);

        BWIntegerGUI gui = new BWIntegerGUI(NB_BLOCKS);
        JFrame frame = new JFrame("BlocksWorld");
        frame.add(gui.getComponent(state));
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 600);
        */

        BWIntegerGUI gui = new BWIntegerGUI(NB_BLOCKS);
        JFrame frame = new JFrame("BlocksWorld Live");
        BWState<Integer> bwState = makeBWState(initialState);
        BWComponent<Integer> component = gui.getComponent(bwState);
        frame.add(component);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 600);

        Map<Variable, Object> newState = new HashMap<>(initialState);

        for (Action a: copyPlan) {
            try { Thread.sleep(750); }
            catch (InterruptedException e) { e.printStackTrace(); }
            newState=a.successor(newState);
            component.setState(makeBWState(newState));
            }
            System.out.println("Simulation of plan: done.");        
    }
}
