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
     * 1. move a block b from on top of block b′ to on top of block b′′,
     * 2. move a block b from on top of block b′ to an empty pile p,
     * 3. move a block b from the bottom of pile p to on top of block b′,
     * 4. move a block b from the bottom of pile p to an empty pile p′.
     */

    private void createActions(){
        // Movements are in the following form:
        // block -> ON -> block or pile -> TO -> block or pile
        // We create a set containing all blocks and all piles for cleaner and more factored code
        Set<Variable> blocksAndPiles = new HashSet<>(blocksWorldConstraint.getSetFixedb());
        blocksAndPiles.addAll(blocksWorldConstraint.getSetFreep());

        SortedSet<Variable> sorteSetFreeAndOnb = new TreeSet<>(BlocksWorld.COMPARATOR);
        SortedSet<Variable> sortedSetOnb = new TreeSet<>(BlocksWorld.COMPARATOR);
        SortedSet<Variable> sortedSetFixedb = new TreeSet<>(BlocksWorld.COMPARATOR);
        SortedSet<Variable> sortedSetFreep = new TreeSet<>(BlocksWorld.COMPARATOR);

        sortedSetOnb.addAll(new HashSet<>(blocksWorldConstraint.getSetOnb()));
        sortedSetFixedb.addAll(new HashSet<>(blocksWorldConstraint.getSetFixedb()));
        sortedSetFreep.addAll(new HashSet<>(blocksWorldConstraint.getSetFreep()));

        // We put all sets into sortedLists to be able to use get
        sorteSetFreeAndOnb.addAll(blocksWorldConstraint.getSetFreep());
        sorteSetFreeAndOnb.addAll(blocksWorldConstraint.getSetOnb());
        ArrayList<Variable> sorteListFreeAndOnb = new ArrayList<>(sorteSetFreeAndOnb);

        sortedSetOnb.addAll(blocksWorldConstraint.getSetOnb());
        ArrayList<Variable> sortedListOnb = new ArrayList<>(sortedSetOnb);
        // The following sorted list will be used to create the precondition that block b must be movable => (fixedb = false)
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
                        
                        // If b' is a block
                        if (bOrPPrime > -1) {
                            effets.put(sortedListFixedb.get(bOrPPrime), false); // b' is fixed
                        } else { // If b' is a pile
                            effets.put(sortedListFreep.get(-(bOrPPrime+1)), true); // -(bPrimeName+1) to find the correct pile in the sortedList
                        }
                        // If b'' is a pile
                        if (bOrPPrimePrime < 0) {
                            preconditions.put(sortedListFreep.get(-(bOrPPrimePrime+1)), true); // p' must be free
                            effets.put(sortedListFreep.get(-(bOrPPrimePrime+1)), false); // p' will be non-free
                        } else { // If b'' is a block
                            preconditions.put(sortedListFixedb.get(bOrPPrimePrime), false); // b'' is not fixed
                            effets.put(sortedListFixedb.get(bOrPPrimePrime), true); // bPrimePrime will be fixed
                        }
                        preconditions.put(sortedListFixedb.get(b), false); // b is not fixed
                        preconditions.put(sortedListOnb.get(b), bOrPPrime); // b is on b' or p
                        effets.put(sortedListOnb.get(b), bOrPPrimePrime); // b goes on b'' or p'

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
