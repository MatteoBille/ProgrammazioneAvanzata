/*
 * Matteo Bille' [Mat. IN2000125]
 * Advanced programming final project
 */
package units.advanceprogramming.finalproject;

import units.advanceprogramming.projectexception.NoResultsException;
import units.advanceprogramming.projectexception.DifferentLengthListsException;
import units.advanceprogramming.projectexception.MalformedRequestException;
import units.advanceprogramming.projectexception.NotFoundVariableException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread {

  protected final Socket socket;
  protected final LineProcessingServer server;

  public ClientHandler(Socket socket, LineProcessingServer server) {
    this.socket = socket;
    this.server = server;
  }

  @Override
  public void run() {
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      while (true) {
        String line = br.readLine();
        if (line.equals(server.getQuitCommand())) {
          socket.close();
          break;
        }
        try {
          bw.write(server.process(line) + System.lineSeparator());
        } catch (MalformedRequestException e) {
          bw.write("Error Statement MalformedRequestException");
        } catch (DifferentLengthListsException | NotFoundVariableException | NoResultsException ex) {
          Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        bw.flush();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
