package cp;

import java.util.Map;
import modelling.Variable;

public interface Solver {
    public Map<Variable, Object> solve();
}
