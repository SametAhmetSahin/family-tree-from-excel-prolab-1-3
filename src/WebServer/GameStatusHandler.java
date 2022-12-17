package WebServer;

import Game.Game;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;

import static WebServer.RootHandler.parseQuery;
import Game.*;

public class GameStatusHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange he) throws IOException
    {
        // parse request
        URI requestedUri = he.getRequestURI();
        String query = requestedUri.getRawQuery();
        HashMap<String, String> queryData = parseQuery(query);

        // send response
        String response = "";
        response += Game.CheckGameStatus(Game.currentRound);

        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());

        os.close();

        System.out.println("query: " + query);
    }
}