package dev.ckateptb.minecraft.atom.chain;

public abstract class AbstractAtomChain<T> implements AtomChain<T> {
    protected final T current;

    AbstractAtomChain(T current) {
        this.current = current;
    }

    @Override
    public T get() {
        return current;
    }
}
