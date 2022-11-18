package dev.ckateptb.minecraft.atom;

import dev.ckateptb.common.tableclothcontainer.IoC;
import org.bukkit.plugin.java.JavaPlugin;

public class Atom extends JavaPlugin {
    public Atom() {
        IoC.scan(Atom.class, s -> !s.contains("internal"));
        IoC.registerBean(this);
    }
}