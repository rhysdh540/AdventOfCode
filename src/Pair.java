import java.util.Objects;

// blatantly stolen from create
public class Pair<F, S> {

	private F first;
	private S second;

	private Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	public static <F, S> Pair<F, S> of(F first, S second) {
		return new Pair<>(first, second);
	}

	public F first() {
		return first;
	}

	public F getLeft() {
		return first;
	}

	public F getKey() {
		return first;
	}

	public F getFirst() {
		return first;
	}

	public S second() {
		return second;
	}

	public S getRight() {
		return second;
	}

	public S getValue() {
		return second;
	}

	public S getSecond() {
		return second;
	}

	public void first(F first) {
		this.first = first;
	}

	public void setLeft(F first) {
		this.first = first;
	}

	public void setKey(F first) {
		this.first = first;
	}

	public void setFirst(F first) {
		this.first = first;
	}

	public void second(S second) {
		this.second = second;
	}

	public void setRight(S second) {
		this.second = second;
	}

	public void setValue(S second) {
		this.second = second;
	}

	public void setSecond(S second) {
		this.second = second;
	}

	public Pair<F, S> copy() {
		return Pair.of(first, second);
	}

	@Override
	public boolean equals(final Object obj) {
		return obj == this
			|| obj instanceof Pair<?, ?> other
			&& Objects.equals(first, other.first) && Objects.equals(second, other.second);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(first) * 31 ^ Objects.hashCode(second);
	}

	public boolean contains(Object obj) {
		return Objects.equals(first, obj) || Objects.equals(second, obj);
	}

	@Override
	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	public Pair<S, F> swapped() {
		return Pair.of(second, first);
	}
}