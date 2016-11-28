package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 27/11/2016.
 */

/* Tổ hợp phụ âm:
*  gi không kết hợp với các âm đệm: oa, oe, uê, uy
*  tr không đi với các âm đệm: oa, oă, oe, uê
*  ng sau là h, a, ă, â, o, ô, ơ, u, ư
*  gh sau là e, ê, i
*  ngh sau là e, ê, i */
public class Rule20 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        String sound_buffer_notNext_gi = "oa oe uê uy";
        String sound_buffer_notNext_tr = "oa oă oe uê";
        String check_ng = "haăâoôơuư";
        String check_gh = "eêi";
        String check_ngh = "eêi";

        word = RemoveAccent.removeAccent(word);
        String consonant_combination = word.substring(0,2);
        if (word.length()>3) {
            // gi
            if (consonant_combination.equals("gi") && (sound_buffer_notNext_gi.contains(word.substring(2, 4)))) {
                return true;
            }

            // tr
            if (consonant_combination.equals("tr") && (sound_buffer_notNext_tr.contains(word.substring(2, 4)))) {
                return true;
            }

            // ngh
            if ((word.substring(0,3).equals("ngh"))&&(!check_ngh.contains(""+word.charAt(3)))){
                return true;
            }
        }

        if (word.length()>2) {
            // ng
            if (consonant_combination.equals("ng") && (!check_ng.contains("" + word.charAt(2)))) {
                return true;
            }

            // gh
            if (consonant_combination.equals("gh") && (!check_gh.contains("" + word.charAt(2)))) {
                return true;
            }
        }

        return false;
    }
}
