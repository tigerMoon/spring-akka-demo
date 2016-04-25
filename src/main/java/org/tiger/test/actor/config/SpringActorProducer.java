package org.tiger.test.actor.config;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import org.springframework.context.ApplicationContext;

/**
 * Created by tiger on 16-2-24.
 */

/**
 * An actor producer that lets Spring create the Actor instances.
 */
public class SpringActorProducer implements IndirectActorProducer {
    final ApplicationContext applicationContext;
    final String actorBeanName;
    final Object[] constructArgs;

    public SpringActorProducer(ApplicationContext applicationContext,
                               String actorBeanName, Object[] constructArgs) {
        this.applicationContext = applicationContext;
        this.actorBeanName = actorBeanName;
        this.constructArgs = constructArgs;
    }

    @Override
    public Actor produce() {
        return (Actor) applicationContext.getBean(actorBeanName,constructArgs);
    }

    @Override
    public Class<? extends Actor> actorClass() {
        return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
    }
}
