package org.tiger.test.actor;

import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.springframework.context.annotation.Scope;
import org.tiger.test.service.StarApp;

import javax.inject.Named;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by tiger on 16-4-19.
 */

@Named("NamerActor")
@Scope("prototype")
public class NamerActor extends UntypedActor {
    final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final List<String> names;
    private int counter;

    private NamerActor(List<String> names) {
        this.names = names;
        log.info("namer actor started");
        getContext().setReceiveTimeout(StarApp.starBaseSpawntime.plus(StarApp.starVariableSpawntime));
        this.counter = new AtomicLong(names.size()).intValue();
    }

    public static Props props(List<String> names) {
        return Props.create(NamerActor.class, names);
    }

    public static Object GetName = new Object();

    public static class SetName {
        private final String name;

        public SetName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message == GetName) {
            String name = "other";
            if(counter>0) {
                name = names.get(counter - 1);
                counter = counter-1;
            }
            sender().tell(new SetName(name), getSelf());
        } else if (message instanceof ReceiveTimeout) {
            getContext().stop(getSelf());
        } else
            unhandled(message);
    }
}
