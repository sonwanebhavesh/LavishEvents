package test.lavishevents;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.ContentValues.TAG;

public class RegisterFragment extends Fragment {

    EditText email, password, confirmPassword;
    Button register;
    TextView login;
    TextInputLayout emailView, passwordView, confirmPasswordView;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        login = view.findViewById(R.id.login);
        register = view.findViewById(R.id.register);
        emailView = view.findViewById(R.id.emailView);
        passwordView = view.findViewById(R.id.passwordView);
        confirmPasswordView = view.findViewById(R.id.confirmPasswordView);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.content_main, new LoginFragment()).commit();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailView.setError(null);
                passwordView.setError(null);

                String Semail = email.getText().toString();
                String Spass = password.getText().toString();
                String SconirmPass = confirmPassword.getText().toString();
                boolean cont = true;
                View focus = null;

                if (!Spass.equals(SconirmPass)) {
                    passwordView.setError("Passwords do not match");
                    confirmPasswordView.setError("Passwords do not match");
                    focus = confirmPassword;
                    cont = false;
                }

                if (SconirmPass.trim().equals("")) {
                    confirmPasswordView.setError("Field cannot be empty");
                    focus = confirmPassword;
                    cont = false;
                } else if (SconirmPass.length() < 8) {
                    confirmPasswordView.setError("Invalid Password");
                    focus = confirmPassword;
                    cont = false;
                }

                if (Spass.trim().equals("")) {
                    passwordView.setError("Field cannot be empty");
                    focus = password;
                    cont = false;
                } else if (Spass.length() < 8) {
                    passwordView.setError("Invalid Password");
                    focus = password;
                    cont = false;
                }

                if (Semail.trim().equals("")) {
                    emailView.setError("Field cannot be empty");
                    focus = email;
                    cont = false;
                } else if (!Semail.contains("@")) {
                    emailView.setError("Invalid Email");
                    focus = email;
                    cont = false;
                }

                if (!cont)
                    focus.requestFocus();
                else {
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Loading, Please Wait...");
                    progressDialog.show();

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(Semail, Spass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "User Registered", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Error Occurred", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }
                    });
                }

            }
        });

        return view;
    }

}
