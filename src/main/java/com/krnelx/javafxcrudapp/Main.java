package com.krnelx.javafxcrudapp;

import atlantafx.base.theme.PrimerLight;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

  private static final String DB_URL = "jdbc:mysql://localhost:3306/database";
  private static final String DB_USERNAME = "";
  private static final String DB_PASSWORD = "";

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

    primaryStage.setTitle("JavaFX CRUD App");

    Label statusLabel = new Label("Loading...");
    statusLabel.setFont(Font.font(20));

    ProgressBar progressBar = new ProgressBar();
    progressBar.setPrefSize(200, 30);

    VBox root = new VBox(statusLabel, progressBar);
    root.setAlignment(Pos.CENTER);

    Scene scene = new Scene(root, 900, 600);

    primaryStage.setScene(scene);
    primaryStage.show();

    // Create and execute data loading task
    DataLoader dataLoader = new DataLoader(DB_URL, DB_USERNAME, DB_PASSWORD);
    progressBar.progressProperty().bind(dataLoader.progressProperty());
      dataLoader.setOnSucceeded(event -> {
          List<String[]> data = dataLoader.getValue();
          statusLabel.setText("Data loaded successfully");

          Platform.runLater(() -> {
              // Create a TableView
              TableView<String[]> tableView = new TableView<>();

              TableColumn<String[], String> idCol = new TableColumn<>("ID");
              idCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));

              TableColumn<String[], String> firstNameCol = new TableColumn<>("First Name");
              firstNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));

              TableColumn<String[], String> lastNameCol = new TableColumn<>("Last Name");
              lastNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));

              TableColumn<String[], String> emailCol = new TableColumn<>("Email");
              emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));

              TableColumn<String[], String> ageCol = new TableColumn<>("Age");
              ageCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));

              TableColumn<String[], String> genderCol = new TableColumn<>("Gender");
              genderCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[5]));

              TableColumn<String[], String> countryCol = new TableColumn<>("Country");
              countryCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[6]));

              TableColumn<String[], String> createdCol = new TableColumn<>("Created at");
              createdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[7]));

              // Add columns to TableView using the correct method
              tableView.getColumns().setAll(idCol, firstNameCol, lastNameCol, emailCol, ageCol, genderCol, countryCol, createdCol);

              // Set items to TableView
              tableView.getItems().addAll(data);

              // Set column resize policy to distribute available space equally among columns
              tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

              // Remove progress bar and status label, then add the table view
              root.getChildren().removeAll(progressBar, statusLabel);
              root.getChildren().add(tableView);
          });
      });

      dataLoader.setOnFailed(event -> {
          statusLabel.setText("Error loading data: " + dataLoader.getException().getMessage());
      });

      // Start the data loading task
      Thread thread = new Thread(dataLoader);
      thread.start();
  }
}
