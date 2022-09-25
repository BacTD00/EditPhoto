package vn.tapbi.photoeditor.common.models;

public class ColorType {
    private String color;
    private int type;

    public ColorType() {
    }

    public ColorType(String color, int type) {
        this.color = color;
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
