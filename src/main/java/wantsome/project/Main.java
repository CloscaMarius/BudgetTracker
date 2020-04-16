package wantsome.project;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;
import static wantsome.project.web.SparkUtil.render;

/**
 * Main class of the application
 */
public class Main {

    public static void main(String[] args) {
        setup();
        configureRoutesAndStart();
    }

    private static void setup() {
        //create and configure all needed resources (db tables, etc)
    }

    private static void configureRoutesAndStart() {
        staticFileLocation("public");

        //configure all routes
        get("/main", (req, res) -> getMainWebPage(req, res));
        get("/time", (req, res) -> getTimeRestResponse(req, res));

        awaitInitialization();
        System.out.println("\nServer started, url: http://localhost:4567/main, http://localhost:4567/time (use Ctrl+C to stop it)\n");
    }

    //example of returning a web page
    private static Object getMainWebPage(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        model.put("serverTime", new Date().toString());
        return render(model, "main.vm");
    }

    //example of returning a JSON response for a REST service
    private static Object getTimeRestResponse(Request req, Response res) {
        res.type("application/json");
        return new Gson().toJson(LocalDateTime.now());
    }
}
