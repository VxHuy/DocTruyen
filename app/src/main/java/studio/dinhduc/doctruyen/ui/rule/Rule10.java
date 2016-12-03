package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 23/11/2016.
 */

/* p đứng cuối từ thì trước phải là nguyên âm (trừ ư, y)
*  p đứng đầu từ thì sau đó phải là 'h' */
public class Rule10 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        String check_pre = "áạặắấậéẹếệíịóọốộớợúụứự";
        int latter = word.length()-1;

        // 'p' dung cuoi tu
        if (word.charAt(latter)=='p'){
            if (!check_pre.contains(""+word.charAt(latter-1))){
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
