package christianedwinp.hive_ver10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;


public class Activity_Welcomepage extends AppCompatActivity {
    private static Button signup_button;
    private static Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomepage_activity);
        OnClickButton();
    }

    public void OnClickButton(){
//        Activity_Signup button
        signup_button = (Button)findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Activity_Welcomepage.this,Activity_Signup.class);
                        startActivity(intent);
                    }
                }
        );

//        Login button
        login_button = (Button)findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Activity_Welcomepage.this,Activity_Main.class);
                        startActivity(intent);
                    }
                }
        );
    }

}
