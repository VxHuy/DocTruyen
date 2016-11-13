package studio.dinhduc.doctruyen.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import studio.dinhduc.doctruyen.R;

/**
 * Created by dinhduc on 13/11/2016.
 */

public class CommonUtils {
    public static String readFileTxt(String filePath) {
        StringBuilder text = new StringBuilder();
        File file = new File(filePath);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader fileReader = new InputStreamReader(fileInputStream, "UTF8");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            bufferedReader.readLine();
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                text.append(line);
                text.append("\n");
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString().replaceAll("(?m)^[ \t]*\r?\n", "");

    }

    public static Dialog showLoadingDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(context.getString(R.string.dialog_loading_message));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }
}
