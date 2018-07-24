package com.example.gagan.bloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

//import org.parceler.Parcels;


public class ProfileInfo extends AppCompatActivity implements Serializable{

    TextView mName;
    TextView mBloodGroup;
    TextView mEmail;
    TextView mGender;
    TextView mDonor;
    TextView mAddress;
    TextView mState;
    TextView mCity;
    Button mEditProfile;
    Button mSignOut;
    DatabaseReference databaseReference;
    String userId;
    Userinfo userInfo;
    Toolbar mActionBarToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        initUI();
        setButtonOnClickListener();

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("AVNI","userid in info " + userId);
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("AVNI","snapshot" + dataSnapshot);
                // Userinfo userInfo = dataSnapshot.child(userId).getValue();
                userInfo = dataSnapshot.getValue(Userinfo.class);
                Log.d("AVNI"," userInfo " + userInfo);

                mName.setText(userInfo.getName());
                mBloodGroup.setText("Blood Group:  " + userInfo.getBloodgroup());
                mEmail.setText("Email:  " + userInfo.getEmail());
                mAddress.setText("Address:  " + userInfo.getAddress());
                mState.setText("State:  " + userInfo.getState());
                mCity.setText("City:  " + userInfo.getCity());
                mGender.setText("Gender:  " + userInfo.getGender());
                if(userInfo.getDonor().equals("Donor"))   mDonor.setText("Donor:  Yes");
                else     mDonor.setText("Donor:  No");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }

    private void initUI() {
        mName = (TextView)findViewById(R.id.xname);
        mBloodGroup = (TextView)findViewById(R.id.xblood_group);
        mEmail = (TextView)findViewById(R.id.xemail);
        mDonor= (TextView) findViewById(R.id.xDonor);
        mGender = (TextView)findViewById(R.id.xgender);
        mAddress = (TextView)findViewById(R.id.xaddr);
        mState = (TextView)findViewById(R.id.xstate);
        mCity = (TextView)findViewById(R.id.xcity);
        mEditProfile = (Button)findViewById(R.id.xedit);
        mSignOut= (Button)findViewById(R.id.xlogout);
    }




    private void setButtonOnClickListener() {
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle = new Bundle();
                // bundle.putBoolean("edit",true);
                // bundle.putParcelable("abc",Parcels.wrap(userInfo));
                //Log.d("AVNI","bundle in profileinfo  " + bundle);
                Intent intent = new Intent(ProfileInfo.this, hello.class);
                intent.putExtra("edit",true);
                intent.putExtra("id",userInfo.userid);
                //intent.putExtra("abc",userInfo);
                // intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
    }
}