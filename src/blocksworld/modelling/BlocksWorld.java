package blocksworld.modelling;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import modelling.BooleanVariable;
import modelling.Variable;

public class BlocksWorld {

    // Variables
    private int nbBlock; // Nombre de bloc dans le monde
    private int nbPile; // Nombre de pile dans le monde
    private Set<Variable> setOnb = new HashSet<>(); // L'ensemble des onB
    private Set<BooleanVariable> setFixedb = new HashSet<>(); // L'ensemble des fixedB
    private Set<BooleanVariable> setFreep = new HashSet<>(); // L'ensemble des freeP
    private Set<Variable> setCombined = new HashSet<>(); // L'nesemble contenant toutes les variables du monde

    public static final Comparator<Variable> COMPARATOR = (var1, var2) -> var1.getName().compareTo(var2.getName());

    // Constructeurs
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

    // Méthodes
    private void create() {
        // On crée un domaine dans lequel on y met tout les blocs possible
        Set<Object> domain = new HashSet<>();
        for (int block = 0; block < nbBlock; block++) {
            domain.add(block);
        }
        // On crée toutes les piles (Le type BooleanVariable fait que le domaine est attribué à la création de l'objet)
        for (int pile = 1; pile <= nbPile; pile++) {
            domain.add(-pile);
            BooleanVariable freep = new BooleanVariable(String.valueOf(-pile)); // Les variables freep auront un nom sous la forme "-1"
            setFreep.add(freep);
        }
        // Pour chaque onB on crée un sous domaine (domaine générer au-dessus privé du bloc lui-même)
        // Et on crée nos fixedB en utilisant le type BooleanVariable 
        for (int block = 0; block < nbBlock; block++) {
            Set<Object> temp = new HashSet<>(domain);
            temp.remove(block);
            Variable onb = new Variable("b" + String.valueOf(block), temp); // Les variables onb auront un nom sous la forme "b0"
            BooleanVariable fixedb = new BooleanVariable(String.valueOf(block)); // Les variables fixedb auront un nom sous la forme "2"
            setOnb.add(onb);
            setFixedb.add(fixedb);
        }
        createSetCombined();
    }

    // Méthodes qui crée un ensemble contenant toutes les varaibles
    private void createSetCombined() {
        setCombined.addAll(setOnb);
        setCombined.addAll(setFixedb);
        setCombined.addAll(setFreep);
    }

    // Méthode auxiliaire nous permetant de récupérer l'entier auquel correspond le bloc via un string.
    // Exemple deleteB("b7") retorunera l'entier 7 pour le bloc n°7
    // On avait oublié la méthode hashCode()
    public static int deleteB(String string) {
        // Pour les onB
        if(string.contains("b")){
            return Integer.valueOf(string.split("b")[1]);
        }
        return Integer.valueOf(string); // Pour les fixedB et les piles
    }

    @Override
    public String toString() {
        return "onb : \n" + setOnb + "\nfixedb : \n" + setFixedb + "\nfreep : \n" + setFreep;
    }
}
