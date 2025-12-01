package blocksworld.modelling;

import java.util.HashSet;
import java.util.Set;
import modelling.Implication;
import modelling.Variable;

public class BlocksWorldRegular extends BlocksWorldConstraint{

    private Set<Implication> setConstraintRegular = new HashSet<>(); // L'ensemble de nos contraintes régulières

    public BlocksWorldRegular(int b, int p) {
        super(b, p);
        constraintRegular();
    }

    private void constraintRegular() {
        // On crée un ensemble qui contient les piles sous formes d'entiers
        Set<Object> piles = new HashSet<>();
        for (int pile = 1; pile <= getNbPile(); pile++) {
            piles.add(-pile);
        }
        // On fait des couples de blocs
        for (Variable onb1 : getSetOnb()) {
            for (Variable onb2 : getSetOnb()) {
                // S'ils ne sont pas égaux
                if (!onb1.equals(onb2)) {
                    int diff = BlocksWorld.deleteB(onb2.getName()) - BlocksWorld.deleteB(onb1.getName()); //On calcul l'écart entre les blocs
                    Set<Object> subDomainOnb1 = new HashSet<>(); // On crée les sous domaines
                    Set<Object> subDomainOnb2 = new HashSet<>(piles); // On ajoute au sous domaine de onb2 les piles car il peut être le bloc qui est posé sur une pile
                    subDomainOnb1.add(BlocksWorld.deleteB(onb2.getName())); // onb1 lui doit être posé sur onb2
                    int nextShouldBe = diff + BlocksWorld.deleteB(onb2.getName()); // On cherche s'il existe un bloc sur lequel pourrait être posé onb2
                    // Si ce bloc existe alors on l'ajoute au sous domaine de onb2
                    if (nextShouldBe < getNbBlock() && nextShouldBe > -1) {
                        subDomainOnb2.add(nextShouldBe);
                    }
                    Implication implication = new Implication(onb1, subDomainOnb1, onb2, subDomainOnb2); // On crée notre contrainte
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
