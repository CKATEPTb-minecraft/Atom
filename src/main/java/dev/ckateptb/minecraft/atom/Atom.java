package dev.ckateptb.minecraft.atom;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import reactor.core.Disposable;
import reactor.core.scheduler.Scheduler;

import java.util.concurrent.atomic.AtomicReference;

public class Atom extends JavaPlugin {
    static AtomicReference<SyncScheduler> CACHED_SYNC = new AtomicReference<>();

    private static Atom plugin;

    public Atom() {
        Atom.plugin = this;
    }

    public static Scheduler syncScheduler() {
        SyncScheduler syncScheduler = CACHED_SYNC.get();
        if (syncScheduler != null) return syncScheduler;
        syncScheduler = new SyncScheduler();
        CACHED_SYNC.set(syncScheduler);
        return syncScheduler;
    }

    static class SyncScheduler implements Scheduler {
        @Override
        public @NotNull Disposable schedule(@NotNull Runnable task) {
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    task.run();
                }
            };

            if (Bukkit.isPrimaryThread()) runnable.run();
            else runnable.runTaskLater(Atom.plugin, 0);
            return new Disposable() {
                @Override
                public void dispose() {
                    runnable.cancel();
                }

                @Override
                public boolean isDisposed() {
                    return runnable.isCancelled();
                }
            };
        }

        @Override
        public @NotNull Worker createWorker() {
            return new Worker() {
                @Override
                public @NotNull Disposable schedule(@NotNull Runnable task) {
                    return SyncScheduler.this.schedule(task);
                }

                @Override
                public void dispose() {
                    SyncScheduler.this.dispose();
                }
            };
        }
    }
}