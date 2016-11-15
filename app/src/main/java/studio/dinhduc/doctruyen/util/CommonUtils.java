package studio.dinhduc.doctruyen.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import studio.dinhduc.doctruyen.R;

/**
 * Created by dinhduc on 13/11/2016.
 */

public class CommonUtils {
    private static final Pattern END_OF_SENTENCE = Pattern.compile("[.?\"!-]+[\\s]*");

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
                text.append("<br>");
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();

    }

    public static Dialog showCircleLoadingDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(context.getString(R.string.dialog_circle_loading_message));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    public static ProgressDialog showProgressLoadingDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage(context.getString(R.string.dialog_progress_loading_message));
        dialog.setMax(100);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    public static String hiLightQueryInText(Context context, String query, String text) {
        String accentColor = Integer.toHexString(
                ContextCompat.getColor(context, R.color.colorAccent) & 0x00ffffff);
        int index = text.toLowerCase().indexOf(query.toLowerCase());
        String queryInText = text.substring(index, index + query.length());
        return text.replaceFirst(
                queryInText,
                "<b><font color=#" + accentColor + ">" + queryInText + "</font></b>"
        );
    }

    public static String getSentence(String text, String word) {
        final String lcword = word.toLowerCase();
        for (String sentence : END_OF_SENTENCE.split(text)) {
            if (sentence.toLowerCase().contains(lcword)) {
                return sentence;
            }
        }
        return null;
    }
}
