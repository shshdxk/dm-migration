package io.github.shshdxk.hibernate.snapshot.extension;

public interface ExtendedSnapshotGenerator<T, U> {

    U snapshot(T object);

    boolean supports(T object);

}
