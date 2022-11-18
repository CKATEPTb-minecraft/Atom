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
- [X] ThreadSafe implementation [part of BukkitAPI](https://github.com/CKATEPTb-minecraft/Atom/tree/development/src/main/java/dev/ckateptb/minecraft/atom/async/AsyncService.java)
- [X] Chain-based wrappers for sync part of code with ServerThread
- [ ] Documented

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
import dev.ckateptb.common.tableclothcontainer.IoC;
import org.bukkit.plugin.java.JavaPlugin;
import reactor.core.scheduler.Schedulers;

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
    }
}
```
* Work with reactive stream [more info](https://www.infoq.com/articles/reactor-by-example/)
```java
import dev.ckateptb.common.tableclothcontainer.IoC;
import dev.ckateptb.minecraft.atom.async.AsyncService;
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
        Schedulers.single().schedule(() -> { // run our code in single async thread
            AsyncService asyncService = IoC.getBean(AsyncService.class); // get AsyncService instance
            Collection<Entity> entities = asyncService.getNearbyEntities(location, 20, 20, 20); // thread safe get Nearby Entities
            Flux.fromIterable(entities) // Create a reactive stream data from entities
                    .parallel(entities.size()) // Each entity will be processed in a separate thread
                    .runOn(Schedulers.boundedElastic()) // We indicate that we want to process each entity in reusable threads
                    .filter(entity -> entity instanceof LivingEntity) // filter entity is living
                    .sorted(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(location))) // sort by distance
                    .doOnNext(entity -> System.out.println("Wow, one more living entity!"))
                    .doOnSubscribe(subscription -> subscription.request(20)) // request 20 entity
                    .subscribe(); // subscribe reactive stream

        });
    }
}
```
* Work with AtomChain
```java
import dev.ckateptb.minecraft.atom.chain.AtomChain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import reactor.util.function.Tuple2;

import java.util.function.Consumer;

public class ChainExample {
    public ChainExample(Consumer<Player> playerConsumer) {
        Player original = null;
        AtomChain.of(original)
                .run(player -> player.sendMessage("Wow, deal message from current thread")) // Send message to player, from current thread
                .zipWith(Bukkit.isPrimaryThread()) // Add data (current thread is server thread) to chain
                .run(tuple -> { // Run in current thread
                    Player player = tuple.getT1();
                    boolean isServerThread = tuple.getT2();
                    if(isServerThread) {
                        player.damage(5); // we can deal damage only in server thread
                    } else {
                        player.setVelocity(Vector.getRandom()); // we can apply velocity in any thread
                    }
                })
                .map(Tuple2::getT1) // remove attached data from chain
                .sync() // switch to server thread scheduler
                .runAndNoWait(player -> player.damage(5)) // deal damage without freeze current thread
                .rude() // switch to mixed thread (process in current thread if allowed else in server thread)
                .run(playerConsumer) // We do not know what is in this Consumer, so we reproduce it in a rude stream
                .immediate() // switch to current thread
                .run(player -> player.sendMessage("Wow, deal message from current thread again"));
    }
}

```
* Start work