package ubb.models.statements;

import ubb.exceptions.InterpreterException;
import ubb.models.*;
import ubb.models.ProgramState;
import ubb.models.adts.MyIDictionary;
import ubb.models.expressions.IExpression;
import ubb.models.expressions.RelationalExpression;
import ubb.models.types.BoolType;
import ubb.models.types.Type;
import ubb.models.values.BoolValue;
import ubb.models.values.IValue;
import ubb.models.adts.MyIStack;

public class SwitchStatement implements IStatement {

    private final IStatement firstStatement, secondStatement;

    private final IStatement defaultStatement;

    private final IExpression switchExpression, firstCaseExpression, secondCaseExpression;

    public SwitchStatement(IExpression switchExpression, IExpression firstCaseExpression,
                           IExpression secondCaseExpression, IStatement firstCaseStatement,
                           IStatement secondCaseStatement, IStatement defaultStatement) {
        this.switchExpression = switchExpression;
        this.firstCaseExpression = firstCaseExpression;
        this.secondCaseExpression = secondCaseExpression;
        this.firstStatement = firstCaseStatement;
        this.secondStatement = secondCaseStatement;
        this.defaultStatement = defaultStatement;
    }

    //transform in if statement with inner if statement

    public ProgramState execute(ProgramState currentState) throws InterpreterException {

        IStatement convertedStatement =
                new IfStatement(
                        new RelationalExpression(switchExpression, firstCaseExpression, "=="),
                        firstStatement,
                        new IfStatement(
                                new RelationalExpression(switchExpression, secondCaseExpression, "=="),
                                secondStatement,
                                defaultStatement
                        )
                );

        currentState.getStack().push(convertedStatement);

        return null;


    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeTable) throws InterpreterException {
        Type switchExpressionType = switchExpression.typeCheck(typeTable);
        Type firstCaseExpressionType = firstCaseExpression.typeCheck(typeTable);
        Type secondCaseExpressionType = secondCaseExpression.typeCheck(typeTable);

        if (!switchExpressionType.equals(firstCaseExpressionType))
            throw new InterpreterException("Switch expression and first case expression do not have the same type!");

        if (!switchExpressionType.equals(secondCaseExpressionType))
            throw new InterpreterException("Switch expression and second case expression do not have the same type!");

        return typeTable;
    }

    @Override
    public String toString() {
        return "Switch(" + switchExpression + ") Case(" + firstCaseExpression + ") Do(" + firstStatement + ") Case(" + secondCaseExpression + ") Do(" + secondStatement + ") Default(" + defaultStatement + ")";
    }




}
