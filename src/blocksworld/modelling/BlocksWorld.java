package blocksworld.modelling;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import modelling.BooleanVariable;
import modelling.Variable;

public class BlocksWorld {

    // Variables
    private int nbBlock; // Number of blocks in the world
    private int nbPile; // Number of piles in the world
    private Set<Variable> setOnb = new HashSet<>(); // The set of onB
    private Set<BooleanVariable> setFixedb = new HashSet<>(); // The set of fixedB
    private Set<BooleanVariable> setFreep = new HashSet<>(); // The set of freeP
    private Set<Variable> setCombined = new HashSet<>(); // The set containing all the variables in the world

    public static final Comparator<Variable> COMPARATOR = (var1, var2) -> var1.getName().compareTo(var2.getName());

    // Constructors
    public BlocksWorld(int nbBlock, int nbPile) {
        this.nbBlock = nbBlock;
        this.nbPile = nbPile;
        create();
    }

    // Getters
    public int getNbBlock() {
        return nbBlock;
    }

    public int getNbPile() {
        return nbPile;
    }

    public Set<Variable> getSetOnb() {
        return setOnb;
    }

    public Set<BooleanVariable> getSetFixedb() {
        return setFixedb;
    }

    public Set<BooleanVariable> getSetFreep() {
        return setFreep;
    }

    public Set<Variable> getSetCombined() {
        return setCombined;
    }

    // Methods
    private void create() {
        // We create a domain in which we put all possible blocks
        Set<Object> domain = new HashSet<>();
        for (int block = 0; block < nbBlock; block++) {
            domain.add(block);
        }
        // We create all the piles (The BooleanVariable type means that the domain is assigned when the object is created)
        for (int pile = 1; pile <= nbPile; pile++) {
            domain.add(-pile);
            BooleanVariable freep = new BooleanVariable(String.valueOf(-pile)); // The freep variables will have a name in the form "-1"
            setFreep.add(freep);
        }
        // For each onB we create a subdomain (domain generated above without the block itself) and we create our fixedB using the BooleanVariable type 
        for (int block = 0; block < nbBlock; block++) {
            Set<Object> temp = new HashSet<>(domain);
            temp.remove(block);
            Variable onb = new Variable("b" + String.valueOf(block), temp); // The onb variables will have a name in the form "b0"
            BooleanVariable fixedb = new BooleanVariable(String.valueOf(block)); // The fixedb variables will have a name in the form "2"
            setOnb.add(onb);
            setFixedb.add(fixedb);
        }
        createSetCombined();
    }

    // Auxiliary method that allows us to create the combined set
    private void createSetCombined() {
        setCombined.addAll(setOnb);
        setCombined.addAll(setFixedb);
        setCombined.addAll(setFreep);
    }

    // Auxiliary method that allows us to retrieve the integer corresponding to the block from a string.
    // Example deleteB("b7") will return the integer 7 for block number 7
    // Should use hashcode instead of string parsing for better performance
    public static int deleteB(String string) {
        // For onB
        if(string.contains("b")){
            return Integer.valueOf(string.split("b")[1]);
        }
        return Integer.valueOf(string); // For fixedB and piles
    }

    @Override
    public String toString() {
        return "onb : \n" + setOnb + "\nfixedb : \n" + setFixedb + "\nfreep : \n" + setFreep;
    }
}
