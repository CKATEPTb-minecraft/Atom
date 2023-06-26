package dev.ckateptb.minecraft.atom.adapter.world;

import dev.ckateptb.minecraft.atom.adapter.Adapter;
import dev.ckateptb.minecraft.atom.adapter.AdapterUtils;
import dev.ckateptb.minecraft.atom.async.EntityLookupHandler;
import dev.ckateptb.minecraft.atom.chain.AtomChain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
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
    public boolean regenerateChunk(int x, int z) {
        return AtomChain.sync(this.handle_)
                .map(world -> world.regenerateChunk(x, z))
                .get();
    }

    public boolean loadChunk(int x, int z, boolean generate) {
        return AtomChain.sync(this.handle_)
                .map(world -> world.loadChunk(x, z, generate))
                .get();
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox, Predicate<Entity> filter) {
        if (Bukkit.isPrimaryThread()) return this.handle_.getNearbyEntities(boundingBox, filter);
        return EntityLookupHandler.getInstance().getEntities(this.handle_).stream()
                .parallel()
                .filter(entity -> boundingBox.contains(entity.getBoundingBox())
                        && (filter == null || filter.test(entity)))
                .map(AdapterUtils::adapt)
                .collect(Collectors.toSet());
    }

    @Override
    public @NotNull Block getBlockAt(int x, int y, int z) {
        return AdapterUtils.adapt(this.handle_.getBlockAt(x, y, z));
    }

    public @NotNull Chunk getChunkAt(int x, int z) {
        return AdapterUtils.adapt(this.handle_.getChunkAt(x, z));
    }

    @Override
    public void save() {
        AtomChain.sync(this.handle_)
                .run(World::save);
    }

    private abstract static class ExcludedMethods {
        public abstract boolean unloadChunk(int x, int z, boolean save);

        public abstract boolean unloadChunkRequest(int x, int z);

        public abstract boolean regenerateChunk(int x, int z);

        public abstract boolean loadChunk(int x, int z, boolean generate);

        public abstract Collection<Entity> getNearbyEntities(BoundingBox boundingBox, Predicate<Entity> filter);

        public abstract Block getBlockAt(int x, int y, int z);

        public abstract Chunk getChunkAt(int x, int z);

        public abstract void save();
    }
}
