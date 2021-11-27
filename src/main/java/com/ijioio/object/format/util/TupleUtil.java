package com.ijioio.object.format.util;

/** Helper class for tuples. */
public class TupleUtil {

  /** Immutable holder for pair of objects. */
  public static class Pair<F, S> {

    /**
     * Creates {@link Pair} of indicated {@code first} and {@code second} objects.
     *
     * @param first object of pair
     * @param second object of par
     * @return pair object holding indicated objects
     */
    public static <F, S> Pair<F, S> of(F first, S second) {
      return new Pair<>(first, second);
    }

    /**
     * Creates new pair object holding swapped objects of indicated {@code pair}.
     *
     * @param pair to swap
     * @return new pair object with swapped objects
     */
    public static <F, S> Pair<S, F> swap(Pair<F, S> pair) {
      return Pair.of(pair.getSecond(), pair.getFirst());
    }

    private final F first;

    private final S second;

    private Pair(F first, S second) {

      this.first = first;
      this.second = second;
    }

    public F getFirst() {
      return first;
    }

    public S getSecond() {
      return second;
    }

    @Override
    public String toString() {
      return "Pair [first=" + first + ", second=" + second + "]";
    }
  }
}
