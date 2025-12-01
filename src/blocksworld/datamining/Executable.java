package blocksworld.datamining;

import java.util.List;
import java.util.Random;
import java.util.Set;

import bwgenerator.BWGenerator;
import datamining.Apriori;
import datamining.AssociationRule;
import datamining.BooleanDatabase;
import datamining.BruteForceAssociationRuleMiner;
import datamining.Itemset;
import modelling.BooleanVariable;

public class Executable {
    
    private static final int NB_BLOCKS = 3;
    private static final int NB_PILES = 3;
    private static final int NB_INSTANCE = 10000;
    private static final float FREQUENCE_MOTIFS = 2/3;
    private static final float FREQUENCE_REGLES = 2/3;
    private static final float CONFIDENCE = 95/100;

    public static void main(String[] args) {
        
        BlocksWorldDataBase blocksWorldDataBase = new BlocksWorldDataBase(NB_BLOCKS, NB_PILES);
        BooleanDatabase db = new BooleanDatabase(blocksWorldDataBase.getSetAllBooleanVariables());
        Random random = new Random();
        BWGenerator worldGenerator = new BWGenerator(NB_BLOCKS, NB_PILES);
        for (int i = 0; i < NB_INSTANCE; i++) {
            List<List<Integer>> state = worldGenerator.generate(random);
            Set<BooleanVariable> instance = blocksWorldDataBase.createInstanceFromState(state);
            db.add(instance);
        }
        
        BruteForceAssociationRuleMiner bruteForceAssociationRuleMiner = new BruteForceAssociationRuleMiner(db);
        System.out.println(bruteForceAssociationRuleMiner.extract(FREQUENCE_MOTIFS, CONFIDENCE));
    }
}
