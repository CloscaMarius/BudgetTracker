package wantsome.project;

import spark.Spark;
import wantsome.project.db.service.DbInitService;
import wantsome.project.ui.web.*;

import static spark.Spark.*;

public class WebApp {

    public static void main(String[] args) {


        //init db
        DbInitService.createMissingTables();

        //configure and start web server
        staticFileLocation("/public"); //location of static resources (like images, .css ..), relative to /resources dir
        configureRoutes();

        //print url in console (after full init)
        Spark.awaitInitialization();
        System.out.println("Web app started, url: http://localhost:4567/main");

    }

    private static void configureRoutes() {

        //--- TRANSACTIONS ---//
        get("/main", TransactionsPageController::showTransactionsPage);

        get("/add", AddEditTransactionPageController::showAddForm);
        post("/add", AddEditTransactionPageController::handleAddUpdateRequest);

        get("/update/:id", AddEditTransactionPageController::showUpdateForm);
        post("/update/:id", AddEditTransactionPageController::handleAddUpdateRequest);

        get("/delete/:id", TransactionsPageController::handleDeleteRequest);


        //--- CATEGORIES ---//
        get("/categories", CategoriesPageController::showCategoriesPage);

        get("/add_cat", AddEditCategoryPageController::showAddForm);
        post("/add_cat", AddEditCategoryPageController::handleAddUpdateRequest);

        get("/update_cat/:id", AddEditCategoryPageController::showUpdateForm);
        post("/update_cat/:id", AddEditCategoryPageController::handleAddUpdateRequest);

        get("/delete_cat/:id", CategoriesPageController::handleDeleteRequest);

        //--- REPORTS ---//
        get("/reports", ReportsPageController::showReportsPage);

        //--- Common error handling ---//
        //basic error handling (to catch/handle any uncaught exceptions)
        exception(Exception.class, (exception, request, response) ->
                response.body("<h2>An unexpected error occurred</h2>" +
                        "Details: " + exception.getMessage() + " <br><br>" +
                        "<button onclick=\"location.href='/main'\" type=\"button\"> Go to main page </button>"));

        //OR: more complex error handling, with separate controller and vm page
        //exception(Exception.class, ErrorPageController::handleException);


    }

}
