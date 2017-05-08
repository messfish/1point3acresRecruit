package a1point3acres.com.example.android.a1point3acres.Utils;

/**
 * Created by messfish on 5/8/17.
 */

import android.text.Html;
import android.widget.TextView;

/**
 * 创建这个类的目的是将TextView后面加上一个红色的*号来表示这是必填项目。
 */
public class AppendAsteriskUtil {

    /**
     * 对所有在array里面的Textview附上一个红色的*号表示必填。
     * @param textViews 需要被处理的TextView。
     */
    public static void appendAsterisk(TextView[] textViews) {
        for (TextView t : textViews) {
            String content = t.getText().toString();
            String asterisk = "<font color='#EE0000'>*</font>";
            t.setText(Html.fromHtml(content + " " + asterisk + " : "));
        }
    }

}
