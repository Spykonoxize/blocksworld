package modelling;

import java.util.Arrays;
import java.util.HashSet;

public class BooleanVariable extends Variable {

    public HashSet<Boolean> domain;

    public BooleanVariable(String name) {
        super(name, new HashSet<>(Arrays.asList(true, false)));
    }
}
