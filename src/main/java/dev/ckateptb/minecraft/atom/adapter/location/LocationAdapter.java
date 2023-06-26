package dev.ckateptb.minecraft.atom.adapter.location;

import dev.ckateptb.minecraft.atom.adapter.Adapter;
import dev.ckateptb.minecraft.atom.adapter.AdapterUtils;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

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

    private abstract static class ExcludedMethods {
        public abstract World getWorld();
        public abstract List<Block> getLineOfSight(Set<Material> transparent, int maxDistance);
        public abstract Block getTargetBlock(Set<Material> transparent, int maxDistance);
        public abstract Block getTargetBlock(int maxDistance, com.destroystokyo.paper.block.TargetBlockInfo.FluidMode fluidMode);
    }
}
