package dev.ckateptb.minecraft.atom;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import reactor.core.Disposable;
import reactor.core.scheduler.Scheduler;

class SyncScheduler implements Scheduler {
    @Override
    public @NotNull Disposable schedule(@NotNull Runnable task) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        };

        if(Bukkit.isPrimaryThread()) runnable.run();
        else runnable.runTaskLater(Atom.getPlugin(), 0);
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
