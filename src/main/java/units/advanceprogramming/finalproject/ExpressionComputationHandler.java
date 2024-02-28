/*
 * Matteo Bille' [Mat. IN2000125]
 * Advanced programming final project
 */
package units.advanceprogramming.finalproject;

import units.advanceprogramming.projectexception.NoResultsException;
import units.advanceprogramming.projectexception.NotFoundVariableException;

public interface ExpressionComputationHandler {

  double solveComputationRequest(String[] equations, ListOfPoints points, ComputationKind compKind) throws NotFoundVariableException, NoResultsException;

}
