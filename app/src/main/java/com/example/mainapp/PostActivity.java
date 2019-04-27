package com.example.mainapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
/*
* @Eslam
*
* this class responsible for
*
* uploading photos and posts to data base
*
* */
public class PostActivity extends AppCompatActivity {

    private EditText post,content;

    FirebaseDatabase mDatabase ;
    DatabaseReference myRef ;
    private StorageReference mSref;
    private ProgressDialog sProgress;
    private Button btn;
    ImageView image;
    Uri imgUri;
    FirebaseStorage store ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        image = findViewById(R.id.imagePost);
        btn = findViewById(R.id.botosn);
        post=findViewById(R.id.postTitle);
        content=findViewById(R.id.postContent);
        mDatabase =  FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Posts");
        mSref =FirebaseStorage.getInstance().getReference();
        sProgress = new ProgressDialog(this);
        store = FirebaseStorage.getInstance();


    }
    public void addImg(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1);

    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data ){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1&&resultCode==RESULT_OK){
            imgUri = data.getData();
            image.setImageURI(imgUri);
        }

    }
    public String getImageExt(Uri uri){

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void addNewPost(View view) {


            Log.i("no tag", "addNewPost: real");
        final StorageReference riversRef = mSref.child("Photos/" + System.currentTimeMillis() + "." + getImageExt(imgUri));

            sProgress.setMessage("Uploaing");
            sProgress.show();
            riversRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Map<String, String> map2 = new HashMap<>();
                                    String spost = post.getText().toString();
                                    String scontent = content.getText().toString();
                                    map2.put("content", scontent);
                                    map2.put("post", spost);
                                    map2.put("photo", uri.toString());

                                    myRef.push().setValue(map2);
                                    Toast.makeText(getApplicationContext(), "uploaded", Toast.LENGTH_SHORT).show();
                                    sProgress.dismiss();
                                    Intent myIntent = new Intent(PostActivity.this, PostActivity.class);
                                    PostActivity.this.startActivity(myIntent);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });




    }


    public void back(View view) { Intent myIntent = new Intent(PostActivity.this, posts.class);
        PostActivity.this.startActivity(myIntent);
    }
}




