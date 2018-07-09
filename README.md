# Image_viewer
Image viewing renaming software



javac Main.java
java Main


======== paste above to command line ========
=========== READ ME ============
    1. Once the app runs, user can select a directory in a new window by clicking on the <Choose Directory> button

    2. After the user chooses a directory, the left list view will show all images in that directory, and the right list
view will show all images in all sub directories of that directory.

    3. The user can select any image from either list view and click Edit Tags to see more info of that Image,
a new window will pop up.

    4. The image will show on the left side, above image is the name of the image.

    5. Under All Tags, is a list view of all the tags that are currently available, and if the user wants to tag a image
with currently available tags, the user can select them and click <Add selected Tags>.
Note: multiple available tags can be selected at once to add

    6. Under This Photo's Tags is a list view of the tags this photo currently have.

    7. The right top corner has a text box which is the tags to be added, when the user performs (5), the selected tags
will be appended in the text box, the user can also add hes own new tags to the text box. Note: the syntax for a valid
set of tags must be "(//s@//w+)+". This means that the input should always start with a space
Once the user has a valid set of tags to be added in the text box, the user can click
<Apply Tags> to add all tags to be added to This Photo's Tags.

    8. The user can also select from either list views to delete tags, the user can select the tags and click
<Delete selected Tags>. when a tag is deleted from All Tags, the tag is deleted in all images that contains that tag.

    9. when the user click History, a new window will pop up, the left list view will show all past names with a time
stamp, the right list view will show all past sets of tags. the user can choose to click <revert name or tags> to
change back to a older set of the selected contents.

    10. The user can click <Open directory containing this image> to open the folder that contains the image.

    11. The user can click <Move image to a different directory> to move the image into another folder, which the user
can select in the new window.



The image will get renamed with the tags added or deleted when the user adds or deletes tags.
Main
When application is first started, the left List view contains the images from the directory, the right list view contains the
images from all sub directories
ImageViewer
the user needs to type in the text box to add tags, with each tags separated by a comma, the user can select existing tags
from the left list view to be added to the text box. when the user is sure about which tags to add, he can press apply tags to
add all the tags in at once.
each tag can only contain a-zA-Z_0-9
