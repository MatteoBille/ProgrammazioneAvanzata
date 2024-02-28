/*
 * Matteo Bille' [Mat. IN2000125]
 * Advanced programming final project
 */
package units.advanceprogramming.finalproject;

import units.advanceprogramming.projectexception.NoResultsException;
import units.advanceprogramming.projectexception.DifferentLengthListsException;
import units.advanceprogramming.projectexception.MalformedRequestException;
import units.advanceprogramming.projectexception.NotFoundVariableException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LineProcessingServerMathExpressionRequest extends LineProcessingServerWithStatistics {

  public LineProcessingServerMathExpressionRequest(int port, String quitCommand) {
    super(port, "BYE");
  }

  @Override
  public void run() throws IOException {

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      while (true) {
        try {
          final Socket socket = serverSocket.accept();

          DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

          ClientHandler clientHandler = new ClientHandlerMathExpressionRequest(socket, this);
          clientHandler.start();

        } catch (IOException e) {
          DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
          LocalDateTime now;
          now = LocalDateTime.now();
          System.err.println("[" + dtf.format(now) + "] " + String.format("Cannot accept connection due to %s", e));
        }

      }
    }
  }

  @Override
  public String process(String input) throws NoResultsException, MalformedRequestException, DifferentLengthListsException, NotFoundVariableException {

    long startTime = System.currentTimeMillis();
    String resp = null;
    Operations type = getRequestType(input);

    if (type instanceof StatisticalRequest) {
      resp = getStatisticalRequest((StatisticalRequest) type);
    }

    if (type instanceof ComputationKind) {
      resp = getCalculationRequest(input, (ComputationKind) type);
    }

    long stopComputationTime = System.currentTimeMillis();

    if (type instanceof ComputationKind) {
      incrementNumRequest();
      modifyTimeVariables(stopComputationTime - startTime);
    }

    return "OK;" + String.format("%.3f;", ((double) ((stopComputationTime - startTime) / 1000.0))) + resp;
  }

  private Operations getRequestType(String input) throws MalformedRequestException, DifferentLengthListsException, NotFoundVariableException, NoResultsException {
    String[] requestParts = input.split(";");
    if (requestParts[0].split("_")[0].equals("STAT")) {
      for (final StatisticalRequest t : StatisticalRequest.values()) {
        if (t.name().equals(requestParts[0])) {
          StatisticalRequest timeO = t;
          return timeO;
        }
      }
    } else {
      String[] command = requestParts[0].split("_");
      for (final ComputationKind c : ComputationKind.values()) {
        if (c.name().equals(command[0])) {
          ComputationKind compK = c;
          return compK;
        }
      }
    }
    throw new MalformedRequestException(String.format("%s doesn't rappresent any valid request", requestParts[0]));
  }

  private String getCalculationRequest(String input, ComputationKind type) throws DifferentLengthListsException, NotFoundVariableException, NoResultsException, IllegalArgumentException, MalformedRequestException {

    String[] requestParts = input.split(";");
    String[] command = requestParts[0].split("_");

    String pointsDisposition = command[1];

    String[] textualDescripionVariables = requestParts[1].split(",");

    ExtendedVariable[] arrayFormatVariables = new ExtendedVariable[textualDescripionVariables.length];
    for (int i = 0; i < textualDescripionVariables.length; i++) {
      arrayFormatVariables[i] = new ExtendedVariable(textualDescripionVariables[i]);
    }

    ListOfPoints spacePointsToEvaluate;
    spacePointsToEvaluate = new ListOfPoints(arrayFormatVariables, pointsDisposition);

    String[] functionsToSolve = new String[requestParts.length - 2];
    for (int i = 2; i < requestParts.length; i++) {
      functionsToSolve[i - 2] = requestParts[i];
    }

    if (type.equals(ComputationKind.COUNT)) {
      if (requestParts.length >= 3) {

        return spacePointsToEvaluate.length() + "";
      } else {
        throw new MalformedRequestException("COUNT require expression part at the end.");
      }
    }

    ExpressionComputationHandler eCalcHandler = (String[] functions, ListOfPoints pTE, ComputationKind computationType) -> {
      ExpressionsSolver mFunSolver = new ExpressionsSolver(functions, pTE);
      return mFunSolver.getResult(computationType);
    };

    double result = eCalcHandler.solveComputationRequest(functionsToSolve, spacePointsToEvaluate, type);

    return String.format("%.6f", result);

  }

  private String getStatisticalRequest(StatisticalRequest operation) {
    switch (operation) {
      case STAT_REQS:
        return String.format("%d", getNumRequest());
      case STAT_AVG_TIME:
        return String.format("%.3f", (double) (getAvgTime() / 1000.0));
      case STAT_MAX_TIME:
        return String.format("%.3f", (double) (getMaxTime() / 1000.0));
      default:
        return "";
    }
  }

}
