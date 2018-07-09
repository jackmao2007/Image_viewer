import Model.ImageFile;
import Model.ImageManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImageFIleTests {
    private File file = File.createTempFile("Image", ".jpg");
    private ImageManager Manager = new ImageManager();
    private ImageFile image1 = new ImageFile(file.getName(), file.getPath(), Manager);

    public ImageFIleTests() throws IOException {
        file.deleteOnExit();
    }


    @Test
    public void testGetName(){
        assertEquals(image1.getName(), file.getName());
    }

    @Test
    public void testToString(){
        assertEquals(image1.getName(), file.getName());
    }

    @Test
    public void testGetPath(){
        assertEquals(image1.getPath(), Paths.get(file.getPath()));
    }

    @Test
    public void testGetManager(){
        assertEquals(image1.getManager(), Manager);
    }


    @Test
    public void testGetCurrentTags(){
        image1.addTags("tag1");
        ArrayList<String> CurrentTags = new ArrayList<>();
        CurrentTags.add("tag1");
        assertEquals(image1.getCurrentTags(), CurrentTags);
    }

    @Test
    public void testAddTagOne(){
        image1.addTags("tag1");
        StringBuilder name = new StringBuilder(file.getName());
        name.insert(name.lastIndexOf("."), " @tag1");
        assertEquals(image1.getCurrentTags().get(0), "tag1");
        assertEquals(image1.getName(), name.toString());;
        assertTrue(image1.getHistoricalNames().size() == 2);
        assertTrue(image1.getHistoricalTags().size() == 2);
        assertTrue(Manager.getTags().contains("tag1"));
    }

    @Test
    public void testAddTagMany(){
        image1.addTags("tag1, tag2,  tag3,tag4");
        StringBuilder name = new StringBuilder(file.getName());
        name.insert(name.lastIndexOf("."), " @tag1");
        name.insert(name.lastIndexOf("."), " @tag2");
        name.insert(name.lastIndexOf("."), " @tag3");
        name.insert(name.lastIndexOf("."), " @tag4");
        assertEquals(image1.getCurrentTags().get(0), "tag1");
        assertEquals(image1.getCurrentTags().get(1), "tag2");
        assertEquals(image1.getCurrentTags().get(2), "tag3");
        assertEquals(image1.getCurrentTags().get(3), "tag4");
        assertEquals(image1.getName(), name.toString());;
        assertTrue(image1.getHistoricalNames().size() == 2);
        assertTrue(image1.getHistoricalTags().size() == 2);
        assertTrue(Manager.getTags().contains("tag1"));
        assertTrue(Manager.getTags().contains("tag2"));
        assertTrue(Manager.getTags().contains("tag3"));
        assertTrue(Manager.getTags().contains("tag4"));
    }

    @Test
    public void testAddTagAlreadyExist(){
        image1.addTags("tag1");
        image1.addTags("tag1");
        image1.addTags("tag1");
        StringBuilder name = new StringBuilder(file.getName());
        name.insert(name.lastIndexOf("."), " @tag1");
        assertEquals(image1.getCurrentTags().get(0), "tag1");
        assertEquals(image1.getName(), name.toString());;
        assertTrue(image1.getHistoricalNames().size() == 2);
        assertTrue(image1.getHistoricalTags().size() == 2);
    }

    @Test
    public void testDeleteTagOne(){
        image1.addTags("tag1, tag2,  tag3,tag4");
        String name = image1.getName().replace(" @tag1", "");
        image1.deleteTags("tag1");
        assertEquals(image1.getCurrentTags().get(0), "tag2");
        assertEquals(image1.getCurrentTags().get(1), "tag3");
        assertEquals(image1.getCurrentTags().get(2), "tag4");
        assertTrue(image1.getCurrentTags().size() == 3);
        assertEquals(image1.getName(), name);
        assertTrue(image1.getHistoricalNames().size() == 3);
        assertTrue(image1.getHistoricalTags().size() == 3);

    }

    @Test
    public void testDeleteTagMany(){
        image1.addTags("tag1, tag2,  tag3,tag4");
        String name = image1.getName().replace(" @tag1 @tag2 @tag3", "");
        image1.deleteTags("tag1, tag2, tag3");
        assertEquals(image1.getCurrentTags().get(0), "tag4");
        assertTrue(image1.getCurrentTags().size() == 1);
        assertEquals(image1.getName(), name);
        assertTrue(image1.getHistoricalNames().size() == 3);
        assertTrue(image1.getHistoricalTags().size() == 3);
    }

    // Since the application never actually uses delete on tags that are not already stored, delete on non existing is
    //ignored

    @Test
    public void testMoveTo(){//no Error
        String path = image1.getPath().toString();
        image1.moveTo(image1.getPath().getParent().toString());//move to the same folder containing for testing
        assertEquals(image1.getPath().toString(), path);

    }

    @Test
    public void testMoveToError(){ //target path does not exit
        try {
            image1.moveTo("!@#!%$");//String but not a directory
            assertTrue(false);
        }catch (Exception IllegalArgumentException){
            assertTrue(true); //passed
        }

    }

    @Test
    public void testRename(){
        String oldPath = image1.getPath().toString();
        String oldName = image1.getName();
        String newName = "imaggge";
        image1.rename(newName);
        assertEquals(image1.getName(), newName);
        assertEquals(image1.getPath().toString(), oldPath.replace(oldName, newName));
    }

    @Test
    public void testSetCurrentTagsEmpty(){
        image1.addTags("tag1, tag2,  tag3,tag4");
        image1.setCurrentTags("");
        assertEquals(image1.getCurrentTags().size(), 0);
        assertTrue(image1.getHistoricalNames().size() == 3);
        assertTrue(image1.getHistoricalTags().size() == 3);
        assertEquals(image1.getHistoricalTags().get(image1.getHistoricalTags().size()-2).getContent(),
            " @tag1 @tag2 @tag3 @tag4");
    }

    @Test
    public void testSetCurrentTagsSomeTags(){
        image1.addTags("tag1, tag2,  tag3,tag4");
        image1.setCurrentTags("tag5, tag6");
        assertEquals(image1.getCurrentTags().size(), 2);
        assertEquals(image1.getCurrentTags().get(0), "tag5");
        assertEquals(image1.getCurrentTags().get(1), "tag6");
        assertTrue(image1.getHistoricalNames().size() == 3);
        assertTrue(image1.getHistoricalTags().size() == 3);
        assertEquals(image1.getHistoricalTags().get(image1.getHistoricalTags().size()-2).getContent(),
                " @tag1 @tag2 @tag3 @tag4");
    }

    @Test
    public void testHistoricalTags(){
        image1.addTags("tag1");
        image1.addTags("tag2");
        image1.deleteTags("tag1");
        assertTrue(image1.getHistoricalTags().size() == 4);
        assertEquals(image1.getHistoricalTags().get(0).getContent(), "");
        assertEquals(image1.getHistoricalTags().get(1).getContent(), " @tag1");
        assertEquals(image1.getHistoricalTags().get(2).getContent(), " @tag1 @tag2");
        assertEquals(image1.getHistoricalTags().get(3).getContent(), " @tag2");
    }


    @Test
    public void testHistoricalNames(){
        image1.rename("Image.jpg");
        image1.addTags("tag1");
        image1.addTags("tag2");
        image1.deleteTags("tag1");
        assertTrue(image1.getHistoricalNames().size() == 5);
        assertEquals(image1.getHistoricalNames().get(0).getContent(), file.getName());
        assertEquals(image1.getHistoricalNames().get(1).getContent(), "Image.jpg");
        assertEquals(image1.getHistoricalNames().get(2).getContent(), "Image @tag1.jpg");
        assertEquals(image1.getHistoricalNames().get(3).getContent(), "Image @tag1 @tag2.jpg");
        assertEquals(image1.getHistoricalNames().get(4).getContent(), "Image @tag2.jpg");

    }
}
