package dev.ckateptb.minecraft.atom.adapter;

import dev.ckateptb.minecraft.atom.adapter.block.BlockAdapter;
import dev.ckateptb.minecraft.atom.adapter.chunk.ChunkAdapter;
import dev.ckateptb.minecraft.atom.adapter.entity.EntityAdapter;
import dev.ckateptb.minecraft.atom.adapter.entity.LivingEntityAdapter;
import dev.ckateptb.minecraft.atom.adapter.entity.PlayerAdapter;
import dev.ckateptb.minecraft.atom.adapter.location.LocationAdapter;
import dev.ckateptb.minecraft.atom.adapter.world.WorldAdapter;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class AdapterUtils {
    public static WorldAdapter adapt(World world) {
        if (world == null) return null;
        return new WorldAdapter(world);
    }

    public static LocationAdapter adapt(Location location) {
        if (location == null) return null;
        return new LocationAdapter(location);
    }

    public static BlockAdapter adapt(Block block) {
        if (block == null) return null;
        return new BlockAdapter(block);
    }

    public static ChunkAdapter adapt(Chunk chunk) {
        if (chunk == null) return null;
        return new ChunkAdapter(chunk);
    }

    public static EntityAdapter adapt(Entity entity) {
        if (entity == null) return null;
        return new EntityAdapter(entity);
    }

    public static LivingEntityAdapter adapt(LivingEntity entity) {
        if (entity == null) return null;
        return new LivingEntityAdapter(entity);
    }

    public static PlayerAdapter adapt(Player entity) {
        if (entity == null) return null;
        return new PlayerAdapter(entity);
    }
}
