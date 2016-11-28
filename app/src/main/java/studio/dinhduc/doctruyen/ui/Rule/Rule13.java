package studio.dinhduc.doctruyen.ui.Rule;

/**
 * Created by duy on 25/11/2016.
 */

/* c đứng cuối từ thì trước đó phải là nguyên âm (trừ y, i)
*  c đứng đầu từ thì sau đó là phụ âm 'h' hoặc nguyên âm (trừ e, ê, i, y) */
public class Rule13 implements Rule{
    @Override
    public boolean checkInvalidate(String word) {

        String check_pre = "qrtpsdghklxcvbnmđ yýỳỷỹỵiíìỉĩị";
        String check_next = "qrtpsdgklxcvbnmđ yýỳỷỹỵêếềểễệiíìỉĩịeéèẻẽẹ";

        word = word.toLowerCase();
        int latter = word.length()-1;

        // c dung cuoi
        if ((word.charAt(latter)=='c')&&check_pre.contains(""+word.charAt(latter-1))){
                return true;
        }

        // c dung dau
        if ((word.charAt(0)=='c')&&check_next.contains(""+word.charAt(1))){
                return true;
        }

        return false;
    }
}
