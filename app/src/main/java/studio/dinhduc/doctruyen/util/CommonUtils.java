package studio.dinhduc.doctruyen.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

import studio.dinhduc.doctruyen.R;
import studio.dinhduc.doctruyen.model.SameWord;
import studio.dinhduc.doctruyen.model.SearchResult;
import studio.dinhduc.doctruyen.model.Sentence;
import studio.dinhduc.doctruyen.model.Word;

/**
 * Created by dinhduc on 13/11/2016.
 */

public class CommonUtils {
    private static final Pattern END_OF_SENTENCE = Pattern.compile("[.?\"!-]+[\\s]*");
    private static ArrayList<Sentence> listSentence = new ArrayList<>();
    private static String lcSearchQuery = "";
    private static String arrWord[];
    private static String listText[];
    private static ArrayList<Integer> ValueMax = new ArrayList<>();
    private static ArrayList<Integer> tmp_value = new ArrayList<>();


    public static String readFileTxt(String filePath) {
        StringBuilder text = new StringBuilder();
        File file = new File(filePath);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader fileReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
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

    public static String highLightQueryInText(Context context, String query, String text) {
        String accentColor = Integer.toHexString(
                ContextCompat.getColor(context, R.color.colorAccent) & 0x00ffffff);
        int index = text.toLowerCase().indexOf(query.toLowerCase());
        query = text.substring(index, index + query.length());
        return text.replace(
                query,
                "<b><font color=#" + accentColor + ">" + query + "</font></b>"
        );
    }

    @Nullable
    public static String getSentence(String text, String word) {
        final String lcword = word.toLowerCase();
        for (String sentence : END_OF_SENTENCE.split(text)) {
            if (findFirstPos2(sentence, lcword) != -1) {
                return sentence;
            }
        }
        return null;
    }

    public static int findFirstPos2(String sen, String word) {
        sen = sen.toLowerCase();
        int result = sen.indexOf(word);
        int tmp_length = sen.length();
        if (result != -1 && tmp_length > result + word.length()) {
            while ('a' <= sen.charAt(result + word.length()) && sen.charAt(result + word.length()) <= 'z') {
                result = sen.indexOf(word, result + word.length());
                if (result == -1 || tmp_length <= result + word.length()) break;
            }
        }
        return result;
    }

    //vxhuy
    public static int findFirstPos(int i, String text, int start) {
        int result = listText[i].indexOf(text, start);
        int tmp_length = listText[i].length();
        if (result != -1 && tmp_length > result + text.length()) {
            while ('a' <= listText[i].charAt(result + text.length()) && listText[i].charAt(result + text.length()) <= 'z') {
                result = listText[i].indexOf(text, result + text.length());
                if (result == -1 || tmp_length <= result + text.length()) break;
            }
        }
        return result;
    }

    public static int findLastPos(int i, String text) {
        int result = listText[i].lastIndexOf(text);
        int tmp_length = listText[i].length();
        if (result > 0 && tmp_length > result + text.length()) {
            while ('a' <= listText[i].charAt(result + text.length()) && listText[i].charAt(result + text.length()) <= 'z') {
                result = listText[i].lastIndexOf(text, result - 1);
                if (result == -1 || tmp_length <= result + text.length()) break;
            }
        }
        return result;
    }

    public static SearchResult getSentenceMultilSearch(String chapterContent, String searchQuery) {
        String chapterContent1 = chapterContent.replaceAll("<br>", " ");
        lcSearchQuery = searchQuery.toLowerCase();
        arrWord = lcSearchQuery.toLowerCase().split(" ");

        ArrayList<Word> listWord = new ArrayList<>();
        for (int i = 0; i < arrWord.length; i++) {
            Word wd = new Word(arrWord[i].length(), -1, i, arrWord[i]);
            listWord.add(wd);
        }
        arrWord = null;
        listText = END_OF_SENTENCE.split(chapterContent1.toLowerCase());
        int tmp[] = new int[100];
        listSentence = new ArrayList<>();
        for (int i = 0; i < listText.length; i++) {
//            Log.e("TEST","============cau " +z +" =============");
            Sentence st = new Sentence(0, listText[i].toLowerCase(), i);
            int lv = 0;
            for (int j = 0; j < listWord.size(); j++) {
                int pos_tmp = findFirstPos(i, listWord.get(j).getContent(), 0);
                int last_pos_tmp = findLastPos(i, listWord.get(j).getContent());
                if (pos_tmp > -1 && listText[i].charAt(pos_tmp) != ' ') {

                    Word w = new Word(listWord.get(j));
                    SameWord sameWord = new SameWord();
                    w.setPosInSentence(pos_tmp);
                    w.setPosInQuery(j);
                    sameWord.setWord(w);
                    sameWord.getPosSameWord().add(pos_tmp);
                    if (pos_tmp < last_pos_tmp) {
                        while (pos_tmp < last_pos_tmp) {
                            pos_tmp = findFirstPos(i, listWord.get(j).getContent(), pos_tmp + listWord.get(j).getContent().length());
                            if (pos_tmp < last_pos_tmp) {
                                sameWord.getPosSameWord().add(pos_tmp);
                            }
                        }
                        sameWord.getPosSameWord().add(last_pos_tmp);
                    }

                    st.getHasWord().add(sameWord);
                    lv++;
                }
            }
            if (lv >= 2) {
                st.setLv(lv);
                listSentence.add(st);
            }
        }

        //tìm kiếm chuỗi phù hợp
        ArrayList<SameWord> samw = new ArrayList<>();
        for (Sentence sen : listSentence) {
            if (sen.getLv() >= 1) {
                //Kiểm tra chuỗi result dài nhất có thể có trong từng sentence;
                int maxLength = 1;
                int posStartMaxLength = 0;
                samw = sen.getHasWord();
                if (samw != null) {
                    int check = 0;
                    int count1 = 0;
                    for (int i = 0; i < samw.size(); i++) {
                        if (i == 0) {
                            count1++;
                        } else {
                            if (samw.get(i).getWord().getPosInQuery() - check != 1) {
                                if (count1 > maxLength) {
                                    maxLength = count1;
                                    posStartMaxLength = i - count1 + 1;
                                }
                                count1 = 1;
                            } else {
                                count1++;
                            }
                            if (i == samw.size() - 1) {
                                if (count1 > maxLength) {
                                    maxLength = count1;
                                    posStartMaxLength = i - count1 + 1;
                                }
                            }

                        }
                        check = samw.get(i).getWord().getPosInQuery();
                    }
                }
                sen.setExpected(maxLength);
                sen.setStartPosResult(posStartMaxLength);
            }
        }
        if (!listSentence.isEmpty()) {
            // sắp xếp lại theo chuỗi con có đô dài dài nhất có thể
            Collections.sort(listSentence);
            // Tìm chuỗi con dài nhất trong  các câu
            int ResultMaxLength = 1;
            Sentence senMax = new Sentence(0);
            for (Sentence sen : listSentence) {
                if (sen.getExpected() >= 2) {
                    ValueMax.add(sen.getStartPosResult());
                    tmp_value.add(sen.getStartPosResult());
                    for (int i = 0; i < sen.getHasWord().get(sen.getStartPosResult()).getPosSameWord().size(); i++) {
                        TRY(sen.getHasWord().get(sen.getStartPosResult()).getPosSameWord().get(i), sen.getStartPosResult() + 1, sen);
                        if (sen.getMaxLength() == sen.getExpected()) break;
                    }
                    if (ResultMaxLength < ValueMax.size()) {
                        ResultMaxLength = ValueMax.size();
                        senMax = sen;
                    } else {
                        ValueMax.clear();
                        tmp_value.clear();
                        continue;
                    }
                    String str = "";
                    for (int x : ValueMax) {
                        str += sen.getHasWord().get(x).getWord().getContent() + " ";
                    }
                    if (str.length() > 1)
                        str = str.substring(0, str.length() - 1);
                    sen.setMaxLength(ValueMax.size());
                    sen.setSubResult(str);
                    if (sen.getMaxLength() == sen.getExpected()) {
                        ValueMax.clear();
                        tmp_value.clear();
                        break;
                    }
                } else {

                    continue;
                }
//                }
                ValueMax.clear();
                tmp_value.clear();
            }
            if (senMax.getMaxLength() < 2) {
                return null;
            }
            //Trả về câu có subResult dài nhất.
            SearchResult searchResult = new SearchResult();
//            searchResult.setLvResult(senMax.getMaxLength());
            searchResult.setLvResult(senMax.getSubResult().length());
            searchResult.setSentence(END_OF_SENTENCE.split(chapterContent1)[senMax.getPosition()]);
            searchResult.setSearchQuery(senMax.getSubResult());
            return searchResult;
        }
        return null;
    }

    private static void TRY(int value_previous, int k, Sentence sen) {
        if (ValueMax.size() == sen.getExpected()) return;
        for (int i = 0; i < sen.getHasWord().get(k).getPosSameWord().size(); i++) {
            ArrayList<Integer> tmp_value2 = new ArrayList<>();
            int tmp = sen.getHasWord().get(k).getPosSameWord().get(i) - (value_previous + sen.getHasWord().get(k - 1).getWord().getLength());
            if (tmp <= 2 && tmp > 0) {
                tmp_value.add(k);
//                Log.e("Di1", " k =" + k + " i = " + i +" word = " +sen.getHasWord().get(k).getWord().getContent()+" pos = "+sen.getHasWord().get(k).getPosSameWord().get(i)+" tmp_value.size = " + tmp_value.size() + " ValueMax.size = " + ValueMax.size()+" tmp_value = "+tmp_value.toString());
            } else {
                if (tmp_value.size() > ValueMax.size()) {
                    ValueMax.clear();
                    ValueMax.addAll(tmp_value);
//                    Log.e("Di2", " k =" + k + " i = " + i +" word = " +sen.getHasWord().get(k).getWord().getContent()+ " pos = "+sen.getHasWord().get(k).getPosSameWord().get(i)+" tmp_value.size = " + tmp_value.size() + " ValueMax.size = " + ValueMax.size()+" tmp_value = "+tmp_value.toString());
                }
                tmp_value2.addAll(tmp_value);
//                Log.e("Di3", " k =" + k + " i = " + i +" word = " +sen.getHasWord().get(k).getWord().getContent()+" pos = "+sen.getHasWord().get(k).getPosSameWord().get(i)+" tmp_value.size = " + tmp_value.size() + " ValueMax.size = " + ValueMax.size()+" tmp_value = "+tmp_value.toString());
                tmp_value.clear();
                tmp_value.add(k);
            }
            if (k - sen.getStartPosResult() == sen.getExpected() || k == sen.getHasWord().size() - 1) {
                if (tmp_value.size() > ValueMax.size()) {
                    ValueMax.clear();
                    ValueMax.addAll(tmp_value);
                }
                if (ValueMax.size() == sen.getExpected()) {
                    return;
                }
            } else {
                TRY(sen.getHasWord().get(k).getPosSameWord().get(i), k + 1, sen); //////////////////
                if (ValueMax.size() == sen.getExpected()) return;
            }
            if (tmp <= 2 && tmp > 0) {
                tmp_value.remove(tmp_value.size() - 1);
            } else {
                tmp_value.clear();
                tmp_value.addAll(tmp_value2);
                tmp_value2.clear();
                if (tmp_value.size() >= 1 && tmp_value.get(tmp_value.size() - 1).equals(sen.getHasWord().get(k).getWord().getContent())) {
                    tmp_value.remove(tmp_value.size() - 1);
                }
            }
        }
    }
}
