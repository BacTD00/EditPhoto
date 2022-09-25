package vn.tapbi.photoeditor.common;

public class Constant {
    public static  final String  LANGUAGE_EN ="en";
    public static  final String  LANGUAGE_VN ="vi";

    /*Param fragment*/
    public static final String ARGUMENT_FRAGMENT_MESSAGE_ID="ARGUMENT_FRAGMENT_MESSAGE_ID";
    public static final String ARGUMENT_FRAGMENT_MESSAGE="ARGUMENT_FRAGMENT_MESSAGE";
    public static final String ARGUMENT_FRAGMENT_MESSAGE_TITLE="ARGUMENT_FRAGMENT_MESSAGE_TITLE";

    public static final String PREF_SETTING_LANGUAGE="pref_setting_language";

    public static final String MY_PREF="MyPref";

    // request
    public static final int REQUEST_CODE_PERMISSIONS = 2020;
    public static final int REQUEST_PICK_IMAGE = 1111;
    public static final int REQUEST_OPEN_SETTINGS = 101;

    // requestUri
    public static final String REQUEST_URI_FROM_MAIN_ACTIVITY = "REQUEST_URI_FROM_MAIN_ACTIVITY";
    public static final String REQUEST_URI_FROM_SPLASH = "REQUEST_URI_FROM_SPLASH";

    public static final String SAMPLE_EDIT_IMAGE_NAME = "image_edit.jpeg";

    // filter
    public static final int SIZE_FILTER = 150;
    public static final String SHOW_FILTER = "SHOW_FILTER";
    public static final String STATE_POSITION_ID_FILTER_ADAPTER = "STATE_POSITION_ID_FILTER_ADAPTER";
    public static final String STATE_URI_FILTER_ADAPTER = "STATE_URI_FILTER_ADAPTER";
    public static final String LIST_MAGIC_FILTER_STATE = "LIST_MAGIC_FILTER_STATE";
    public static final String SAVE_URI_FILTER = "SAVE_URI_FILTER";

    // draw
    public static final String SHOW_DRAW = "SHOW_DRAW";
    public static final String SHOW_DRAW_SIZE = "SHOW_DRAW_SIZE";
    public static final String SHOW_DRAW_COLOR = "SHOW_DRAW_COLOR";
    public static final String STATE_COLOR_DRAW = "STATE_COLOR_DRAW";
    public static final String STATE_ID_COLOR_DRAW = "STATE_ID_COLOR_DRAW";
    public static final String STATE_CUSTOM_COLOR_DRAW = "STATE_CUSTOM_COLOR_DRAW";
    public static final int TYPE_COLOR = 100;
    public static final String STATE_CHOOSE_COLOR_DRAW = "STATE_CHOOSE_COLOR_DRAW";

    // sticker
    public static final String SHOW_STICKER = "SHOW_STICKER";

    // text
    public static final String SHOW_TEXT = "SHOW_TEXT";
    public static final int TYPE_TEXT_COLOR = 101;
    public static final int TYPE_TEXT_BACKGROUND = 102;
    public static final int TYPE_TEXT_FONT = 103;

    // set color and font
    public static final String COLOR_START_SYMBOL = "#";
    public static final String FONT_START ="font/";
    public static final String REGEX = "/";
    public static final String DOT = "\\.";
    public static final String FOLDER_FONT = "font";

    // share
    public static final String STATE_IMAGE_SHARE_FRAGMENT = "STATE_IMAGE_SHARE_FRAGMENT";
    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String SHARE_IMAGE = "Share Image";
    public static final String CHECK_SHARE = "CHECK_SHARE";
}