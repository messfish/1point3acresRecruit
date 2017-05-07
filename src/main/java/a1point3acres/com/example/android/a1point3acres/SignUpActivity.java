package a1point3acres.com.example.android.a1point3acres;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SignUpActivity extends AppCompatActivity {

    Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mSpinner = (Spinner)findViewById(R.id.spinner_sign_up);
        String[] items = new String[]{"A", "B", "O", "AB"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, items);
        mSpinner.setAdapter(adapter);
    }
}
