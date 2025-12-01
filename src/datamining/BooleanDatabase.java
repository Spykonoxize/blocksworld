package datamining;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import modelling.BooleanVariable;

public class BooleanDatabase {

    private Set<BooleanVariable> items;
    private List<Set<BooleanVariable>> transactions = new ArrayList<>();

    public BooleanDatabase(Set<BooleanVariable> items) {
        this.items = items;
    }

    public void add(Set<BooleanVariable> transaction) {
        transactions.add(transaction);
    }

    public Set<BooleanVariable> getItems() {
        return items;
    }

    public List<Set<BooleanVariable>> getTransactions() {
        return transactions;
    }
}
