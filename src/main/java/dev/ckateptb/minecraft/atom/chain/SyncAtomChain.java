package dev.ckateptb.minecraft.atom.chain;

import dev.ckateptb.minecraft.atom.scheduler.SyncScheduler;
import lombok.Getter;
import reactor.core.scheduler.Scheduler;

public class SyncAtomChain<T> extends AbstractAtomChain<T, SyncAtomChain<?>> {
    @Getter
    private final Scheduler scheduler = new SyncScheduler();

    SyncAtomChain(T current) {
        super(current);
    }

    @Override
    public <R, AC extends AtomChain<R, ?>> AC set(R obj) {
        return (AC) AtomChain.sync(obj);
    }
}
