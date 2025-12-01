package cp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import modelling.Variable;

public class RandomValueHeuristic implements ValueHeuristic {

    private Random r;

    public RandomValueHeuristic(Random r) {
        this.r = r;
    }

    @Override
    public List<Object> ordering(Variable variable, Set<Object> domain) {
        List<Object> list = new ArrayList<>(domain);
        Collections.shuffle(list, r);
        return list;
    }
}