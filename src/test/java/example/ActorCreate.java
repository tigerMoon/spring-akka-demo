package example;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by tiger on 15-12-23.
 */
public class ActorCreate {
    static Config config = ConfigFactory.parseString("akka.loglevel = DEBUG \n" +
            "akka.actor.debug.lifecycle = on");
    public static final ActorSystem system = ActorSystem.create("actor-demo-java",config);
}
