package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 07/11/2016.
 */

/* nếu trong từ có nhiều hơn 1 nguyên âm thì chúng phải đứng cạnh nhau */
public class Rule5 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {
        String consonant = "qrtpsdghklxcvbnmđ";
        int[] vowel = new int[3];
        int count = 0;
        for (int i=0; i<word.length(); i++){
            if (!consonant.contains("" + word.charAt(i))){
                vowel[count] = i;
                count++;
            }
        }

        if (count>1) {
            for (int j = 0; j < count-1; j++) {
                if (!(vowel[j+1]==(vowel[j]+1))){
                    return true;
                }
            }
        }

        return false;
    }
}
