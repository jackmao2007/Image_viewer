package View;

import Controller.FileSelector;
import Model.ImageFile;
import Model.ImageManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;

import static jdk.nashorn.internal.runtime.Context.printStackTrace;

public class Main extends Application {

    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        ImageManager manager;
        FileInputStream config = null;
        try {
            config = new FileInputStream("Manager.txt");
            ObjectInputStream configObj = new ObjectInputStream(config);
            manager = (ImageManager) configObj.readObject();
        }catch (Exception FileNotFoundException){
            manager = new ImageManager();
        }
        finally{
            if(config!=null){
                try{
                    config.close();
                }
                catch (IOException e){
                    printStackTrace(e);
                }
            }
        }
        FileSelector files = new FileSelector(manager);
        final File[] selectedDirectory = {null};


        final Label selectPicture = new Label("Select a directory");
        final Label labelSelectedDirectory = new Label();
        HBox dir_path = new HBox(10);
        dir_path.setPadding(new Insets(0, 10, 5, 10));
        dir_path.getChildren().addAll(labelSelectedDirectory);


        ObservableList<ImageFile> these_photos = FXCollections.observableArrayList();
        ObservableList<ImageFile> sub_photos = FXCollections.observableArrayList();

        window = primaryStage;
        window.setTitle("Photo file viewer");

        //two ListViews are created and populated with our ObservableLists

        final ListView<ImageFile> listView = new ListView<>(these_photos);
        listView.setPrefSize(300, 300);
        listView.setEditable(true);


        final ListView<ImageFile> listView2 = new ListView<>(sub_photos);
        listView2.setPrefSize(300, 300);
        listView2.setEditable(true);


        //Clicking on one listView clears the selection in the other listView so that only one image is selected

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                listView2.getSelectionModel().clearSelection();
            }
        });


        listView2.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                listView.getSelectionModel().clearSelection();
            }
        });

        // This HBox will hold the two ListViews and be placed in the center of the main BorderPanel
        HBox root = new HBox(20);
        root.setPadding(new Insets(0, 12, 7, 12));
        root.setSpacing(10);

        //setting the horizontal growth priority to ALWAYS allows the ListViews to grow when the stage is resized
        root.getChildren().addAll(listView,listView2);
        HBox.setHgrow(listView, Priority.ALWAYS);
        HBox.setHgrow(listView2, Priority.ALWAYS);

        //using a TilePane allows all the buttons to be the same size
        TilePane menu = new TilePane(Orientation.HORIZONTAL);
        menu.setPadding(new Insets(10, 15, 10, 15));
        menu.setHgap(10.0);
        menu.setHgap(8.0);

        // Button and logic for creating a system file browser window to choose a directory
        Button chooseDirButton = new Button("Choose directory");

        chooseDirButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                selectedDirectory[0] =
                        directoryChooser.showDialog(primaryStage);

                if(selectedDirectory[0] == null){
                    selectPicture.setText("No Directory selected");
                }

                else{
                    labelSelectedDirectory.setText(selectedDirectory[0].getAbsolutePath());
                    files.generateImages(selectedDirectory[0]);
                    ObservableList<ImageFile>[] observation = files.getObservableLists(selectedDirectory[0]);
                    //In this observation, there are two items, first is a list of images in the selectedDirectory
                    // second is a list of images in all sub directories of selectedDirectory.
                    these_photos.setAll(observation[0]);
                    sub_photos.setAll(observation[1]);
                    selectPicture.setText("Select a photo");
                }
            }
        });

        // Button and logic for creating View.ImageViewer view that supports tag editing
        Button ImageViewerButton = new Button("Edit Tags");

        ImageViewerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                ImageFile img = listView.getSelectionModel().getSelectedItem();
                ImageFile img2 = listView2.getSelectionModel().getSelectedItem();

                // If both selections are null then no image was selected
                if(img == null && img2 == null){
                    selectPicture.setText("No Photo selected");
                }

                // Otherwise an image was selected and is passed into the View.ImageViewer view
                else{
                    selectPicture.setText("Select a photo");
                    if(img != null)
                        ImageViewer.display(img,these_photos,sub_photos, files, selectedDirectory[0]);
                    else{
                        ImageViewer.display(img2,these_photos,sub_photos, files, selectedDirectory[0]);
                    }

                }
            }
        });

        chooseDirButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        ImageViewerButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        menu.getChildren().addAll(chooseDirButton, ImageViewerButton, selectPicture);


        //BorderPane() is used since it supports resizing children and can be later modified for more functionality
        BorderPane top = new BorderPane();
        top.setTop(menu);
        top.setCenter(root);
        top.setBottom(dir_path);


        window.setScene(new Scene(top, 600, 500));
        window.setMinWidth(300);
        window.setMinHeight(300);
        window.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we){
                try {
                    FileOutputStream fout = new FileOutputStream("Manager.txt", false);
                    ObjectOutputStream oos = new ObjectOutputStream(fout);
                    oos.writeObject(files.getManager());
                }catch (Exception FileNotFoundException){
                    FileNotFoundException.printStackTrace();
                }
            }
        });
    }
}