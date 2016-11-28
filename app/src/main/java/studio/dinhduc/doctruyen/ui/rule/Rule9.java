package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 23/11/2016.
 */

/* t đứng cuối từ thì trước phải là nguyên âm (trừ y) ngoại lệ: suýt
*  t đứng đầu, nếu sau đó là phụ âm thì phải là 'h' hoặc 'r' */
public class Rule9 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        String consonant = "qrtpsdghklxcvbnmđ";
        String check_y = "yýỳỷỹỵ";
        int leng = word.length();
        String check_h_r = "hr";
        word = word.toLowerCase();

        // 't' dung cuoi tu
        if (word.charAt(leng-1)=='t'){
            if (word.equals("suýt")){
                return false;
            }
            if (consonant.contains(""+word.charAt(leng-2))||check_y.contains(""+word.charAt(leng-2))){
                return true;
            }
        }

        // 't' dung dau tu
        if (word.charAt(0)=='t'){

            if (consonant.contains(""+word.charAt(1))){
                if (!check_h_r.contains(""+word.charAt(1))){
                    return true;
                }
            }
        }

        return false;
    }
}
