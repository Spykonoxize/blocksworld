package datamining;

import java.util.HashSet;
import java.util.Set;
import modelling.BooleanVariable;

public class BruteForceAssociationRuleMiner extends AbstractAssociationRuleMiner {

    public BruteForceAssociationRuleMiner(BooleanDatabase database) {
        super(database);
    }

    public static Set<Set<BooleanVariable>> allCandidatePremises(Set<BooleanVariable> items) {
        Set<Set<BooleanVariable>> candidatePremises = new HashSet<>();
        Set<BooleanVariable> currentCombination = new HashSet<>();

        generateCombinations(items, candidatePremises, currentCombination, 0);
        return candidatePremises;
    }

    private static void generateCombinations(Set<BooleanVariable> items, Set<Set<BooleanVariable>> candidatePremises,
            Set<BooleanVariable> currentCombination, int currentIndex) {
        if (currentIndex == items.size()) {
            if (!currentCombination.isEmpty() && currentCombination.size() < items.size()) {
                candidatePremises.add(new HashSet<>(currentCombination));
            }
            return;
        }
        currentCombination.add((BooleanVariable) items.toArray()[currentIndex]);
        generateCombinations(items, candidatePremises, currentCombination, currentIndex + 1);
        currentCombination.remove((BooleanVariable) items.toArray()[currentIndex]);
        generateCombinations(items, candidatePremises, currentCombination, currentIndex + 1);
    }

    @Override
    public Set<AssociationRule> extract(float minFr, float minConf) {
        Set<AssociationRule> rules = new HashSet<>();
        Apriori apriori = new Apriori(database);
        Set<Itemset> itemSet = apriori.extract(minFr);
        for (Itemset x : itemSet) {
            for (Set<BooleanVariable> y : allCandidatePremises(x.getItems())) {
                if (!y.isEmpty()) {
                    Set<BooleanVariable> xPrivateY = new HashSet<>(x.getItems());
                    xPrivateY.removeAll(y);
                    float conf = frequency(x.getItems(), itemSet)/frequency(xPrivateY, itemSet);
                    if (conf >= minConf) {
                        AssociationRule rule = new AssociationRule(xPrivateY, y, frequency(x.getItems(), itemSet), conf);
                        rules.add(rule);
                    }
                }
            }
        }
        return rules;
    }
}