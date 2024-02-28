/*
 * Matteo Bille' [Mat. IN2000125]
 * Advanced programming final project
 */
package units.advanceprogramming.finalproject;

public class LineProcessingServerWithStatistics extends LineProcessingServer {

  protected int totalNumberOfClient;
  protected int numRequest;
  protected long totalTime;
  protected long avgTime;
  protected long maxTime;

  public LineProcessingServerWithStatistics(int port, String quitCommand) {
    super(port, quitCommand);
    maxTime = 0;
    avgTime = 0;
    totalTime = 0;
    numRequest = 0;
    totalNumberOfClient = 0;
  }

  public synchronized int getNumRequest() {
    return numRequest;
  }

  public synchronized long getAvgTime() {
    return avgTime;
  }

  public synchronized long getMaxTime() {
    return maxTime;
  }

  public synchronized void incrementNumRequest() {
    numRequest += 1;
  }

  public synchronized int getTotalNumberOfClient() {
    this.totalNumberOfClient += 1;
    return totalNumberOfClient;
  }

  public synchronized void modifyTimeVariables(long time) {
    totalTime += time;
    avgTime = totalTime / numRequest;
    if (time > maxTime) {
      maxTime = time;
    }
  }

}
