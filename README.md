<p align="center">
<h3 align="center">Atom</h3>

------

<p align="center">
A set of tools that simplify asynchronous development.
</p>

<p align="center">
<img alt="License" src="https://img.shields.io/github/license/CKATEPTb-minecraft/Atom">
<a href="#Download"><img alt="Sonatype Nexus (Snapshots)" src="https://img.shields.io/nexus/s/dev.ckateptb.minecraft/Atom?label=repo&server=https://repo.animecraft.fun/"></a>
<img alt="Publish" src="https://img.shields.io/github/workflow/status/CKATEPTb-minecraft/Atom/Publish/production">
<a href="https://docs.gradle.org/7.5/release-notes.html"><img src="https://img.shields.io/badge/Gradle-7.5-brightgreen.svg?colorB=469C00&logo=gradle"></a>
<a href="https://discord.gg/P7FaqjcATp" target="_blank"><img alt="Discord" src="https://img.shields.io/discord/925686623222505482?label=discord"></a>
</p>

------

# Versioning

We use [Semantic Versioning 2.0.0](https://semver.org/spec/v2.0.0.html) to manage our releases.

# Features

- [X] Easy to use
- [X] Include [reactor core](https://github.com/reactor/reactor-core)
- [X] ServerThread sync
- [X] ThreadSafe implementation [part of BukkitAPI](https://github.com/CKATEPTb-minecraft/Atom/tree/development/src/main/java/dev/ckateptb/minecraft/atom/adapter)
- [X] Chain-based wrappers for sync part of code with ServerThread

# Download

Download from our repository or depend via Gradle:

```kotlin
repositories {
    maven("https://repo.animecraft.fun/repository/maven-snapshots/")
}
dependencies {
    implementation("dev.ckateptb.minecraft:Atom:<version>")
}
```

# How To

* Import the dependency [as shown above](#Download)
* Add Atom as a dependency to your `plugin.yml`
```yaml
name: ...
version: ...
main: ...
depend: [ Atom ]
authors: ...
description: ...
```
* Run tasks in async thread
```java
public class PluginExample extends JavaPlugin {
    public PluginExample() {
        Schedulers.boundedElastic().schedule(() -> {
            // The common boundedElastic instance, a Scheduler that dynamically
            // creates a bounded number of ExecutorService-based
            // Workers, reusing them once the Workers have been
            // shut down. The underlying daemon threads can be
            // evicted if idle for more than 60 seconds.
        });
        Schedulers.single().schedule(() -> {
            // The common single instance, a Scheduler that hosts a 
            // single-threaded ExecutorService-based worker.
            // Only one instance of this common scheduler will be created
            // on the first call and is cached. The same instance is returned
            // on subsequent calls until it is disposed.
        });
        Schedulers.immediate().schedule(() -> {
            // Executes tasks immediately instead of scheduling them.
        });
        Schedulers.parallel().schedule(() -> {
            // The common parallel instance, a Scheduler that hosts
            // a fixed pool of single-threaded ExecutorService-based
            // workers and is suited for parallel work.
            // Only one instance of this common scheduler will be created
            // on the first call and is cached. The same instance is returned
            // on subsequent calls until it is disposed.
        });
        Atom.syncScheduler().schedule(() -> {
            // The bukkit tick thread instance, a Scheduler that 
            // can be used for sync sth with main server thread.
        });
    }
}
```
* Work with reactive stream [more info](https://www.infoq.com/articles/reactor-by-example/)
```java
import dev.ckateptb.common.tableclothcontainer.IoC;
import dev.ckateptb.minecraft.atom.adapter.AdapterUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Collection;
import java.util.Comparator;

public class PluginExample extends JavaPlugin {
    public PluginExample() {
        Location location = ...;
        Mono.just(location)
                .publishOn(Atom.syncScheduler()) // Switch to main-thread
                .flatMapMany(value -> Flux.fromIterable(value.getNearbyEntities(20, 20, 20))) // Call AsyncCatchOp method 
                .publishOn(Schedulers.boundedElastic()) // Switch to other thread
                .filter(entity -> entity instanceof LivingEntity) // filter entity is living
                .sort(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(location))) // sort by distance
                .doOnNext(entity -> System.out.println("Wow, one more living entity!"))
                .doOnSubscribe(subscription -> subscription.request(20)) // request 20 entity
                .subscribe(living -> /*to do*/); // subscribe reactive stream
    }
}
```
* Work with block
```java
public class ThreadSafeBlockExample {
    public void example() {
        // Pseudo async block placement example with queue
        Schedulers.boundedElastic().schedule(() -> // Async thread 
                Flux.fromIterable(blocks) // Flux from blocks iterator
                        // Declare queue 1 bpms = 50 bpt = 1000 bps
                        .concatMap(block -> Mono.just(block).delayElement(Duration.of(1, ChronoUnit.MILLIS)))
                        .publishOn(Atom.syncScheduler()) // Switch to main-thread
                        .subscribe(block -> block.setType(Material.AIR, false))); // do sth
    }
}
```
* Start work