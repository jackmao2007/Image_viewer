package Controller;

import Model.ImageFile;
import Model.ImageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.nio.file.Paths;

public class FileSelector {

    public ImageManager Manager;


    public ImageManager getManager() {
        return Manager;
    }

    public FileSelector(ImageManager manager){
        this.Manager = manager;
    }

    /* returns two ObservableList, first one being images in the Direct Directory, second one being images in the Sub
        * Directory*/

    //This is hard to check and it is indeed correct. Suppressed to avoid javac warnings
    @SuppressWarnings("unchecked")
    public ObservableList<ImageFile>[] getObservableLists(File Directory){

        ObservableList[] result = new ObservableList[2];
        ObservableList<ImageFile> these_photos = FXCollections.observableArrayList();
        ObservableList<ImageFile> sub_photos = FXCollections.observableArrayList();

        for (ImageFile image : Manager.getImages()){
            if (image.getPath().getParent().equals(Paths.get(Directory.getPath()))){
                these_photos.add(image);
            }else if (image.getPath().startsWith(Directory.getAbsolutePath())){
                sub_photos.add(image);
            }
        }

        result[0] = these_photos;
        result[1] = sub_photos;

        return result;
    }

    private static boolean isImage(File f)
    {
        return f.getName().toLowerCase().endsWith(".png") | f.getName().toLowerCase().endsWith(".jpg");
    }

    public void generateImages(File thisDirectory){


        /*
           - Loop will iterate only on valid file array (ie not null)

           - On each iteration, if the current file is an image, an Image object is created and added to an instance
             of Model.ImageManager.

           - The Image object is also added to an ObservableList in order to populate a listView

           - If the current file is a directory then the recursive traversal method is called and an ObservableList
             is passed in to get all of the ImageFiles located in subdirectories
         */

        listf(thisDirectory, Manager);
    }

    /*adds all images into the Model.ImageManager*/
    private void listf(File directory, ImageManager manager) {

        // get all the image_files from a directory into a File array
        File[] fList = directory.listFiles();

        // only iterate on not null File array to avoid NullPointerException
        if(fList != null) {
            //On each iteration, create new Image objects if valid image files or recursively traverse subdirectories
            for (File file : fList) {
                if (file.isFile() && isImage(file)) {
                    if (!Manager.existsImage(file)) {
                        ImageFile image = new ImageFile(file.getName(), file.getPath(), manager);
                        manager.addImage(image);
                    }
                } else if (file.isDirectory()) {
                    listf(file, manager);
                }
            }
        }
    }
}
