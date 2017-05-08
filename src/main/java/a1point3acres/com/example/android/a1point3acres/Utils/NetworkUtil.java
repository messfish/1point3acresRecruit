package a1point3acres.com.example.android.a1point3acres.Utils;

/**
 * Created by messfish on 5/7/17.
 */

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个类主要是用来处理与web后端的交互。将用户输入的参数用post的方式送给web app的后端并
 * 把结果以toast的方式反馈给用户。
 */
public class NetworkUtil {

    private static final String TAG = NetworkUtil.class.getSimpleName();
    // 写这个的目的是用来debug。

    private static final String WEB_BASE_URL = "http://www.1point3acres.com/bbs/member.php?";
    private static final String MODE_KEY = "mod";
    private static final String MODE_VALUE = "logging";
    private static final String ACTION_KEY = "action";
    private static final String ACTION_VALUE = "login";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String QUESTION_ID_KEY = "questionid";
    private static final String ANSWER_KEY = "answer";
    private static final String LOGIN_SUBMIT_KEY = "loginsubmit";
    private static final String LOGIN_SUBMIT_VALUE = "yes";
    private static final String LOGIN_HASH_KEY = "loginhash";

    private static final String REGEX_FOR_HASH_VALUE = "&amp;loginhash=.{5}";
    // 因为hash的值固定为5位数，因而我采用这个方式来写regex。
    private static final String REGEX_FOR_RESULT = "<div class=\"jump_c\"><p>[^<]*";
    // 把<p>里面的值提取出来并返回给用户。
    private static final int PUSH_FORWARD_HASH_VALUE = 15;
    private static final int PUSH_FORWARD_RESULT = 23;
    // 这两个值主要是用于把无关的字符弄出去。

    /**
     * 这个method用于构造登录主界面的url。目的是为了提取出form里面hash的值。
     * @return 登录主界面的url。
     */
    private static URL buildLoginURL() {
        Uri buildUri = Uri.parse(WEB_BASE_URL).buildUpon()
                          .appendQueryParameter(MODE_KEY, MODE_VALUE)
                          .appendQueryParameter(ACTION_KEY, ACTION_VALUE).build();
        URL result = null;
        try {
            result = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "构造出的url为: " + result);
        return result;
    }

    /**
     * 这个method主要用于构造返回结果的url。
     * @param nameValue 用户名的值。
     * @param passwordValue 密码的值。
     * @param questionIDValue 安全提问id的值。
     * @param answerValue 安全提问结果的值。
     * @return 返回登录结果的url。
     */
    public static URL buildQueryURL(String nameValue, String passwordValue,
                                    String questionIDValue, String answerValue) {
        String hashValue = null;
        try {
            hashValue = regexForHashValue(getWebPage(buildLoginURL()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri.Builder build = Uri.parse(WEB_BASE_URL).buildUpon()
                          .appendQueryParameter(MODE_KEY, MODE_VALUE)
                          .appendQueryParameter(ACTION_KEY, ACTION_VALUE)
                          .appendQueryParameter(USERNAME_KEY, nameValue)
                          .appendQueryParameter(PASSWORD_KEY, passwordValue);
        /* 用户没有设置安全问题，跳过与之相关的参数。*/
        if (!questionIDValue.equals("0")) {
            build.appendQueryParameter(QUESTION_ID_KEY, questionIDValue)
                    .appendQueryParameter(ANSWER_KEY, answerValue);
        }
        Uri buildUri = build.appendQueryParameter(LOGIN_SUBMIT_KEY, LOGIN_SUBMIT_VALUE)
                          .appendQueryParameter(LOGIN_HASH_KEY, hashValue).build();
        URL result = null;
        try {
            result = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "构造出的url为: " + result);
        return result;
    }

    /**
     * 这个method主要用于读取url对应的网页并把结果以string的形式输出， 用于之后的正则表达式。
     * @param url 我们感兴趣的url
     * @return 以string形式返回的web page。
     * @throws IOException 当url没有找到webpage时扔出这个exception。
     */
    public static String getWebPage(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();
        return response.toString();
    }

    /**
     * 这个method主要是为了找到login网页对应的hash值并返回。
     * @param input 以string形式的web page。
     * @return login web page 里头的hash value。
     */
    private static String regexForHashValue(String input) {
        Pattern pattern = Pattern.compile(REGEX_FOR_HASH_VALUE);
        Matcher matcher = pattern.matcher(input);
        int startIndex = 0, endIndex = 0;
        if (matcher.find()) {
            startIndex = matcher.start();
            endIndex = matcher.end();
        }
        return input.substring(startIndex + PUSH_FORWARD_HASH_VALUE, endIndex);
    }

    /**
     * 这个method主要是将user的登录请求的结果找出，用于之后的toast message。
     * @param input 以string形式的web page。
     * @return login result 里头的message。
     */
    public static String regexForResult(String input) {
        Pattern pattern = Pattern.compile(REGEX_FOR_RESULT);
        Matcher matcher = pattern.matcher(input);
        int startIndex = 0, endIndex = 0;
        if (matcher.find()) {
            startIndex = matcher.start();
            endIndex = matcher.end();
        }
        return input.substring(startIndex + PUSH_FORWARD_RESULT, endIndex);
    }

}
