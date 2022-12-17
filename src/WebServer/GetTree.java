package WebServer;

import Person.*;
import Tree.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;

import static WebServer.RootHandler.parseQuery;

public class GetTree implements HttpHandler
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
        //response = Game.GetGameInfo();

        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());

        os.close();

        System.out.println("query: " + query);
    }
}