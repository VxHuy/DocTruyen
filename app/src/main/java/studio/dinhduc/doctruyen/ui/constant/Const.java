package studio.dinhduc.doctruyen.ui.constant;

import android.os.Environment;

import java.io.File;

/**
 * Created by dinhduc on 13/11/2016.
 */

public class Const {
    public static final String APP_DIR_NAME = "DocTruyen";
    public static final String EXTERNAL_STORAGE_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String APP_DIR_PATH = EXTERNAL_STORAGE_PATH + File.separator + APP_DIR_NAME;

    public class KeyIntent {
        public static final String KEY_NOVEL_DIR_PATH = "NOVEL_DIR";
    }
}
