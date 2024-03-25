package ubb.models.statements;

import ubb.exceptions.InterpreterException;
import ubb.models.ProgramState;
import ubb.models.expressions.IExpression;
import ubb.models.types.BoolType;
import ubb.models.types.Type;
import ubb.models.values.BoolValue;
import ubb.models.values.IValue;
import ubb.models.adts.MyIDictionary;
import ubb.models.adts.MyIHeap;
import ubb.models.adts.MyIStack;
public class ForStatement implements IStatement {
    private final IStatement initStatement;
    private final IExpression conditionExpression;
    private final IStatement incrementStatement;
    private final IStatement innerStatement;

    public ForStatement(IStatement initStatement, IExpression conditionExpression, IStatement incrementStatement, IStatement innerStatement)
    {
        this.initStatement = initStatement;
        this.conditionExpression = conditionExpression;
        this.incrementStatement = incrementStatement;
        this.innerStatement = innerStatement;
    }

    /**
     * Executes a ForStatement, pushing the initStatement, the WhileStatement and the incrementStatement onto the program stack.
     *
     * @param currentState The current program state.
     * @return The updated program state after executing the for statement.
     * @throws InterpreterException If the conditionExpression does not evaluate to a boolean.
     * @throws InterpreterException          If there is an error during expression evaluation.
     */
    @Override
    public ProgramState execute(ProgramState currentState) throws InterpreterException {
        MyIDictionary<String, IValue> symbolTable = currentState.getSymbolTable();
        MyIHeap heapTable = currentState.getHeapTable();
        MyIStack<IStatement> programStack = currentState.getStack();

        // Push the initStatement onto the program stack
        programStack.push(initStatement);

        // Push the WhileStatement onto the program stack
        programStack.push(new WhileStatement(conditionExpression, new CompoundStatement(innerStatement, incrementStatement)));

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeTable) throws InterpreterException {
        initStatement.typeCheck(typeTable.copy());

        Type conditionType = conditionExpression.typeCheck(typeTable.copy());

        // Check if the conditionExpression evaluates to a boolean
        if (!conditionType.equals(new BoolType()))
            throw new InterpreterException("Condition expression used in for statement cannot be evaluated as boolean!");

        incrementStatement.typeCheck(typeTable.copy());
        innerStatement.typeCheck(typeTable.copy());

        return typeTable;
    }

    @Override
    public String toString() {
        return "for(" + initStatement + "; " + conditionExpression + "; " + incrementStatement + ") " + innerStatement;
    }
}
