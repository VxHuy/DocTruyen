package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 23/11/2016.
 */

/* h đứng cuối từ thì kí tự trước nó phải là 'n', 'c'
*  h đứng đầu từ thì ký hiệu sau nó phải là nguyên âm
*  h đứng giữa câu thì ký tự trước nó phải là 'k', 'c', 'n', 'ng', 'g', 't', 'p' */
public class Rule12 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        String consonant = "qrtpsdghklxcvbnmđ";
        String check_pre_h = "kcngtp";
        String check_h = "nc";
        int latter = word.length()-1;

        word = word.toLowerCase();

        // h dung cuoi tu
        if (word.charAt(latter)=='h'&&(!check_h.contains(""+word.charAt(latter-1)))){
            return true;
        }

        // h dung dau tu
        if (word.charAt(0)=='h'&&consonant.contains(""+word.charAt(1))){
            return true;
        }

        // h giua tu
        for (int i=1; i<word.length()-1; i++){
            if (word.charAt(i)=='h'&&(!check_pre_h.contains(""+word.charAt(i-1)))){
                return true;
            }
        }

        return false;
    }
}
