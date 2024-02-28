/*
 * Matteo Bille' [Mat. IN2000125]
 * Advanced programming final project
 */
package units.advanceprogramming.finalproject;

public enum StatisticalRequest implements Operations {
  STAT_REQS("STAT_REQS"), STAT_AVG_TIME("STAT_AVG_TIME"), STAT_MAX_TIME("STAT_MAX_TIME");
  public final String timeOperationsString;

  private StatisticalRequest(String kind) {
    this.timeOperationsString = kind;
  }
}
