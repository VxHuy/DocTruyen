package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 11/11/2016.
 */

/* trong X có một phụ âm mà phụ âm này ko có bất kỳ một phụ âm nào khác theo sau nó.
* nếu theo sau phụ âm đó là một phụ âm thì sai chính tả */
public class Rule8 implements Rule {

    @Override
    public boolean checkInvalidate(String word) {

        String consonant = "qrtpsdghklxcvbnmđ";
        String not_consonant_follow = "qrsdhlxvbmđ";

        for (int i=0; i<(word.length()-1); i++){
            if (not_consonant_follow.contains(""+word.charAt(i)) && consonant.contains(""+word.charAt(i+1))){
                return true;
            }
        }
        return false;
    }
}
