package dev.ckateptb.minecraft.atom.sync;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import dev.ckateptb.common.tableclothcontainer.annotation.Component;
import dev.ckateptb.minecraft.nicotine.annotation.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
@NoArgsConstructor
class SyncHandler implements Listener {
    private final Set<Runnable> threadSafeSet = ConcurrentHashMap.newKeySet();

    @EventHandler
    public void on(ServerTickEndEvent event) {
        this.syncTasks();
    }


    @EventHandler
    public void on(ServerTickStartEvent event) {
        this.syncTasks();
    }

    @Schedule(fixedRate = 0, initialDelay = 0)
    public void on() {
        this.syncTasks();
    }

    private void syncTasks() {
        final Iterator<Runnable> iterator = threadSafeSet.iterator();
        while (iterator.hasNext()) {
            try {
                iterator.next().run();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            iterator.remove();
        }
    }
}
