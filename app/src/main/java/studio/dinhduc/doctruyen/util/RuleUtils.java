package studio.dinhduc.doctruyen.util;

import java.util.ArrayList;

import studio.dinhduc.doctruyen.ui.rule.Rule;
import studio.dinhduc.doctruyen.ui.rule.Rule0;
import studio.dinhduc.doctruyen.ui.rule.Rule1;
import studio.dinhduc.doctruyen.ui.rule.Rule10;
import studio.dinhduc.doctruyen.ui.rule.Rule11;
import studio.dinhduc.doctruyen.ui.rule.Rule12;
import studio.dinhduc.doctruyen.ui.rule.Rule13;
import studio.dinhduc.doctruyen.ui.rule.Rule14;
import studio.dinhduc.doctruyen.ui.rule.Rule15;
import studio.dinhduc.doctruyen.ui.rule.Rule16;
import studio.dinhduc.doctruyen.ui.rule.Rule17;
import studio.dinhduc.doctruyen.ui.rule.Rule18;
import studio.dinhduc.doctruyen.ui.rule.Rule19;
import studio.dinhduc.doctruyen.ui.rule.Rule2;
import studio.dinhduc.doctruyen.ui.rule.Rule20;
import studio.dinhduc.doctruyen.ui.rule.Rule21;
import studio.dinhduc.doctruyen.ui.rule.Rule22;
import studio.dinhduc.doctruyen.ui.rule.Rule23;
import studio.dinhduc.doctruyen.ui.rule.Rule3;
import studio.dinhduc.doctruyen.ui.rule.Rule4;
import studio.dinhduc.doctruyen.ui.rule.Rule5;
import studio.dinhduc.doctruyen.ui.rule.Rule6;
import studio.dinhduc.doctruyen.ui.rule.Rule7;
import studio.dinhduc.doctruyen.ui.rule.Rule8;
import studio.dinhduc.doctruyen.ui.rule.Rule9;

/**
 * Created by dinhduc on 28/11/2016.
 */

public class RuleUtils {
    private static ArrayList<Rule> mRules = new ArrayList<>();

    public static void setUp() {
        // create group rule
        Rule rule0 = new Rule0();
        Rule rule1 = new Rule1();
        Rule rule2 = new Rule2();
        Rule rule3 = new Rule3();
        Rule rule4 = new Rule4();
        Rule rule5 = new Rule5();
        Rule rule6 = new Rule6();
        Rule rule7 = new Rule7();
        Rule rule8 = new Rule8();
        Rule rule9 = new Rule9();
        Rule rule10 = new Rule10();
        Rule rule11 = new Rule11();
        Rule rule12 = new Rule12();
        Rule rule13 = new Rule13();
        Rule rule14 = new Rule14();
        Rule rule15 = new Rule15();
        Rule rule16 = new Rule16();
        Rule rule17 = new Rule17();
        Rule rule18 = new Rule18();
        Rule rule19 = new Rule19();
        Rule rule20 = new Rule20();
        Rule rule21 = new Rule21();
        Rule rule22 = new Rule22();
        Rule rule23 = new Rule23();

        mRules.add(rule0);
        mRules.add(rule1);
        mRules.add(rule2);
        mRules.add(rule3);
        mRules.add(rule4);
        mRules.add(rule5);
        mRules.add(rule6);
        mRules.add(rule7);
        mRules.add(rule8);
        mRules.add(rule9);
        mRules.add(rule10);
        mRules.add(rule11);
        mRules.add(rule12);
        mRules.add(rule13);
        mRules.add(rule14);
        mRules.add(rule15);
        mRules.add(rule16);
        mRules.add(rule17);
        mRules.add(rule18);
        mRules.add(rule19);
        mRules.add(rule20);
        mRules.add(rule21);
        mRules.add(rule22);
        mRules.add(rule23);
    }

    // check word with all rules
    public static boolean check(String word) {
        if (word.length() == 1) {
            if (mRules.get(0).checkInvalidate(word)) {
                return true;
            }
        } else {
            for (int i = 1; i < mRules.size(); i++) {
                if (mRules.get(i).checkInvalidate(word)) {
                    return true;
                }
            }
        }
        return false;
    }

}
