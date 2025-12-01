package blocksworld.modelling;

import java.util.*;
import modelling.Constraint;
import modelling.Implication;
import modelling.Variable;

public class Executable {

    private static final int NB_BLOCKS = 5;
    private static final int NB_PILES = 5;

    public static void main(String[] args) throws Exception {
        // Booléen qui va nous servir pour faire des tests
        boolean ok = true;

        // On crée nos monde
        BlocksWorldRegular blocksWorldRegular = new BlocksWorldRegular(NB_BLOCKS, NB_PILES);
        BlocksWorldCroissant blocksWorldCroissant = new BlocksWorldCroissant(NB_BLOCKS, NB_PILES);

        // On va créer des listes trées pour faciliter la création de configuration
        SortedSet<Variable> sortedSetOnb = new TreeSet<>(BlocksWorld.COMPARATOR);
        SortedSet<Variable> sortedSetFixedb = new TreeSet<>(BlocksWorld.COMPARATOR);
        SortedSet<Variable> sortedSetFreep = new TreeSet<>(BlocksWorld.COMPARATOR);
        sortedSetOnb.addAll(new HashSet<>(blocksWorldRegular.getSetOnb()));
        sortedSetFixedb.addAll(new HashSet<>(blocksWorldRegular.getSetFixedb()));
        sortedSetFreep.addAll(new HashSet<>(blocksWorldRegular.getSetFreep()));
        ArrayList<Variable> listOnb = new ArrayList<>(sortedSetOnb);
        ArrayList<Variable> listFixedb = new ArrayList<>(sortedSetFixedb);
        ArrayList<Variable> listFreep = new ArrayList<>(sortedSetFreep);

        HashMap<Variable, Object> setupForCroissant = new HashMap<>();
        HashMap<Variable, Object> setupForRegular = new HashMap<>();

        // Voici la première configuration pour un monde régulier
        //
        //           2
        //  1        3
        //  0        4
        // -1   -2  -3  -4  -5
        setupForRegular.put(listOnb.get(0), -1);
        setupForRegular.put(listOnb.get(1), 0);
        setupForRegular.put(listOnb.get(2), 3);
        setupForRegular.put(listOnb.get(3), 4);
        setupForRegular.put(listOnb.get(4), -3);

        setupForRegular.put(listFixedb.get(0), true);
        setupForRegular.put(listFixedb.get(1), false);
        setupForRegular.put(listFixedb.get(2), false);
        setupForRegular.put(listFixedb.get(3), true);
        setupForRegular.put(listFixedb.get(4), true);

        setupForRegular.put(listFreep.get(0), false);
        setupForRegular.put(listFreep.get(1), true);
        setupForRegular.put(listFreep.get(2), false);
        setupForRegular.put(listFreep.get(3), true);
        setupForRegular.put(listFreep.get(4), true);

        // On teste les contraintes régulières sur cette configuration
        for (Implication implication : blocksWorldRegular.getSetConstraintRegular()) {
            if (!implication.isSatisfiedBy(setupForRegular)) {
                ok = false;
                // System.out.println(implication);
            }
        }
        if (ok){
            System.out.println("Success");
        } else {
            System.out.println("Error");
            ok = true;
        }
                
        // Voici la seconde configuration pour un monde croissant
        //
        //       4
        //       3
        //       1       2   0
        // -1   -2  -3  -4  -5
        setupForCroissant.put(listOnb.get(0), -5);
        setupForCroissant.put(listOnb.get(1), -2);
        setupForCroissant.put(listOnb.get(2), -4);
        setupForCroissant.put(listOnb.get(3), 1);
        setupForCroissant.put(listOnb.get(4), 3);

        setupForCroissant.put(listFixedb.get(0), false);
        setupForCroissant.put(listFixedb.get(1), true);
        setupForCroissant.put(listFixedb.get(2), false);
        setupForCroissant.put(listFixedb.get(3), true);
        setupForCroissant.put(listFixedb.get(4), false);

        setupForCroissant.put(listFreep.get(0), true);
        setupForCroissant.put(listFreep.get(1), false);
        setupForCroissant.put(listFreep.get(2), true);
        setupForCroissant.put(listFreep.get(3), false);
        setupForCroissant.put(listFreep.get(4), false);

        // On teste les contraintes croissantes sur cette configuration
        for (Implication implication : blocksWorldCroissant.getSetConstraintCroissant()) {
            if (!implication.isSatisfiedBy(setupForCroissant)) {
                ok = false;
                // System.out.println(implication);
            }
        }
        if (ok){
            System.out.println("Success");
        } else {
            System.out.println("Error");
            ok = true;
        }

        // Voici la troisième configuration pour un monde régulier et croissant
        //
        //  4        
        //  2        
        //  0        1   3
        // -1   -2  -3  -4  -5
        setupForRegular.clear();
        setupForRegular.put(listOnb.get(0), -1);
        setupForRegular.put(listOnb.get(1), -3);
        setupForRegular.put(listOnb.get(2), 0);
        setupForRegular.put(listOnb.get(3), -4);
        setupForRegular.put(listOnb.get(4), 2);

        setupForRegular.put(listFixedb.get(0), true);
        setupForRegular.put(listFixedb.get(1), false);
        setupForRegular.put(listFixedb.get(2), true);
        setupForRegular.put(listFixedb.get(3), false);
        setupForRegular.put(listFixedb.get(4), false);

        setupForRegular.put(listFreep.get(0), false);
        setupForRegular.put(listFreep.get(1), true);
        setupForRegular.put(listFreep.get(2), false);
        setupForRegular.put(listFreep.get(3), false);
        setupForRegular.put(listFreep.get(4), true);

        // On teste les contraintes régulières et croissantes sur cette configuration
        for (Implication implication : blocksWorldRegular.getSetConstraintRegular()) {
            if (!implication.isSatisfiedBy(setupForRegular)) {
                ok = false;
                // System.out.println(implication);
            }
        }
        for (Implication implication : blocksWorldCroissant.getSetConstraintCroissant()) {
            if (!implication.isSatisfiedBy(setupForRegular)) {
                ok = false;
                // System.out.println(implication);
            }
        }
        if (ok){
            System.out.println("Success");
        } else {
            System.out.println("Error");
            ok = true;
        }

        //On essaye sur les contraintes de BlocksWorldConstraint aussi
        HashMap<Variable, Object> mapOnb = new HashMap<>();
        HashMap<Variable, Object> mapFixedb = new HashMap<>();
        HashMap<Variable, Object> mapFreep = new HashMap<>();

        mapOnb.put(listOnb.get(0), -1);
        mapOnb.put(listOnb.get(1), -3);
        mapOnb.put(listOnb.get(2), 0);
        mapOnb.put(listOnb.get(3), -4);
        mapOnb.put(listOnb.get(4), 2);

        mapFixedb.put(listFixedb.get(0), true);
        mapFixedb.put(listFixedb.get(1), false);
        mapFixedb.put(listFixedb.get(2), true);
        mapFixedb.put(listFixedb.get(3), false);
        mapFixedb.put(listFixedb.get(4), false);

        mapFreep.put(listFreep.get(0), false);
        mapFreep.put(listFreep.get(1), true);
        mapFreep.put(listFreep.get(2), false);
        mapFreep.put(listFreep.get(3), false);
        mapFreep.put(listFreep.get(4), false);
        
        for (Constraint constraint : blocksWorldRegular.getSetDifferenceConstraint()){
            if (!constraint.isSatisfiedBy(mapOnb)){
                System.out.println(constraint);
                ok = false;
            }
        }
        mapFixedb.putAll(mapOnb);
        for (Constraint constraint : blocksWorldRegular.getSetImplicationFixedb()){
            if (!constraint.isSatisfiedBy(mapFixedb)){
                System.out.println(constraint);
                ok = false;
            }
        }
        mapFreep.putAll(mapOnb);
        for (Constraint constraint : blocksWorldRegular.getSetImplicationFreep()){
            if (!constraint.isSatisfiedBy(mapFreep)){
                System.out.println(constraint);
                ok = false;
            }
        }
        if (ok){
            System.out.println("Success");
        } else {
            System.out.println("Error");
            ok = true;
        }
    }
}
