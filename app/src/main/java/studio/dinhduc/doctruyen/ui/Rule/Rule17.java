package studio.dinhduc.doctruyen.ui.Rule;

/**
 * Created by duy on 26/11/2016.
 */

/* r khi đứng đầu từ, không kết hợp với các âm đệm oa, oe, uê, uy (ngoại lệ: roa) */
public class Rule17 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {
        String sound_buffer = "oa oe uê uy";

        if (word.length()>2) {
            word = RemoveAccent.removeAccent(word);

            if (word.charAt(0) == 'r') {
                if (word.equals("roa")) {
                    return false;
                } else if (sound_buffer.contains(word.substring(1, 3))) {
                    return true;
                }
            }
        }

        return false;
    }
}
