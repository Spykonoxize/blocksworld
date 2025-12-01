package datamining;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import modelling.BooleanVariable;

public class Apriori extends AbstractItemsetMiner {

    public Apriori(BooleanDatabase base) {
        super(base);
    }

    public Set<Itemset> frequentSingletons(float frequency) {
        Set<Itemset> frequentItemsets = new HashSet<>();
        for (BooleanVariable item : base.getItems()) {
            Set<BooleanVariable> singletonItemset = new HashSet<>();
            singletonItemset.add(item);
            if (frequency(singletonItemset) >= frequency) {
                frequentItemsets.add(new Itemset(singletonItemset, frequency(singletonItemset)));
            }
        }
        return frequentItemsets;
    }

    public static SortedSet<BooleanVariable> combine(SortedSet<BooleanVariable> sortedItemSet1,
            SortedSet<BooleanVariable> sortedItemSet2) {
        if (sortedItemSet1.size() == 1 && sortedItemSet2.size() == 1) {
            if (!sortedItemSet1.first().equals(sortedItemSet2.first())) {
                SortedSet<BooleanVariable> combinedSet = new TreeSet<>(COMPARATOR);
                combinedSet.add(sortedItemSet1.first());
                combinedSet.add(sortedItemSet2.first());
                return combinedSet;
            }
        }
        if (sortedItemSet1.size() == sortedItemSet2.size() && sortedItemSet1.size() > 1) {
            SortedSet<BooleanVariable> sortedItemSet1RemoveLast = new TreeSet<>(
                    sortedItemSet1.headSet(sortedItemSet1.last()));
            SortedSet<BooleanVariable> sortedItemSet2RemoveLast = new TreeSet<>(
                    sortedItemSet2.headSet(sortedItemSet2.last()));
            if (sortedItemSet1RemoveLast.equals(sortedItemSet2RemoveLast)) {
                if (!(sortedItemSet1.containsAll(sortedItemSet2) || sortedItemSet2.containsAll(sortedItemSet1))) {
                    SortedSet<BooleanVariable> combinedSet = new TreeSet<>(COMPARATOR);
                    combinedSet.addAll(sortedItemSet1);
                    combinedSet.add(sortedItemSet2.last());
                    return combinedSet;
                }
            }
        }
        return null;
    }

    public static boolean allSubsetsFrequent(Set<BooleanVariable> itemset,
            Collection<SortedSet<BooleanVariable>> frequentItemsets) {
        for (BooleanVariable item : itemset) {
            Set<BooleanVariable> subset = new HashSet<>(itemset);
            subset.remove(item);
            if (!frequentItemsets.contains(subset)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Set<Itemset> extract(float frequency) {
        Set<Itemset> singletons = frequentSingletons(frequency);
        Set<Itemset> itemsets = new HashSet<>(singletons);
        LinkedList<SortedSet<BooleanVariable>> list = new LinkedList<>(new TreeSet<>(COMPARATOR));
        for (Itemset item : itemsets) {
            SortedSet<BooleanVariable> set = new TreeSet<>(COMPARATOR);
            set.addAll(item.getItems());
            list.add(set);
        }
        while (!list.isEmpty()) {
            SortedSet<BooleanVariable> combinedSet = new TreeSet<>(COMPARATOR);
            LinkedList<SortedSet<BooleanVariable>> tampon = new LinkedList<>(new TreeSet<>(COMPARATOR));
            for (SortedSet<BooleanVariable> set1 : list) {
                for (SortedSet<BooleanVariable> set2 : list) {
                    if (!set1.equals(set2)) {
                        combinedSet = combine(set1, set2);
                        if (combinedSet != null) {
                            if (frequency(combinedSet) >= frequency && allSubsetsFrequent(combinedSet, list)) {
                                if (!tampon.contains(combinedSet)) {
                                    tampon.add(combinedSet);
                                    itemsets.add(new Itemset(combinedSet, frequency(combinedSet)));
                                }
                            }
                        }
                    }
                }
            }
            list = tampon;
        }
        return itemsets;
    }
}