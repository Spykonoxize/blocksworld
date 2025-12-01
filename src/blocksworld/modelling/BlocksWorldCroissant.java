package blocksworld.modelling;

import java.util.HashSet;
import java.util.Set;
import modelling.Constraint;
import modelling.Implication;
import modelling.Variable;

public class BlocksWorldCroissant extends BlocksWorldConstraint{

    private Set<Implication> setConstraintCroissant = new HashSet<>();

    public BlocksWorldCroissant(int b, int p) {
        super(b, p);
        constraintCroissant();
    }

    private void constraintCroissant() {
        // Creation of the set of piles (as negative integers)
        Set<Object> piles = new HashSet<>();
        for (int pile = 1; pile <= getNbPile(); pile++) {
            piles.add(-pile);
        }
        // We make pairs of blocks
        for (Variable onb1 : getSetOnb()) {
            for (Variable onb2 : getSetOnb()) {
                Set<Object> subDomainOnb1 = new HashSet<>(); // We create the subdomains
                Set<Object> onbSup = new HashSet<>(piles); // We add to the subdomain of onb2 the piles because it can be the block that is placed on a pile
                int b1 = BlocksWorld.deleteB(onb1.getName()); // We retrieve the value of the blocks
                int b2 = BlocksWorld.deleteB(onb2.getName());
                // If b2 is different and smaller than b1 then we add b2 to the subdomain of onb1 because it implies that these blocks are in ascending order.
                if (!onb1.equals(onb2) && b2 < b1) {
                    subDomainOnb1.add(b2);
                    // We add all the blocks on which onb2 could be placed (that is smaller than it) to its subdomain
                    for (int i = b2 - 1; i >= 0; i--) {
                        onbSup.add(i);
                    }
                    Implication c = new Implication(onb1, subDomainOnb1, onb2, onbSup); // We create our constraint
                    setConstraintCroissant.add(c);
                }
            }
        }
    }

    public Set<Implication> getSetConstraintCroissant() {
        return setConstraintCroissant;
    }

    @Override
    public String toString() {
        String r = "";
        for (Constraint c : setConstraintCroissant) {
            r += c + "\n";
        }
        return r;
    }

}
