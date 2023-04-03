package dev.ckateptb.minecraft.atom;

import dev.ckateptb.common.tableclothcontainer.IoC;
import dev.ckateptb.minecraft.atom.scheduler.SyncScheduler;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import reactor.core.scheduler.Scheduler;

public class Atom extends JavaPlugin {
    @Getter
    private static Atom plugin;

    public Atom() {
        plugin = this;
        IoC.scan(Atom.class, s -> !s.contains("internal"));
        IoC.registerBean(this);
    }

    public static Scheduler syncScheduler() {
        return new SyncScheduler();
    }
}