package constructors;

/**
 * Created by ALAN on 7/14/2018.
 */

public class ItemData {

    String text;
    int imageId;
    public ItemData(String text, int imageId){
        this.text=text;
        this.imageId=imageId;
    }

    public String getText(){
        return text;
    }

    public int getImageId(){
        return imageId;
    }
}

