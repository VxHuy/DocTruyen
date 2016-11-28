package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 11/11/2016.
 */

/* 2 từ giống nhau đứng cạnh nhau (trừ OO) -> lỗi chính tả */
public class Rule7 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        word = word.toLowerCase();

        for (int i=0; i<(word.length()-1); i++){
            if ((!(word.charAt(i)=='o'))&&(word.charAt(i)==word.charAt(i+1))){
                return true;
            }
        }
        return false;
    }
}
