package studio.dinhduc.doctruyen.ui.Rule;

/**
 * Created by duy on 26/11/2016.
 */

/* k đứng đầu từ, sau đó phải là h, e, ê, i, y */
public class Rule19 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {
        String check_next = "heéèẻẽẹêếềểễệiíìỉĩịyýỳỷỹỵ";
        word = word.toLowerCase();

        if ((word.charAt(0)=='k')&&(!check_next.contains(""+word.charAt(1)))){
            return true;
        }

        return false;
    }
}
