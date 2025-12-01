package blocksworld.modelling;

import java.util.HashSet;
import java.util.Set;
import modelling.Constraint;
import modelling.DifferenceConstraint;
import modelling.Implication;
import modelling.Variable;

public class BlocksWorldConstraint extends BlocksWorld {

	private Set<Constraint> setConstraint = new HashSet<>(); // L'ensemble de toutes les contraintes
	private Set<DifferenceConstraint> setDifferenceConstraint = new HashSet<>(); // L'ensemble des contraintes b != b'
	private Set<Implication> setImplicationFixedb = new HashSet<>(); // L'ensemble des contraintes onB = b' => b' = true
	private Set<Implication> setImplicationFreep = new HashSet<>(); // L'ensemble des contraintes onB = p => p = false

	public BlocksWorldConstraint(int nbBloc, int nbPile) {
		super(nbBloc, nbPile);
		//createAllConstraint();
		createConstraint();
	}

	public Set<Constraint> getSetConstraint() {
		return setConstraint;
	}

	public Set<DifferenceConstraint> getSetDifferenceConstraint() {
		return setDifferenceConstraint;
	}

	public Set<Implication> getSetImplicationFixedb() {
		return setImplicationFixedb;
	}

	public Set<Implication> getSetImplicationFreep() {
		return setImplicationFreep;
	}

	// Méthode qui crée nos contraintes de différences
	private void createConstraint() {
		// On crée les sous domaines pour les implications
		Set<Object> domainTrue = new HashSet<>();
		Set<Object> domainFalse = new HashSet<>();
		domainTrue.add(true);
		domainFalse.add(false);
		// Couple onb, onb
		for (Variable onb : getSetOnb()) {
			for (Variable onb2 : getSetOnb()) {
				// S'ils ne sont pas égaux
				if (!onb.equals(onb2)) {
					// On crée notre contrainte
					DifferenceConstraint constraint = new DifferenceConstraint(onb, onb2);
					setDifferenceConstraint.add(constraint);
					setConstraint.add(constraint);
				}
			}
			// Couple onb, fixedb
			for (Variable fixedb : getSetFixedb()) {
				// Si jamais b peut prendre la valeur de b'
				if (onb.getDomain().contains(Integer.valueOf(fixedb.getName()))) {
					// Alors le sous domaine du bloc du dessus sera le bloc du dessous
					Set<Object> subDomainOnb = new HashSet<>();
					subDomainOnb.add(Integer.valueOf(fixedb.getName()));
					Implication constraint = new Implication(onb, subDomainOnb, fixedb, new HashSet<>(domainTrue)); // On crée notre contrainte
					setImplicationFixedb.add(constraint);
					setConstraint.add(constraint);
				}
			}
			// couple onb, freep
			for (Variable freep : getSetFreep()) {
				// Si jamais b peut prendre la valeur de la pile
				if (onb.getDomain().contains(Integer.valueOf(freep.getName()))) {
					// Alors le sous domaine du bloc sera la pile sur laquelle il est posé
					Set<Object> subDomainOnb = new HashSet<>();
					subDomainOnb.add(Integer.valueOf(freep.getName()));
					Implication constraint = new Implication(onb, subDomainOnb, freep, new HashSet<>(domainFalse)); // On crée notre contrainte
					setImplicationFreep.add(constraint);
					setConstraint.add(constraint);
				}
			}
		}
	}

	@Override
	public String toString() {
		String r = "";
		for (Constraint c : setConstraint) {
			r += c + "\n";
		}
		return r;
	}
}