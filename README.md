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

`SpringExtension`: 

`SpringActorProducer`:
    
    

