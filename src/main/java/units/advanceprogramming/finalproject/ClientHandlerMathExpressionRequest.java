/*
 * Matteo Bille' [Mat. IN2000125]
 * Advanced programming final project
 */
package units.advanceprogramming.finalproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ClientHandlerMathExpressionRequest extends ClientHandler {

  private final String Name;
  private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  public ClientHandlerMathExpressionRequest(Socket socket, LineProcessingServer lineProcessingServer) {
    super(socket, lineProcessingServer);
    Name = "Client " + ((LineProcessingServerWithStatistics) lineProcessingServer).getTotalNumberOfClient();
  }

  @Override
  public void run() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    LocalDateTime now;
    now = LocalDateTime.now();
    System.err.println("[" + dtf.format(now) + " " + Name + "] new connection from " + socket.toString());
    try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

      while (true) {
        final String line = br.readLine();
        if (line == null) {
          now = LocalDateTime.now();
          System.err.println("[" + dtf.format(now) + " " + Name + "] abruptly closed connection");
          break;
        }
        if (line.equals(server.getQuitCommand())) {
          now = LocalDateTime.now();
          System.err.println("[" + dtf.format(now) + " " + Name + "] close connection");
          break;
        }
        System.err.println("[" + dtf.format(now) + " " + Name + "] new request: " + line);

        try {
          Future<String> computationResult = EXECUTOR.submit((Callable<String>) () -> server.process(line));

          String response;
          response = computationResult.get();
          bw.write(response + System.lineSeparator());
          System.err.println("[" + dtf.format(now) + " " + Name + "] new response: " + response);

        } catch (InterruptedException | ExecutionException e) {
          now = LocalDateTime.now();
          System.err.println("[" + dtf.format(now) + " " + Name + "]" + "(" + e.getCause().getClass().getSimpleName() + ")" + e.getCause().getMessage());
          bw.write("ERR;(" + e.getCause().getClass().getSimpleName() + ") " + e.getCause().getMessage() + System.lineSeparator());

        } finally {
          System.gc();
          bw.flush();
        }
      }
    } catch (IOException e) {
      now = LocalDateTime.now();
      System.err.println("[" + dtf.format(now) + "] " + e.toString());
    }
  }
}
