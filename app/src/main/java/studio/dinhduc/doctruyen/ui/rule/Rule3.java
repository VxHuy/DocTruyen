package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 07/11/2016.
 */

/* một từ chỉ có tối đa 7 ký tự */
public class Rule3 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        return word.length() > 7;
    }
}
