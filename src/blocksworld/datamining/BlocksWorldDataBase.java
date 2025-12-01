package blocksworld.datamining;

import java.util.HashSet;
import blocksworld.modelling.BlocksWorldConstraint;
import java.util.*;
import modelling.*;

public class BlocksWorldDataBase {
    
    private BlocksWorldConstraint blocksWorld;
    private Set<BooleanVariable> setAllBooleanVariables = new HashSet<>();

    public BlocksWorldDataBase(int b, int p){
        this.blocksWorld = new BlocksWorldConstraint(b, p);
        createSetAllBooleanVariables();
    }

    public Set<BooleanVariable> getSetAllBooleanVariables() {
        return setAllBooleanVariables;
    }

    private void createSetAllBooleanVariables() {
        setAllBooleanVariables.addAll(blocksWorld.getSetFixedb());
        setAllBooleanVariables.addAll(blocksWorld.getSetFreep());
    }

    public Set<BooleanVariable> createInstanceFromState(List<List<Integer>> state) {
        Set<BooleanVariable> instance = new HashSet<>();
        int valPile = -1;
        for (List<Integer> pile : state) {
            BooleanVariable varPile = new BooleanVariable(String.valueOf(valPile));
            if (pile.isEmpty()) {
                varPile.getDomain().remove(false);
            } else {
                varPile.getDomain().remove(true);
                Iterator<Integer> it = pile.iterator();
                while (it.hasNext()) {
                    int block = it.next();
                    BooleanVariable varBlock = new BooleanVariable(String.valueOf(block));
                    if (it.hasNext()) {
                        varBlock.getDomain().remove(false);
                    } else {
                        varBlock.getDomain().remove(true);
                    }
                    instance.add(varBlock);
                }                    
            }
            valPile--;
            instance.add(varPile);
        }
        return instance;
    }
}