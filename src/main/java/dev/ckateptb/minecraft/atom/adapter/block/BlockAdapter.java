package dev.ckateptb.minecraft.atom.adapter.block;

import dev.ckateptb.minecraft.atom.adapter.Adapter;
import dev.ckateptb.minecraft.atom.adapter.AdapterUtils;
import dev.ckateptb.minecraft.atom.chain.AtomChain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class BlockAdapter implements Block, Adapter<Block> {
    @Delegate(excludes = ExcludedMethods.class)
    private final Block handle_;

    @Override
    public @NotNull Block getRelative(int modX, int modY, int modZ) {
        return AdapterUtils.adapt(this.handle_.getRelative(modX, modY, modZ));
    }

    @Override
    public @NotNull Block getRelative(@NotNull BlockFace face) {
        return AdapterUtils.adapt(this.handle_.getRelative(face));
    }

    @Override
    public @NotNull Block getRelative(@NotNull BlockFace face, int distance) {
        return AdapterUtils.adapt(this.handle_.getRelative(face, distance));
    }

    @Override
    public @NotNull World getWorld() {
        return AdapterUtils.adapt(this.handle_.getWorld());
    }

    public @NotNull Chunk getChunk() {
        return AdapterUtils.adapt(this.handle_.getChunk());
    }

    @Override
    public void setBlockData(@NotNull BlockData data) {
        AtomChain.sync(this.handle_).run(block -> block.setBlockData(data));
    }

    @Override
    public void setBlockData(@NotNull BlockData data, boolean applyPhysics) {
        AtomChain.sync(this.handle_).run(block -> block.setBlockData(data, applyPhysics));
    }

    @Override
    public void setType(@NotNull Material type) {
        AtomChain.sync(this.handle_).run(block -> block.setType(type));
    }

    @Override
    public void setType(@NotNull Material type, boolean applyPhysics) {
        AtomChain.sync(this.handle_).run(block -> block.setType(type, applyPhysics));
    }

    public Location getLocation(@Nullable Location loc) {
        return AdapterUtils.adapt(this.handle_.getLocation(loc));
    }

    public @NotNull Location getLocation() {
        return AdapterUtils.adapt(this.handle_.getLocation());
    }

    public boolean equals(Object other) {
        if (other instanceof BlockAdapter adapter) other = adapter.handle_;
        return Objects.equals(this.handle_, other);
    }

    public int hashCode() {
        return this.handle_.hashCode();
    }

    @SuppressWarnings("all")
    private abstract static class ExcludedMethods {
        public abstract World getWorld();

        public abstract Chunk getChunk();

        public abstract Location getLocation(Location loc);

        public abstract Location getLocation();

        public abstract Block getRelative(BlockFace face);

        public abstract Block getRelative(BlockFace face, int distance);

        public abstract Block getRelative(final int modX, final int modY, final int modZ);

        public abstract void setBlockData(BlockData data);

        public abstract void setBlockData(BlockData data, boolean applyPhysics);

        public abstract void setType(Material type);

        public abstract void setType(Material type, boolean applyPhysics);

    }
}
