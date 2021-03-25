import java.util.EmptyStackException;
import java.util.Random;

@SuppressWarnings("unchecked")
public class StackImpl<T> {
    private static final int INITIAL_CAPACITY = 2;

    private T[] array;
    private int numElements;

    public StackImpl(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity has to be positive");
        }
        this.array = (T[]) new Object[capacity];
        this.numElements = 0;
    }

    public StackImpl() {
        this(INITIAL_CAPACITY);
    }

    public void push(T item) {
        if (this.numElements == this.array.length) {
            resize(this.array.length * 2);
        }
        this.array[this.numElements++] = item;
    }

    public T pop() {
        if (this.numElements == 0) {
            throw new EmptyStackException();
        }

        T target = this.array[--this.numElements];
        if (this.numElements > 0 && this.numElements < this.array.length / 4) {
            resize(this.array.length / 2);
        }
        return target;
    }

    public T peek() {
        if (this.numElements == 0) {
            throw new EmptyStackException();
        }

        return this.array[this.numElements - 1];
    }

    public boolean isEmpty() {
        return this.numElements == 0;
    }

    public int getSize() {
        return this.numElements;
    }

    private void resize(int capacity) {
        T[] resized = (T[]) new Object[capacity];
        System.arraycopy(this.array, 0, resized, 0, this.numElements);
        this.array = resized;
//        System.out.print(" *" + "Resized to " + capacity + "* ");
    }

    // test client
    public static void main(String[] args) {
        StackImpl<String> stack = new StackImpl<>();

        final int SIZE = 10;
        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            int rand = random.nextInt();
            stack.push(String.valueOf(rand));
            System.out.print(rand + " ");
        }

        System.out.println();
        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " ");
        }
        System.out.println();
        System.out.println(stack.pop());
    }
}
