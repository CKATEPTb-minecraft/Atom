package dev.ckateptb.minecraft.atom.chain;

import reactor.core.scheduler.Scheduler;
import reactor.util.function.Tuple2;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface AtomChain<T, I extends AtomChain<?, ?>> extends Supplier<T> {
    /**
     * Processes an object on the current thread
     * Can be useful when you don't know which thread the AtomChain is called from,
     * but you know which thread you need to execute your piece of code on.
     * With immediate, you can always return to the current thread.
     */
    static <T> ImmediateAtomChain<T> immediate(T obj) {
        return new ImmediateAtomChain<>(obj);
    }

    /**
     * Processes an object on the server thread
     */
    static <T> SyncAtomChain<T> sync(T obj) {
        return new SyncAtomChain<>(obj);
    }

    /**
     * Processes an object on the bounder elastic thread
     */
    static <T> AsyncAtomChain<T> async(T obj) {
        return new AsyncAtomChain<>(obj);
    }

    /**
     * Switch chain to server thread
     */
    SyncAtomChain<T> sync();

    /**
     * Switch chain to current thread
     */
    ImmediateAtomChain<T> immediate();


    /**
     * Switch chain to async thread
     */
    AsyncAtomChain<T> async();

    /**
     * Transform the item emitted by this Chain
     */
    <R, AC extends AtomChain<R, ?>> AC map(Function<T, R> mapper);

    /**
     * Transform the item emitted by this Chain, returning the value
     * emitted by another Chain (possibly changing the value type)
     */
    <R, AC extends AtomChain<R, ?>> AC flatMap(Function<T, AC> mapper);

    /**
     * Combine the result from this chain with other into a Tuple2
     */
    <T2, AC extends AtomChain<Tuple2<T, T2>, ?>> AC zipWith(T2 other);

    /**
     * Performs this operation on the chain value blocking the immediate thread
     */
    <AC extends AtomChain<T, ?>> AC run(Consumer<T> consumer);

    /**
     * Performs this operation on the chain value NOT blocking the immediate thread
     */
    <AC extends AtomChain<T, ?>> AC promise(Consumer<T> consumer);

    /**
     * Sets a new value for this chain
     */
    <R, AC extends AtomChain<R, ?>> AC set(R obj);

    Scheduler getScheduler();
}
