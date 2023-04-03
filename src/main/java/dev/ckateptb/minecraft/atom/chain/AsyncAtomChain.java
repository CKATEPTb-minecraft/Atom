package dev.ckateptb.minecraft.atom.chain;

import lombok.Getter;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class AsyncAtomChain<T> extends AbstractAtomChain<T, AsyncAtomChain<?>> {
    @Getter
    private final Scheduler scheduler = Schedulers.boundedElastic();

    AsyncAtomChain(T current) {
        super(current);
    }

    @Override
    public <R, AC extends AtomChain<R, ?>> AC set(R obj) {
        return (AC) AtomChain.async(obj);
    }
}
