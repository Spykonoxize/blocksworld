package datamining;

import java.util.HashSet;
import java.util.Set;
import modelling.BooleanVariable;

public abstract class AbstractAssociationRuleMiner implements AssociationRuleMiner {

    protected BooleanDatabase database;

    public AbstractAssociationRuleMiner(BooleanDatabase database) {
        this.database = database;
    }

    @Override
    public BooleanDatabase getDataBase() {
        return this.database;
    }

    public static float frequency(Set<BooleanVariable> variables, Set<Itemset> itemsets) {
        for (Itemset transaction : itemsets) {
            Set<BooleanVariable> itemset = transaction.getItems();
            if (itemset.equals(variables)) {
                return transaction.getFrequency();
            }
        }
        throw new IllegalArgumentException("The itemset " + variables + " is not in the database");
    }

    public static float confidence(Set<BooleanVariable> premisse, Set<BooleanVariable> conclusion, Set<Itemset> itemsets) {
        Set<BooleanVariable> XY = new HashSet<BooleanVariable>(premisse);
        XY.addAll(conclusion);
        float fPremisse = frequency(premisse, itemsets);
        float fXY = frequency(XY, itemsets);
        return fXY / fPremisse;
    }
}