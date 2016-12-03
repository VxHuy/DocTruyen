package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 03/12/2016.
 */

/* 3 phụ âm không đứng cạnh nhau trừ ngh */
public class Rule24 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {
        String consonant = "qrtpsdghklxcvbnmđ";
        word = word.toLowerCase();
        if (word.length()>2) {
            for (int i = 0; i < word.length() - 2; i++) {
                if (consonant.contains(""+word.charAt(i))&&consonant.contains(""+word.charAt(i+1))
                        &&consonant.contains(""+word.charAt(i+2))){
                    if (!word.substring(i,i+3).equals("ngh")){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
