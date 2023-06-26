package dev.ckateptb.minecraft.atom.adapter.chunk;

import dev.ckateptb.minecraft.atom.adapter.Adapter;
import dev.ckateptb.minecraft.atom.adapter.AdapterUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class ChunkAdapter implements Chunk, Adapter<Chunk> {
    @Delegate(excludes = ExcludedMethods.class)
    private final Chunk handle_;

    @Override
    public @NotNull World getWorld() {
        return AdapterUtils.adapt(this.handle_.getWorld());
    }

    @Override
    public @NotNull Block getBlock(int x, int y, int z) {
        return AdapterUtils.adapt(this.handle_.getBlock(x, y, z));
    }

    @Override
    public Entity[] getEntities() {
        return Arrays.stream(this.handle_.getEntities()).map(AdapterUtils::adapt).toArray(Entity[]::new);
    }

    public boolean equals(Object other) {
        if (other instanceof ChunkAdapter adapter) other = adapter.handle_;
        return Objects.equals(this.handle_, other);
    }

    public int hashCode() {
        return this.handle_.hashCode();
    }

    @SuppressWarnings("all")
    private abstract static class ExcludedMethods {
        public abstract World getWorld();

        public abstract Block getBlock(int x, int y, int z);

        public abstract Entity[] getEntities();
    }
}
