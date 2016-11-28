package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 07/11/2016.
 */

/* từ không chứa nguyên âm -> sai chính tả */
public class Rule2 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        String consonant = "qrtpsdghklxcvbnmđQRTPSDGHKLXCVBNMĐ";
        int count = 0;
        for (int i=0; i<word.length(); i++){
            if (consonant.contains("" + word.charAt(i))){
                count++;
            }
        }

        return count==word.length();
    }
}
