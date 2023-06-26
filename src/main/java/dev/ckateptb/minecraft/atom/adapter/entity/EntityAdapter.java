package dev.ckateptb.minecraft.atom.adapter.entity;

import dev.ckateptb.minecraft.atom.adapter.Adapter;
import dev.ckateptb.minecraft.atom.adapter.AdapterUtils;
import dev.ckateptb.minecraft.atom.chain.AtomChain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class EntityAdapter implements Entity, Adapter<Entity> {
    @Delegate(excludes = ExcludedMethods.class)
    private final Entity handle_;

    @Override
    public @NotNull Location getLocation() {
        return AdapterUtils.adapt(this.handle_.getLocation());
    }

    @Override
    public @Nullable Location getLocation(@Nullable Location loc) {
        return AdapterUtils.adapt(this.handle_.getLocation(loc));
    }

    @Override
    public @NotNull World getWorld() {
        return AdapterUtils.adapt(this.handle_.getWorld());
    }

    @Override
    public boolean teleport(@NotNull Location location, PlayerTeleportEvent.@NotNull TeleportCause cause, boolean ignorePassengers, boolean dismount) {
        return AtomChain.sync(this.handle_).map(entity -> entity.teleport(location, cause, ignorePassengers, dismount)).get();
    }

    @Override
    public @NotNull List<Entity> getNearbyEntities(double x, double y, double z) {
        return this.getWorld().getNearbyEntities(this.getLocation(), x, y, z).stream().toList();
    }

    @Override
    @SuppressWarnings("all")
    public @Nullable Entity getPassenger() {
        return AdapterUtils.adapt(this.handle_.getPassenger());
    }

    @Override
    public @NotNull List<Entity> getPassengers() {
        return this.handle_.getPassengers().stream().map(AdapterUtils::adapt).collect(Collectors.toList());
    }

    @Override
    public @Nullable Entity getVehicle() {
        return AdapterUtils.adapt(this.handle_.getVehicle());
    }

    @Override
    public @Nullable Location getOrigin() {
        return AdapterUtils.adapt(this.handle_.getOrigin());
    }

    @Override
    public @NotNull Chunk getChunk() {
        return AdapterUtils.adapt(this.handle_.getChunk());
    }

    @Override
    public @NotNull Set<Player> getTrackedPlayers() {
        return this.handle_.getTrackedPlayers().stream().map(AdapterUtils::adapt).collect(Collectors.toSet());
    }

    private static abstract class ExcludedMethods {
        public abstract Location getLocation();

        public abstract Location getLocation(@Nullable Location loc);

        public abstract World getWorld();

        public abstract boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause, boolean ignorePassengers, boolean dismount);

        public abstract List<Entity> getNearbyEntities(double x, double y, double z);

        public abstract Entity getPassenger();

        public abstract List<Entity> getPassengers();

        public abstract Entity getVehicle();

        public abstract Location getOrigin();

        public abstract Chunk getChunk();

        public abstract Set<Player> getTrackedPlayers();
    }
}
