package vn.tapbi.photoeditor.common.models;

public class TextColor {
    private int colorInt;
    private int colorImg;

    public TextColor(int colorInt, int colorImg) {
        this.colorInt = colorInt;
        this.colorImg = colorImg;
    }

    public int getColorInt() {
        return colorInt;
    }

    public void setColorInt(int colorInt) {
        this.colorInt = colorInt;
    }

    public int getColorImg() {
        return colorImg;
    }

    public void setColorImg(int colorImg) {
        this.colorImg = colorImg;
    }
}
