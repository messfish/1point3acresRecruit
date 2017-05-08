package a1point3acres.com.example.android.a1point3acres;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import a1point3acres.com.example.android.a1point3acres.Utils.AppendAsteriskUtil;
import a1point3acres.com.example.android.a1point3acres.Utils.NetworkUtil;

public class SignInActivity extends AppCompatActivity {

    private EditText mUserText;
    private EditText mPasswordText;
    private EditText mAnswerText;
    private Spinner mSpinner;
    private Map<String, Integer> indexMap;

    private static final String TAG = SignInActivity.class.getSimpleName();
    private static final int TIME_FOR_SWAP_IN_MILLISECONDS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mSpinner = (Spinner)findViewById(R.id.spinner_sign_in);
        String[] items = new String[]{"安全提问（未设置请忽略）",
                                      "母亲的名字",
                                      "爷爷的名字",
                                      "父亲出生的城市",
                                      "您其中一位老师的名字",
                                      "您个人计算机的型号",
                                      "您最喜欢的餐馆名称",
                                      "驾驶执照最后四位数字"};
        indexMap = new HashMap<>();
        for(int i=0;i<items.length;i++) {
            indexMap.put(items[i], i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mUserText = (EditText) findViewById(R.id.username_input_sign_in);
        mPasswordText = (EditText) findViewById(R.id.password_input_sign_in);
        mAnswerText = (EditText) findViewById(R.id.answer_sign_in);
        TextView[] array = new TextView[2];
        array[0] = (TextView) findViewById(R.id.username_sign_in);
        array[1] = (TextView) findViewById(R.id.password_sign_in);
        AppendAsteriskUtil.appendAsterisk(array);
    }

    /**
     * 这是点击button时会进行的method,尝试进行登录。
     * @param v 在这里并没有什么卵用。
     */
    public void tryToLogIn(View v) {
        String userValue = mUserText.getText().toString();
        String passwordValue = mPasswordText.getText().toString();
        String selectedValue = mSpinner.getSelectedItem().toString();
        int id = indexMap.get(selectedValue);
        String questionIDValue = String.valueOf(id);
        String answerValue = mAnswerText.getText().toString();
        new FetchResultTask().execute(userValue,passwordValue,questionIDValue,answerValue);
    }

    /**
     * 因为web的访问请求是比较耗时的工作，因而把它放到主线程上会出错。
     * 解决方法是创造一个AsyncTask来处理web的访问。
     */
    private class FetchResultTask extends AsyncTask<String, Void, String> {

        private URL resultURL;

        /**
         * 用登录所必要的参数构建一个新的url，以这个url进行web访问并将访问结果提取出来。
         * @param params 进行用户登录所必须的参数。
         * @return web访问的结果。
         */
        @Override
        protected String doInBackground(String... params) {
            String userValue = params[0];
            String passwordValue = params[1];
            String questionIDValue = params[2];
            String answerValue = params[3];
            resultURL = NetworkUtil.buildQueryURL(userValue,passwordValue,
                                                     questionIDValue,answerValue);
            String webPage = null;
            try {
                webPage = NetworkUtil.getWebPage(resultURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String result = NetworkUtil.regexForResult(webPage);
            return result;
        }

        /**
         * 如果登陆成功，将会把url的值传给web browser并在web browser里面展示最终结果。
         * 如果失败会显示失败信息。
         * @param message 储存登录结果的string。
         */
        @Override
        protected void onPostExecute(String message) {
            if (message != null && message.length() != 0) {
                Toast.makeText(SignInActivity.this,message,Toast.LENGTH_SHORT).show();
                if (message.startsWith("欢迎")) {
                    try {
                        Thread.sleep(TIME_FOR_SWAP_IN_MILLISECONDS);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    Log.v(TAG, "最终的URL是：" + resultURL);
                    Uri resultUri = Uri.parse(resultURL.toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW, resultUri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }
        }

    }

}
