package org.cqframework.cql.cql2elm.model.invocation;

import org.hl7.elm.r1.AnyInCodeSystem;
import org.hl7.elm.r1.CodeSystemRef;
import org.hl7.elm.r1.Expression;
import org.hl7.elm.r1.InCodeSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InCodeSystemInvocation extends OperatorExpressionInvocation {
    public InCodeSystemInvocation(InCodeSystem expression) {
        super(expression);
    }

    @Override
    public Iterable<Expression> getOperands() {
        List<Expression> result = new ArrayList<>();
        result.add(((InCodeSystem)expression).getCode());
        if (((InCodeSystem)expression).getCodesystem() instanceof CodeSystemRef) {
            result.add(((InCodeSystem)expression).getCodesystem());
        }
        else {
            result.add(((InCodeSystem)expression).getCodesystemEx());
        }
        return result;
    }

    @Override
    public void setOperands(Iterable<Expression> operands) {
        int i = 0;
        for (Expression operand : operands) {
            switch (i) {
                case 0: ((InCodeSystem)expression).setCode(operand); break;
                case 1:
                    if (operand instanceof CodeSystemRef) {
                        ((InCodeSystem)expression).setCodesystem((CodeSystemRef)operand);
                    }
                    else {
                        ((InCodeSystem)expression).setCodesystemEx(operand);
                    }
                break;
            }
            i++;
        }

        if (i != 2) {
            throw new IllegalArgumentException("Binary operator expected");
        }
    }
}
