import java.util.Scanner;

/*
Problem 1.
1. Takes infix notation as input.
2. Converts the infix notation to postfix notation. (Reverse Polish Notation)
3. Evaluates the the post fix notation using operand stack.
*/
public class ArithmeticExpressionEvaluator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter arithmetic expression: ");
        String input = scanner.nextLine();
        input = input.replaceAll("\\s", "");

        if (!isValid(input)) {
            System.out.println("Not Valid");
        } else {
            try {
                double result = evaluate(input);

                System.out.print("Valid expression, Computed value: ");
                // removes decimal if integer
                if (Double.isFinite(result) && result == Math.floor(result)) {
                    System.out.println((int) result);
                } else {
                    System.out.println(result);
                }
            } catch (Exception e) {
                System.out.println("Not valid");
                e.printStackTrace();
            }
        }
    }

    private static boolean isValid(String input) {
        // checks parenthesis matching, non repeating operators, negative numbers wrapped in parenthesis
        // operators are not placed consecutively, operators have operands around them (except wrapped negatives)
        boolean isValid = true;
        int numLeftParenthesis = 0, numRightParenthesis = 0;

        if (!Character.isDigit(input.charAt(0)) && input.charAt(0) != '(') {
            // can't start with non digits other than (
            return false;
        }

        char prevChar = input.charAt(0);
        for (int i = 0; isValid && i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                numLeftParenthesis++;
            } else if (input.charAt(i) == ')') {
                numRightParenthesis++;
                if (numRightParenthesis > numLeftParenthesis) {
                    isValid = false;
                }
            }
            if (isOperator(String.valueOf(input.charAt(i))) &&
                    isOperator(String.valueOf(prevChar))) {
                // consecutive operators
                isValid = false;
            } else if (input.charAt(i) == '.' && prevChar == '.') {
                // consecutive decimal points
                isValid = false;
            } else if (input.charAt(i) == '-' && prevChar == '(') {
                // check that '(' closes with ')' for negative number wrap
                int j = i + 1;
                while (j < input.length() &&
                        (Character.isDigit(input.charAt(j)) || input.charAt(j) == '.')) {
                    j++;
                }
                if (j == input.length() || input.charAt(j) != ')') {
                    isValid = false;
                }
            } else if (isOperator(String.valueOf(input.charAt(i))) &&
                    ((!Character.isDigit(prevChar) && prevChar != ')' && prevChar != '.') ||
                    (i + 1 < input.length() && input.charAt(i + 1) != '(' &&
                            !Character.isDigit(input.charAt(i + 1))))) {
                // check that operator has digits before and after it, or
                // has closing and opening parenthesis before and after respectively,
                // that would result to a number. Wrapped negative numbers will be caught
                // before this check, so all other operators must have valid operands around them
                isValid = false;
            } else if ((Character.isDigit(input.charAt(i)) &&
                    (i > 0 && prevChar == ')' || i + 1 < input.length() && input.charAt(i + 1) == '(')) ||
                    (input.charAt(i) == '(' && i > 0 && prevChar == ')' ||
                            input.charAt(i) == ')' && i + 1 < input.length() &&
                                    input.charAt(i + 1) == '(')) {
                // numbers and different parenthesis segments must have operators between them. Not allowed: (5)(6)
                isValid = false;
            } else if (input.charAt(i) == '0' && prevChar == '/') {
                // division by zero
                int j = i;
                while (input.charAt(j) == '0' || input.charAt(j) == '.') {
                    j++;
                }
                if (!Character.isDigit(input.charAt(j))) {
                    // not digit other than zero
                    isValid = false;
                }
            } else if (!Character.isDigit(input.charAt(i)) &&
                    !isOperator(String.valueOf(input.charAt(i))) &&
                    input.charAt(i) != '.' && input.charAt(i) != '(' && input.charAt(i) != ')') {
                isValid = false;
            }
            prevChar = input.charAt(i);
        }

        if (numLeftParenthesis != numRightParenthesis) {
            isValid = false;
        }

        return isValid;
    }

    private static double evaluate(String input) throws ArithmeticException {
        ReversePolishConverter converter = new ReversePolishConverter();
        String rpn = converter.toPostFixNotation(input);

        // Two Stack Evaluation of Reverse Polish Notation
        StackImpl<Double> operandStack = new StackImpl<>();

        Scanner scanner = new Scanner(rpn);
        String token;
        while (scanner.hasNext()) {
            token = scanner.next();

            if (isNumber(token)) {
                // token is an operand
                operandStack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                // token is an operator
                double operand2 = operandStack.pop();
                double operand1 = operandStack.pop();

                char operator = token.charAt(0); // operators are 1 character long

                double result = performOperation(operand1, operand2, operator);

                operandStack.push(result);
            } else {
                // evaluate unary functions, sin, cos, tan, log
            }
        }

        return operandStack.pop();
    }

    public static boolean isNumber(String token) {
        // Acceptable: 123, 43, -45, +345, 0.123, 1.234, -0.34, +0.23
        // Negative Fractions greater than -1 must have leading 0.
        return Character.isDigit(token.charAt(0)) || (token.length() > 1 &&
                Character.isDigit(token.charAt(1)));
    }

    public static boolean isOperator(String token) {
        return token.equals("+") ||
                token.equals("-") ||
                token.equals("*") ||
                token.equals("/");
    }

    private static double performOperation(double operand1, double operand2, char operator) {
        double result = 1;
        switch (operator) {
            case '+':
                result = operand1 + operand2;
                break;
            case '-':
                result = operand1 - operand2;
                break;
            case '*':
                result = operand1 * operand2;
                break;
            case '/':
                if (operand2 == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }
                result = operand1 / operand2;
                break;
            default:
                // can be extended to evaluate unary functions - log, sin, cos etc.
        }
        return result;
    }
}
