/*
Sub-problem of problem 1.
1. Uses Dijkstra's shunting yard algorithm to convert infix notation to postfix notation
2. ArithmeticExpressionTokenizer is written to distinctly get one token per iteration. [when infix string has no space between tokens]
    Can identify positive and negative real numbers. Can identify basic operators + - * /.
    Can identify unary negative sign (-) ONLY if the number is surrounded in parenthesis ie. (-3).
    Can be extended to include logarithmic, trigonometric and other unary functions.
 */
public class ReversePolishConverter {
    public String toPostFixNotation(String infix) {
        StringBuilder outputQ = new StringBuilder();
        StackImpl<Character> operatorStack = new StackImpl<>();
        ArithmeticExpressionTokenizer tokenizer = new ArithmeticExpressionTokenizer(infix);

        // shunting yard algorithm has no way of handling dangling operators ie. +2, -2,
        // 2 + (+2), so they must be filtered, either by invalidating input, or handling these
        // cases on the tokenizer to return normal numbers.

        String token;
        while (tokenizer.hasNext()) {
            token = tokenizer.nextToken();
            if (ArithmeticExpressionEvaluator.isNumber(token)) {
                outputQ.append(token).append(" ");
            } else if (ArithmeticExpressionEvaluator.isOperator(token)) {
                char operator = token.charAt(0);

                while (!operatorStack.isEmpty() &&
                        (precedenceCompareTo(operator, operatorStack.peek()) < 0 ||
                                (precedenceCompareTo(operator, operatorStack.peek()) == 0 &&
                                        isLeftAssociative(operator))) &&
                        operatorStack.peek() != '(') {
                    outputQ.append(operatorStack.pop()).append(" ");
                }

                operatorStack.push(operator);
            } else if (token.charAt(0) == '(') {
                operatorStack.push(token.charAt(0));
            } else if (token.charAt(0) == ')') {
                while (operatorStack.peek() != '(') {
                    outputQ.append(operatorStack.pop()).append(" ");
                }
                operatorStack.pop();
            }
        }

        while (!operatorStack.isEmpty()) {
            outputQ.append(operatorStack.pop()).append(" ");
        }
        return outputQ.toString();
    }

    private boolean isLeftAssociative(char operator) {
        return operator == '-' || operator == '/';
    }

    private int precedenceCompareTo(char op1, char op2) {
        return Integer.compare(operatorPrecedenceScore(op1), operatorPrecedenceScore(op2));
    }

    private int operatorPrecedenceScore(char operator) {
        int score;
        if (operator == '/') {
            score = 3;
        } else if (operator == '*') {
            score = 2;
        } else if (operator == '-') {
            score = 1;
        } else {
            score = 0;
        }
        return score;
    }


    public static class ArithmeticExpressionTokenizer {
        private final String expression;
        private int currentPtr;

        // cache
        private String prevToken;

        public ArithmeticExpressionTokenizer(String expression) {
            this.expression = expression;
            this.currentPtr = 0;
        }

        // gets positive and negative numbers, operators, parenthesis.
        // Can extend to unary functions sin(5), log(5) etc.
        public String nextToken() {
            if (!hasNext()) {
                throw new IndexOutOfBoundsException("Stream pointer has reached end of string");
            }

            int tokenFirstPtr = this.currentPtr;

            incrementPtrTillDifferentTokenEncountered();

            this.prevToken = this.expression.substring(tokenFirstPtr, this.currentPtr);
            return this.prevToken;
        }

        private void incrementPtrTillDifferentTokenEncountered() {
            if (Character.isDigit(this.expression.charAt(this.currentPtr))) {
                while (this.currentPtr < this.expression.length() &&
                        (Character.isDigit(this.expression.charAt(this.currentPtr)) ||
                                this.expression.charAt(this.currentPtr) == '.')) {
                    this.currentPtr++;
                }
            } else if (this.expression.charAt(this.currentPtr) == '+' ||
                    this.expression.charAt(this.currentPtr) == '*' ||
                    this.expression.charAt(this.currentPtr) == '/' ||
                    this.expression.charAt(this.currentPtr) == '(' ||
                    this.expression.charAt(this.currentPtr) == ')') {
                this.currentPtr++;
            } else if (this.expression.charAt(this.currentPtr) == '-') {
                if (this.prevToken.equals("(")) {
                    this.currentPtr++;
                    while (this.currentPtr < this.expression.length() &&
                            (Character.isDigit(this.expression.charAt(this.currentPtr)) ||
                                    this.expression.charAt(this.currentPtr) == '.')) {
                        this.currentPtr++;
                    }
                } else {
                    this.currentPtr++;
                }
            }
        }

        public boolean hasNext() {
            return this.currentPtr < this.expression.length();
        }
    }

    // test client
    public static void main(String[] args) {
        String infix = "(2+3*45-((-4)+2))";
        ReversePolishConverter reversePolishConverter = new ReversePolishConverter();
        System.out.println(reversePolishConverter.toPostFixNotation(infix));
    }
}
