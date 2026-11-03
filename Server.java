import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

// This class starts a small web server on your own computer (localhost:8080).
// It does 2 jobs:
//   1. When the browser opens "/"          -> send the index.html page
//   2. When the browser asks "/generate"   -> make a password and send it back
public class Server {

    public static void main(String[] args) throws IOException {

        PasswordGenerator generator = new PasswordGenerator();

        // Create the server on port 8080
           HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);

        // JOB 1: show the web page
        server.createContext("/", exchange -> {
            byte[] page = Files.readAllBytes(Paths.get("index.html"));
            sendResponse(exchange, "text/html", new String(page, "UTF-8"));
        });

        // JOB 2: generate a password
        server.createContext("/generate", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            // Example query: length=12&upper=true&lower=true&digits=true&symbols=false

            int length = 12;
            try {
                length = Integer.parseInt(getValue(query, "length"));
            } catch (Exception e) {
                // if length is missing or not a number, keep 12
            }
            if (length < 4)  length = 4;     // keep the length in a safe range
            if (length > 32) length = 32;

            boolean upper   = getValue(query, "upper").equals("true");
            boolean lower   = getValue(query, "lower").equals("true");
            boolean digits  = getValue(query, "digits").equals("true");
            boolean symbols = getValue(query, "symbols").equals("true");

            String password = generator.generate(length, upper, lower, digits, symbols);
            String strength = "";
            if (!password.isEmpty()) {
                strength = generator.checkStrength(password);
            }

            // Send the answer in JSON format
            String json = "{\"password\":\"" + password + "\",\"strength\":\"" + strength + "\"}";
            sendResponse(exchange, "application/json", json);
        });

        server.start();
        System.out.println("Server started!");
        System.out.println("Open this in your browser: http://localhost:9090");
    }

    // Find the value of one key inside the query text
    private static String getValue(String query, String key) {
        if (query == null) return "";
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] parts = pair.split("=");
            if (parts.length == 2 && parts[0].equals(key)) {
                return parts[1];
            }
        }
        return "";
    }

    // Send text back to the browser
    private static void sendResponse(HttpExchange exchange, String type, String body) throws IOException {
        byte[] bytes = body.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", type + "; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream out = exchange.getResponseBody();
        out.write(bytes);
        out.close();
    }
}
