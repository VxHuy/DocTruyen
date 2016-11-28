package studio.dinhduc.doctruyen.ui.Rule;

/**
 * Created by duy on 27/11/2016.
 */

/* có 32 nguyên âm đôi: AI, AO, AU, ÂU, AY, ÂY, EO, ÊU, IA, IÊ/YÊ, IU, OA, OĂ, OE, OI, ÔI,
                        ƠI, OO, ÔÔ, UA, UĂ, UÂ, ƯA, UÊ, UI, ƯI,UO, UÔ, UƠ, ƯƠ, ƯU, UY
*  có 13 nguyên âm ba: IÊU/YÊU, OAI, OAO, OAY, OEO, UAO, UÂY, UÔI, ƯƠI, ƯƠU, UYA, UYÊ, UYU */
public class Rule21 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        String double_vowels = "ai ao au âu ay ây eo êu ia iê yê iu oa oă" +
                " oe oi ôi ơi oo ôô ua uă uâ ưa uê ui ưi uo uô uơ ươ ưu uy";
        String three_vowels = "iêu yêu oai oao oay oeo uao uây uôi ươi ươu uya uyê uyu";
        String consonant = "qrtpsdghklxcvbnmđ";
        word = RemoveAccent.removeAccent(word);
        String vowels = "";

        for (int i=0; i<word.length(); i++){
            if (!consonant.contains(""+word.charAt(i))){
                vowels = vowels + word.charAt(i);
            }
        }

        String w = word.substring(0,2);
        if (w.equals("gi")||w.equals("qu")){
            vowels = vowels.substring(1);
        }

        if ((vowels.length()==2)&&(!double_vowels.contains(vowels))){
            return true;
        }

        if ((vowels.length()==3)&&(!three_vowels.contains(vowels))){
            return true;
        }

        return false;
    }
}
