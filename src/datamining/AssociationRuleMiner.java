package datamining;

import java.util.Set;

public interface AssociationRuleMiner {

    public BooleanDatabase getDataBase();

    public Set<AssociationRule> extract(float minFr, float minConf);

}
