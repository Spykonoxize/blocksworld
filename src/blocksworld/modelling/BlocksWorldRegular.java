package blocksworld.modelling;

import java.util.HashSet;
import java.util.Set;
import modelling.Implication;
import modelling.Variable;

public class BlocksWorldRegular extends BlocksWorldConstraint{

    private Set<Implication> setConstraintRegular = new HashSet<>(); // Set of all regular constraints

    public BlocksWorldRegular(int b, int p) {
        super(b, p);
        constraintRegular();
    }

    private void constraintRegular() {
        // Creation of the set of piles (as negative integers)
        Set<Object> piles = new HashSet<>();
        for (int pile = 1; pile <= getNbPile(); pile++) {
            piles.add(-pile);
        }
        // We make pairs of blocks
        for (Variable onb1 : getSetOnb()) {
            for (Variable onb2 : getSetOnb()) {
                // If they are not equal
                if (!onb1.equals(onb2)) {
                    int diff = BlocksWorld.deleteB(onb2.getName()) - BlocksWorld.deleteB(onb1.getName()); // We calculate the difference between the blocks
                    Set<Object> subDomainOnb1 = new HashSet<>(); // We create the subdomains
                    Set<Object> subDomainOnb2 = new HashSet<>(piles); // We add to the subdomain of onb2 the piles because it can be the block that is placed on a pile
                    subDomainOnb1.add(BlocksWorld.deleteB(onb2.getName())); // onb1 must be placed on onb2
                    int nextShouldBe = diff + BlocksWorld.deleteB(onb2.getName()); // We look for whether there exists a block on which onb2 could be placed
                    // If this block exists then we add it to the subdomain of onb2
                    if (nextShouldBe < getNbBlock() && nextShouldBe > -1) {
                        subDomainOnb2.add(nextShouldBe);
                    }
                    Implication implication = new Implication(onb1, subDomainOnb1, onb2, subDomainOnb2); // We create our constraint
                    setConstraintRegular.add(implication);
                }
            }
        }
    }

    public Set<Implication> getSetConstraintRegular() {
        return setConstraintRegular;
    }

    @Override
    public String toString() {
        String r = "";
        for (Implication c : setConstraintRegular) {
            r += c + "\n";
        }
        return r;
    }

}
