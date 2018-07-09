package Model;

import java.io.Serializable;
import java.util.ArrayList;
import  java.util.regex.Pattern;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageManager implements Serializable{

    private ArrayList<ImageFile> images = new ArrayList<>();
    private ArrayList<String> tags = new ArrayList<>();
    //ImagePath + TimeStamp
    public ImageManager(){}

    public void addImage(ImageFile image){
        images.add(image);
    }

    /* add all tags that have been tagged to any image at least once to tags*/
    public void updateTags(){
        for (ImageFile image : images){
            for (String tag : image.getCurrentTags()){
                if (!tags.contains(tag))
                    tags.add(tag);
            }
        }
    }

    /* creates new tag to be added in to the tags this ImageManager holds
    *
    * @param newTag a String that represents a new tag*/
    public void createNewTag(String newTag){
        if (Pattern.matches("\\w+", newTag) && !tags.contains(newTag)){
            tags.add(newTag);
        }else{
            throw new IllegalArgumentException("not a legal tag");
        }
    }

    /* delete a existing tag from tags this ImageManager holds, and remove the tag from any Image in images that holds
    the tag. do nothing if the tag does not exist in tags
    *
    * @param dTag a String that represents a tag to be deleted from tags*/
    public void deleteExistingTag(String dTag){
        if (tags.contains(dTag)){
            tags.remove(dTag);
            for (ImageFile image : images){
                if (image.getCurrentTags().contains(dTag))
                    image.deleteTags(dTag);
            }
        }
    }

    /* this method checks if the Model.ImageManager already has the Image stored
    *
    * @param file a File that represents the image file in the system
    * @return     true if ImageManager already manages that image, false otherwise*/
    public boolean existsImage(File file){
        Path filePath = Paths.get(file.getPath());
        for (ImageFile image : images){
            if (image.getPath().equals(filePath))
                return true;
        }
        return false;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public ArrayList<ImageFile> getImages() {
        return images;
    }
}
