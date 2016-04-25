package org.tiger.test.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;
import org.springframework.context.annotation.Scope;
import org.tiger.test.service.StarApp;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import scala.util.Random;

import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by tiger on 16-4-19.
 */
@Named("StarActor")
@Scope("prototype")
public class StarActor extends UntypedActor {
    final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final String greeting;
    private final String parent;
    private final int gennum;

    private StarActor(String greeting, int gennum, String parent) {
        this.greeting = greeting;
        this.gennum = gennum;
        this.parent = parent;
    }

    public static class Greet {
        private final ActorRef peer;

        public Greet(ActorRef peer) {
            this.peer = peer;
        }
    }

    public static class TellName {
        private final String name;

        public TellName(String name) {
            this.name = name;
        }
    }

    public static Object AskName = new Object();
    public static Object Spawn = new Object();
    public static Object IntroduceMe = new Object();
    public static Object Die = new Object();

    public static Props props(String greeting, int gennum, String parent) {
        return Props.create(StarActor.class, greeting, gennum, parent);
    }


    private String myName = "";
    private Map<String, ActorRef> starsKnown = new HashMap<>();
    private final Random random =new Random();
    private ActorSelection namer;
    private String namerPath = "/user/namer";

    @Override
    public void preStart() throws Exception {
        namer = context().actorSelection(namerPath);
        namer.tell(NamerActor.GetName, self());

        /**
         * schedule define . Look
         no further than ActorSystem! There you find the scheduler method that returns an instance of
         akka.actor.Scheduler, this instance is unique per ActorSystem and is used internally for scheduling things
         to happen at specific points in time
         */
        FiniteDuration killtime = scaledDuration(StarApp.starBaseLifetime, StarApp.starVariableLifetime);
        /**
         * after {@code killtime} send Die to getSelf
         */
        getContext().system().scheduler().scheduleOnce(killtime, getSelf(), Die, getContext().system().dispatcher(), null);
        FiniteDuration spawntime = scaledDuration(StarApp.starBaseSpawntime, StarApp.starVariableSpawntime);
        /**
         * after {@code spawntime}  repeating every 1 second
         */
        getContext().system().scheduler().schedule(spawntime, Duration.create(1, TimeUnit.SECONDS), getSelf(), Spawn, getContext().system().dispatcher(), null);
        if (gennum > 1) {
            getContext().system().scheduler().scheduleOnce(Duration.create(1, TimeUnit.SECONDS), getContext().parent(), IntroduceMe, getContext().system().dispatcher(), null);
        }
    }

    private FiniteDuration scaledDuration(FiniteDuration base, FiniteDuration variable) {
        return base.plus(variable.mul(random.nextInt(1000)).div(1000));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof NamerActor.SetName) {
            myName =  ((NamerActor.SetName) message).getName();
            log.info("{} is the {}th generation child of {}", myName, gennum, parent);
        }
        /**
         * hotswapping the Actor’s message loop (e.g. its implementation) at runtime
         * Warning: Please note that the actor will revert to its original behavior when restarted by its Supervisor.
         */
        getContext().become(named(myName));
    }

    private Procedure<Object> named(String myName) {
        return message -> {
            if (message instanceof Greet) {
                Greet greet = (Greet) message;
                getSelf().tell(AskName,greet.peer);
            }else if (message == AskName){
                getSender().tell(new TellName(myName),getSelf());
            }else if(message instanceof TellName){
                TellName tellName = (TellName) message;
                log.info("{} says : {} , {}",myName,greeting,tellName.name);
                starsKnown.put(tellName.name,getSender());
            }else if (message == Spawn){
                log.info("{} says: A star is born!",myName);
                getContext().actorOf(props(greeting,gennum+1,myName));
            }else if(message == IntroduceMe){
                starsKnown.forEach((key,value) -> {
                    log.info("key:{} , value:{}", key, value);
                    value.tell(new Greet(getSender()),getSelf());
                });
            }else if(message == Die){
                log.info("{} says: I`d like to thank the Academy...",myName);
                getContext().stop(getSelf());
            }
        };
    }


}
