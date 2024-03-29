package montacer.elfazazi.ejerc5clasepmdmtema1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import montacer.elfazazi.ejerc5clasepmdmtema1.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance(); //importantee para inicializar el euth

        binding.btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.txtEmailLogin.getText().toString();
                String password = binding.txtPasswordLogin.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()){ //comprueba q ha rellenado los campos
                    doLogin(email, password);
                }
            }
        });

        binding.btnRegistrarrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.txtEmailLogin.getText().toString();
                String password = binding.txtPasswordLogin.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()){ //comprueba q ha rellenado los campos
                    doRegister(email, password);
                }
            }
        });
    }

    //usuario ejemplo para probar: qwertyuiop@gmail.es
    //password: 123456789

    private void doRegister(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){ //si se ha creado el usuario correctamente
                            updateUI(auth.getCurrentUser()); //envia ese usuario a updateUi
                        }else{
                            Toast.makeText(LoginActivity.this, "error en el registro", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "error en el registro", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void doLogin(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){ //si se ha iniciado sesion correctamente
                    updateUI(auth.getCurrentUser()); //envia ese usuario a updateUi
                }else{
                    Toast.makeText(LoginActivity.this, "error en el login", Toast.LENGTH_SHORT).show();
                }
            }
        })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "error en el login", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){ //comprueba que hay un usuario y se va a mainActivity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart(); //se ejecuta nada mas iniciar la aplicacion y envia el usuario a updateUi
                //si existe un usuario (porq no se cerro la sesion) en updateUi ira directamente a la mainActivity
                //sino, lo mandara a login activity
        updateUI(auth.getCurrentUser());
    }
}