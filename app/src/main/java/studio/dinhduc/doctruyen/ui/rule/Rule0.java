package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 27/11/2016.
 */

/* không phải nguyên âm nào cũng có thể đứng một mình */
public class Rule0 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {
        String check = "aáàảạóồôổơởờợớuúùủụưứừeèêếỉịĩìyý";
        if (word.length()==1){
            if (!check.contains(word)){
                return true;
            }
        }
        return false;
    }
}
