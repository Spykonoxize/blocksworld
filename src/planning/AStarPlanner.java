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
        Map<Map<Variable, Object>, Action> plan = new HashMap<>(); // Cette variable contiendra la pile des actions pour arriver au but
        Map<Map<Variable, Object>, Map<Variable, Object>> father = new HashMap<>(); // Cette variable contient les pères des noeuds explorés
        Map<Map<Variable, Object>, Float> distance = new HashMap<>(); // Celle-ci contiendra distance pour chaque état
        Map<Map<Variable, Object>, Float> value = new HashMap<>(); // Celle-ci la liste du coût des actions pour arriver jusqu'à l'état associé
        ArrayList<Map<Variable, Object>> open = new ArrayList<>(); // Celle-ci la liste des ouverts, donc des états à explorer
        open.add(etatInit); // On ajoute l'état initial car ici c'est le prochain que nous allons explorer tant qu'il reste des noeuds à explorer
        father.put(etatInit, null); // On ajoute l'état initial dont le père est null car il est l'état de départ
        distance.put(etatInit, 0f); // On ajoute l'état initial dont la distance est 0 car c'est notre point de départ
        value.put(etatInit, heuristic.estimate(etatInit)); // On estime le chemin le plus court depuis l'état initial.
        while (!open.isEmpty()) {
            // On instancie en prenant l'élément qui est à la plus courte distance hestimé et on l'enlève de la liste des ouverts
            Map<Variable, Object> instantiation = argmin(open, value);
            if (activate == true) {
                nodeCount++;
            }
            // Si on atteint un but on l'ajoute à la liste
            if (but.isSatisfiedBy(instantiation)) {
                return getBFSPlan(father, plan, instantiation);
            } else {
                // Sinon on enlève l'instantiation de la liste des ouverts
                open.remove(instantiation);
                // Pour chaque action applicable à l'état actuel
                for (Action action : actions) {
                    if (action.isApplicable(instantiation)) {
                        // On enregistre l'état suivant dans suivant
                        Map<Variable, Object> suivant = action.successor(instantiation);
                        // Si jamais l'état suivant n'était pas connu alors on initialise sa distance à +∞
                        if (!distance.containsKey(suivant)) {
                            distance.put(suivant, Float.MAX_VALUE);
                        }
                        // Si sa distance était déjà connu et si elle est plus petite alors on met à jour dans distance.
                        // La distance est calculée du point de départ vers le point où l'on est, donc les coûts de parcours s'accumulent
                        if (distance.get(suivant) > distance.get(instantiation) + action.getCost()) {
                            // On met à jour la distance de suivant en additionnant la distance jusqu'à intantiation + le coût de l'action.
                            distance.put(suivant, distance.get(instantiation) + action.getCost());
                            // On ajoute la valeur du chemin jusque suivant + "le plus court" hestimé jusqu'au but
                            value.put(suivant, distance.get(suivant) + heuristic.estimate(suivant));
                            // On met à jour father, plan et open en y ajoutant l'état suivant
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
