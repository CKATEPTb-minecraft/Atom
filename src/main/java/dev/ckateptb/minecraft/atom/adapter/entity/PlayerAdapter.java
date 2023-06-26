package dev.ckateptb.minecraft.atom.adapter.entity;

import dev.ckateptb.minecraft.atom.adapter.AdapterUtils;
import dev.ckateptb.minecraft.atom.chain.AtomChain;
import io.papermc.paper.entity.RelativeTeleportFlag;
import lombok.Getter;
import lombok.experimental.Delegate;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

@Getter
public class PlayerAdapter extends LivingEntityAdapter implements Player {
    @Delegate(excludes = ExcludedMethods.class)
    private final Player handle_;

    public PlayerAdapter(Player player) {
        super(player);
        this.handle_ = player;
    }

    @Override
    public @NotNull Location getCompassTarget() {
        return AdapterUtils.adapt(this.handle_.getCompassTarget());
    }

    @Override
    @SuppressWarnings("all")
    public void kickPlayer(@Nullable String message) {
        AtomChain.sync(this.handle_).run(player -> player.kickPlayer(message));
    }

    @Override
    public void kick() {
        AtomChain.sync(this.handle_).run(Player::kick);
    }

    @Override
    public void kick(@Nullable Component message) {
        AtomChain.sync(this.handle_).run(player -> player.kick(message));
    }

    @Override
    public void kick(@Nullable Component message, PlayerKickEvent.@NotNull Cause cause) {
        AtomChain.sync(this.handle_).run(player -> player.kick(message, cause));
    }

    @Override
    public @Nullable Location getBedSpawnLocation() {
        return AdapterUtils.adapt(this.handle_.getBedSpawnLocation());
    }

    @Override
    public boolean breakBlock(@NotNull Block block) {
        return AtomChain.sync(this.handle_).map(player -> player.breakBlock(block)).get();
    }

    @Override
    public @Nullable Entity getSpectatorTarget() {
        return AdapterUtils.adapt(this.handle_.getSpectatorTarget());
    }

    @Override
    @SuppressWarnings("all")
    public boolean teleport(@NotNull Location location, PlayerTeleportEvent.@NotNull TeleportCause cause, boolean ignorePassengers, boolean dismount, @NotNull RelativeTeleportFlag @NotNull ... teleportFlags) {
        return AtomChain.sync(this.handle_).map(player -> player.teleport(location, cause, ignorePassengers, dismount, teleportFlags)).get();
    }

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

        public abstract Location getCompassTarget();

        public abstract void kickPlayer(String message);

        public abstract void kick();

        public abstract void kick(final net.kyori.adventure.text.Component message);

        public abstract void kick(final Component message, org.bukkit.event.player.PlayerKickEvent.Cause cause);

        public abstract Location getBedSpawnLocation();

        public abstract boolean breakBlock(Block block);

        public abstract Entity getSpectatorTarget();

        public abstract boolean teleport(Location location, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause cause, boolean ignorePassengers, boolean dismount, io.papermc.paper.entity.RelativeTeleportFlag ... teleportFlags);

    }
}
