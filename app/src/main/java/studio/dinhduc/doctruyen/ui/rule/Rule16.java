package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 25/11/2016.
 */

/* q đứng ở đầu từ, sau q phải là 'u' */
public class Rule16 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {
        word = word.toLowerCase();

        if ((word.charAt(0)=='q')&&(word.charAt(1)!='u')){
            return true;
        }

        return false;
    }
}
