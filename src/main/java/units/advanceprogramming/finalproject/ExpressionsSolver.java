/*
 * Matteo Bille' [Mat. IN2000125]
 * Advanced programming final project
 */
package units.advanceprogramming.finalproject;

import units.advanceprogramming.projectexception.NoResultsException;
import units.advanceprogramming.projectexception.NotFoundVariableException;
import units.advanceprogramming.expressionparser.Constant;
import units.advanceprogramming.expressionparser.Operator;
import units.advanceprogramming.expressionparser.Variable;
import units.advanceprogramming.expressionparser.Node;
import units.advanceprogramming.expressionparser.Parser;
import units.advanceprogramming.expressionparser.Operator.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.ArrayUtils;

public class ExpressionsSolver {

  private final ListOfPoints points;
  private final String[] equations;

  public ExpressionsSolver(String[] equations, ListOfPoints points) {
    this.points = points;
    this.equations = equations;
  }

  public double getResult(ComputationKind cK) throws NotFoundVariableException, NoResultsException {
    double[][] ResultArray = new double[equations.length][points.length()];
    for (int i = 0; i < equations.length; i++) {
      ResultArray[i] = solve(equations[i]);
    }

    switch (cK) {
      case MIN:
        return findMin(ResultArray);
      case MAX:
        return findMax(ResultArray);
      case AVG:
        return findAvg(ResultArray[0]);
      default:
        return 0;
    }
  }

  private double findMax(double[][] results) throws NoSuchElementException, NoResultsException {

    double[] allFunctionsResult = new double[0];
    for (double[] result : results) {
      if (result.length != 0) {
        allFunctionsResult = ArrayUtils.addAll(allFunctionsResult, result);
      }
    }
    if (allFunctionsResult.length == 0) {
      System.out.println(allFunctionsResult.length);
      throw new NoResultsException("All the points are out of the domain of the function");
    }
    double max = allFunctionsResult[0];
    for (int idx = 1; idx < allFunctionsResult.length; ++idx) {
      if (allFunctionsResult[idx] > max) {

        max = allFunctionsResult[idx];
      }
    }
    return max;
  }

  private double findMin(double[][] results) throws NoResultsException {
    double[] allFunctionsResult = new double[0];
    for (double[] result : results) {
      if (result.length != 0) {
        allFunctionsResult = ArrayUtils.addAll(allFunctionsResult, result);
      }
    }
    if (allFunctionsResult.length == 0) {
      System.out.println(allFunctionsResult.length);
      throw new NoResultsException("All the points are out of the domain of the function");
    }

    double min = allFunctionsResult[0];
    for (int idx = 1; idx < allFunctionsResult.length; idx++) {
      if (allFunctionsResult[idx] < min) {
        min = allFunctionsResult[idx];
      }
    }
    return min;
  }

  private double findAvg(double[] result) {
    double average = 0;
    for (double element : result) {
      average += element;
    }
    return average / result.length;
  }

  private double[] solve(String equation) throws NotFoundVariableException {
    Parser pa = new Parser(equation);
    Node firstNode = pa.parse();
    List<Double> result = new ArrayList<>();

    if (firstNode instanceof Variable) {
      for (double[] tuple : points) {

        result.add(solveEasyCaseNode((Variable) firstNode, points.getVariableNames(), tuple));
      }
    } else if (firstNode instanceof Constant) {
      for (int i = 0; i < points.length(); i++) {
        result.add(((Constant) firstNode).getValue());
      }
    } else {

      for (int i = 0, j = 0; j < points.length(); j++, i++) {
        double[] arr = new double[2];
        try {
          result.add(solve(firstNode, arr, points.getVariableNames(), points.getSpacePointsByLine(j)));
        } catch (ArithmeticException ex) {
          i -= 1;
        }
      }
    }

    if (result.isEmpty()) {
      double[] emptyArray = new double[0];
      return emptyArray;
    }

    return result.stream().mapToDouble(Double::doubleValue).toArray();
  }

  private double solve(Node node, double[] result, String[] varName, double[] varValue) throws NotFoundVariableException, ArithmeticException {
    for (int i = 0; i < node.getChildren().size(); i++) {
      if (node.getChildren().get(i) instanceof Constant) {
        result[i] = Double.parseDouble(node.getChildren().get(i).toString());
      } else if (node.getChildren().get(i) instanceof Operator) {
        double[] arr = new double[2];
        result[i] = solve(node.getChildren().get(i), arr, varName, varValue);
      } else if (node.getChildren().get(i) instanceof Variable) {
        result[i] = solveEasyCaseNode((Variable) node.getChildren().get(i), varName, varValue);
      }
    }
    if (((Operator) node).getType() == Type.DIVISION && result[1] == 0) {
      throw new ArithmeticException("Divide for zero");
    }
    return ((Operator) node).getType().getFunction().apply(result);
  }

  private double solveEasyCaseNode(Variable varNode, String[] varName, double[] varValue) throws NotFoundVariableException {
    for (int j = 0; j < varName.length; j++) {
      if (varNode.getName().equals(varName[j])) {
        return varValue[j];
      }
    }
    throw new NotFoundVariableException(String.format("Variable %s not found", varNode.getName()));
  }
}
