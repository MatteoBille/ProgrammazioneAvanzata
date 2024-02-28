/*
 * Matteo Bille' [Mat. IN2000125]
 * Advanced programming final project
 */
package units.advanceprogramming.finalproject;

import units.advanceprogramming.projectexception.MalformedRequestException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExtendedVariable {

  public final String name;
  public final float step;
  private double[] values;
  public final int length;

  public ExtendedVariable(String variableValuesFunction) throws MalformedRequestException {
    String[] parsed = variableValuesFunction.split(":");

    if (parsed.length != 4) {
      throw new MalformedRequestException(String.format("variable %s has bad values specification", parsed[0]));
    }
    name = parsed[0];

    BigDecimal minBigD = new BigDecimal(parsed[1]);
    BigDecimal maxBigD = new BigDecimal(parsed[3]);
    BigDecimal stepBigD = new BigDecimal(parsed[2]);

    step = stepBigD.longValue();
    int scale = stepBigD.scale();
    length = (int) (Math.abs(maxBigD.subtract(minBigD).doubleValue()) / stepBigD.doubleValue()) + 1;

    values = new double[length];
    BigDecimal nextValueBigD = minBigD;
    BigDecimal tempValueBigD = nextValueBigD;
    for (int i = 0; tempValueBigD.doubleValue() < maxBigD.add(stepBigD.divide(new BigDecimal("2.0"))).doubleValue(); i++) {
      nextValueBigD = tempValueBigD.setScale(scale, RoundingMode.CEILING);
      values[i] = nextValueBigD.doubleValue();
      tempValueBigD = nextValueBigD.add(stepBigD);

    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ExtendedVariable other = (ExtendedVariable) obj;

    if (values.length != other.values.length) {
      return false;
    }
    if (values[0] != other.values[0]) {
      return false;
    }
    if (values[values.length - 1] != other.values[other.values.length - 1]) {
      return false;
    }
    return step != other.step;
  }

  public double[] getValues() {
    return values;
  }

  public double getValuesByIndex(int j) {
    return values[j];
  }

  @Override
  public String toString() {
    String output = name + ": ";
    for (int i = 0; i < values.length; i++) {
      output += String.format("%2f ", values[i]);
    }
    return output;
  }

}
