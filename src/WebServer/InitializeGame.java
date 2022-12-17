package WebServer;

import Game.Game;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

import static WebServer.RootHandler.parseQuery;

public class InitializeGame implements HttpHandler
{
    @Override
    public void handle(HttpExchange he) throws IOException
    {
        // parse request
        InputStreamReader isr = new InputStreamReader(he.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String payload = br.readLine();
        //HashMap<String, String> queryData = parseQuery(query);

        System.out.println("payload: " + payload);

        JSONObject jo;
        try {
            jo = (JSONObject) new JSONParser().parse(payload);


        } catch (ParseException e) {
            System.out.println("Parse failed");
            throw new RuntimeException(e);
        };

        System.out.println(jo.toString());
        System.out.println(jo.keySet());
        System.out.println(jo.get("deck"));

        String[] deckString = jo.get("deck").toString().replace("[", "").replace("]", "").split(",");
        System.out.println("DeckString: " + Arrays.toString(deckString));
        int[] deck = new int[5];
        for (int i=0;i<deckString.length;i++) {

            deck[i] = Integer.parseInt(deckString[i]);
            System.out.println("Converted " + i);
        }
        System.out.println("deck:" + Arrays.toString(deck));
        String response = "";
        response += Arrays.toString(deck);
        // send response
        /*
        String response = "Starting ";
        System.out.println(Boolean.parseBoolean(queryData.get("human")));
        if () {
            response += "Human";
        }
        else {
            response += "AI";
        }

        response += "-AI Game.\n";
         */

        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());

        os.close();


        int[] someDeck = { 0, 1, 2, 0, 1 };

        //System.out.println("query: " + query);
        Game.InitializeGame(deck);
        Game.InitializeLogFile();

        //Game.InitializeGame(Boolean.parseBoolean(queryData.get("human")));

    }
}