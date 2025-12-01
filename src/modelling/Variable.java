package modelling;

import java.util.Objects;
import java.util.Set;

public class Variable {

    // Variables
    private String name;
    private Set<Object> domain;

    // Constructors
    public Variable(String name, Set<Object> domain) {
        this.name = name;
        this.domain = domain;
    }

    // Getters
    public String getName() {
        return name;
    }

    public Set<Object> getDomain() {
        return domain;
    }

    // Methods
    // Checks if two variables are equal by comparing their names
    @Override
    public boolean equals(Object x) {
        if (x instanceof Variable) {
            Variable object = (Variable) x;
            return (this.name.equals(object.name));
        }
        return false;
    }

    @Override
    // Return hash code based on the variable's name
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "(Name = " + name + ", domain = " + domain + ")";
    }
}