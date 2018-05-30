package com.example.dialog;

        import java.lang.reflect.Field;

        import android.os.Bundle;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        showWaiterAuthorizationDialog();
    }

    // 显示对话框
    public void showWaiterAuthorizationDialog() {

        // LayoutInflater是用来找layout文件夹下的xml布局文件，并且实例化
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        // 把activity_login中的控件定义在View中
        final View textEntryView = factory.inflate(R.layout.activity_main,
                null);

        // 将LoginActivity中的控件显示在对话框中
        new AlertDialog.Builder(MainActivity.this)
                // 对话框的标题
                .setTitle("登陆")
                // 设定显示的View
                .setView(textEntryView)
                // 对话框中的“登陆”按钮的点击事件
                .setPositiveButton("登陆", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        // 获取用户输入的“用户名”，“密码”
                        // 注意：textEntryView.findViewById很重要，因为上面factory.inflate(R.layout.activity_login,
                        // null)将页面布局赋值给了textEntryView了
                        final EditText etUserName = (EditText) textEntryView
                                .findViewById(R.id.etuserName);
                        final EditText etPassword = (EditText) textEntryView
                                .findViewById(R.id.etPWD);

                        // 将页面输入框中获得的“用户名”，“密码”转为字符串
                        String userName = etUserName.getText().toString()
                                .trim();
                        String password = etPassword.getText().toString()
                                .trim();

                        // 现在为止已经获得了字符型的用户名和密码了，接下来就是根据自己的需求来编写代码了
                        // 这里做一个简单的测试，假定输入的用户名是abc 密码是123则进入其他操作页面（OperationActivity）
                        if (userName.equals("abc") && password.equals("123")) {
                            // 跳转到OperationActivity
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, OperationActivity.class);
                            startActivity(intent);
                            // 关闭当前页面
                            MainActivity.this.finish();

                        } else {
                            Toast.makeText(MainActivity.this, "密码或用户名错误",
                                    Toast.LENGTH_SHORT).show();

                            try {
                                // 注意此处是通过反射，修改源代码类中的字段mShowing为true，系统会认为对话框打开
                                // 从而调用dismiss()
                                Field field = dialog.getClass().getSuperclass()
                                        .getDeclaredField("mShowing");
                                field.setAccessible(true);
                                field.set(dialog, false);
                                dialog.dismiss();

                            } catch (Exception e) {

                            }
                        }
                    }
                })
                // 对话框的“退出”单击事件
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity.this.finish();
                    }
                })
                // 设置dialog是否为模态，false表示模态，true表示非模态
                .setCancelable(false)
                // 对话框的创建、显示
                .create().show();
    }
}
