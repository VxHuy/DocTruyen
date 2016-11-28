package studio.dinhduc.doctruyen.ui.Rule;

/**
 * Created by duy on 25/11/2016.
 */
public class RemoveAccent {
    public static String removeAccent(String str){

        str= str.toLowerCase();

        str= str.replaceAll("[àáạảã]","a");
        str= str.replaceAll("[ằắặẳẵ]","ă");
        str= str.replaceAll("[ầấậẩẫ]","â");
        str= str.replaceAll("[èéẹẻẽ]","e");
        str= str.replaceAll("[ềếệểễ]","ê");
        str= str.replaceAll("[ìíịỉĩ]","i");
        str= str.replaceAll("[òóọỏõ]","o");
        str= str.replaceAll("[ồốộổỗ]","ô");
        str= str.replaceAll("[ờớợởỡ]","ơ");
        str= str.replaceAll("[ùúụủũ]","u");
        str= str.replaceAll("[ừứựửữ]","ư");
        str= str.replaceAll("[ỳýỵỷỹ]","y");

        return str;
    }
}
