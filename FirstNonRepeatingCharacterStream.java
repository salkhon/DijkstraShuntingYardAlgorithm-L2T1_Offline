import java.util.Scanner;

/*
Problem 2.
1. Take next character from string_new.
2. If that character is contained in uniqueQ, remove that character from uniqueQ and enqueue it
    on the repeatedQ.
3. Else if that character is not contained in uniqueQ, but is contained in the repeatedQ,
    ignore that character.
4. Else if that character is not contained in uniqueQ and is not contained in the repeatedQ,
    enqueue that character in the uniqueQ.
5. Append the next character in uniqueQ to string_new without removing it from uniqueQ.
6. Repeat step 1 if string_old is not completely iterated.
 */
public class FirstNonRepeatingCharacterStream {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter String_old: ");
        String string_old = scanner.next();
        scanner.nextLine();

        System.out.println("String_new: " + transform(string_old));
    }

    private static String transform(String string_old) {
        StringBuilder string_new = new StringBuilder();
        QueueImpl<Character> uniqueQ = new QueueImpl<>();
        QueueImpl<Character> repeatedQ = new QueueImpl<>();

        for (int i = 0; i < string_old.length(); i++) {
            // contains could be implemented in constant time, but queue is used so it's linear time
            if (contains(string_old.charAt(i), uniqueQ)) {
                repeatedQ.enqueue(remove(string_old.charAt(i), uniqueQ));
            } else if (!contains(string_old.charAt(i), repeatedQ)) {
                uniqueQ.enqueue(string_old.charAt(i));
            }

            if (!uniqueQ.isEmpty()) {
                string_new.append(uniqueQ.peek());
            } else {
                string_new.append("#");
            }
        }

        return string_new.toString();
    }

    private static char remove(char c, QueueImpl<Character> uniqueQ) {
        char removed = 0;
        int size = uniqueQ.getSize();
        for (int i = 0; i < size; i++) {
            char qChar = uniqueQ.dequeue();
            if (c == qChar) {
                removed = c;
            } else {
                uniqueQ.enqueue(qChar);
            }
        }
        return removed;
    }

    private static boolean contains(char c, QueueImpl<Character> uniqueQ) {
        boolean contains = false;
        int size = uniqueQ.getSize();
        for (int i = 0; i < size; i++) {
            char qChar = uniqueQ.dequeue();
            if (c == qChar) {
                contains = true;
            }
            uniqueQ.enqueue(qChar);
        }
        return contains;
    }
}
