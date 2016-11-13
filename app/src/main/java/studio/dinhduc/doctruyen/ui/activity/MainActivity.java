package studio.dinhduc.doctruyen.ui.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import studio.dinhduc.doctruyen.R;

public class MainActivity extends ListActivity {
    ArrayList<String> mNovelNames = new ArrayList<>();
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
