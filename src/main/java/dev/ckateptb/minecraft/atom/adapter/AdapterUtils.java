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
        if (world instanceof WorldAdapter adapter) return adapter;
        return new WorldAdapter(world);
    }

    public static LocationAdapter adapt(Location location) {
        if (location == null) return null;
        if (location instanceof LocationAdapter adapter) return adapter;
        return new LocationAdapter(location);
    }

    public static BlockAdapter adapt(Block block) {
        if (block == null) return null;
        if (block instanceof BlockAdapter adapter) return adapter;
        return new BlockAdapter(block);
    }

    public static ChunkAdapter adapt(Chunk chunk) {
        if (chunk == null) return null;
        if (chunk instanceof ChunkAdapter adapter) return adapter;
        return new ChunkAdapter(chunk);
    }

    public static PlayerAdapter adapt(Player entity) {
        if (entity == null) return null;
        if (entity instanceof PlayerAdapter adapter) return adapter;
        return new PlayerAdapter(entity);
    }

    public static LivingEntityAdapter adapt(LivingEntity entity) {
        if (entity == null) return null;
        if (entity instanceof LivingEntityAdapter adapter) return adapter;
        if (entity instanceof Player player) return new PlayerAdapter(player);
        return new LivingEntityAdapter(entity);
    }

    public static EntityAdapter adapt(Entity entity) {
        if (entity == null) return null;
        if (entity instanceof EntityAdapter adapter) return adapter;
        if (entity instanceof Player player) return new PlayerAdapter(player);
        if (entity instanceof LivingEntity livingEntity) return new LivingEntityAdapter(livingEntity);
        return new EntityAdapter(entity);
    }
}
