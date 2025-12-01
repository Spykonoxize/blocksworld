package blocksworld.modelling;

import java.util.HashSet;
import java.util.Set;
import modelling.Constraint;
import modelling.DifferenceConstraint;
import modelling.Implication;
import modelling.Variable;

public class BlocksWorldConstraint extends BlocksWorld {

	private Set<Constraint> setConstraint = new HashSet<>(); // Set of all constraints
	private Set<DifferenceConstraint> setDifferenceConstraint = new HashSet<>(); // Set of all b != b' constraints
	private Set<Implication> setImplicationFixedb = new HashSet<>(); // Set of all onB = b' => b' = true constraints
	private Set<Implication> setImplicationFreep = new HashSet<>(); // Set of all onB = p => p = false constraints

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

	// Method that creates all the constraints of the Blocks World
	private void createConstraint() {
		// We create the subdomains for the implications
		Set<Object> domainTrue = new HashSet<>();
		Set<Object> domainFalse = new HashSet<>();
		domainTrue.add(true);
		domainFalse.add(false);
		// Couple onb, onb
		for (Variable onb : getSetOnb()) {
			for (Variable onb2 : getSetOnb()) {
				// If they are not equal
				if (!onb.equals(onb2)) {
					// We create our constraint
					DifferenceConstraint constraint = new DifferenceConstraint(onb, onb2);
					setDifferenceConstraint.add(constraint);
					setConstraint.add(constraint);
				}
			}
			// Couple onb, fixedb
			for (Variable fixedb : getSetFixedb()) {
				// If ever b can take the value of b'
				if (onb.getDomain().contains(Integer.valueOf(fixedb.getName()))) {
					// Then the subdomain of the block on top will be the block below
					Set<Object> subDomainOnb = new HashSet<>();
					subDomainOnb.add(Integer.valueOf(fixedb.getName()));
					Implication constraint = new Implication(onb, subDomainOnb, fixedb, new HashSet<>(domainTrue)); // We create our constraint
					setImplicationFixedb.add(constraint);
					setConstraint.add(constraint);
				}
			}
			// Couple onb, freep
			for (Variable freep : getSetFreep()) {
				// If ever b can take the value of the pile
				if (onb.getDomain().contains(Integer.valueOf(freep.getName()))) {
					// Then the subdomain of the block will be the pile on which it is placed
					Set<Object> subDomainOnb = new HashSet<>();
					subDomainOnb.add(Integer.valueOf(freep.getName()));
					Implication constraint = new Implication(onb, subDomainOnb, freep, new HashSet<>(domainFalse)); // We create our constraint
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