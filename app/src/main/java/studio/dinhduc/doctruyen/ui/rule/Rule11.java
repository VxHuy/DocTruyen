package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 23/11/2016.
 */

/* g đứng cuối từ thì trước đó phải là 'n'
*  g đứng đầu từ thì sau đó là phụ âm h hoặc nguyên âm (trừ e, ê) */
public class Rule11 implements Rule{
    @Override
    public boolean checkInvalidate(String word) {

        String check_invalid = "qrtpsdgklxcvbnmđeéèẻẽẹêếềểễệ";
        int latter = word.length()-1;
        word = word.toLowerCase();

        // 'g' dung cuoi tu
        if (word.charAt(latter)=='g'){
            if (word.charAt(latter-1)!='n'){
                return true;
            }
        }

        // 'g' dung dau tu
        if (word.charAt(0)=='g'){
            if (check_invalid.contains(""+word.charAt(1))){
                return true;
            }
        }

        return false;
    }
}
