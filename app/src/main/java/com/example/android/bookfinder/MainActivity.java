package com.example.android.bookfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = (Button) findViewById(R.id.search_button);
        final EditText searchEntry = (EditText) findViewById(R.id.search_bar_edit_text);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(searchEntry.getText().toString())) {
                    startActivity(new Intent(getApplicationContext(), BookInfoActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, searchEntry.getText().toString()));
                }
                else{
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_message),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
