package vn.tapbi.photoeditor.common.models;

public  class MessageEvent {
    private int typeEvent =0;
    private String stringValue ="";

    public MessageEvent(int typeEvent, String stringValue) {
        this.typeEvent = typeEvent;
        this.stringValue = stringValue;
    }

    public MessageEvent(int typeEvent) {
        this.typeEvent = typeEvent;
    }

    public int getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(int typeEvent) {
        this.typeEvent = typeEvent;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}