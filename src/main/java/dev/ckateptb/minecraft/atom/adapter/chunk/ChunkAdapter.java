package dev.ckateptb.minecraft.atom.adapter.chunk;

import dev.ckateptb.minecraft.atom.adapter.Adapter;
import dev.ckateptb.minecraft.atom.adapter.AdapterUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

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

    private abstract static class ExcludedMethods {
        public abstract World getWorld();

        public abstract Block getBlock(int x, int y, int z);
    }
}
