package View;

import Controller.FileSelector;
import Model.ImageFile;
import Model.State;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;

public class History {
    public static void display(ImageFile image, BorderPane pane, ObservableList<ImageFile> list1, ObservableList<ImageFile> list2, FileSelector fs, File selectedDirectory){
        Stage window = new Stage();
        window.setTitle("View.History of this image");

        BorderPane borderpane = new BorderPane();
        BorderPane borderpane2 = new BorderPane();
        BorderPane borderpane3 = new BorderPane();
        BorderPane borderpane4 = new BorderPane();

        borderpane.setLeft(borderpane2);
        borderpane.setCenter(borderpane3);
        borderpane.setRight(borderpane4);

        ObservableList<State> names = FXCollections.observableList(image.getHistoricalNames());
        ListView<State> lognamesview = new ListView<State>(names);

        borderpane2.setCenter(lognamesview);

        ObservableList<State> tags = FXCollections.observableList(image.getHistoricalTags());
        ListView<State> logtagsview = new ListView<State>(tags);

        borderpane3.setCenter(logtagsview);

        lognamesview.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                logtagsview.getSelectionModel().clearSelection();
            }
        });


        logtagsview.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                lognamesview.getSelectionModel().clearSelection();
            }
        });

        Button revert = new Button("Revert name or tags");
        revert.setOnAction(new EventHandler<ActionEvent>() {
                               @Override @SuppressWarnings("unchecked")
                               public void handle(ActionEvent event) {
                                    ListView<State> listview = (ListView<State>) borderpane2.getCenter();
                                    if(listview.getSelectionModel().getSelectedItems().size()!=0){
                                        image.rename(listview.getSelectionModel().getSelectedItems().get(0).getContent());
                                        ListView<State> new_lognamesview = new ListView<State>(names);
                                        borderpane2.setCenter(new_lognamesview);
                                        BorderPane leftpane = (BorderPane) pane.getLeft();
                                        Label imgname = new Label(image.getName());
                                        imgname.setWrapText(true);
                                        imgname.setMaxWidth(600);
                                        leftpane.setTop(imgname);
                                        ObservableList<ImageFile>[] observation = fs.getObservableLists(selectedDirectory);
                                        list1.setAll(observation[0]);
                                        list2.setAll(observation[1]);
                                    }

                                    ListView<State> listview2 = (ListView<State>) borderpane3.getCenter();
                                    if(listview2.getSelectionModel().getSelectedItems().size()!=0){
                                        image.setCurrentTags(listview2.getSelectionModel().getSelectedItems()
                                                .get(0).getContent().replaceAll(" @", ", ")
                                                .replaceFirst(", ", ""));
                                        ListView<State> new_logtagsview = new ListView<State>(tags);
                                        borderpane3.setCenter(new_logtagsview);

                                        BorderPane rightpane = (BorderPane) pane.getRight();

                                        ObservableList<String> tags2 = FXCollections.observableList(image.getCurrentTags());
                                        ListView<String> current_tags = new ListView<>(tags2);
                                        current_tags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                                        current_tags.setEditable(true);
                                        rightpane.setLeft(current_tags);
                                    }
                               }
                           });
        borderpane4.setTop(revert);

        Scene scene = new Scene(borderpane);
        window.setScene(scene);
        window.showAndWait();
    }
}
