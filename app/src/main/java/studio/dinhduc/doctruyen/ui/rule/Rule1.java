package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 07/11/2016.
 */

/* kiểm tra các ký tự không có trong tiếng việt */
public class Rule1 implements Rule {

    @Override
    public boolean checkInvalidate(String word) {
        //String invalid_string = "0123456789fjwzFJWZ!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

        String invalid_string = "0123456789fjwz#$%&'*+/<=>@[\\]^_`{|}~";
        for (int i=0; i<word.length(); i++){
            if (invalid_string.contains("" + word.charAt(i))){
                return true;
            }
        }
        return false;
    }
}
