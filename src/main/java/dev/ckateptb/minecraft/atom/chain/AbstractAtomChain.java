package dev.ckateptb.minecraft.atom.chain;

import lombok.SneakyThrows;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractAtomChain<T, I extends AtomChain<?, ?>> implements AtomChain<T, I> {
    protected final T current;
    private boolean await;

    AbstractAtomChain(T current) {
        this.current = current;
    }

    @SneakyThrows
    protected <T2> Tuple2<T, T2> tuple(T2 other) {
        AtomicReference<Tuple2<T, T2>> result = new AtomicReference<>();
        CountDownLatch downLatch = new CountDownLatch(await ? 1 : 0);
        this.getScheduler().schedule(() -> {
            result.set(Tuples.of(current, other));
            downLatch.countDown();
        });
        downLatch.await();
        return result.get();
    }

    private Mono<T> publish() {
        return Mono.just(this.current)
                .publishOn(this.getScheduler());
    }

    private <R> R apply(Function<T, R> mapper) {
        return this.publish()
                .map(mapper)
                .block();
    }

    @Override
    public <R, AC extends AtomChain<R, ?>> AC map(Function<T, R> mapper) {
        return this.set(this.apply(mapper));
    }

    @Override
    public <T2, AC extends AtomChain<Tuple2<T, T2>, ?>> AC zipWith(T2 other) {
        return this.map(t -> Tuples.of(t, other));
    }

    @Override
    public <AC extends AtomChain<T, ?>> AC run(Consumer<T> consumer) {
        return this.set(this.apply(t -> {
            consumer.accept(t);
            return t;
        }));
    }

    @Override
    public <AC extends AtomChain<T, ?>> AC promise(Consumer<T> consumer) {
        this.publish().subscribeOn(this.getScheduler()).subscribe(consumer);
        return (AC) this;
    }

    @Override
    public <R, AC extends AtomChain<R, ?>> AC flatMap(Function<T, AC> mapper) {
        return this.apply(mapper);
    }

    @Override
    public T get() {
        return current;
    }

    public final SyncAtomChain<T> sync() {
        return AtomChain.sync(current);
    }

    public final ImmediateAtomChain<T> immediate() {
        return AtomChain.immediate(current);
    }

    public final AsyncAtomChain<T> async() {
        return AtomChain.async(current);
    }
}
