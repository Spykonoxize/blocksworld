package blocksworld.planning;

import planning.*;
import java.util.*;
import modelling.*;

public class H1 implements Heuristic{

    private Map<Variable, Object> but;

    public H1(Map<Variable, Object> but){
        this.but = but;
    }

    @Override
    public float estimate(Map<Variable, Object> etat){
        float res = 0;
        for(Map.Entry<Variable, Object> entry : but.entrySet()){
            if(etat.get(entry.getKey()) != entry.getValue()){
                res += 1;
            }
        }
        return res;
    }

}