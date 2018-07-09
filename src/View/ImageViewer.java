package View;

import Controller.FileSelector;
import Model.ImageFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;

import static jdk.nashorn.internal.runtime.Context.printStackTrace;

public class ImageViewer {

    public static void display(ImageFile image, ObservableList<ImageFile> list1, ObservableList<ImageFile> list2, FileSelector fs, File selectedDirectory){
        final Label ImageName = new Label(image.getName() + " not found.");
        ImageName.setPadding(new Insets(3, 10, 6, 15));

        final Label all_tags = new Label("All Tags");
        final Label this_photos_tags = new Label("This Photo's Tags");

        Stage window = new Stage();
        window.setTitle("Tag Editor");

        //creating all the borderpanes to be used
        BorderPane borderPane = new BorderPane();
        BorderPane borderpane2 = new BorderPane();
        borderpane2.setPadding(new Insets(10, 10, 15, 15));
        BorderPane borderpane3 = new BorderPane();
        borderpane3.setPadding(new Insets(10, 10, 15, 8));
        BorderPane borderpane4 = new BorderPane();
        borderpane4.setPadding(new Insets(10, 8, 15, 15));
        BorderPane borderpane5 = new BorderPane();
        BorderPane borderpane6 = new BorderPane();
        BorderPane borderpane7 = new BorderPane();

        Button ButtonApply = new Button("Apply Tags");

        TextField name = new TextField();

        //creating the left list
        ObservableList<String> tags = FXCollections.observableList(image.getManager().getTags());
        ListView<String> availabletags = new ListView<>(tags);
        availabletags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        availabletags.setEditable(true);
        borderpane3.setLeft(availabletags);

        //creating the center list
        ObservableList<String> tags2 = FXCollections.observableList(image.getCurrentTags());
        ListView<String> current_tags = new ListView<>(tags2);
        current_tags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        current_tags.setEditable(true);
        borderpane4.setTop(this_photos_tags);
        borderpane4.setLeft(current_tags);

        //clicking into one list deselects the other one
        availabletags.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                current_tags.getSelectionModel().clearSelection();
            }
        });


        current_tags.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                availabletags.getSelectionModel().clearSelection();
            }
        });

        ButtonApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    image.addTags(name.getCharacters().toString());
                    ObservableList<ImageFile>[] observation = fs.getObservableLists(selectedDirectory);
                    list1.setAll(observation[0]);
                    list2.setAll(observation[1]);
                }
                catch (IllegalArgumentException | InputMismatchException e){
                    Stage window2 = new Stage();
                    BorderPane borderpane = new BorderPane();
                    Label notag = new Label("Not a valid set of tags.");
                    borderpane.setCenter(notag);
                    Scene scene = new Scene(borderpane);
                    window2.setScene(scene);
                    window2.showAndWait();
                }

                ListView<String> new_available_tags = new ListView<>(tags);
                new_available_tags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                borderpane3.setLeft(new_available_tags);
                ListView<String> new_current_tags = new ListView<>(tags2);
                new_current_tags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                borderpane4.setLeft(new_current_tags);
                name.clear();
                Label newimgname = new Label(image.getName());
                newimgname.setWrapText(true);
                newimgname.setMaxWidth(600);
                borderpane2.setTop(newimgname);
            }
        });

        Button ButtonAdd = new Button("Add selected Tags");

        ButtonAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override  @SuppressWarnings("unchecked")
            public void handle(ActionEvent event) {
                ListView<String> list_of_available = (ListView<String>) borderpane3.getLeft();
                if (name.getCharacters().length() == 0) {
                    name.appendText(list_of_available.getSelectionModel().getSelectedItem());
                }else{
                    name.appendText(", " + list_of_available.getSelectionModel().getSelectedItem());
                }
            }
        });
        Button ButtonDelete = new Button("Delete selected Tags");
        ButtonDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override  @SuppressWarnings("unchecked")
            public void handle(ActionEvent event) {
                ListView<String> list_of_available = (ListView<String>) borderpane3.getLeft();
                ArrayList<String> list_of_selected = new ArrayList<>();
                ListView<String> list_of_current = (ListView<String>) borderpane4.getLeft();
                ArrayList<String> list_of_selected_2 = new ArrayList<>();
                if(list_of_available.getSelectionModel().getSelectedItems().size()!=0) {
                    list_of_selected.addAll(list_of_available.getSelectionModel().getSelectedItems());
                }
                if(list_of_current.getSelectionModel().getSelectedItems().size()!=0) {
                    for (int k = 0; k < list_of_current.getSelectionModel().getSelectedItems().size(); k++) {
                        if (!list_of_selected.contains(list_of_current.getSelectionModel().getSelectedItems().get(k))) {
                            list_of_selected_2.add(list_of_current.getSelectionModel().getSelectedItems().get(k));
                        }
                    }
                }
                if(list_of_selected.size()!=0) {
                    for (String aList_of_selected : list_of_selected) {
                        image.getManager().deleteExistingTag(aList_of_selected);
                    }
                }

                StringBuilder to_delete = new StringBuilder();
                if(list_of_selected_2.size()!=0) {
                    for (String aList_of_selected_2 : list_of_selected_2) {
                        to_delete.append(aList_of_selected_2);
                    }
                    image.deleteTags(to_delete.toString());
                }
                //update everything
                ListView<String> new_available_tags = new ListView<>(tags);
                new_available_tags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                borderpane3.setLeft(new_available_tags);
                ListView<String> new_current_tags = new ListView<>(tags2);
                new_current_tags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                borderpane4.setLeft(new_current_tags);
                Label newimgname = new Label(image.getName());
                newimgname.setWrapText(true);
                newimgname.setMaxWidth(600);
                borderpane2.setTop(newimgname);
                ObservableList<ImageFile>[] observation = fs.getObservableLists(selectedDirectory);
                list1.setAll(observation[0]);
                list2.setAll(observation[1]);
            }
        });

        borderPane.setLeft(borderpane2);
        borderPane.setCenter(borderpane3);
        borderPane.setRight(borderpane4);

        FileInputStream fis = null;
        try {
            //displaying the image
            fis = new FileInputStream(image.getPath().toString());
            ImageView imgview = new ImageView(new Image(fis));
            imgview.setFitWidth(600);
            imgview.setPreserveRatio(true);
            imgview.setSmooth(true);
            imgview.setCache(true);
            Label imgname = new Label(image.getName());
            imgname.setWrapText(true);
            imgname.setMaxWidth(600);
            borderpane2.setTop(imgname);
            borderpane2.setCenter(imgview);
        }
        catch (FileNotFoundException e){
            Label imgname = new Label(image.getName());
            imgname.setWrapText(true);
            imgname.setMaxWidth(600);
            borderpane2.setTop(imgname);
            borderpane2.setCenter(ImageName);
        }
        finally{
            if(fis != null) {
                try {
                    fis.close();
                }
                catch (IOException e){
                    //This is not something we can control because our program can't rename during this.
                    printStackTrace(e);
                }
            }
        }

        //placing things
        borderpane3.setTop(all_tags);

        borderpane4.setRight(borderpane5);
        BorderPane.setAlignment(name, Pos.CENTER);
        borderpane5.setTop(name);
        BorderPane.setMargin(name,new Insets(10,10,10,10));

        Button buttonhistory = new Button("View.History");
        borderpane5.setCenter(borderpane7);
        borderpane7.setTop(buttonhistory);
        BorderPane.setAlignment(buttonhistory,Pos.CENTER);

        buttonhistory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                History.display(image, borderPane,list1, list2, fs, selectedDirectory);
            }
        });

        Button buttonopendirectory = new Button("Open directory containing this image");
        borderpane7.setCenter(buttonopendirectory);
        BorderPane.setAlignment(buttonopendirectory,Pos.CENTER);
        buttonopendirectory.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new Thread(() -> {
                    try {
                        Desktop.getDesktop().open(new File(image.getPath().getParent().toString()) );
                    } catch (Exception IOException) {
                        IOException.printStackTrace();
                    }
                }).start();
            }
        });

        Button buttonmove = new Button("Move image to a different directory");
        borderpane7.setBottom(buttonmove);
        BorderPane.setAlignment(buttonmove,Pos.CENTER);

        buttonmove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory =
                        directoryChooser.showDialog(window);

                if(selectedDirectory == null){
                    Stage window2 = new Stage();
                    BorderPane borderpane = new BorderPane();
                    Label nodir = new Label("No directory selected.");
                    borderpane.setCenter(nodir);
                    Scene scene = new Scene(borderpane);
                    window.setScene(scene);
                    window.showAndWait();
                }
                else{
                    image.moveTo(selectedDirectory.toString());
                    ObservableList<ImageFile>[] observation = fs.getObservableLists(selectedDirectory);
                    list1.setAll(observation[0]);
                    list2.setAll(observation[1]);
                }
            }
        });

        borderpane5.setBottom(borderpane6);
        BorderPane.setAlignment(ButtonAdd,Pos.CENTER);
        borderpane6.setTop(ButtonAdd);
        borderpane6.setCenter(ButtonDelete);
        BorderPane.setAlignment(ButtonApply,Pos.CENTER);
        borderpane6.setBottom(ButtonApply);

        Scene scene = new Scene(borderPane);
        window.setScene(scene);
        window.showAndWait();

    }
}