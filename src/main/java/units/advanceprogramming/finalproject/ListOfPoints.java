/*
 * Matteo Bille' [Mat. IN2000125]
 * Advanced programming final project
 */
package units.advanceprogramming.finalproject;

import units.advanceprogramming.projectexception.DifferentLengthListsException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListOfPoints implements Iterable<double[]> {

  private String[] variableNames;
  private List spacePoints;

  public ListOfPoints(ExtendedVariable[] variableList, String Method) throws DifferentLengthListsException {
    spacePoints = new ArrayList<double[]>();
    variableNames = new String[variableList.length];
    for (int i = 0; i < variableList.length; i++) {
      variableNames[i] = variableList[i].name;
    }

    switch (Method) {
      case "LIST":
        getSpacePointsListMethod(variableList);
        break;
      case "GRID":
        getSpacePointsGridMethod(variableList);
        break;
      default:
        break;
    }
  }

  public ListOfPoints(String[] variableNames) {
    this.variableNames = variableNames;
    spacePoints = new ArrayList<ArrayList>();

  }

  public int length() {
    return spacePoints.size();
  }

  public String[] getVariableNames() {
    return variableNames;
  }

  public void setVariableNames(String[] variableNames) {
    this.variableNames = variableNames;
  }

  public List getSpacePoints() {
    return spacePoints;
  }

  public double[] getSpacePointsByLine(int j) {
    return (double[]) spacePoints.get(j);
  }

  private void getSpacePointsListMethod(ExtendedVariable[] variableList) throws DifferentLengthListsException {

    boolean flag = true;
    for (int i = 0; i < variableList.length - 1; i++) {
      if (variableList[i].length != variableList[i + 1].length) {
        throw new DifferentLengthListsException("Number of elements for each variable is different");
      }
    }

    if (flag == true) {

      for (int j = 0; j < variableList[0].getValues().length; j++) {

        List<Double> points = new ArrayList<>();
        for (ExtendedVariable variableList1 : variableList) {
          points.add(variableList1.getValuesByIndex(j));
        }
        double[] tuple;
        tuple = points.stream().mapToDouble(Double::doubleValue).toArray();
        spacePoints.add(tuple);
      }
    }
  }

  private void getSpacePointsGridMethod(ExtendedVariable[] variableList) {
    List<Double> cordinates = new ArrayList<>();
    getCartesianProduct(0, (ArrayList<Double>) cordinates, variableList);

  }

  private void getCartesianProduct(int index, ArrayList<Double> temp, ExtendedVariable[] variableList) {
    if (index == variableList.length) {
      List<Double> points = new ArrayList<>(temp);
      double[] tuple;
      tuple = points.stream().mapToDouble(Double::doubleValue).toArray();
      spacePoints.add(tuple);
    } else {
      for (double value : variableList[index].getValues()) {
        temp.add(value);
        getCartesianProduct(index + 1, temp, variableList);
      }
    }
    if (!temp.isEmpty()) {
      temp.remove(temp.size() - 1);
    }
  }

  @Override
  public String toString() {
    String result = "Points: \r\n";

    result += String.format("(");
    for (int i = 0; i < variableNames.length; i++) {
      if (i != variableNames.length - 1) {
        result += variableNames[i] + ",";
      } else {
        result += variableNames[i] + "";
      }
    }
    result += String.format(")\r\n");
    result += "[";

    for (Object spacePoint : spacePoints) {
      result += "(";
      for (int i = 0; i < ((double[]) spacePoint).length; i++) {
        if (i != ((double[]) spacePoint).length - 1) {
          result += ((double[]) spacePoint)[i] + ",";
        } else {
          result += ((double[]) spacePoint)[i] + "";
        }
      }
      result += ")";
    }
    result += "]";
    return result;
  }

  @Override
  public Iterator<double[]> iterator() {
    return spacePoints.iterator();
  }

}
