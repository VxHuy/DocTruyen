package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 07/11/2016.
 */

/* không phải phụ âm nào cũng có thể đứng ở cuối từ
*  các phụ âm chỉ đứng ở đầu từ: q, s, d, k, l, x, v, b, đ
*  các phụ âm không thể đứng ở giữa từ: t, p, m (không tính những phụ âm chỉ đứng ở đầu từ) */
public class Rule6 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        String consonant_not_latter = "qrsdklxvbđ";
        String consonant_just_ahead = "qsdklxvbđ";
        String consonant_not_mid = "tpm";

        // latter
        if (consonant_not_latter.contains("" + word.charAt(word.length()-1))){
            return true;
        }

        // ahead
        for (int i=1; i<word.length(); i++){
            if (consonant_just_ahead.contains(""+word.charAt(i))){
                return true;
            }
        }

        // mid
        for (int j=1; j<word.length()-1; j++){
            if (consonant_not_mid.contains(""+word.charAt(j))){
                return true;
            }
        }

        return false;
    }
}
