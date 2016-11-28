package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 25/11/2016.
 */

/* n đứng cuối từ thì trước đó là nguyên âm (trừ ư, y)
* n đứng đầu từ thì sau đó là phụ âm g, h
*                          hoặc nguyên âm
*                          không có âm đệm oa, oă, uâ, oe,uê, uy (ngoại lệ: noãn, noa) */
public class Rule14 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        String check_pre = "qrtpsdghklxcvbnmđ yýỳỷỹỵưứừửữự";
        String check_next = "qrtpsdklxcvbnmđ";

        word = word.toLowerCase();
        int latter = word.length()-1;

        // n dung cuoi
        if ((word.charAt(latter)=='n')&&check_pre.contains(""+word.charAt(latter-1))){
            return true;
        }

        // n dung dau
        if ((word.charAt(0)=='n')&&check_next.contains(""+word.charAt(1))){
            return true;
        }

        // check sound buffer
        String sound_buffer = "oa oă uâ oe uê uy";

        // truong hop ngoai le
        if (word.length()>2) {
            if (word.equals("noãn") || word.equals("noa")) {
                return false;
            }

            word = RemoveAccent.removeAccent(word);

            if (word.charAt(0) == 'n') {
                if (sound_buffer.contains(word.substring(1, 3))) {
                    return true;
                }
            }
        }

        return false;
    }
}
