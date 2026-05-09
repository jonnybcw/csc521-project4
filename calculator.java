import java.util.Stack;
import java.util.Collections;
import java.util.Scanner;

class Calculator {
    // Check if the character is an operator
    public static Boolean isOperator(char c) {
        return c == '/' || c == '*' || c == '+' || c == '-';
    }

    // Get the precedence of the operator
    public static Integer getPrecedence(String op) {
        switch (op) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return 0;
        }
    }

    // Convert the expression to a stack of tokens
    public static Stack<String> tokenize(String expression) {
        Stack<String> tokens = new Stack<String>();
        Integer index = 0;
        String prevToken = "";
        while (index < expression.length()) {
            char c = expression.charAt(index);
            // Skip spaces
            if (c == ' ') {
                index++;
                continue;
            }

            // Digits
            if (Character.isDigit(c)) {
                String digits = "";
                while (index < expression.length() && Character.isDigit(expression.charAt(index))) {
                    digits += expression.charAt(index);
                    index++;
                }
                index--; // Adjust for outer loop increment
                prevToken = digits;
                tokens.push(digits);
            } else if (isOperator(c) || c == '(' || c == ')') {
                // Maps unary minus to ^ symbol
                if (c == '-' && (prevToken.isEmpty() || prevToken.equals("("))) {
                    prevToken = "^";
                    tokens.push("^");
                } else {
                    prevToken = String.valueOf(c);
                    tokens.push(String.valueOf(c));
                }
            } else {
                throw new IllegalArgumentException("Invalid token");
            }
            index++;
        }
        return tokens;
    }

    // Stack evaluation of prefix expression
    public static byte evaluatePrefix(Stack<String> prefix) {
        Stack<Byte> s = new Stack<>();

        // Scan the prefix expression from right to left
        for (int i = prefix.size() - 1; i >= 0; i--) {
            String token = prefix.get(i);

            // If token is a number
            if (token.matches("\\d+")) {
                s.push((byte) Integer.parseInt(token));
            }
            else if (token.equals("^")) {
                byte val = s.pop();
                s.push((byte) (-val));
            }
            else if (isOperator(token.charAt(0))) {
                byte d1 = s.pop(); // Left operand
                byte d2 = s.pop(); // Right operand
                int result = 0;
                char op = token.charAt(0);

                switch (op) {
                    case '+':
                        result = d1 + d2;
                        checkOverflow(result);
                        break;
                    case '-':
                        result = d1 - d2;
                        checkOverflow(result);
                        break;
                    case '*':
                        result = d1 * d2;
                        checkOverflow(result);
                        break;
                    case '/':
                        if (d2 == 0) throw new ArithmeticException("Division by zero");
                        // Round up remainder
                        result = (int) Math.ceil((double) d1 / d2);
                        break;
                }
                s.push((byte) result);
            }
        }
        return s.pop(); // Final value
    }

    private static void checkOverflow(int result) {
        if (result < -128 || result > 127) {
            System.out.println("overflow occurs!");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the arithmetic expression in infix notation: ");
        String expression = scanner.nextLine();
        scanner.close();

        try {
        Stack<String> tokens = tokenize(expression);
        // Infix to Prefix conversion logic
        Collections.reverse(tokens);
        Stack<String> swapped = new Stack<String>();
        for (String c : tokens) {
            if (c.equals("("))  swapped.push(")");
            else if (c.equals(")")) swapped.push("(");
            else    swapped.push(c);
        }
            Stack<String> postfix = new Stack<String>();
            Stack<String> operators = new Stack<String>();
            for (String c : swapped) {
                if (c.matches("\\d+") || c.equals("^")) postfix.push(c);
                else if (c.equals("(")) operators.push(c);
                else if (c.equals(")")) {
                    while (!operators.isEmpty() && !operators.peek().equals("("))   postfix.push(operators.pop());
                    if (operators.isEmpty()) throw new IllegalArgumentException("Unbalanced set of parenthesis");
                    operators.pop();
                }
                else {
                    while (!operators.isEmpty() && getPrecedence(operators.peek()) > getPrecedence(c)) {
                        postfix.push(operators.pop());
                    }
                    operators.push(c);
                }
            }
            while (!operators.isEmpty()) {
                if (operators.peek().equals("(")) throw new IllegalArgumentException("Unbalanced set of parenthesis");
                postfix.push(operators.pop());
            }

            Stack<String> prefix = new Stack<>();
            prefix.addAll(postfix);
            Collections.reverse(prefix);

            // Output Prefix
            System.out.print("Prefix: ");
            for (String s : prefix) System.out.print(s + " ");
            System.out.println();

            // Output Result
            byte finalResult = evaluatePrefix(prefix);
            System.out.println("Value: " + finalResult);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}