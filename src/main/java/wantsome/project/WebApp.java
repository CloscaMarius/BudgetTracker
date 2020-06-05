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


    }

}
