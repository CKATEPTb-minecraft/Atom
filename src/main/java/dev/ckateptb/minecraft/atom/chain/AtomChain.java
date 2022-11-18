package dev.ckateptb.minecraft.atom.chain;

import reactor.util.function.Tuple2;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface AtomChain<T> extends Supplier<T> {
    /**
     * Processes an object on the current thread
     */
    static <T> CurrentThreadAtomChain<T> of(T obj) {
        return new CurrentThreadAtomChain<>(obj);
    }

    /**
     * Processes an object on the server thread
     */
    static <T> ServerThreadAtomChain<T> sync(T obj) {
        return new ServerThreadAtomChain<>(obj);
    }

    /**
     * Try to process object on current thread and catch refer to server thread
     *
     * @see org.spigotmc.AsyncCatcher
     */
    static <T> RudeAtomChain<T> rude(T obj) {
        return new RudeAtomChain<>(obj);
    }

    <R> AtomChain<R> map(Function<? super T, ? extends R> mapper);

    <R, RT extends AtomChain<R>> RT flatMap(Function<? super T, RT> mapper);

    <T2> AtomChain<Tuple2<T, T2>> zipWith(T2 other);

    AtomChain<T> run(Consumer<T> consumer);
}
