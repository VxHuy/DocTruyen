package studio.dinhduc.doctruyen.ui.Rule;

/**
 * Created by duy on 26/11/2016.
 */

/* s đứng đầu từ, không đi với các vần oa, oă, oe, uê, uâ
*                                       ngoại lệ: soát, soạt, soạn, soạng, suất */
public class Rule18 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        String sound_buffer = "oa oă oe uê uâ";
        word = word.toLowerCase();

        if (word.charAt(0)=='s'){
            if (word.equals("soát")||word.equals("soạt")||word.equals("soạn")||word.equals("soạng")||word.equals("suất")){
                return false;
            }else{
                word = RemoveAccent.removeAccent(word);
                if ((word.length()>3)&&sound_buffer.contains(word.substring(1,3))){
                    return true;
                }
            }
        }
        return false;
    }
}
