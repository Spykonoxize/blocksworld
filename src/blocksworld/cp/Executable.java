package blocksworld.cp;

import blocksworld.modelling.BlocksWorld;
import blocksworld.modelling.BlocksWorldConstraint;
import blocksworld.modelling.BlocksWorldCroissant;
import blocksworld.modelling.BlocksWorldRegular;
import bwmodel.BWState;
import bwmodel.BWStateBuilder;
import bwui.BWIntegerGUI;
import cp.BacktrackSolver;
import cp.DomainSizeVariableHeuristic;
import cp.HeuristicMACSolver;
import cp.MACSolver;
import cp.NbConstraintsVariableHeuristic;
import cp.RandomValueHeuristic;
import cp.ValueHeuristic;
import cp.VariableHeuristic;
import modelling.Constraint;
import modelling.Variable;
import java.util.*;
import javax.swing.JFrame;

public class Executable {
    
    private static final int NB_BLOCKS = 7;
    private static final int NB_PILES = 3;

    public static void main(String[] args) {

        BlocksWorldConstraint blocksWorld = new BlocksWorldConstraint(NB_BLOCKS, NB_PILES);
        BlocksWorldRegular bwRegular = new BlocksWorldRegular(NB_BLOCKS, NB_PILES);
        BlocksWorldCroissant bwCroissant = new BlocksWorldCroissant(NB_BLOCKS, NB_PILES);
        
        Set<Constraint> setConstraintRegulier = new HashSet<>(blocksWorld.getSetConstraint());
        setConstraintRegulier.addAll(bwRegular.getSetConstraintRegular());

        Set<Constraint> setConstraintCroissant = new HashSet<>(blocksWorld.getSetConstraint());
        setConstraintCroissant.addAll(bwCroissant.getSetConstraintCroissant());

        Set<Constraint> setConstraintRegulierCroissant = new HashSet<>(setConstraintRegulier);
        setConstraintRegulierCroissant.addAll(setConstraintCroissant);

        System.out.println("Régulier : \n");
        MACSolver macSolverRegulier = new MACSolver(blocksWorld.getSetCombined(), setConstraintRegulier);
        double start = System.currentTimeMillis();
        Map<Variable, Object> solve = macSolverRegulier.solve();
        double end = System.currentTimeMillis();
        System.out.println("Mac Solver => ");
        System.out.println(solve);
        System.out.println("Time => " + (end - start)/1000 + " s\n");
        
        BacktrackSolver backtrackSolverRegulier = new BacktrackSolver(blocksWorld.getSetCombined(), setConstraintRegulier);
        start = System.currentTimeMillis();
        solve = backtrackSolverRegulier.solve();
        end = System.currentTimeMillis();
        System.out.println("BackTrack Solver => ");
        System.out.println(solve);
        System.out.println("Time => " + (end - start) + " ms\n");

        // On fini par avoir un stack overflow et ce même en réduisant la taile du monde
        /*
        Random r = new Random();
        VariableHeuristic variableHeuristic = new NbConstraintsVariableHeuristic(setConstraintRegulier, false);
        ValueHeuristic valueHeuristic = new RandomValueHeuristic(r);

        HeuristicMACSolver heuristicMACSolverRegulier = new HeuristicMACSolver(blocksWorld.getSetCombined(), setConstraintRegulier, variableHeuristic, valueHeuristic);
        start = System.currentTimeMillis();
        solve = heuristicMACSolverRegulier.solve();
        end = System.currentTimeMillis();
        System.out.println("HeuristicMAC => ");
        System.out.println(solve);
        System.out.println("Time => " + (end - start) + " ms\n");
        */

        System.out.println("Croissant : \n");
        
        MACSolver macSolverCroissant = new MACSolver(blocksWorld.getSetCombined(), setConstraintCroissant);
        start = System.currentTimeMillis();
        solve = macSolverCroissant.solve();
        end = System.currentTimeMillis();
        System.out.println("Mac Solver => ");
        System.out.println(solve);
        System.out.println("Time => " + (end - start)/1000 + " s\n");
        
        BacktrackSolver backtrackSolverCroissant = new BacktrackSolver(blocksWorld.getSetCombined(), setConstraintCroissant);
        start = System.currentTimeMillis();
        solve = backtrackSolverCroissant.solve();
        end = System.currentTimeMillis();
        System.out.println("BackTrack Solver => ");
        System.out.println(solve);
        System.out.println("Time => " + (end - start) + " ms\n");

        // On fini par avoir un stack overflow et ce même en réduisant la taile du monde
        /*
        variableHeuristic = new NbConstraintsVariableHeuristic(setConstraintCroissant, true);
        valueHeuristic = new RandomValueHeuristic(r);

        HeuristicMACSolver heuristicMACSolverCroissant = new HeuristicMACSolver(blocksWorld.getSetCombined(), setConstraintCroissant, variableHeuristic, valueHeuristic);
        start = System.currentTimeMillis();
        solve = heuristicMACSolverCroissant.solve();
        end = System.currentTimeMillis();
        System.out.println("HeuristicMAC => ");
        System.out.println(solve);
        System.out.println("Time => " + (end - start) + " ms\n");
        */

        System.out.println("Régulier et Croissant : \n");
        
        MACSolver macSolverRegulierCroissant = new MACSolver(blocksWorld.getSetCombined(), setConstraintRegulierCroissant);
        start = System.currentTimeMillis();
        solve = macSolverRegulierCroissant.solve();
        end = System.currentTimeMillis();
        System.out.println("Mac Solver => ");
        System.out.println(solve);
        System.out.println("Time => " + (end - start)/1000 + " s\n");
        
        BacktrackSolver backtrackSolverRegulierCroissant = new BacktrackSolver(blocksWorld.getSetCombined(), setConstraintRegulierCroissant);
        start = System.currentTimeMillis();
        solve = backtrackSolverRegulierCroissant.solve();
        end = System.currentTimeMillis();
        System.out.println("BackTrack Solver => ");
        System.out.println(solve);
        System.out.println("Time => " + (end - start) + " ms\n");

        // On fini par avoir un stack overflow et ce même en réduisant la taile du monde
        /*
        variableHeuristic = new DomainSizeVariableHeuristic(false);
        valueHeuristic = new RandomValueHeuristic(r);

        HeuristicMACSolver heuristicMACSolverRegulierCroissant = new HeuristicMACSolver(blocksWorld.getSetCombined(), setConstraintRegulierCroissant, variableHeuristic, valueHeuristic);
        start = System.currentTimeMillis();
        solve = heuristicMACSolverRegulierCroissant.solve();
        end = System.currentTimeMillis();
        System.out.println("HeuristicMAC => ");
        System.out.println(solve);
        System.out.println("Time => " + (end - start) + " ms\n");
        */

        BWStateBuilder<Integer> builder = BWStateBuilder.makeBuilder(NB_BLOCKS);
        ArrayList<Variable> listOnb = new ArrayList<>(blocksWorld.getSetOnb());
        for (int b = 0; b < NB_BLOCKS; b++){
            Variable onB = listOnb.get(b);
            int under = (int) solve.get(onB);
            if (under >= 0) {
                builder.setOn(BlocksWorld.deleteB(onB.getName()), under);
            }
        }
        BWState<Integer> state = builder.getState();
        BWIntegerGUI gui = new BWIntegerGUI(NB_BLOCKS);
        JFrame frame = new JFrame("BlocksWorld");
        frame.add(gui.getComponent(state));
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800, 600);
    }
}
