# Expression Evaluation Using a Stack-based Calculator Using Java

This folder contains a Java program, called "calculator.java", that implements a stack-based calculator.

## Compilation and Execution

To compile the Java program, navigate to the project directory and run:

```bash
javac calculator.java
```

To run the program, navigate to the project directory and run:

```bash
java Calculator
```

The program will prompt the user to enter an arithmetic expression in infix notation. Possible operators are \*, /, +, - and parentheses are used to specify the order of operations.

```bash
Enter the arithmetic expression in infix notation:
(-5+3)*3
```

### Output

The program will print:

- The expression converted to prefix notation
- The result value of the expression

### Extra Credit

The program also checks the balance of the parentheses in the expression. If the input has an unbalanced set of parentheses, the program will throw an exception:

```bash
Exception in thread "main" java.lang.IllegalArgumentException: Unbalanced set of parenthesis
```

### Screenshots

The screenshots in the folder [screenshots](screenshots) show the successful execution of the program with different inputs.

### Contributors

Erdenetulga Bilegdemberel
Jonny Eduardo Banach
