package dev.ckateptb.minecraft.atom.async;

import dev.ckateptb.common.tableclothcontainer.annotation.Component;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import oshi.annotation.concurrent.ThreadSafe;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AsyncService {
    private final EntityLookupHandler entityLookupHandler;

    public AsyncService(EntityLookupHandler entityLookupHandler) {
        this.entityLookupHandler = entityLookupHandler;
    }

    /**
     * Returns a list of entities within a bounding box centered around a Location.
     * <p>
     * Some implementations may impose artificial restrictions on the size of the search bounding box.
     *
     * @param x 1/2 the size of the box along x axis
     * @param y 1/2 the size of the box along y axis
     * @param z 1/2 the size of the box along z axis
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    @ThreadSafe
    public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
        if (Bukkit.isPrimaryThread()) return location.getNearbyEntities(x, y, z);
        if (location == null || !location.isWorldLoaded()) return Collections.emptySet();
        World world = location.getWorld();
        BoundingBox aabb = BoundingBox.of(location, x, y, z);
        return entityLookupHandler.getEntities(world).stream()
                .parallel()
                .filter(entity -> aabb.contains(entity.getBoundingBox()))
                .collect(Collectors.toSet());
    }

    /**
     * Get a list of all entities
     *
     * @return A List of all Entities
     */
    @ThreadSafe
    public Set<Entity> getEntities() {
        return entityLookupHandler.getEntities();
    }

    /**
     * Get a list of all entities in specified World
     *
     * @param world World
     * @return A List of all Entities currently residing in specified world
     */
    @ThreadSafe
    public Set<Entity> getEntities(World world) {
        return entityLookupHandler.getEntities(world);
    }
}