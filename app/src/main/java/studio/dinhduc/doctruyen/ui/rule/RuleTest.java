package studio.dinhduc.doctruyen.ui.rule;

/**
 * Created by duy on 23/11/2016.
 */
public class RuleTest {

    public String getVietnamese(String x){

        String invalid_string = "123456789fjwzFJWZ~`!@#$%^&*()-_+={[]}|\\:;'\"<,>./?";

        for (int i=0; i<x.length(); i++){
            if (invalid_string.contains(""+x.charAt(i))){
                x = x.replace(x.charAt(i), ' ');
            }
        }
        // delete space
        x = x.replaceAll("\\s+", " ");
        x = x.replaceAll("(^\\s+|\\s+$)", "");

        return x;
    }
}