package model;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Configuration;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-10T12:26:38")
@StaticMetamodel(Score.class)
public class Score_ { 

    public static volatile SingularAttribute<Score, Integer> scoreId;
    public static volatile SingularAttribute<Score, Date> inittime;
    public static volatile SingularAttribute<Score, Configuration> configId;
    public static volatile SingularAttribute<Score, Date> endtime;
    public static volatile SingularAttribute<Score, BigDecimal> speed;

}