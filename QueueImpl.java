import java.util.EmptyStackException;
import java.util.Random;

@SuppressWarnings("unchecked")
public class QueueImpl<T> {
    private static final int INITIAL_CAPACITY = 2;

    private T[] array;
    private int numElements;

    private int head;

    public QueueImpl(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity has to be positive");
        }
        this.array = (T[]) new Object[capacity];
        this.numElements = 0;
    }

    public QueueImpl() {
        this(INITIAL_CAPACITY);
    }

    public void enqueue(T item) {
        if (this.numElements == this.array.length) {
            resize(this.array.length * 2);
        }

        this.array[circularIndex(this.head + this.numElements++)] = item;
    }

    public T dequeue() {
        if (this.numElements == 0) {
            throw new EmptyStackException();
        }

        T target = this.array[this.head];
        this.head = circularIndex(this.head + 1);
        this.numElements--;
        if (this.numElements > 0 && this.numElements < this.array.length / 4) {
            resize(this.array.length / 2);
        }
        return target;
    }

    public T peek() {
        if (this.numElements == 0) {
            throw new EmptyStackException();
        }
        return this.array[this.head];
    }

    public int getSize() {
        return this.numElements;
    }

    public boolean isEmpty() {
        return this.numElements == 0;
    }

    private int circularIndex(int fromHead) {
        return fromHead % this.array.length;
    }

    private void resize(int capacity) {
        T[] resized = (T[]) new Object[capacity];

        int tail = circularIndex(this.head + this.numElements - 1);
        int arrPtr = circularIndex(this.head - 1); // do-while increments first to include tail
        int resPtr = 0;
        do {
            arrPtr = circularIndex(arrPtr + 1);
            resized[resPtr++] = this.array[arrPtr];
        } while (arrPtr != tail);
        this.array = resized;
        this.head = 0;
    }

    // test client
    public static void main(String[] args) {
        QueueImpl<String> q = new QueueImpl<>();

        final int SIZE = 10;
        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            int rand = random.nextInt();
            q.enqueue(String.valueOf(rand));
            System.out.print(rand + " ");
        }

        System.out.println();
        while (!q.isEmpty()) {
            System.out.print(q.dequeue() + " ");
        }
        System.out.println();
        System.out.println(q.dequeue());
    }
}
