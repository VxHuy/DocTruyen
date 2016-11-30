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
//        Log.e("HEXSTRING","query = "+query);
//        Log.e("HEXSTRING","sentence" + text);
        int index = text.toLowerCase().indexOf(query.toLowerCase());
//        Log.e("HEXSTRING","index = "+index);
        String queryInText = text.substring(index, index + query.length());
        return text.replaceFirst(
                queryInText,
                "<b><font color=#" + accentColor + ">" + queryInText + "</font></b>"
        );
    }

    @Nullable
    public static String getSentence(String text, String word) {
        final String lcword = word.toLowerCase();
        for (String sentence : END_OF_SENTENCE.split(text)) {
            if (sentence.toLowerCase().contains(lcword)) {
                return sentence;
            }
        }
        return null;
    }

    //vxhuy
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
        int count;
        int z = 0;
        //duyệt cả chapter 1 lần và lưu lại các trạng thái của từng sentance
//        Log.e("DUYET", "listText.length= " + listText.length);
        for (int i = 0; i < listText.length; i++) {
            z++;
            Sentence st = new Sentence(0, listText[i].toLowerCase(), i);
            int lv = 0;
            for (int j = 0; j < listWord.size(); j++) {
                int pos_tmp = listText[i].indexOf(listWord.get(j).getContent());
                int last_pos_tmp = listText[i].lastIndexOf(listWord.get(j).getContent());
                if (pos_tmp > -1) {
                    Word w = new Word(listWord.get(j));
                    SameWord sameWord = new SameWord();
                    w.setPosInSentence(pos_tmp);
                    w.setPosInQuery(j);
//                    Log.e("DUYET", "word = " + w.getContent());
                    sameWord.setWord(w);
                    sameWord.getPosSameWord().add(pos_tmp);
                    if (pos_tmp < last_pos_tmp) {
                        while (pos_tmp < last_pos_tmp) {
                            pos_tmp = listText[i].indexOf(listWord.get(j).getContent(), pos_tmp + listWord.get(j).getContent().length());
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
            Sentence senMax = listSentence.get(0);

            int l = 0;
            for (Sentence sen : listSentence) {
                l++;
                if (sen.getLv() == 1) {
                    continue;
                } else {
                    if (sen.getExpected() >= 2) {
                        ValueMax.add(sen.getStartPosResult());
                        tmp_value.add(sen.getStartPosResult());
                        for (int i = 0; i < sen.getHasWord().get(sen.getStartPosResult()).getPosSameWord().size(); i++) {
                            TRY(sen.getHasWord().get(sen.getStartPosResult()).getPosSameWord().get(i), sen.getStartPosResult() + 1, sen);
                            if (sen.getMaxLength() == sen.getExpected()) break;
                        }
                        String str = "";
                        for (int x : ValueMax) {
                            str += sen.getHasWord().get(x).getWord().getContent() + " ";
                        }
                        if (str.length() > 1)
                            str = str.substring(0, str.length() - 1);
                        if (ResultMaxLength < ValueMax.size()) {
                            ResultMaxLength = ValueMax.size();
                            senMax = sen;
                        }
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
                }
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
            if (tmp <= 2 && tmp >= 0) {
                tmp_value.add(k);
            } else {
                if (tmp_value.size() > ValueMax.size()) {
                    ValueMax.clear();
                    ValueMax.addAll(tmp_value);

                }
                tmp_value2.addAll(tmp_value);
                tmp_value.clear();
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
                TRY(sen.getHasWord().get(k).getWord().getPosInSentence(), k + 1, sen);
                if (ValueMax.size() == sen.getExpected()) return;
            }
            if (tmp <= 2 && tmp >= 0) {
                tmp_value.remove(tmp_value.size() - 1);
            } else {
                tmp_value.clear();
                tmp_value.addAll(tmp_value2);
                tmp_value2.clear();
            }
        }
    }
}
