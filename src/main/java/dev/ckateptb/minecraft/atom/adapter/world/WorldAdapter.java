package dev.ckateptb.minecraft.atom.adapter.world;

import dev.ckateptb.minecraft.atom.adapter.Adapter;
import dev.ckateptb.minecraft.atom.adapter.AdapterUtils;
import dev.ckateptb.minecraft.atom.async.EntityLookupHandler;
import dev.ckateptb.minecraft.atom.chain.AtomChain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class WorldAdapter implements World, Adapter<World> {
    @Delegate(excludes = ExcludedMethods.class)
    private final World handle_;

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        return AtomChain.sync(this.handle_)
                .map(world -> world.unloadChunk(x, z, save))
                .get();
    }

    @Override
    public boolean unloadChunkRequest(int x, int z) {
        return AtomChain.sync(this.handle_)
                .map(world -> world.unloadChunkRequest(x, z))
                .get();
    }

    @Override
    @SuppressWarnings("all")
    public boolean regenerateChunk(int x, int z) {
        return AtomChain.sync(this.handle_)
                .map(world -> world.regenerateChunk(x, z))
                .get();
    }

    @Override
    public @NotNull Collection<Chunk> getForceLoadedChunks() {
        return this.handle_.getForceLoadedChunks().stream().map(AdapterUtils::adapt).collect(Collectors.toList());
    }

    @Override
    public @NotNull Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        Map<Plugin, Collection<Chunk>> map = new HashMap<>();
        this.handle_.getPluginChunkTickets().forEach((key, value) -> {
            map.put(key, value.stream().map(AdapterUtils::adapt).collect(Collectors.toList()));
        });
        return map;
    }

    @Override
    public @Nullable Location findLightningRod(@NotNull Location location) {
        return AdapterUtils.adapt(this.handle_.findLightningRod(location));
    }

    @Override
    public @Nullable Location findLightningTarget(@NotNull Location location) {
        return AdapterUtils.adapt(this.handle_.findLightningTarget(location));
    }

    @Override
    public @NotNull List<Entity> getEntities() {
        return this.handle_.getEntities().stream().map(AdapterUtils::adapt).collect(Collectors.toList());
    }

    @Override
    public @NotNull List<LivingEntity> getLivingEntities() {
        return this.handle_.getLivingEntities().stream().map(AdapterUtils::adapt).collect(Collectors.toList());
    }

    @Override
    public @NotNull Collection<Entity> getEntitiesByClasses(@NotNull Class<?>... classes) {
        return this.handle_.getEntitiesByClasses(classes).stream().map(AdapterUtils::adapt).collect(Collectors.toList());
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z) {
        return this.getNearbyEntities(location, x, y, z, null);
    }

    @Override
    public @Nullable Entity getEntity(@NotNull UUID uuid) {
        return AdapterUtils.adapt(this.handle_.getEntity(uuid));
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z, @Nullable Predicate<Entity> filter) {
        BoundingBox aabb = BoundingBox.of(location, x, y, z);
        return this.getNearbyEntities(aabb, filter);
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox) {
        return this.getNearbyEntities(boundingBox, null);
    }

    public boolean loadChunk(int x, int z, boolean generate) {
        return AtomChain.sync(this.handle_)
                .map(world -> world.loadChunk(x, z, generate))
                .get();
    }

    @Override
    public boolean unloadChunk(@NotNull Chunk chunk) {
        return AtomChain.sync(this.handle_)
                .map(world -> world.unloadChunk(chunk))
                .get();
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        return AtomChain.sync(this.handle_)
                .map(world -> world.unloadChunk(x, z))
                .get();
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox, Predicate<Entity> filter) {
//        if (Bukkit.isPrimaryThread()) return this.handle_.getNearbyEntities(boundingBox, filter);
        return EntityLookupHandler.getInstance().getEntities(this.handle_).stream()
                .parallel()
                .filter(entity -> boundingBox.contains(entity.getBoundingBox())
                        && (filter == null || filter.test(entity)))
                .map(AdapterUtils::adapt)
                .collect(Collectors.toSet());
    }

    @Override
    public @NotNull Location getSpawnLocation() {
        return AdapterUtils.adapt(this.handle_.getSpawnLocation());
    }

    @Override
    public @NotNull Block getBlockAt(int x, int y, int z) {
        return AdapterUtils.adapt(this.handle_.getBlockAt(x, y, z));
    }

    @Override
    public @NotNull Block getBlockAt(@NotNull Location location) {
        return AdapterUtils.adapt(this.handle_.getBlockAt(location));
    }

    @Override
    public @NotNull Block getHighestBlockAt(int x, int z) {
        return AdapterUtils.adapt(this.handle_.getHighestBlockAt(x, z));
    }

    @Override
    public @NotNull Block getHighestBlockAt(@NotNull Location location) {
        return AdapterUtils.adapt(this.handle_.getHighestBlockAt(location));
    }

    @Override
    public @NotNull Block getHighestBlockAt(int x, int z, @NotNull HeightMap heightMap) {
        return AdapterUtils.adapt(this.handle_.getHighestBlockAt(x, z, heightMap));
    }

    @Override
    public @NotNull Block getHighestBlockAt(@NotNull Location location, @NotNull HeightMap heightMap) {
        return AdapterUtils.adapt(this.handle_.getHighestBlockAt(location, heightMap));
    }

    public @NotNull Chunk getChunkAt(int x, int z) {
        return AdapterUtils.adapt(this.handle_.getChunkAt(x, z));
    }

    @Override
    public @NotNull Chunk getChunkAt(@NotNull Location location) {
        return AdapterUtils.adapt(this.handle_.getChunkAt(location));
    }

    @Override
    public @NotNull Chunk getChunkAt(@NotNull Block block) {
        return AdapterUtils.adapt(this.handle_.getChunkAt(block));
    }

    @Override
    public @NotNull Chunk[] getLoadedChunks() {
        return Arrays.stream(this.handle_.getLoadedChunks()).map(AdapterUtils::adapt).toArray(Chunk[]::new);
    }

    @Override
    public void loadChunk(@NotNull Chunk chunk) {
        AtomChain.sync(this.handle_)
                .run(world -> world.loadChunk(chunk));
    }

    @Override
    public void loadChunk(int x, int z) {
        AtomChain.sync(this.handle_)
                .run(world -> world.loadChunk(x, z));
    }

    @Override
    public void save() {
        AtomChain.sync(this.handle_)
                .run(World::save);
    }

    @Override
    @SuppressWarnings("all")
    public @Nullable Location locateNearestStructure(@NotNull Location origin, @NotNull StructureType structureType, int radius, boolean findUnexplored) {
        return AdapterUtils.adapt(this.handle_.locateNearestStructure(origin, structureType, radius, findUnexplored));
    }

    @Override
    public @Nullable Location locateNearestBiome(@NotNull Location origin, @NotNull Biome biome, int radius) {
        return AdapterUtils.adapt(this.handle_.locateNearestBiome(origin, biome, radius));
    }

    @Override
    public @Nullable Location locateNearestBiome(@NotNull Location origin, @NotNull Biome biome, int radius, int step) {
        return AdapterUtils.adapt(this.handle_.locateNearestBiome(origin, biome, radius, step));
    }

    public boolean equals(Object other) {
        if (other instanceof WorldAdapter adapter) other = adapter.handle_;
        return Objects.equals(this.handle_, other);
    }

    public int hashCode() {
        return this.handle_.hashCode();
    }

    @SuppressWarnings("all")
    private abstract static class ExcludedMethods {
        public abstract boolean unloadChunk(Chunk chunk);

        public abstract boolean unloadChunk(int x, int z);

        public abstract boolean unloadChunk(int x, int z, boolean save);

        public abstract boolean unloadChunkRequest(int x, int z);

        public abstract boolean regenerateChunk(int x, int z);

        public abstract void loadChunk(Chunk chunk);

        public abstract void loadChunk(int x, int z);

        public abstract boolean loadChunk(int x, int z, boolean generate);

        public abstract Block getBlockAt(int x, int y, int z);

        public abstract Block getBlockAt(Location location);

        public abstract Block getBlockAtKey(long key);

        public abstract Location getLocationAtKey(long key);

        public abstract Block getHighestBlockAt(int x, int z);

        public abstract Block getHighestBlockAt(Location location);

        public abstract Block getHighestBlockAt(int x, int z, com.destroystokyo.paper.HeightmapType heightmap) throws UnsupportedOperationException;

        public abstract Block getHighestBlockAt(Location location, com.destroystokyo.paper.HeightmapType heightmap) throws UnsupportedOperationException;

        public abstract Block getHighestBlockAt(int x, int z, HeightMap heightMap);

        public abstract Block getHighestBlockAt(Location location, HeightMap heightMap);

        public abstract Chunk getChunkAt(int x, int z);

        public abstract Chunk getChunkAt(Location location);

        public abstract Chunk getChunkAt(Block block);

        public abstract Chunk getChunkAt(long chunkKey);

        public abstract Chunk[] getLoadedChunks();

        public abstract Collection<Chunk> getForceLoadedChunks();

        public abstract Map<Plugin, Collection<Chunk>> getPluginChunkTickets();

        public abstract Location findLightningRod(Location location);

        public abstract Location findLightningTarget(Location location);

        public abstract Collection<LivingEntity> getNearbyLivingEntities(Location loc, double radius);

        public abstract Collection<LivingEntity> getNearbyLivingEntities(Location loc, double xzRadius, double yRadius);

        public abstract Collection<LivingEntity> getNearbyLivingEntities(Location loc, double xRadius, double yRadius, double zRadius);

        public abstract Collection<LivingEntity> getNearbyLivingEntities(Location loc, double radius, Predicate<LivingEntity> predicate);

        public abstract Collection<LivingEntity> getNearbyLivingEntities(Location loc, double xzRadius, double yRadius, Predicate<LivingEntity> predicate);

        public abstract Collection<LivingEntity> getNearbyLivingEntities(Location loc, double xRadius, double yRadius, double zRadius, Predicate<LivingEntity> predicate);

        public abstract Collection<Player> getNearbyPlayers(Location loc, double radius);

        public abstract Collection<Player> getNearbyPlayers(Location loc, double xzRadius, double yRadius);

        public abstract Collection<Player> getNearbyPlayers(Location loc, double xRadius, double yRadius, double zRadius);

        public abstract Collection<Player> getNearbyPlayers(Location loc, double radius, Predicate<Player> predicate);

        public abstract Collection<Player> getNearbyPlayers(Location loc, double xzRadius, double yRadius, Predicate<Player> predicate);

        public abstract Collection<Player> getNearbyPlayers(Location loc, double xRadius, double yRadius, double zRadius, Predicate<Player> predicate);

        public abstract Collection<Entity> getNearbyEntities(Location location, double x, double y, double z);

        public abstract Entity getEntity(java.util.UUID uuid);

        public abstract Collection<Entity> getNearbyEntities(Location location, double x, double y, double z, Predicate<Entity> filter);

        public abstract Collection<Entity> getNearbyEntities(BoundingBox boundingBox);

        public abstract Collection<Entity> getNearbyEntities(BoundingBox boundingBox, Predicate<Entity> filter);

        public abstract Location getSpawnLocation();

        public abstract Location locateNearestStructure(Location origin, org.bukkit.StructureType structureType, int radius, boolean findUnexplored);

        public abstract Location locateNearestBiome(Location origin, Biome biome, int radius);

        public abstract Location locateNearestBiome(Location origin, Biome biome, int radius, int step);

        public abstract void save();

        public abstract List<Entity> getEntities();

        public abstract List<LivingEntity> getLivingEntities();

        public abstract Collection<Entity> getEntitiesByClasses(Class<?>... classes);
    }
}
