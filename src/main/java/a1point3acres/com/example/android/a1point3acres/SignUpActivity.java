package a1point3acres.com.example.android.a1point3acres;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import a1point3acres.com.example.android.a1point3acres.Utils.AppendAsteriskUtil;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();

    private Spinner mSpinner;
    private ImageButton mImageButton;
    private Queue<Integer> imageQueue;
    private Set<Integer> selectedImageIdSet;
    private static final String DRAWABLE_BASE = "checkcode";
    private static final int NUM_OF_IMAGES = 25;
    private static final int NUM_WITHOUT_DUPLICATES = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mSpinner = (Spinner)findViewById(R.id.spinner_sign_up);
        String[] items = new String[]{"A", "B", "O", "AB"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, items);
        mSpinner.setAdapter(adapter);
        TextView[] array = new TextView[5];
        array[0] = (TextView) findViewById(R.id.username_sign_up);
        array[1] = (TextView) findViewById(R.id.password_sign_up);
        array[2] = (TextView) findViewById(R.id.confirm_password_sign_up);
        array[3] = (TextView) findViewById(R.id.email_sign_up);
        array[4] = (TextView) findViewById(R.id.check_code);
        AppendAsteriskUtil.appendAsterisk(array);
        imageQueue = new LinkedList<>();
        selectedImageIdSet = new HashSet<>();
        int idToShow = (int)(Math.random() * NUM_OF_IMAGES);
        imageQueue.offer(idToShow);
        selectedImageIdSet.add(idToShow);
        String mDrawableName = DRAWABLE_BASE + String.valueOf(idToShow);
        int resID = getResources().getIdentifier
                (mDrawableName , "drawable", getPackageName());
        mImageButton = (ImageButton) findViewById(R.id.check_code_image);
        mImageButton.setImageResource(resID);
    }

    /**
     * 当用户单机图片时，验证码的图片会发生改变。在连续5次之内不会出现
     * 相同的验证码图片。
     * @param v 依旧没什么用的parameter。
     */
    public void changeImage(View v) {
        if (selectedImageIdSet.size() == NUM_WITHOUT_DUPLICATES) {
            int idToDriveout = imageQueue.poll();
            selectedImageIdSet.remove(idToDriveout);
        }
        int idToShow = -1;
        while(true) {
            idToShow = (int)(Math.random() * NUM_OF_IMAGES);
            if (!selectedImageIdSet.contains(idToShow)) {
                selectedImageIdSet.add(idToShow);
                imageQueue.offer(idToShow);
                break;
            }
        }
        String mDrawableName = DRAWABLE_BASE + String.valueOf(idToShow);
        Log.v(TAG, "最终选取的结果图片： " + mDrawableName);
        int resID = getResources().getIdentifier
                (mDrawableName , "drawable", getPackageName());
        mImageButton.setImageResource(resID);
    }

}
