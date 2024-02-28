/*
 * Matteo Bille' [Mat. IN2000125]
 * Advanced programming final project
 */
package units.advanceprogramming.finalproject;

import units.advanceprogramming.projectexception.NoResultsException;
import units.advanceprogramming.projectexception.DifferentLengthListsException;
import units.advanceprogramming.projectexception.NotFoundVariableException;
import java.io.IOException;

public class StartServer {

  public static void main(String[] args) throws IOException, DifferentLengthListsException, NotFoundVariableException, NoResultsException {
    int port = 0;
    if (args[0] != null) {
      port = Integer.parseInt(args[0]);
      System.out.println("Port: " + port);
      LineProcessingServer myServ = new LineProcessingServerMathExpressionRequest(port, "BYE");
      myServ.run();
    } else {
      System.out.println("missing port number");
    }
  }
}
