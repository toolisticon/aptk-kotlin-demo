package io.toolisticon.spiap.interationtest;

import io.toolisticon.example.spiapexample.api.DecimalCalculationOperation

class SquareDecimalOperationServiceImpl : DecimalCalculationOperation {

    override fun invokeOperation(operand1: Int,  operand2: Int) : Int {

        if (operand2 <= 0) {
            return 0
        }

        var result = operand1

        if (operand2 > 1) {
            for (i in 2..operand2) {
                result = result * operand1
            }
        }

        return result
    }


}
