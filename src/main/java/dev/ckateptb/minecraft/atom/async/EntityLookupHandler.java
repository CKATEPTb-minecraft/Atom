package dev.ckateptb.minecraft.atom.async;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import dev.ckateptb.common.tableclothcontainer.annotation.Component;
import lombok.NoArgsConstructor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
class EntityLookupHandler implements Listener {
    private final Map<UUID, Set<Entity>> entities = new ConcurrentHashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void on(EntityAddToWorldEvent event) {
        Entity entity = event.getEntity();
        UUID uid = entity.getWorld().getUID();
        entities.computeIfAbsent(uid, key -> ConcurrentHashMap.newKeySet()).add(entity);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void on(EntityRemoveFromWorldEvent event) {
        Entity entity = event.getEntity();
        UUID uid = entity.getWorld().getUID();
        if (entities.containsKey(uid)) {
            Set<Entity> worldEntities = entities.get(uid);
            if (worldEntities.remove(entity) && worldEntities.size() == 0) {
                entities.remove(uid);
            }
        }
    }

    public Set<Entity> getEntities() {
        return entities.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Set<Entity> getEntities(World world) {
        return Collections.unmodifiableSet(entities.getOrDefault(world.getUID(), Collections.emptySet()));
    }
}
