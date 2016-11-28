package studio.dinhduc.doctruyen.ui.Rule;

/**
 * Created by duy on 27/11/2016.
 */

/* không phải nguyên âm nào cũng có thể đứng một mình */
public class Rule0 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {
        String check = "-aáàảạóồôổơởờợớuúùủụưứừeèêếỉịĩìyý";
        if (word.length()==1){
            word = word.toLowerCase();
            if (!check.contains(word)){
                return true;
            }
        }
        return false;
    }
}
