package com.example.autoarticle;

import static com.example.autoarticle.command.C.TYPE_MSG_RECEIVE;

import org.junit.Test;

import android.util.Log;
import android.util.Printer;

import com.example.autoarticle.model.talkBean;
import com.example.autoarticle.model.talkResult;
import com.google.gson.Gson;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        List<talkBean> talkBeans=new ArrayList<>();
        talkBean bean=new talkBean();
        bean.setRole("[GPT]" );
        bean.setText("message.getMsgContent()");
        talkBeans.add(bean);
        talkBean bean1=new talkBean();
        bean1.setRole("[User]" );
        bean1.setText("getMsgContent()");
        talkBeans.add(bean1);
        talkResult result=new talkResult();
        result.setConversations(new Gson().toJson(talkBeans));
        String a=new Gson().toJson(result);
        a.trim();

    }
}