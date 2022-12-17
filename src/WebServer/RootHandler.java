package WebServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class RootHandler implements HttpHandler
{
    public static HashMap<String, String> parseQuery(String query)
    {
        String[] queryPairs = query.split("&");
        HashMap<String, String> queryData = new HashMap<>();

        for (String pair : queryPairs)
            queryData.put(pair.split("=")[0], pair.split("=")[1]);

        return queryData;
    }

    @Override
    public void handle(HttpExchange he) throws IOException
    {
        String response = "<h1>Server start success " +
        "if you see this message </h1 > " + " Merhaba Nusret ";

        he.sendResponseHeaders(200, response.length());

        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}