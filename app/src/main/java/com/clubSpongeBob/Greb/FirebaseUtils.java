package com.clubSpongeBob.Greb;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;

public class FirebaseUtils {
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static DatabaseReference customerRef = db.getReference("customers");
    private static DatabaseReference driverRef = db.getReference("drivers");
    private static String TAG = "firebase utils";

    public static void registerUser(Context context, String email, String name, String password, String emergency){
        // Assuming all input is validated
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            Customer customer = new Customer(email, name, emergency);
                            customerRef
                                    .child(mAuth.getCurrentUser().getUid())
                                    .setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(context,"User has been registered successfully!", Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(context,"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }else {
                            Toast.makeText(context, "Failed to register", Toast.LENGTH_LONG).show();
                        }
                    }
        });

//        String[] errorCodes = new String[3];
//        email = email.trim();

//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//            errorCodes[0] = "Please provide valid email";
//        }
//
    }

    public static Customer getOneUser(String uid){
        final Customer[] c = new Customer[1];

        customerRef
                .child(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e(TAG, "Error getting user data", task.getException());
                        } else {
                            c[0] = task.getResult().getValue(Customer.class);
                            Log.d(TAG, "Successfully get data from user: " + c[0].getName());
                        }
                    }
                });

        return c[0];
    }

    public static Customer loginUser(Context context, String email, String password){
        final Customer[] c = new Customer[1];

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            c[0] = getOneUser(FirebaseAuth.getInstance().getCurrentUser().getUid());


                        }else {
                            Toast.makeText(context, "Failed to login", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        return c[0];
    }

    public static boolean isLogin(Context context){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return false;
        Toast.makeText(context, "Welcome "+ user.getDisplayName(), Toast.LENGTH_LONG);
        return true;
    }

    public static void signOutUser(Context context){
        mAuth.signOut();
        Toast.makeText(context, "Sign out", Toast.LENGTH_LONG).show();
    }

    public static String createUID(){
        return UUID.randomUUID().toString();
    }

    public static void addDriver(Context context, Driver driver){
        DatabaseReference ref = driverRef.child(createUID());
        ref.setValue(driver).addOnCompleteListener(new OnCompleteListener<Void>(){

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Added new driver", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context, "Failed to add new driver", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public static void addOrder(String currentLoc, String destination, int capacity, String EAT){
        Map<String,Object> values=new HashMap<>();
        values.put("location",currentLoc);
        values.put("destination",destination);
        values.put("capacity",capacity);
        values.put("eat",EAT);
        values.put("status",1);
        customerRef.child(mAuth.getCurrentUser().getUid()).updateChildren(values).addOnCompleteListener(new OnCompleteListener(){
            @Override
            public void onComplete(@NonNull Task task){
                if(task.isSuccessful()){
                    Log.d(TAG,"Successfully add order: "+mAuth.getCurrentUser().getUid());
                }
                else{
                    Log.e(TAG,"Unable to add order: "+mAuth.getCurrentUser().getUid());
                }
            }
        });
    }

    public static void getOneDriver(Context context, String uid){

        Task<DataSnapshot> dataSnapshotTask = driverRef.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                }}
            });
//        DataSnapshot dataSnapshot = dataSnapshotTask.getResult();
//        if (dataSnapshot == null) return null;
//        return new Driver(dataSnapshot.child("name").getValue().toString(), dataSnapshot.child("location").getValue().toString(),
//                Integer.parseInt(dataSnapshot.child("capacity").getValue().toString()), dataSnapshot.child("carPlate").getValue().toString(),
//                dataSnapshot.child("carModel").getValue().toString(), dataSnapshot.child("carColour").getValue().toString(),
//                Integer.parseInt(dataSnapshot.child("rating").getValue().toString()), Integer.parseInt(dataSnapshot.child("numOfRating").getValue().toString()),
//                Integer.parseInt(dataSnapshot.child("status").getValue().toString()), dataSnapshot.child("eat").getValue().toString());
    }

    public static void adminGetDriver(){
        driverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Query query=driverRef.orderByChild("status");
                query.addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Driver> dList = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Driver driver = data.getValue(Driver.class);
                            dList.add(driver);
                            System.out.println(driver.getName()); //for test
                        }
                        //do what you want to do with your list
                        //Put in recyclerview (adapter)
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error){
                        Log.d(TAG,"Unavailable to retrieve data");
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG,"Unavailable to retrieve data");
            }
        });

    }

    public static void customerGetDriver(){
        driverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Query query=driverRef.orderByChild("status").startAt(1);
                query.addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){
                        Queue<Driver>dQueue=new PriorityQueue<>();
                        for(DataSnapshot data: dataSnapshot.getChildren()){
                            Driver driver=data.getValue(Driver.class);
                            dQueue.add(driver);
                            System.out.println(driver.getName());//for test
                        }
                        //Do whatever
                        //Put in recyclerview
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error){
                        Log.d(TAG,"Unavailable to retrieve data");
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Log.d(TAG,"Unavailable to retrieve data");
            }
        });
    }

    public static void updateStatus(boolean customer, String uid, int status){
        DatabaseReference ref;
        if (customer){
            ref = customerRef;
        } else {
            ref = driverRef;
        }
        ref.child(uid).child("status").setValue(status).addOnCompleteListener(new OnCompleteListener(){
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Successfully update status: " + uid);
                }else {
                    Log.e(TAG,"Unable to update status: " + uid);
                }
            }
        });

    }

    public static void updateRating(String uid, int rate, int numOfRating){
        Map<String, Object> values = new HashMap<>();
        values.put("rating", rate);
        values.put("numOfRating", numOfRating);
        driverRef.child(uid).updateChildren(values).addOnCompleteListener(new OnCompleteListener(){
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Successfully update rating: " + uid);
                }else {
                    Log.e(TAG,"Unable to update rating: " + uid);
                }
            }
        });
    }



}
