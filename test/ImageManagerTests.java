import Model.ImageFile;
import Model.ImageManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImageManagerTests {
    private ImageManager manager = new ImageManager();
    private File file1 = File.createTempFile("image1", ".jpg");
    private File file2 = File.createTempFile("image2", ".jpg");
    private ImageFile image1 = new ImageFile(file1.getName(), file1.getPath(), manager);
    private ImageFile image2 = new ImageFile(file2.getName(), file2.getPath(), manager);

    public ImageManagerTests() throws IOException {
        file1.deleteOnExit();
        file2.deleteOnExit();
    }


    @Test
    public void testAddImage_testGetImages(){
        manager.addImage(image1);
        manager.addImage(image2);
        assertEquals(manager.getImages().size(), 2);
        assertEquals(manager.getImages().get(0), image1);
        assertEquals(manager.getImages().get(1), image2);
    }

    @Test
    public void testCreateNewTagValid_testGetTags(){
        manager.createNewTag("hello");
        assertTrue(manager.getTags().size() == 1);
        assertEquals(manager.getTags().get(manager.getTags().size() - 1), "hello");
    }

    @Test
    public void testCreateNewTagInValid(){
        try {
            manager.createNewTag("!!@#$%^,");
            assertTrue(false);
        }catch (Exception IllegalArgumentException){
            assertTrue(true);
        }
    }

    @Test
    public void testDeleteExistingTag(){
        manager.addImage(image1);
        manager.addImage(image2);
        image1.addTags("tag1");
        image2.addTags("tag2");
        manager.deleteExistingTag("tag1");
        assertTrue(manager.getTags().size() == 1);
        assertEquals(manager.getTags().get(0), "tag2");
        assertEquals(image1.getCurrentTags().size(), 0);
    }

    // Since application actually never delete tag that are not existing in tags, the case is ignored

    @Test
    public void testExistsImageYes(){
        manager.addImage(image1);
        manager.addImage(image2);
        File file3 = new File(file1.getPath());
        assertTrue(manager.existsImage(file3));
    }

    @Test
    public void testExistsImageNo(){
        manager.addImage(image1);
        manager.addImage(image2);
        File file3 = new File("123kk.txt");
        assertTrue(!manager.existsImage(file3));
    }
}
