package modelling;

import java.util.Objects;
import java.util.Set;

public class Variable {

    // Variables
    private String name;
    private Set<Object> domain;

    // Constructeurs
    public Variable(String name, Set<Object> domain) {
        this.name = name;
        this.domain = domain;
    }

    // Accesseurs
    public String getName() {
        return name;
    }

    public Set<Object> getDomain() {
        return domain;
    }

    // Méthodes
    // Vérifie si deux variables sont égales en comparant le nom et le domaine
    @Override
    public boolean equals(Object x) {
        if (x instanceof Variable) {
            Variable object = (Variable) x;
            return (this.name.equals(object.name));
        }
        return false;
    }

    @Override
    // Retourne une valeur de hachage pour l'objet
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "(Name = " + name + ", domain = " + domain + ")";
    }
}