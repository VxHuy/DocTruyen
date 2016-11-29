package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 27/11/2016.
 */

/* có 32 nguyên âm đôi: AI, AO, AU, ÂU, AY, ÂY, EO, ÊU, IA, IÊ/YÊ, IU, OA, OĂ, OE, OI, ÔI,
                        ƠI, OO, ÔÔ, UA, UĂ, UÂ, ƯA, UÊ, UI, ƯI,UO, UÔ, UƠ, ƯƠ, ƯU, UY
*  có 13 nguyên âm ba: IÊU/YÊU, OAI, OAO, OAY, OEO, UAO, UÂY, UÔI, ƯƠI, ƯƠU, UYA, UYÊ, UYU
*  Bắt buộc thêm nguyên âm cuối, hoặc phụ âm cuối: Â, IÊ,UÂ,UÔ,ƯƠ,YÊ.
   Bắt buộc thêm phụ âm cuối: Ă, OĂ, OO, ÔÔ, UĂ, UYÊ.
   có 29 nguyên âm ghép không thêm được phần âm cuối là: AI, AO, AU, ÂU, AY, ÂY, EO, ÊU, IA, IÊU/YÊU, IU, OI,
    ÔI, ƠI, OAI, OAO, OAY, OEO, ƯA, UI, ƯI, ƯU, UƠ, UAI, UÂY, UÔI, ƯƠI, ƯƠU, UYA và UYU */
public class Rule21 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        String double_vowels = "ai ao au âu ay ây eo êu ia iê yê iu oa oă" +
                " oe oi ôi ơi oo ôô ua uă uâ ưa uê ui ưi uo uô uơ ươ ưu uy";
        String three_vowels = "iêu yêu oai oao oay oeo uao uây uôi ươi ươu uya uyê uyu";
        String[] vowels_not_end = {"â", "iê", "uâ", "uô", "ươ", "yê"};
        String[] vowels_end_consonant = {"ă", "oă", "oo", "ôô", "uă", "uyê"};
        String[] vowels_end = {"ai", "ao", "au", "âu", "ay", "ây", "eo", "êu", "ia", "iêu", "yêu", "iu", "oi", "ôi", "ơi", "oai",
                "oao", "oay", "oeo", "ưa", "ui", "ưi", "ưu", "uơ", "uai", "uây", "uôi", "ươi", "ươu", "uya", "uyu"};
        String consonant = "qrtpsdghklxcvbnmđ";
        word = RemoveAccent.removeAccent(word);
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
        if (w.equals("gi")||w.equals("qu")){
            vowels = vowels.substring(1);
        }

        // nguyen am doi
        if ((vowels.length()==2)&&(!double_vowels.contains(vowels))){
            return true;
        }

        // nguyen am ba
        if ((vowels.length()==3)&&(!three_vowels.contains(vowels))){
            return true;
        }

        // nguyen am khong dung cuoi
        for (String i:vowels_not_end){
            if (i.equals(vowels)&&(latter==(word.length()-1))){
                return true;
            }
        }

        // nguyen am co phu am cuoi
        for (String i:vowels_end_consonant) {
            if (i.equals(vowels)) {
                if (latter == (word.length() - 1)) {
                    return true;
                } else if (!consonant.contains("" + word.charAt(latter + 1))) {
                    return true;
                }
            }
        }

        // nguyen am dung cuoi
        for (String i:vowels_end) {
            if (i.equals(vowels)&&(latter!=(word.length()-1))){
                return true;
            }
        }


        return false;
    }
}
