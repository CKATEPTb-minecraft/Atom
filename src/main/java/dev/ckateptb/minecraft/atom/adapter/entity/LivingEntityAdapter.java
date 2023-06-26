package dev.ckateptb.minecraft.atom.adapter.entity;

import com.destroystokyo.paper.block.TargetBlockInfo;
import dev.ckateptb.minecraft.atom.adapter.AdapterUtils;
import dev.ckateptb.minecraft.atom.chain.AtomChain;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class LivingEntityAdapter extends EntityAdapter implements LivingEntity {
    @Delegate(excludes = ExcludedMethods.class)
    private final LivingEntity handle_;

    public LivingEntityAdapter(LivingEntity entity) {
        super(entity);
        this.handle_ = entity;
    }

    @Override
    public @NotNull Location getEyeLocation() {
        return AdapterUtils.adapt(this.handle_.getEyeLocation());
    }

    @Override
    public @NotNull List<Block> getLineOfSight(@Nullable Set<Material> transparent, int maxDistance) {
        return this.handle_.getLineOfSight(transparent, maxDistance).stream().map(AdapterUtils::adapt).collect(Collectors.toList());
    }

    @Override
    public @NotNull Block getTargetBlock(@Nullable Set<Material> transparent, int maxDistance) {
        return AdapterUtils.adapt(this.handle_.getTargetBlock(transparent, maxDistance));
    }

    @Override
    public @Nullable Block getTargetBlock(int maxDistance) {
        return AdapterUtils.adapt(this.handle_.getTargetBlock(maxDistance));
    }

    @Override
    public @Nullable Block getTargetBlock(int maxDistance, TargetBlockInfo.@NotNull FluidMode fluidMode) {
        return AdapterUtils.adapt(this.handle_.getTargetBlock(maxDistance, fluidMode));
    }

    @Override
    public @Nullable Entity getTargetEntity(int maxDistance) {
        return AdapterUtils.adapt(this.handle_.getTargetEntity(maxDistance));
    }

    @Override
    public @Nullable Entity getTargetEntity(int maxDistance, boolean ignoreBlocks) {
        return AdapterUtils.adapt(this.handle_.getTargetEntity(maxDistance, ignoreBlocks));
    }

    @Override
    public @NotNull List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> transparent, int maxDistance) {
        return this.handle_.getLastTwoTargetBlocks(transparent, maxDistance).stream().map(AdapterUtils::adapt).collect(Collectors.toList());
    }

    @Override
    public @Nullable Block getTargetBlockExact(int maxDistance) {
        return AdapterUtils.adapt(this.handle_.getTargetBlockExact(maxDistance));
    }

    @Override
    public @Nullable Block getTargetBlockExact(int maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        return AdapterUtils.adapt(this.handle_.getTargetBlockExact(maxDistance, fluidCollisionMode));
    }

    @Override
    public @Nullable Player getKiller() {
        return AdapterUtils.adapt(this.handle_.getKiller());
    }

    @Override
    public boolean addPotionEffect(@NotNull PotionEffect effect) {
        return AtomChain.sync(this.handle_).map(livingEntity -> livingEntity.addPotionEffect(effect)).get();
    }

    @Override
    @SuppressWarnings("all")
    public boolean addPotionEffect(@NotNull PotionEffect effect, boolean force) {
        return AtomChain.sync(this.handle_).map(livingEntity -> livingEntity.addPotionEffect(effect, force)).get();
    }

    @Override
    public boolean addPotionEffects(@NotNull Collection<PotionEffect> effects) {
        return AtomChain.sync(this.handle_).map(livingEntity -> livingEntity.addPotionEffects(effects)).get();
    }

    @Override
    public void removePotionEffect(@NotNull PotionEffectType type) {
        AtomChain.sync(this.handle_).run(livingEntity -> livingEntity.removePotionEffect(type));
    }

    @Override
    public @NotNull Entity getLeashHolder() throws IllegalStateException {
        return AdapterUtils.adapt(this.handle_.getLeashHolder());
    }

    @Override
    public void damage(double amount) {
        AtomChain.sync(this.handle_).run(livingEntity -> livingEntity.damage(amount));
    }

    @Override
    public void damage(double amount, @Nullable Entity source) {
        AtomChain.sync(this.handle_).run(livingEntity -> livingEntity.damage(amount, source));
    }

    public boolean equals(Object other) {
        if (other instanceof LivingEntityAdapter adapter) other = adapter.handle_;
        return Objects.equals(this.handle_, other);
    }

    public int hashCode() {
        return this.handle_.hashCode();
    }

    @SuppressWarnings("all")
    private abstract static class ExcludedMethods {
        public abstract Location getLocation();

        public abstract Location getLocation(Location loc);

        public abstract World getWorld();

        public abstract boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause, boolean ignorePassengers, boolean dismount);

        public abstract List<Entity> getNearbyEntities(double x, double y, double z);

        public abstract Entity getPassenger();

        public abstract List<Entity> getPassengers();

        public abstract Entity getVehicle();

        public abstract Location getOrigin();

        public abstract Chunk getChunk();

        public abstract Set<Player> getTrackedPlayers();

        public abstract Location getEyeLocation();

        public abstract Entity getTargetEntity(int maxDistance, boolean ignoreBlocks);

        public abstract List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance);

        public abstract Block getTargetBlockExact(int maxDistance, FluidCollisionMode fluidCollisionMode);

        public abstract Player getKiller();

        public abstract boolean addPotionEffect(PotionEffect effect, boolean force);

        public abstract void removePotionEffect(PotionEffectType type);

        public abstract Entity getLeashHolder() throws IllegalStateException;

        public abstract void damage(double amount);

        public abstract void damage(double amount, Entity source);

        public abstract boolean teleport(Location location, boolean ignorePassengers);

        public abstract boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause, boolean ignorePassengers);

        public abstract boolean teleport(Location location, boolean ignorePassengers, boolean dismount);

        public abstract boolean teleport(Location location);

        public abstract boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause);

        public abstract boolean teleport(Entity destination);

        public abstract boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause);

        public abstract List<Block> getLineOfSight(Set<Material> transparent, int maxDistance);

        public abstract Block getTargetBlock(Set<Material> transparent, int maxDistance);

        public abstract Block getTargetBlock(int maxDistance);

        public abstract Block getTargetBlock(int maxDistance, com.destroystokyo.paper.block.TargetBlockInfo.FluidMode fluidMode);

        public abstract Entity getTargetEntity(int maxDistance);

        public abstract Block getTargetBlockExact(int maxDistance);

        public abstract boolean addPotionEffect(PotionEffect effect);

        public abstract boolean addPotionEffects(Collection<PotionEffect> effects);

    }
}
