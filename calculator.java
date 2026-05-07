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
                char nextChar = c;
                while (Character.isDigit(nextChar)) {
                    digits += nextChar;
                    nextChar = index != expression.length() - 1 ? expression.charAt(index + 1) : '\0';
                    if (Character.isDigit(nextChar)) {
                        index++;
                    }
                }
                prevToken = digits;
                tokens.push(digits);
            } else if (isOperator(c) || c == '(' || c == ')') {
                // Maps minus to ^ operator
                if (c == '-' && (prevToken.isEmpty() || prevToken.equals("("))) {
                    prevToken = String.valueOf(c);
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the arithmetic expression in infix notation: ");
        String expression = scanner.nextLine();
        scanner.close();

        Stack<String> tokens = tokenize(expression);
        System.out.println("Tokens extracted: " + tokens);

        // Reverse tokens and swap parentheses
        Collections.reverse(tokens);
        Stack<String> swapped = new Stack<String>();
        for (String c : tokens) {
            if (c.equals("(")) {
                swapped.push(")");
            } else if (c.equals(")")) {
                swapped.push("(");
            } else {
                swapped.push(c);
            }
        }

        System.out.println("Swapped: " + swapped);

        // Convert to postfix notation
        Stack<String> postfix = new Stack<String>();
        Stack<String> operators = new Stack<String>();
        for (String c : swapped) {
            // Digits
            if (c.matches("\\d+")) {
                postfix.push(c);
            } else if (c.equals("(")) {
                operators.push(c);
            } else if (c.equals(")")) {
                String op = "";
                while (!op.equals("(")) {
                    if (operators.isEmpty()) {
                        throw new IllegalArgumentException("Unbalanced set of parenthesis");
                    }
                    op = operators.pop();
                    if (!op.equals("(")) {
                        postfix.push(op);
                    }
                }
            } else {
                // Handle precedence of operators
                while (getPrecedence(operators.isEmpty() ? "" : operators.peek()) > getPrecedence(c)) {
                    postfix.push(operators.pop());
                }
                operators.push(c);
            }
        }

        // Handle remaining operators
        while (!operators.isEmpty()) {
            if (operators.peek().equals("(") || operators.peek().equals(")")) {
                throw new IllegalArgumentException("Unbalanced set of parenthesis");
            }
            postfix.push(operators.pop());
        }

        // Convert postfix to prefix
        Stack<String> prefix = new Stack<>();
        prefix.addAll(postfix);
        Collections.reverse(prefix);

        // Print prefix
        String output = "Prefix: ";
        for (String c : prefix) {
            output += c + " ";
        }
        System.out.println(output);
    }
}
