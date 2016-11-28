package studio.dinhduc.doctruyen.ui.Rule;

/**
 * Created by duy on 23/11/2016.
 */

/* p đứng cuối từ thì trước phải là nguyên âm (trừ ư, y)
*  p đứng đầu từ thì sau đó phải là 'h' */
public class Rule10 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        String consonant = "qrtpsdghklxcvbnmđ";
        String check_u_y = "ưứừửữựyýỳỷỹỵ";
        int latter = word.length()-1;
        word = word.toLowerCase();

        // 'p' dung cuoi tu
        if (word.charAt(latter)=='p'){
            if (consonant.contains(""+word.charAt(latter-1)) ||
                    check_u_y.contains(""+word.charAt(latter-1))){
                return true;
            }
        }

        // 'p' dung dau tu
        if (word.charAt(0)=='p'){
            if (word.charAt(1)!='h'){
                return true;
            }
        }
        return false;
    }
}
