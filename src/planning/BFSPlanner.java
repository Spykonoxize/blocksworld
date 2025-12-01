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
        Map<Map<Variable, Object>, Map<Variable, Object>> father = new HashMap<>(); // Cette variable contient les pères
                                                                                    // des noeuds explorés
        Map<Map<Variable, Object>, Action> plan = new HashMap<>(); // Celle-ci contiendra la pile des actions pour
                                                                   // arriver au but
        ArrayList<Map<Variable, Object>> closed = new ArrayList<>(); // Celle-ci contiendra la liste des états fermés
        closed.add(etatInit); // On ajoute l'état initial à la liste des états fermés
        LinkedList<Map<Variable, Object>> open = new LinkedList<>(); // Celle ci contiendra les états que nous
                                                                     // explorerons une fois l'état actuel exploré
        open.add(etatInit); // On ajoute l'état initial car ici c'est le prochain que nous allons explorer
        father.put(etatInit, null); // On ajoute l'état initial dont le père est null car il est l'état de départ
        // Si jamais l'état initial est le but alors on retourne un plan vide
        if (but.isSatisfiedBy(etatInit)) {
            return new Stack<>();
        }
        // Tant qu'il reste des noeuds à explorer
        while (!open.isEmpty()) {
            // On instancie en prenant l'élément en tête de file et on l'ajoute aux états
            // fermés car nous allons l'explorer
            Map<Variable, Object> instantiation = open.poll();
            closed.add(instantiation);
            if (activate == true) {
                nodeCount++;
            }
            // Pour chacune des actions on va vérifier si on peut la réaliser
            for (Action action : actions) {
                if (action.isApplicable(instantiation)) {
                    // Si oui alors on met dans la variable next le noeud suivant
                    Map<Variable, Object> next = action.successor(instantiation);
                    // Si ce noeud ne fait pas partie des états fermés et des états ouverts alors on
                    // l'ajoute dans la map father avec pour père instanciation et on ajoute
                    // l'action au plan
                    if (!(closed.contains(next)) && !(open.contains(next))) {
                        father.put(next, instantiation);
                        plan.put(next, action);
                        // Dans la mesure où cet état suivant est le but alors on retourne la création
                        // du plan
                        if (but.isSatisfiedBy(next)) {
                            return getBFSPlan(father, plan, next);
                            // Sinon on l'ajoute à la liste des ouverts
                        } else {
                            open.add(next);
                        }
                    }
                }
            }
        }
        // S'il n'y a pas de but alors pas de plan
        return null;
    }

    private List<Action> getBFSPlan(Map<Map<Variable, Object>, Map<Variable, Object>> father,
            Map<Map<Variable, Object>, Action> plan, Map<Variable, Object> goal) {
        LinkedList<Action> bfsPlan = new LinkedList<>();
        // Tant qu'on a un père
        while (father.get(goal) != null) {
            // On ajoute au plan l'action du père vers le fils
            bfsPlan.add(plan.get(goal));
            // Enfin le père devient le fils
            goal = father.get(goal);
        }
        // On retorune le plan que l'on a remonté via la boucle au-dessus
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