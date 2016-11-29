package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 29/11/2016.
 */

/* Nếu là tập hợp hai (2) nguyên âm (nhị trùng âm) thì đánh dấu ở nguyên âm đầu.
   Tập hợp ba (3) nguyên âm (tam trùng âm) hoặc hai nguyên âm + phụ âm cuối thì vị trí dấu chuyển đến nguyên âm thứ nhì
   Ngoại lệ là chữ "ê" và "ơ" chiếm ưu tiên, bất kể vị trí */
public class Rule22 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        String consonant = "qrtpsdghklxcvbnmđ";
        String vowels_not_accent = "aăâoôơeêuưiy";
        word = word.toLowerCase();
        String vowels = "";
        int latter = 0;

        // lay nguyen am
        for (int i=0; i<word.length(); i++){
            if (!consonant.contains(""+word.charAt(i))){
                vowels = vowels + word.charAt(i);
                latter = i;
            }
        }

        String w = word.substring(0,2);
        w = RemoveAccent.removeAccent(w);
        if (w.equals("gi")||w.equals("qu")){
            vowels = vowels.substring(1);
        }

        String vowels_re_ac = RemoveAccent.removeAccent(vowels);
        if (vowels.length()>1){

            // ngoai le
            if (vowels_re_ac.contains("ê")||vowels_re_ac.contains("ơ")){
                String vo = vowels.replaceAll("[êếềểễệơớờởỡợ]","");
                for (int i=0; i<vo.length(); i++){
                    if (!vowels_not_accent.contains(""+vo.charAt(i))){
                        return true;
                    }
                }
            }else {

                // nguyen am doi
                if (vowels.length() == 2) {
                    if (latter == (word.length() - 1)) {
                        if (!vowels_not_accent.contains(""+vowels.charAt(1))){
                            return true;
                        }
                    }else {
                        if (!vowels_not_accent.contains(""+vowels.charAt(0))){
                            return true;
                        }
                    }
                }

                // nguyen am ba
                if (vowels.length() == 3){
                    if ((!vowels_not_accent.contains(""+vowels.charAt(0))) || (!vowels_not_accent.contains(""+vowels.charAt(2)))){
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
