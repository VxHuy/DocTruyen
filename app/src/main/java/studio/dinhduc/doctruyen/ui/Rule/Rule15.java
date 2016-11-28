package studio.dinhduc.doctruyen.ui.Rule;

/**
 * Created by duy on 25/11/2016.
 */

/* m đứng cuối từ thì trước đó là nguyên âm (trừ ư, y) ngoại lệ: hừm
*  m đứng đầu thì sau đó phải là nguyên âm */
public class Rule15 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        String consonant = "qrtpsdghklxcvbnmđ";
        String check_pre = "qrtpsdghklxcvbnmđ yýỳỷỹỵưứừửữự";

        word = word.toLowerCase();
        int latter = word.length()-1;

        // m dung cuoi

        if (word.equals("hừm")){
            return false;
        }
        if ((word.charAt(latter)=='m')&&check_pre.contains(""+word.charAt(latter-1))){
            return true;
        }

        // m dung dau
        if ((word.charAt(0)=='m')&&consonant.contains(""+word.charAt(1))){
            return true;
        }

        return false;
    }
}
