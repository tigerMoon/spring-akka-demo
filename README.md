# spring-boot-akka-demo



## Introduction

this is a spring boot project. build with maven. show a example of use akka with spring. and this demo is java version of [Building actor applications with Akka] (http://www.ibm.com/developerworks/library/j-jvmc6/index.html) from IBM developerWorks.

 
## How to run it

mvn spring-boot:run

## Before start it

I assume that you have the base knowledge of:

- actorSystem
- how to create actor 
- akka extension
- Dependency injection  

## Why we need to use Dependency Injection?
In a spring system . when we want to use actorSystem. so here is the problem.
If your UntypedActor has a constructor that takes parameters then those need to be part of the Props as well.
But there are cases when a factory method must be used, for example when the actual constructor arguments are determined by a dependency injection framework.

## How it works
At first. the spring load actorSystem ,which we config it when spring start. set applicationContext to springExtension, then we use SpringExtension to get the reference of applicationContext. 

Second. we use SpringExtension build Props(which use to create actor). 

Third step. Props use SpringActorProducer create actor.

## Exploring the Code

`AkkaConfig` : register Actor system singleton for this application.

`SpringExtension`: Extensions will only be loaded once per ActorSystem, which will be managed by Akka. You can choose to have your Extension loaded on-demand or 
at ActorSystem creation time through the Akka configuration.and is comprised of 2 basic components: an Extension and an ExtensionId. (Warning: Since an extension is a way to hook into Akka itself, the implementor of the extension needs to
ensure the thread safety of his/her extension.)

`SpringActorProducer`: implements from `IndirectActorProducer` , which use to defines a class of actor creation strategies deviating from the usual default of just reflectively instantiating the [[Actor]]
subclass. It can be used to allow a dependency injection framework to determine the actual actor class and how it shall be instantiated. so you can build several producer for your own use
    
` **Actor` : `@Scope("prototype")` must use this. by default, spring use singleton. but when actor restart see [ What Restarting Means](http://doc.akka.io/docs/akka/2.4.0/general/supervision.html#supervision-restart)
   
## Conclusions

Akka actor devote itself to write correct distributed, concurrent, fault-tolerant and scalable applications easily. and i think it`s more useful to the application with more
interactions. Akka isolation application from **rate condition** and **synchronized** code and has a integrity fault-tolerant strategies.(it is a highly abstraction)    

## reference

1. http://www.lightbend.com/activator/template/akka-java-spring#code/src/test/java/sample/SpringTest.java 
2. http://doc.akka.io/docs/akka/2.4.0/java/untyped-actors.html?_ga=1.93414229.97008230.1461550105#Dependency_Injection
3. http://doc.akka.io/docs/akka/2.4.4/java/extending-akka.html   
   