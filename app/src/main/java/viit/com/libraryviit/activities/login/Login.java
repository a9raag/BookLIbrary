package viit.com.libraryviit.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import viit.com.libraryviit.R;
import viit.com.libraryviit.activities.MainActivity;
import viit.com.libraryviit.activities.register.RegisterActivity;

public class Login extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;
    private static final String TAG = "Login.java" ;
    @InjectView(R.id.et_library_no)
    EditText etUsername;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.bt_go)
    Button btGo;
    @InjectView(R.id.cv)
    CardView cv;
//    @InjectView(R.id.fab)
//    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
        if(shared.contains("username") && shared.contains("password")){
            Toast.makeText(this,"Exists",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, MainActivity.class));
        }
        setContentView(R.layout.content_login);

        ButterKnife.inject(this);

    }

    @OnClick({R.id.bt_go, R.id.fab,R.id.register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    ActivityOptions options =
//                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class));
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.bt_go:

                signInUser(etUsername.getText().toString().trim(),etPassword.getText().toString());

                break;
        }

    }
    public void signInUser(final String user1, final String password){
        final String user=user1+"@viitlib.ac.in";
        final ProgressBar userSearch = (ProgressBar) findViewById(R.id.user_serach_progress);
        userSearch.setVisibility(View.VISIBLE);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        "anurag@viitlib.ac.in",anurag
        Toast.makeText(this,"Signing in the user",Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword( user,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(getApplicationContext(),"Authentication Failed username or password incorrect try again",
                                    Toast.LENGTH_SHORT).show();
                            saveInformation(user,password);
                            userSearch.setVisibility(View.GONE);

                        }
                        else {
//
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(intent);
//                            finish();
                            saveInformation(user,password);
                            Toast.makeText(getApplicationContext(),"Authentication Successful",
                                    Toast.LENGTH_SHORT).show();
                            Explode explode = new Explode();
                            explode.setDuration(500);
                            getWindow().setExitTransition(explode);
                            getWindow().setEnterTransition(explode);
//                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation);
                            Intent i2 = new Intent(getApplicationContext(), viit.com.libraryviit.activities.MainActivity.class);
                            startActivity(i2 );
                            finish();
                        }

                        // ...
                    }
                });
    }
    public void saveInformation(String username,String password) {
        SharedPreferences shared = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

}
