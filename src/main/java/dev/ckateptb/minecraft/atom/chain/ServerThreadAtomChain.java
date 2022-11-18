package dev.ckateptb.minecraft.atom.chain;

import dev.ckateptb.common.tableclothcontainer.IoC;
import dev.ckateptb.minecraft.atom.sync.SyncService;
import lombok.SneakyThrows;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.function.Consumer;
import java.util.function.Function;

public class ServerThreadAtomChain<T> extends AbstractAtomChain<T> {
    private final SyncService syncService;
    ServerThreadAtomChain(T current) {
        super(current);
        this.syncService = IoC.getBean(SyncService.class);
    }

    public final CurrentThreadAtomChain<T> immediate() {
        return AtomChain.of(current);
    }

    public final RudeAtomChain<T> rude() {
        return AtomChain.rude(current);
    }

    @Override
    @SneakyThrows
    public <R> ServerThreadAtomChain<R> map(Function<? super T, ? extends R> mapper) {
        return AtomChain.sync(syncService.sync(() -> mapper.apply(current)));
    }

    @Override
    @SneakyThrows
    public <R, RC extends AtomChain<R>> RC flatMap(Function<? super T, RC> mapper) {
        return syncService.sync(() -> mapper.apply(current));
    }

    @Override
    @SneakyThrows
    public <T2> ServerThreadAtomChain<Tuple2<T, T2>> zipWith(T2 other) {
        return syncService.sync(() -> AtomChain.sync(Tuples.of(current, other)));
    }

    @Override
    @SneakyThrows
    public ServerThreadAtomChain<T> run(Consumer<T> consumer) {
        return syncService.sync(() -> {
            consumer.accept(current);
            return this;
        });
    }

    @SneakyThrows
    public ServerThreadAtomChain<T> runAndNoWait(Consumer<T> consumer) {
        syncService.sync(() -> {
            consumer.accept(current);
            return true;
        }, false);
        return this;
    }
}
