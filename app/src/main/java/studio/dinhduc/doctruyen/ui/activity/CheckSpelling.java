package studio.dinhduc.doctruyen.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.dinhduc.doctruyen.R;
import studio.dinhduc.doctruyen.ui.Rule.Rule;
import studio.dinhduc.doctruyen.ui.Rule.Rule0;
import studio.dinhduc.doctruyen.ui.Rule.Rule1;
import studio.dinhduc.doctruyen.ui.Rule.Rule10;
import studio.dinhduc.doctruyen.ui.Rule.Rule11;
import studio.dinhduc.doctruyen.ui.Rule.Rule12;
import studio.dinhduc.doctruyen.ui.Rule.Rule13;
import studio.dinhduc.doctruyen.ui.Rule.Rule14;
import studio.dinhduc.doctruyen.ui.Rule.Rule15;
import studio.dinhduc.doctruyen.ui.Rule.Rule16;
import studio.dinhduc.doctruyen.ui.Rule.Rule17;
import studio.dinhduc.doctruyen.ui.Rule.Rule18;
import studio.dinhduc.doctruyen.ui.Rule.Rule19;
import studio.dinhduc.doctruyen.ui.Rule.Rule2;
import studio.dinhduc.doctruyen.ui.Rule.Rule20;
import studio.dinhduc.doctruyen.ui.Rule.Rule21;
import studio.dinhduc.doctruyen.ui.Rule.Rule3;
import studio.dinhduc.doctruyen.ui.Rule.Rule4;
import studio.dinhduc.doctruyen.ui.Rule.Rule5;
import studio.dinhduc.doctruyen.ui.Rule.Rule6;
import studio.dinhduc.doctruyen.ui.Rule.Rule7;
import studio.dinhduc.doctruyen.ui.Rule.Rule8;
import studio.dinhduc.doctruyen.ui.Rule.Rule9;
import studio.dinhduc.doctruyen.ui.constant.Const;
import studio.dinhduc.doctruyen.util.CommonUtils;

/**
 * Created by duy on 27/11/2016.
 */

public class CheckSpelling extends AppCompatActivity {

    ListView list_error;
    ArrayList<String> list_words_error = new ArrayList<>();
    ArrayAdapter<String> adapter=null;

    ArrayList<Rule> allRules = new ArrayList<>();
    String word;

    @BindView(R.id.tool_bar) Toolbar mToolBar;
    private String mChapterContent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_spelling);

        ButterKnife.bind(this);

        list_error = (ListView)findViewById(R.id.list_error);

        // display list words invalidate
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_words_error);
        list_error.setAdapter(adapter);


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

        allRules.add(rule0);
        allRules.add(rule1);
        allRules.add(rule2);
        allRules.add(rule3);
        allRules.add(rule4);
        allRules.add(rule5);
        allRules.add(rule6);
        allRules.add(rule7);
        allRules.add(rule8);
        allRules.add(rule9);
        allRules.add(rule10);
        allRules.add(rule11);
        allRules.add(rule12);
        allRules.add(rule13);
        allRules.add(rule14);
        allRules.add(rule15);
        allRules.add(rule16);
        allRules.add(rule17);
        allRules.add(rule18);
        allRules.add(rule19);
        allRules.add(rule20);
        allRules.add(rule21);

        initView();

    }

    private void initView() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String chapterPath = getIntent().getStringExtra(Const.KeyIntent.KEY_CHAPTER_PATH);
                mChapterContent = CommonUtils.readFileTxt(chapterPath);
                mChapterContent = mChapterContent.replace("<br>"," ");
                String[] words = mChapterContent.split("\\s+");
                for (int i=0; i<words.length; i++){
                    //list_words_error.add(words[i]);
                    word = deleteSign(words[i]);
                    if (word.length()>0){
                        check(word);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();

                    }
                });

            }
        });
        thread.start();

    }

    // check word with all rules
    public void check(String word) {
        if (word.length()==1){
            if (allRules.get(0).checkInvalidate(word)){
                list_words_error.add(word);
            }
        }else {
            for (int i = 1; i < allRules.size(); i++) {
                if (allRules.get(i).checkInvalidate(word)) {
                    list_words_error.add(word);
                    break;
                }
            }
        }
    }

    // delete sign: ? : ! ...
    public String deleteSign(String word){

        String sign = "?.,;:\"!)";
        if ((word.charAt(0)=='"')||(word.charAt(0)=='(')){
            word = word.substring(1);
        }
        while ((word.length()>0)&&(sign.contains(""+word.charAt(word.length()-1)))){
            word = word.substring(0, word.length()-1);
        }
        return word;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
