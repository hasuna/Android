package com.example.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
public class MainActivity extends Activity {
    private TextView text_view = null;
    private Button start = null;
    private Button end = null;
    int i;

    //使用handler时首先要创建一个handler
    Handler handler = new Handler();
    //要用handler来处理多线程可以使用runnable接口，这里先定义该接口
    //线程中运行该接口的run函数
    Runnable update_thread = new Runnable()
    {
        public void run()
        {
            //线程每次执行时输出"UpdateThread..."文字,且自动换行
            //textview的append功能和Qt中的append类似，不会覆盖前面
            //的内容，只是Qt中的append默认是自动换行模式
            text_view.append("\n更新数据...");
            //延时1s后又将线程加入到线程队列中
            handler.postDelayed(update_thread, 2000);

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_view = (TextView)findViewById(R.id.text_view);
        start = (Button)findViewById(R.id.start);
        start.setOnClickListener(new StartClickListener());
        end = (Button)findViewById(R.id.end);
        end.setOnClickListener(new EndClickListener());

    }
    private class StartClickListener implements OnClickListener
    {
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //将线程接口立刻送到线程队列中
            handler.post(update_thread);
        }
    }

    private class EndClickListener implements OnClickListener
    {
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //将接口从线程队列中移除
            handler.removeCallbacks(update_thread);
        }
    }
}