package dev.ckateptb.minecraft.atom.chain;

import reactor.util.function.Tuple2;

import java.util.function.Consumer;
import java.util.function.Function;

public class RudeAtomChain<T> extends AbstractAtomChain<T> {
    RudeAtomChain(T current) {
        super(current);
    }

    public final CurrentThreadAtomChain<T> immediate() {
        return AtomChain.of(current);
    }


    public final ServerThreadAtomChain<T> sync() {
        return AtomChain.sync(current);
    }

    @Override
    public <R> RudeAtomChain<R> map(Function<? super T, ? extends R> mapper) {
        try {
            return AtomChain.rude(AtomChain.of(current).map(mapper).get());
        } catch (IllegalStateException exception) {
            return AtomChain.rude(AtomChain.sync(current).map(mapper).get());
        }
    }

    @Override
    public <R, RC extends AtomChain<R>> RC flatMap(Function<? super T, RC> mapper) {
        try {
            return AtomChain.of(current).map(mapper).get();
        } catch (IllegalStateException exception) {
            return AtomChain.sync(current).map(mapper).get();
        }
    }

    @Override
    public <T2> RudeAtomChain<Tuple2<T, T2>> zipWith(T2 other) {
        try {
            return AtomChain.rude(AtomChain.of(current).zipWith(other).get());
        } catch (IllegalStateException exception) {
            return AtomChain.rude(AtomChain.sync(current).zipWith(other).get());
        }
    }

    @Override
    public RudeAtomChain<T> run(Consumer<T> consumer) {
        try {
            return AtomChain.rude(AtomChain.of(current).run(consumer).get());
        } catch (IllegalStateException exception) {
            return AtomChain.rude(AtomChain.sync(current).run(consumer).get());
        }
    }

    public RudeAtomChain<T> runAndNoWait(Consumer<T> consumer) {
        try {
            return AtomChain.rude(AtomChain.of(current).run(consumer).get());
        } catch (IllegalStateException exception) {
            return AtomChain.rude(AtomChain.sync(current).runAndNoWait(consumer).get());
        }
    }
}
