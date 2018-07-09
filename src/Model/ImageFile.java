package Model;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.regex.Pattern;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;



public class ImageFile implements Serializable{

    private String name; // Name of the image
    private String path; // Path of the image
    private ArrayList<String> currentTags = new ArrayList<>(); // Tags this image currently have
    private ArrayList<State> historicalNames = new ArrayList<>(); // View.History of the names this image had
    private ArrayList<State> historicalTags = new ArrayList<>(); // View.History of the sets of tags this image had
    //ArrayList<States>, Model.State = (Time, (Tags))
    private ImageManager manager;

    public ImageFile(String name, String path, ImageManager manager){
        this.name = name;
        this.path = path;
        this.manager = manager;
        this.historicalNames.add(new State(LocalDateTime.now(), name));
        this.historicalTags.add(new State(LocalDateTime.now(), ""));
    }

    /* adds tags into the image, raise a error if the input is not a valid chain of tags
    *
    * @param tags: a set of tags beginning with @*/
    public void addTags(String tags){
        StringBuilder changedName = new StringBuilder(name);
        for (String tag : tagsToList(tags)){
            if (!currentTags.contains(tag)) {
                currentTags.add(tag);
                changedName.insert(changedName.lastIndexOf("."), " @" + tag);
                if (!manager.getTags().contains(tag))
                    manager.createNewTag(tag);
            }
        }
        if(!changedName.toString().equals(name)){
            rename(changedName.toString());
            historicalTags.add(new State(LocalDateTime.now(), listToTags(currentTags)));
        }
    }

    /* delete tags from the image, raise a error if the input is not a valid chain of tags
    *
    * @param tags: a set of tags beginning with @*/
    public void deleteTags(String tags){
        for (String tag : tagsToList(tags)) {
            if (currentTags.contains(tag)) {
                currentTags.remove(tag);
                name = name.replace(" @" + tag, "");
            }
        }
        rename(this.name);
        historicalTags.add(new State(LocalDateTime.now(), listToTags(currentTags)));
    }

    /* change the directory of this image
    *
    *
    * @param dir: a new directory for this image*/
    public void moveTo(String dir){
        try {
            Path target = Paths.get(dir, name);
            Files.move(Paths.get(this.path), target, REPLACE_EXISTING);
            this.path = target.toString();
        }catch (Exception IOException){
            throw new IllegalArgumentException("Target directory does not exist");
        }
    }

    /* rename the image to a new name and adds the old name into history
    *
    * @param newName a String that is the new name for the image*/
    public void rename(String newName){
        try {
            Path target = Paths.get(Paths.get(this.path).getParent().toString(), newName);
            Files.move(Paths.get(this.path), target, REPLACE_EXISTING);
            this.path = target.toString();
            this.name = newName;
            this.historicalNames.add(new State(LocalDateTime.now(), this.name));
        }catch (Exception IOException){
            IOException.printStackTrace();
        }

    }

    /* set the currentTags to a new set of tags given and puts the old set of tags into history. Also rename the file
    *
    * @param setOfTag a String that represents a set of tag to be set to currentTags*/
    public void setCurrentTags(String setOfTags){
        if (setOfTags.equals("")){
            currentTags = new ArrayList<>();
        }else {
            currentTags = tagsToList(setOfTags);
        }
        historicalTags.add(new State(LocalDateTime.now(), listToTags(currentTags)));
        rename(historicalNames.get(0).getContent() + listToTags(currentTags));

    }


    /* This separates the tags into a ArrayList of tags
    *
    * @param tags a String that represents a set of tags in the given format
    * @return     an ArrayList of Strings that represents the set of tags*/
    private static ArrayList<String> tagsToList(String tags){
        ArrayList<String> setOfTags = new ArrayList<>();
        if(Pattern.matches("(\\w+)(,\\s*\\w+)*$", tags)){
            int i = 1;
            int j = 0;
            while (i < tags.length()){
                if (tags.charAt(i) == ','){
                    setOfTags.add(tags.substring(j, i).trim());
                    j = i + 1;
                    i += 2;
                }else{
                    i += 1;
                }
            }
            setOfTags.add(tags.substring(j, tags.length()).trim());
        }else{
            throw new InputMismatchException("wrong format of tags");
        }
        return setOfTags;
    }

    /* This separates the tags into a ArrayList of tags
    *
    * @param setOfTags  an ArrayList of Strings that represents the set of tags a String that represents a set of tags
    * in the given format
    * @return           a String that represents a set of tags in the given format*/
    private static String listToTags(ArrayList<String> setOfTags){
        StringBuilder result = new StringBuilder();
        for (String tag : setOfTags){
            result.append(" @").append(tag);
        }
        return result.toString();
    }

    @Override
    public String toString(){
        return getName();
    }

    public ArrayList<String> getCurrentTags() {
        return currentTags;
    }

    public ArrayList<State> getHistoricalNames() {
        return historicalNames;
    }

    public ArrayList<State> getHistoricalTags() {
        return historicalTags;
    }

    public String getName() {
        return name;
    }

    public Path getPath() {
        return Paths.get(this.path);
    }

    public ImageManager getManager() {
        return manager;
    }
}
