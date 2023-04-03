package dev.ckateptb.minecraft.atom.chain;

import lombok.Getter;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class ImmediateAtomChain<T> extends AbstractAtomChain<T, ImmediateAtomChain<?>> {
    @Getter
    private final Scheduler scheduler = Schedulers.immediate();

    ImmediateAtomChain(T current) {
        super(current);
    }

    @Override
    public <R, AC extends AtomChain<R, ?>> AC set(R obj) {
        return (AC) AtomChain.immediate(obj);
    }
}
