package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Score;
import model.Users;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-10T12:26:38")
@StaticMetamodel(Configuration.class)
public class Configuration_ { 

    public static volatile SingularAttribute<Configuration, String> configName;
    public static volatile SingularAttribute<Configuration, Integer> difficulty;
    public static volatile SingularAttribute<Configuration, Integer> spaceship;
    public static volatile ListAttribute<Configuration, Score> scoreList;
    public static volatile SingularAttribute<Configuration, Integer> moon;
    public static volatile SingularAttribute<Configuration, Integer> configId;
    public static volatile SingularAttribute<Configuration, Users> userId;

}