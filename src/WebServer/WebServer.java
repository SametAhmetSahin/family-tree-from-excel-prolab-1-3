package WebServer;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer
{
    public void Start(int port) throws IOException
    {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        System.out.println("server started at " + port);

        server.createContext("/", new RootHandler());
        server.createContext("/get", new GetHandler());
        server.createContext("/initializegame", new InitializeGame());
        server.createContext("/playaround", new PlayARoundHandler());
        server.createContext("/getgamestatus", new GetGameStatusHandler());
        server.createContext("/checkgamestatus", new GameStatusHandler());
        server.createContext("/getscores", new GetScoresHandler());
        /*
        server.createContext("/echoHeader", new EchoHeaderHandler());
        server.createContext("/echoPost", new EchoPostHandler());
         */

        server.setExecutor(null);
        server.start();
    }
}

