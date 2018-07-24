package com.example.gagan.bloodbank;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class PhoneVerify extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private  EditText ephonenum;
    private Button sendOtp;
    private Dialog dialog;

    private String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private FirebaseAuth.AuthStateListener mstate;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        ephonenum=(EditText)findViewById(R.id.field_phone_number);
        firebaseAuth=FirebaseAuth.getInstance();
        sendOtp=(Button)findViewById(R.id.button_start_verification);

        mstate= new FirebaseAuth.AuthStateListener() {
            @Override

            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){// to check whether user is already logged in or not
                    startActivity(new Intent(PhoneVerify.this,UserHome.class));

                }
            }
        };


        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenum=ephonenum.getText().toString();
                if(phonenum.length()<10|| phonenum.length()>10||phonenum.isEmpty())
                {
                    Toast.makeText(PhoneVerify.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phonenum,60,TimeUnit.SECONDS,PhoneVerify.this,mcallbacks);
                Toast.makeText(PhoneVerify.this, "OTP Sent,Please Check Your Phone", Toast.LENGTH_SHORT).show();
                dialog.show();
                dialog.setCancelable(false);
            }
        });

        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                //Toast.makeText(PhoneVerify.this, "Verified", Toast.LENGTH_LONG).show();
                String code = credential.getSmsCode();
                if (code != null) {
                    Toast.makeText(PhoneVerify.this, "Code is "+code, Toast.LENGTH_SHORT).show();
                    //verifyOtp(code);
                    signInWithPhoneAuthCredential(credential);
                }

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(PhoneVerify.this, "Login Failed "+e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Gagan","ERROR IS "+e.getMessage());
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(PhoneVerify.this, "In Valid Request", Toast.LENGTH_SHORT).show();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(PhoneVerify.this, "Too Many Request Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Toast.makeText(PhoneVerify.this, "Code Has Been Sent", Toast.LENGTH_SHORT).show();
                mVerificationId = verificationId;
                mResendToken = token;

            }

        };


        final Button verify=dialog.findViewById(R.id.button_verify_phone);
        final EditText otp=dialog.findViewById(R.id.field_verification_code);
        final TextView mob=dialog.findViewById(R.id.mob);
        final Button resend=dialog.findViewById(R.id.button_resend_otp);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        try {
            mob.setText(user.getPhoneNumber());
        }catch (Exception e){
            Toast.makeText(this,"Phone Number Not Found",Toast.LENGTH_SHORT).show();
        }
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PhoneVerify.this, "RESENDING....", Toast.LENGTH_SHORT).show();
                resendVerificationCode(ephonenum.getText().toString(),mResendToken);

            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.length()!=6)
                {
                    Toast.makeText(PhoneVerify.this, "Please,Enter Correct OTP", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    dialog.dismiss();
                    verifyOtp(otp.getText().toString());

                }

            }
        });


    }
    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mcallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void verifyOtp(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            // dialog.dismiss();
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            final String userid = firebaseUser.getUid();
                            databaseReference= FirebaseDatabase.getInstance().getReference("User");
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(userid))
                                    {
                                        startActivity(new Intent(PhoneVerify.this,UserHome.class));
                                        finish();
                                    }
                                    else {
                                        startActivity(new Intent(PhoneVerify.this,hello.class));
                                        finish();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }

                        else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(PhoneVerify.this, "Invalid Credential", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(PhoneVerify.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });


    }

    protected void onStart () {
        super.onStart();
        firebaseAuth.addAuthStateListener(mstate);
    }

}

