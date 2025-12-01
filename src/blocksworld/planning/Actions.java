package blocksworld.planning;

import java.util.*;
import blocksworld.modelling.BlocksWorld;
import blocksworld.modelling.BlocksWorldConstraint;
import modelling.Variable;
import planning.BasicAction;
import planning.Action;

public class Actions {

    private Set<Action> setActions = new HashSet<>();
    private BlocksWorldConstraint blocksWorldConstraint;

    public Actions(int b, int p) {
        this.blocksWorldConstraint = new BlocksWorldConstraint(b, p);
        createActions();
    }

    public Set<Action> getSetActions() {
        return setActions;
    }

    /*
     * ACTIONS:
     * 1. déplacer un bloc b du dessus d’un bloc b′ vers le dessus d’un bloc b′′,
     * 2. déplacer un bloc b du dessus d’un bloc b′ vers une pile vide p,
     * 3. déplacer un bloc b du dessous d’une pile p vers le dessus d’un bloc b′,
     * 4. déplacer un bloc b du dessous d’une pile p vers une pile vide p′.
     */

    private void createActions(){
        // Les déplacements sont sous la forme suivante :
        // bloc -> SUR -> bloc ou pile -> VERS -> bloc ou pile
        // On fait donc un ensemble qui contient tout les blocks et toutes les piles ce qui permet d'avoir un code plus propre et plus factorisé
        Set<Variable> blocksAndPiles = new HashSet<>(blocksWorldConstraint.getSetFixedb());
        blocksAndPiles.addAll(blocksWorldConstraint.getSetFreep());

        SortedSet<Variable> sorteSetFreeAndOnb = new TreeSet<>(BlocksWorld.COMPARATOR);
        SortedSet<Variable> sortedSetOnb = new TreeSet<>(BlocksWorld.COMPARATOR);
        SortedSet<Variable> sortedSetFixedb = new TreeSet<>(BlocksWorld.COMPARATOR);
        SortedSet<Variable> sortedSetFreep = new TreeSet<>(BlocksWorld.COMPARATOR);

        sortedSetOnb.addAll(new HashSet<>(blocksWorldConstraint.getSetOnb()));
        sortedSetFixedb.addAll(new HashSet<>(blocksWorldConstraint.getSetFixedb()));
        sortedSetFreep.addAll(new HashSet<>(blocksWorldConstraint.getSetFreep()));

        // On crée met tous les ensembles dans des sortedList pour pouvoir faire des get
        sorteSetFreeAndOnb.addAll(blocksWorldConstraint.getSetFreep());
        sorteSetFreeAndOnb.addAll(blocksWorldConstraint.getSetOnb());
        ArrayList<Variable> sorteListFreeAndOnb = new ArrayList<>(sorteSetFreeAndOnb);

        sortedSetOnb.addAll(blocksWorldConstraint.getSetOnb());
        ArrayList<Variable> sortedListOnb = new ArrayList<>(sortedSetOnb);
        // La liste triée qui suit nous servira à créer la précondition qui dit que le bloc b doit pouvoir être déplacé => (fixedb = false)
        sortedSetFixedb.addAll(blocksWorldConstraint.getSetFixedb());
        ArrayList<Variable> sortedListFixedb = new ArrayList<>(sortedSetFixedb);

        sortedSetFixedb.addAll(blocksWorldConstraint.getSetFreep());
        ArrayList<Variable> sortedListFreep = new ArrayList<>(sortedSetFreep);

        for (Variable Varb : sortedSetOnb) {
            for (Variable VarbOrPPrime : sorteListFreeAndOnb){
                for (Variable VarbOrPPrimePrime : sorteListFreeAndOnb) {
                    if(!(Varb.equals(VarbOrPPrime) || Varb.equals(VarbOrPPrimePrime) || VarbOrPPrime.equals(VarbOrPPrimePrime))){
                        int b = BlocksWorld.deleteB(Varb.getName());
                        int bOrPPrime = BlocksWorld.deleteB(VarbOrPPrime.getName());
                        int bOrPPrimePrime = BlocksWorld.deleteB(VarbOrPPrimePrime.getName());

                        Map<Variable, Object> preconditions = new HashMap<>();
                        Map<Variable, Object> effets = new HashMap<>();
                        
                        // Si b' est un bloc
                        if (bOrPPrime > -1) {
                            effets.put(sortedListFixedb.get(bOrPPrime), false); // b' est fixé
                        } else { // Si b' est une pile
                            effets.put(sortedListFreep.get(-(bOrPPrime+1)), true); // -(bPrimeName+1) pour trouvé la bonne pile dans la sortedList
                        }
                        // Si b'' est une pile
                        if (bOrPPrimePrime < 0) {
                            preconditions.put(sortedListFreep.get(-(bOrPPrimePrime+1)), true); // p' doit être free
                            effets.put(sortedListFreep.get(-(bOrPPrimePrime+1)), false); // p' va être non free
                        } else { // Si b'' est un bloc
                            preconditions.put(sortedListFixedb.get(bOrPPrimePrime), false); // b'' n'est pas fixé
                            effets.put(sortedListFixedb.get(bOrPPrimePrime), true); // bPrimePrime va être fixé
                        }
                        preconditions.put(sortedListFixedb.get(b), false); // b n'est pas fixé
                        preconditions.put(sortedListOnb.get(b), bOrPPrime); // b est sur b' ou p
                        effets.put(sortedListOnb.get(b), bOrPPrimePrime); // b va sur b'' ou p'

                        Action action = new BasicAction(preconditions, effets, 2);
                        setActions.add(action);
                    }
                }
            }
        }
        
    }

    @Override
    public String toString(){
        String res = "";
        for(Action action : setActions){
            res += action + "\n";
        }
        return res;
    }
}
