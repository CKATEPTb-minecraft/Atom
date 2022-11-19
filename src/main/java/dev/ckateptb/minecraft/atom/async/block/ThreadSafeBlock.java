package dev.ckateptb.minecraft.atom.async.block;

import com.fastasyncworldedit.core.Fawe;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ThreadSafeBlock {
    public static ThreadSafeBlock of(Block block) {
        org.bukkit.World bukkitWorld = block.getWorld();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        return new ThreadSafeBlock(BukkitAdapter.adapt(bukkitWorld), BlockVector3.at(x, y, z));
    }

    public static ThreadSafeBlock of(org.bukkit.World bukkitWorld, int x, int y, int z) {
        return new ThreadSafeBlock(BukkitAdapter.adapt(bukkitWorld), BlockVector3.at(x, y, z));
    }

    private final World world;
    private final BlockVector3 position;

    public ThreadSafeBlock(World world, BlockVector3 position) {
        this.world = world;
        this.position = position;
    }

    public BoundingBox getBoundingBox() {
        return this.getLocation().getBlock().getBoundingBox();
    }

    public boolean isSolid() {
        return world.getBlock(position).getMaterial().isSolid();
    }

    public boolean isBurnable() {
        return world.getBlock(position).getMaterial().isBurnable();
    }

    public boolean isLiquid() {
        return world.getBlock(position).getMaterial().isLiquid();
    }

    public boolean isEmpty() {
        return world.getBlock(position).isAir();
    }

    public ThreadSafeBlock setBiome(Biome biome) {
        emit(editSession -> editSession.setBiome(position, BukkitAdapter.adapt(biome)));
        return this;
    }

    public Biome getBiome() {
        return BukkitAdapter.adapt(world.getBiome(position));
    }

    public BlockData getBlockData() {
        return BukkitAdapter.adapt(world.getBlock(position));
    }

    public BlockFace getFace(@NotNull Block block) {
        BlockFace[] values = BlockFace.values();

        for (BlockFace face : values) {
            if ((this.getX() + face.getModX() == block.getX()) && (this.getY() + face.getModY() == block.getY()) && (this.getZ() + face.getModZ() == block.getZ())) {
                return face;
            }
        }

        return null;
    }

    public ThreadSafeBlock setType(Material type, boolean applyPhysics) {
        // TODO check applyPhysics
        emit(editSession -> editSession.smartSetBlock(position, BukkitAdapter.adapt(type.createBlockData())));
        return this;
    }

    public ThreadSafeBlock setType(Material type) {
        return this.setType(type, true);
    }

    public ThreadSafeBlock setBlockData(BlockData data, boolean applyPhysics) {
        // TODO check applyPhysics
        this.emit(editSession -> editSession.smartSetBlock(position, BukkitAdapter.adapt(data)));
        return this;
    }

    public ThreadSafeBlock setBlockData(BlockData data) {
        return this.setBlockData(data, true);
    }

    public byte getLightFromBlocks() {
        return (byte) world.getEmittedLight(position);
    }

    public byte getLightFromSky() {
        return (byte) world.getSkyLight(this.getX(), this.getY(), this.getZ());
    }

    public byte getLightLevel() {
        return (byte) world.getBlockLightLevel(position);
    }

    public Material getType() {
        return BukkitAdapter.adapt(world.getBlock(position).getBlockType());
    }

    public ThreadSafeBlock getRelative(final int modX, final int modY, final int modZ) {
        int x = this.getX() + modX;
        int y = this.getY() + modY;
        int z = this.getZ() + modZ;
        return new ThreadSafeBlock(world, BlockVector3.at(x, y, z));
    }

    public ThreadSafeBlock getRelative(BlockFace face) {
        return this.getRelative(face, 1);
    }

    public ThreadSafeBlock getRelative(BlockFace face, int distance) {
        return this.getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }

    public org.bukkit.World getWorld() {
        return BukkitAdapter.adapt(world);
    }

    public Location getLocation() {
        return BukkitAdapter.adapt(this.getWorld(), position);
    }

    public int getX() {
        return this.position.getX();
    }

    public int getY() {
        return this.position.getY();
    }

    public int getZ() {
        return this.position.getZ();
    }

    // EditSession part
    private EditSession editSession;

    // sometime fawe work wrong, so need to define EditSession for task queue
    public ThreadSafeBlock setEditSession(EditSession editSession) {
        this.editSession = editSession;
        return this;
    }

    private void emit(Consumer<EditSession> consumer) {
        if(this.editSession != null) {
            consumer.accept(this.editSession);
        } else {
            try (EditSession sessions = defaultEditSession(world)) {
                consumer.accept(sessions);
            }
        }
    }

    public static EditSession defaultEditSession(World world) {
        return Fawe.instance().getWorldEdit().newEditSessionBuilder().fastMode(true).allowedRegionsEverywhere().world(world).build();
    }

    // EditSession part end
}
