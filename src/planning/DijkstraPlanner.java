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
        Map<Map<Variable, Object>, Action> plan = new HashMap<>(); // Cette variable contiendra la pile des actions pour
                                                                   // arriver au but
        Map<Map<Variable, Object>, Integer> distance = new HashMap<>(); // Celle-ci contiendra distance pour chaque état
        Map<Map<Variable, Object>, Map<Variable, Object>> father = new HashMap<>(); // Cette variable contient les pères
                                                                                    // des noeuds explorés
        ArrayList<Map<Variable, Object>> goals = new ArrayList<>(); // Celle-ci la liste de nos buts
        ArrayList<Map<Variable, Object>> open = new ArrayList<>(); // Celle-ci la liste des ouverts, donc des états à
                                                                   // explorer
        father.put(etatInit, null); // On ajoute l'état initial dont le père est null car il est l'état de départ
        distance.put(etatInit, 0); // On ajoute l'état initial dont la distance est 0 car c'est notre point de
                                   // départ
        open.add(etatInit); // On ajoute l'état initial car ici c'est le prochain que nous allons explorer
        // Tant qu'il reste des noeuds à explorer
        while (!open.isEmpty()) {
            // On instancie en prenant l'élément qui est à la plus courte distance et on
            // l'enlève de la liste des ouverts
            Map<Variable, Object> instantiation = argmin(open, distance);
            open.remove(instantiation);
            if (activate == true) {
                nodeCount++;
            }
            // Si on atteint un but on l'ajoute à la liste
            if (but.isSatisfiedBy(instantiation)) {
                goals.add(instantiation);
            }
            // Pour chaque action applicable à l'état actuel
            for (Action action : actions) {
                if (action.isApplicable(instantiation)) {
                    // On enregistre l'état suivant dans next
                    Map<Variable, Object> next = action.successor(instantiation);
                    // Si jamais l'état suivant n'était pas connu alors on initialise sa distance à
                    // +∞
                    if (!distance.containsKey(next)) {
                        distance.put(next, Integer.MAX_VALUE);
                    }
                    // Si sa distance était déjà connu et si elle est plus petite alors on met à
                    // jour dans distance
                    // La distance est calculée du point de départ vers le point où l'on est, donc
                    // les coûts de parcours s'accumulent
                    if (distance.get(next) > distance.get(instantiation) + action.getCost()) {
                        distance.put(next, distance.get(instantiation) + action.getCost());
                        // On met à jour father, plan et open en y ajoutant l'état suivant
                        father.put(next, instantiation);
                        plan.put(next, action);
                        open.add(next);
                    }
                }
            }
        }
        // S'il n'y a pas de but alors pas de plan
        if (goals.isEmpty()) {
            return null;
        } else {
            // Sinon on le construit
            return getDijkstraPlan(father, plan, goals, distance);
        }
    }

    private List<Action> getDijkstraPlan(Map<Map<Variable, Object>, Map<Variable, Object>> father,
            Map<Map<Variable, Object>, Action> plan, ArrayList<Map<Variable, Object>> goals,
            Map<Map<Variable, Object>, Integer> distance) {
        LinkedList<Action> dijPlan = new LinkedList<>();
        // On va chercher notre but qui est à la plus courte distance grâce à la méthode
        // argmin
        Map<Variable, Object> goal = argmin(goals, distance);
        // Tant qu'on a un père
        while (father.get(goal) != null) {
            // On ajoute au plan l'action du père vers le fils
            dijPlan.add(plan.get(goal));
            // Enfin le père devient le fils
            goal = father.get(goal);
        }
        // On retorune le plan que l'on a remonté via la boucle au-dessus
        return dijPlan;
    }

    private Map<Variable, Object> argmin(ArrayList<Map<Variable, Object>> open,
            Map<Map<Variable, Object>, Integer> distance) {
        // On initialise la variable min qui a pour but de contenir l'état le plus
        // proche avec le premier état de la liste des ouverts
        Map<Variable, Object> min = open.get(0);
        // On va parcourir cette liste et comparer les distances entre les noeuds pour
        // obtenir le plus proche
        for (int i = 1; i < open.size(); i++) {
            if (distance.get(open.get(i)) < distance.get(min)) {
                min = open.get(i);
            }
        }
        // On retourne le noeud le plus proche
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
