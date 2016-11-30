package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 30/11/2016.
 */

/* không phải nguyên âm đôi, ba nào cũng có thể đứng một mình. */
public class Rule23 implements Rule {
    @Override
    public boolean checkInvalidate(String word) {

        String[] double_vowels = {"ai", "ái", "ải", "ao", "ào", "áo", "ảo", "au", "âu", "ấu", "ẩu", "áy",
                "ây", "ấy", "eo", "ẹo", "éo", "ẻo", "êu", "ỉa", "ỉu", "oa", "òa", "óa", "oe", "ọe", "oi", "ói",
                "ôi", "ối", "ổi", "ơi", "ới", "ùa", "úa", "ủa", "ưa", "ựa", "ứa", "uế", "uể", "ui",
                "úi", "ủi", "ưu", "uy", "úy", "ủy"};
        String[] three_vowels = {"yêu", "yếu", "yểu", "oai", "oái", "oải", "ươi"};
        String consonant = "qrtpsdghklxcvbnmđ";
        word = word.toLowerCase();
        for (int i=0; i<word.length();i++){
            if (consonant.contains(""+word.charAt(i)))
                return false;
        }

        // nguyen am doi
        if (word.length()==2){
            boolean check = true;
            for (String i:double_vowels){
                if (i.equals(word)){
                    check = false;
                    break;
                }
            }
            if (check){
                return true;
            }
        }

        // nguyen am ba
        if (word.length()==3){
            boolean check = true;
            for (String i:three_vowels){
                if (i.equals(word)){
                    check = false;
                    break;
                }
            }
            if (check){
                return true;
            }
        }


        return false;
    }
}
