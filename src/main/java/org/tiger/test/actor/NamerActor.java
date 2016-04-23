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

/**
 * Created by tiger on 16-4-19.
 */

@Named("NamerActor")
@Scope("prototype")
public class NamerActor extends UntypedActor {
    final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final List<String> names;

    private NamerActor(List<String> names) {
        this.names = names;
        getContext().setReceiveTimeout(StarApp.starBaseSpawntime.plus(StarApp.starVariableSpawntime));
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
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message == GetName) {
            String name = "haha getName instance";
            sender().tell(new SetName(name), getSelf());
        } else if (message instanceof ReceiveTimeout) {
            getContext().stop(getSelf());
        } else
            unhandled(message);
    }
}
