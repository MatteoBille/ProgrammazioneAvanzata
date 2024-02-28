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

public class LineProcessingServer {

  protected final int port;
  protected final String quitCommand;

  public LineProcessingServer(int port, String quitCommand) {
    this.port = port;
    this.quitCommand = quitCommand;
  }

  public void run() throws IOException {
    ServerSocket serverSocket = new ServerSocket(port);
    while (true) {
      Socket socket = serverSocket.accept();
      ClientHandler clientHandler = new ClientHandler(socket, this);
      clientHandler.start();
    }
  }

  public String process(String input) throws MalformedRequestException, DifferentLengthListsException, NotFoundVariableException, NoResultsException {
    return input;
  }

  public String getQuitCommand() {
    return quitCommand;
  }
}
