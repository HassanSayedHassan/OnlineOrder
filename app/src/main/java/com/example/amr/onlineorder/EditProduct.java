package com.example.amr.onlineorder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class EditProduct extends AppCompatActivity {

    EditText namee, price;
    Button bTnReg;
    ImageView Image_product;
    String id_pro, name_pro, price_pro, image_pro;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    String imageurl = "";
    private StorageReference storageReference;
    private static final int PICK_IMAGE = 100;
    //uri to store file
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        mFirebaseDatabase = mFirebaseInstance.getReference("productsC");

        Bundle extras = getIntent().getExtras();
        id_pro = extras.getString("iD_pro");
        name_pro = extras.getString("na_pro");
        price_pro = extras.getString("pri_pro");
        image_pro = extras.getString("img_pro");

        namee = (EditText) findViewById(R.id.input_product_name_edit);
        price = (EditText) findViewById(R.id.input_product_price_edit);
        bTnReg = (Button) findViewById(R.id.btn_edit_product);
        Image_product = (ImageView) findViewById(R.id.image_product_edit);

        namee.setText(name_pro);
        price.setText(price_pro);
        Glide.with(EditProduct.this).load(image_pro).into(Image_product);

        Image_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        bTnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (namee.getText().toString().isEmpty()) {
                    namee.setError("Please Enter Name");
                }
                if (price.getText().toString().isEmpty()) {
                    price.setError("Please Enter Price");
                } else {
                    updatePro(namee.getText().toString(), price.getText().toString(), id_pro);
                    Toast.makeText(EditProduct.this, "Done", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }

    /*
        public String getFileExtension(Uri uri) {
            ContentResolver cR = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(cR.getType(uri));
        }

        private void uploadFile() {
            //checking if file is available
            if (filePath != null) {
                //displaying progress dialog while image is uploading
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                //getting the storage reference
                StorageReference sRef = storageReference.child("productsC/" + System.currentTimeMillis() + "." + getFileExtension(filePath));

                //adding the file to reference
                sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //dismissing the progress dialog
                        progressDialog.dismiss();

                        //displaying success toast
                        Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                        imageurl = taskSnapshot.getDownloadUrl().toString();

                        //  Toast.makeText(AddProduct.this, catch_id+imageurl, Toast.LENGTH_SHORT).show();

                        updatePro(namee.getText().toString().trim(), price.getText().toString().trim(), id_pro);
                        Toast.makeText(EditProduct.this, "Done", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //displaying the upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
            } else {
                Toast.makeText(EditProduct.this, "File Error", Toast.LENGTH_SHORT).show();
            }
        }
    */
    private void updatePro(String name, String price, String id) {

        mFirebaseDatabase.child(id).child("name").setValue(name);
        mFirebaseDatabase.child(id).child("price").setValue(price);
//        mFirebaseDatabase.child(id).child("url").setValue(url);

    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Image_product.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}