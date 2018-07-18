# Image_viewer
This application is an image viewer renaming software.



javac Main.java
java Main

======== paste above to command line ========
=========== READ ME ============
    
    1. Once the app runs, users can select a directory in a new window by clicking the <Choose Directory> button.

    2. After users choose a directory, the left list view will show all images in that directory. The right list
view will show all images in all sub directories of that directory.

    3. The user can select any image from list view and click "Edit Tags" to see more info of that Image. A new window will pop up.

    4. The image will be shown on the left side. The name of the image is shown above.

    5. Under "All Tags" is a list view of all the tags that are currently available. If users want to tag an image
with currently available tags, the user can select them and click <Add selected Tags>.
Note: multiple available tags can be selected at once to be added.

    6. Under "This Photo's Tags" is a list view of the tags this photo currently has.

    7. The right top corner has a text box which is the "tags to be added". When the user performs (5), the selected tags
will be appended in the text box. User can also add thei own new tags to the text box. 
Note: the syntax for a valid set of tags must be "(//s@//w+)+". This means that the input should always start with a space.
Once users have a valid set of tags to be added in the text box, users can click
<Apply Tags> to add all tags to be added to "This Photo's Tags".

    8. Users can select from list views to delete tags. Users can select the tags and click
<Delete selected Tags>. When a tag is deleted from "All Tags", the tag is deleted in all images that contains that tag.

    9. When users click "History", a new window will pop up. The left list view will show all past names with a time
stamp. The right list view will show all past sets of tags. Users can click <revert name or tags> to
revert to an older set of selected contents.

    10. Users can click <Open directory containing this image> to open the folder that contains the image.

    11. Users can click <Move image to a different directory> to move the image into another folder, which can be select in the new window.

Notes:

- Users needs to type in the text box to add tags.
- Each tags must be separated by a comma.
- Users can select existing tags from the "left list" view to be added to the text box. 
- When users are sure about which tags to add, they can press <apply tags> to add all the tags at once.
- Each tag can only contain a-zA-Z_0-9.
