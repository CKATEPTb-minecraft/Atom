package dev.ckateptb.minecraft.atom.adapter.location;

import dev.ckateptb.minecraft.atom.adapter.Adapter;
import dev.ckateptb.minecraft.atom.adapter.AdapterUtils;
import dev.ckateptb.minecraft.atom.adapter.world.WorldAdapter;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.Chunk;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
public class LocationAdapter extends Location implements Adapter<Location> {
    @Delegate(excludes = ExcludedMethods.class)
    private final Location handle_;

    public LocationAdapter(Location location) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.handle_ = location;
    }

    public World getWorld() {
        return AdapterUtils.adapt(this.handle_.getWorld());
    }

    public @NotNull Chunk getChunk() {
        return AdapterUtils.adapt(this.handle_.getChunk());
    }

    @Override
    public @NotNull Block getBlock() {
        return AdapterUtils.adapt(this.handle_.getBlock());
    }

    public @NotNull Location setDirection(@NotNull Vector vector) {
        return AdapterUtils.adapt(this.handle_.setDirection(vector));
    }

    public @NotNull Location add(@NotNull Location vec) {
        return AdapterUtils.adapt(this.handle_.add(vec));
    }

    public @NotNull Location add(@NotNull Vector vec) {
        return AdapterUtils.adapt(this.handle_.add(vec));
    }

    public @NotNull Location add(double x, double y, double z) {
        return AdapterUtils.adapt(this.handle_.add(x, y, z));
    }

    public @NotNull Location subtract(@NotNull Location vec) {
        return AdapterUtils.adapt(this.handle_.subtract(vec));
    }

    public @NotNull Location subtract(@NotNull Vector vec) {
        return AdapterUtils.adapt(this.handle_.subtract(vec));
    }

    public @NotNull Location subtract(double x, double y, double z) {
        return AdapterUtils.adapt(this.handle_.subtract(x, y, z));
    }

    public @NotNull Location multiply(double m) {
        return AdapterUtils.adapt(this.handle_.multiply(m));
    }

    public @NotNull Location zero() {
        return AdapterUtils.adapt(this.handle_.zero());
    }

    public @NotNull Location set(double x, double y, double z) {
        return AdapterUtils.adapt(this.handle_.set(x, y, z));
    }

    public @NotNull Location add(@NotNull Location base, double x, double y, double z) {
        return AdapterUtils.adapt(this.handle_.add(base, x, y, z));
    }

    public @NotNull Location subtract(@NotNull Location base, double x, double y, double z) {
        return AdapterUtils.adapt(this.handle_.subtract(base, x, y, z));
    }

    public @NotNull Location toBlockLocation() {
        return AdapterUtils.adapt(this.handle_.toBlockLocation());
    }

    public @NotNull Location toCenterLocation() {
        return AdapterUtils.adapt(this.handle_.toCenterLocation());
    }

    public @NotNull Location toHighestLocation() {
        return AdapterUtils.adapt(this.handle_.toHighestLocation());
    }

    @SuppressWarnings("all")
    public @NotNull Location toHighestLocation(@NotNull final com.destroystokyo.paper.HeightmapType heightmap) {
        return AdapterUtils.adapt(this.handle_.toHighestLocation(heightmap));
    }

    public @NotNull Location toHighestLocation(@NotNull final HeightMap heightMap) {
        return AdapterUtils.adapt(this.handle_.toHighestLocation(heightMap));
    }

    public @NotNull Collection<Entity> getNearbyEntities(double x, double y, double z) {
        World world = this.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location has no world");
        }
        return this.getWorld().getNearbyEntities(this, x, y, z);
    }

    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(double radius) {
        return this.getNearbyLivingEntities(radius, radius);
    }

    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(double xzRadius, double yRadius) {
        return this.getNearbyLivingEntities(xzRadius, yRadius, xzRadius);
    }

    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(double xRadius, double yRadius, double zRadius) {
        return this.getNearbyLivingEntities(xRadius, yRadius, zRadius, null);
    }

    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(double radius, @Nullable Predicate<LivingEntity> predicate) {
        return this.getNearbyLivingEntities(radius, radius, predicate);
    }

    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(double xzRadius, double yRadius, @Nullable Predicate<LivingEntity> predicate) {
        return this.getNearbyLivingEntities(xzRadius, yRadius, xzRadius, predicate);
    }

    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(double xRadius, double yRadius, double zRadius, @Nullable Predicate<LivingEntity> predicate) {
        return this.getNearbyEntities(xRadius, yRadius, zRadius).stream()
                .filter(entity -> entity instanceof LivingEntity le && (predicate == null || predicate.test(le)))
                .map(entity -> (LivingEntity) entity)
                .collect(Collectors.toList());
    }

    public @NotNull Collection<Player> getNearbyPlayers(double radius) {
        return this.getNearbyPlayers(radius, radius);
    }

    public @NotNull Collection<Player> getNearbyPlayers(double xzRadius, double yRadius) {
        return this.getNearbyPlayers(xzRadius, yRadius, xzRadius);
    }

    public @NotNull Collection<Player> getNearbyPlayers(double xRadius, double yRadius, double zRadius) {
        return this.getNearbyPlayers(xRadius, yRadius, zRadius, null);
    }

    public @NotNull Collection<Player> getNearbyPlayers(double radius, @Nullable Predicate<Player> predicate) {
        return this.getNearbyPlayers(radius, radius, predicate);
    }

    public @NotNull Collection<Player> getNearbyPlayers(double xzRadius, double yRadius, @Nullable Predicate<Player> predicate) {
        return this.getNearbyPlayers(xzRadius, yRadius, xzRadius, predicate);
    }

    public @NotNull Collection<Player> getNearbyPlayers(double xRadius, double yRadius, double zRadius, @Nullable Predicate<Player> predicate) {
        return this.getNearbyEntities(xRadius, yRadius, zRadius).stream()
                .filter(entity -> entity instanceof Player le && (predicate == null || predicate.test(le)))
                .map(entity -> (Player) entity)
                .collect(Collectors.toList());
    }

    public @NotNull Location clone() {
        Location clone = super.clone();
        return AdapterUtils.adapt(clone);
    }

    public boolean equals(Object other) {
        if (other instanceof LocationAdapter adapter) other = adapter.handle_;
        return Objects.equals(this.handle_, other);
    }

    public int hashCode() {
        return this.handle_.hashCode();
    }

    @SuppressWarnings("all")
    private abstract static class ExcludedMethods {
        public abstract World getWorld();

        public abstract Chunk getChunk();

        public abstract Block getBlock();

        public abstract Location setDirection(Vector vector);

        public abstract Location add(Location vec);

        public abstract Location add(Vector vec);

        public abstract Location add(double x, double y, double z);

        public abstract Location subtract(Location vec);

        public abstract Location subtract(Vector vec);

        public abstract Location subtract(double x, double y, double z);

        public abstract Location multiply(double m);

        public abstract Location zero();

        public abstract Location set(double x, double y, double z);

        public abstract Location add(Location base, double x, double y, double z);

        public abstract Location subtract(Location base, double x, double y, double z);

        public abstract Location toBlockLocation();

        public abstract Location toCenterLocation();

        public abstract Location toHighestLocation();

        public abstract Location toHighestLocation(final com.destroystokyo.paper.HeightmapType heightmap);

        public abstract Location toHighestLocation(final HeightMap heightMap);

        public abstract Collection<Entity> getNearbyEntities(double x, double y, double z);

        public abstract Collection<LivingEntity> getNearbyLivingEntities(double radius);

        public abstract Collection<LivingEntity> getNearbyLivingEntities(double xzRadius, double yRadius);

        public abstract Collection<LivingEntity> getNearbyLivingEntities(double xRadius, double yRadius, double zRadius);

        public abstract Collection<LivingEntity> getNearbyLivingEntities(double radius, Predicate<LivingEntity> predicate);

        public abstract Collection<LivingEntity> getNearbyLivingEntities(double xzRadius, double yRadius, Predicate<LivingEntity> predicate);

        public abstract Collection<LivingEntity> getNearbyLivingEntities(double xRadius, double yRadius, double zRadius, Predicate<LivingEntity> predicate);

        public abstract Collection<Player> getNearbyPlayers(double radius);

        public abstract Collection<Player> getNearbyPlayers(double xzRadius, double yRadius);

        public abstract Collection<Player> getNearbyPlayers(double xRadius, double yRadius, double zRadius);

        public abstract Collection<Player> getNearbyPlayers(double radius, Predicate<Player> predicate);

        public abstract Collection<Player> getNearbyPlayers(double xzRadius, double yRadius, Predicate<Player> predicate);

        public abstract Collection<Player> getNearbyPlayers(double xRadius, double yRadius, double zRadius, Predicate<Player> predicate);

        public abstract Location clone();
    }
}
