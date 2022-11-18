package dev.ckateptb.minecraft.atom.chain;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.function.Consumer;
import java.util.function.Function;

public class CurrentThreadAtomChain<T> extends AbstractAtomChain<T> {
    CurrentThreadAtomChain(T current) {
        super(current);
    }

    public final ServerThreadAtomChain<T> sync() {
        return AtomChain.sync(current);
    }

    public final RudeAtomChain<T> rude() {
        return AtomChain.rude(current);
    }

    public <R> CurrentThreadAtomChain<R> map(Function<? super T, ? extends R> mapper) {
        return AtomChain.of(mapper.apply(current));
    }

    public <R, RC extends AtomChain<R>> RC flatMap(Function<? super T, RC> mapper) {
        return mapper.apply(current);
    }

    public <T2> CurrentThreadAtomChain<Tuple2<T, T2>> zipWith(T2 other) {
        return AtomChain.of(Tuples.of(current, other));
    }

    @Override
    public CurrentThreadAtomChain<T> run(Consumer<T> consumer) {
        consumer.accept(current);
        return this;
    }
}
