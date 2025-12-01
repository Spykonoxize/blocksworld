package blocksworld.planning;

import planning.*;
import java.util.*;
import blocksworld.modelling.BlocksWorld;
import modelling.*;

public class H2 implements Heuristic{

    private Map<Variable, Object> but;
    private Set<BooleanVariable> setFixed;

    public H2(Map<Variable, Object> but, Set<BooleanVariable> setFixed){
        this.but = but;
        this.setFixed = setFixed;
    }

    @Override
    public float estimate(Map<Variable, Object> etat){
        SortedSet<Variable> sortedSetFixedb = new TreeSet<>(BlocksWorld.COMPARATOR);
        sortedSetFixedb.addAll(setFixed);
        ArrayList<Variable> sortedListFixedB = new ArrayList<>(sortedSetFixedb);
        float res = 0;
        for(Map.Entry<Variable, Object> entry : but.entrySet()){
            // If block is in a wrong position
            if(etat.get(entry.getKey()) != entry.getValue() && BlocksWorld.deleteB(entry.getKey().getName()) > -1){
                res += 1;
                //Retrieving the fixedB variable 
                Variable fixedB = sortedListFixedB.get(BlocksWorld.deleteB(entry.getKey().getName()));
                // If the misplaced block is fixed
                if(etat.get(fixedB) == (Object) true){
                    res += 1;
                }
            }
        }
        return res;
    }

}