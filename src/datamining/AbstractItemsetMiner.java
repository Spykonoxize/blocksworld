package datamining;

import java.util.Comparator;
import java.util.Set;

import modelling.BooleanVariable;

public abstract class AbstractItemsetMiner implements ItemsetMiner {

    protected BooleanDatabase base;
    public static final Comparator<BooleanVariable> COMPARATOR = (var1, var2) -> var1.getName()
            .compareTo(var2.getName());

    public AbstractItemsetMiner(BooleanDatabase base) {
        this.base = base;
    }

    @Override
    public BooleanDatabase getDatabase() {
        return base;
    }

    public float frequency(Set<BooleanVariable> items) {
        float count = 0;
        for (Set<BooleanVariable> transaction : base.getTransactions()) {
            if (transaction.containsAll(items)) {
                count++;
            }
        }
        return count / base.getTransactions().size();
    }

}
