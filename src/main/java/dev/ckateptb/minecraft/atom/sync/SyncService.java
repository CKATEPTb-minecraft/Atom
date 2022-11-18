package dev.ckateptb.minecraft.atom.sync;

import dev.ckateptb.common.tableclothcontainer.annotation.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class SyncService {
    private final SyncHandler listener;

    public SyncService(SyncHandler listener) {
        this.listener = listener;
    }

    /**
     * Computes a result in server thread and wait response
     *
     * @param callable interface-function
     * @param <T>      return type
     * @return callable result after complete
     * @throws Exception any exception during callable
     */
    @Nullable
    public <T> T sync(@NotNull Callable<T> callable) throws Exception {
        return this.sync(callable, true);
    }

    /**
     * Computes a result in server thread
     *
     * @param callable interface-function
     * @param await if true then current thread will be frozen while server thread process callable
     * @param <T>      return type
     * @return callable result after complete
     * @throws Exception any exception during callable
     */
    public <T> T sync(@NotNull Callable<T> callable, boolean await) throws Exception {
        if (Bukkit.isPrimaryThread()) {
            return callable.call();
        }
        CountDownLatch latch = new CountDownLatch(await ? 1 : 0);
        AtomicReference<T> ref = new AtomicReference<>();
        listener.getThreadSafeSet().add(() -> {
            try {
                ref.set(callable.call());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            latch.countDown();
        });
        latch.await();
        return ref.get();
    }
}
