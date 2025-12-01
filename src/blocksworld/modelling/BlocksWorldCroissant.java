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
        // On crée un ensemble qui contient les piles sous formes d'entiers
        Set<Object> piles = new HashSet<>();
        for (int pile = 1; pile <= getNbPile(); pile++) {
            piles.add(-pile);
        }
        // On fait des couples de blocs
        for (Variable onb1 : getSetOnb()) {
            for (Variable onb2 : getSetOnb()) {
                Set<Object> subDomainOnb1 = new HashSet<>(); // On crée les sous domaines
                Set<Object> onbSup = new HashSet<>(piles); // On ajoute au sous domaine de onb2 les piles car il peut être le bloc qui est posé sur une pile
                int b1 = BlocksWorld.deleteB(onb1.getName()); // On récupère la valeur des blocs
                int b2 = BlocksWorld.deleteB(onb2.getName());
                // Si b2 est différent et plus petit que b1 alors on ajoute b2 au sous domaine de onb1 car ça implique que ces blocs sont dans un ordre croissant.
                if (!onb1.equals(onb2) && b2 < b1) {
                    subDomainOnb1.add(b2);
                    // On ajoute tout les blocs sur lesquels pourrait être posé onb2 (c'est à dire plus petit que lui) à son sous domaine
                    for (int i = b2 - 1; i >= 0; i--) {
                        onbSup.add(i);
                    }
                    Implication c = new Implication(onb1, subDomainOnb1, onb2, onbSup); // On crée notre contrainte
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
