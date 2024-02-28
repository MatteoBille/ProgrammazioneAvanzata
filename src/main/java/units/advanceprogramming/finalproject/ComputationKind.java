/*
 * Matteo Bille' [Mat. IN2000125]
 * Advanced programming final project
 */
package units.advanceprogramming.finalproject;

public enum ComputationKind implements Operations {
  MIN("MIN"), MAX("MAX"), AVG("AVG"), COUNT("COUNT");
  public final String computationalKindString;

  private ComputationKind(String kind) {
    this.computationalKindString = kind;
  }
}
