package org.tiger.test.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tiger.test.actor.StarActor;
import org.tiger.test.actor.config.SpringExtension;
import scala.concurrent.duration.FiniteDuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by tiger on 16-4-19.
 */
@Service
public class StarApp {
    public static final FiniteDuration starBaseLifetime = FiniteDuration.apply(5000, TimeUnit.MILLISECONDS);
    public static final FiniteDuration starVariableLifetime = FiniteDuration.apply(2000, TimeUnit.MILLISECONDS);
    public static final FiniteDuration starBaseSpawntime = FiniteDuration.apply(2000, TimeUnit.MILLISECONDS);
    public static final FiniteDuration starVariableSpawntime = FiniteDuration.apply(1000, TimeUnit.MILLISECONDS);

    @Autowired
    private ActorSystem actorSystem;

    public void run() {
        List<String> names = new ArrayList<>();
        names.add("Bob");
        names.add("Alice");
        names.add("Rock");
        names.add("Paper");
        actorSystem.actorOf(SpringExtension.SpringExtProvider.get(actorSystem).props("NamerActor",names), "namer");

        ActorRef starActor1 = actorSystem.actorOf(SpringExtension.SpringExtProvider.get(actorSystem).props("StarActor","How are you doing ?", 1, "Nobody"), "star_actor1");
        ActorRef starActor2 = actorSystem.actorOf(SpringExtension.SpringExtProvider.get(actorSystem).props("StarActor","Happy to meet you", 1, "Nobody"), "star_actor2");
        try {
            Thread.sleep(500);
            starActor1.tell(new StarActor.Greet(starActor2), starActor1);
            starActor2.tell(new StarActor.Greet(starActor1), starActor2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
