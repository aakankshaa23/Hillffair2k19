package com.appteam.hillfair2k19;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.appteam.dialog.CautionDialog;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends AppCompatActivity {
    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 10;
    Bitmap selectedImage = null;
    int openDialog = 1;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    LinearLayout progress, loadPic;
    String pass = "";
    FirebaseVisionFaceDetectorOptions highAccuracyOpts =
            new FirebaseVisionFaceDetectorOptions.Builder()
                    .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                    .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                    .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                    .build();
    private int GALLERY = 1, CAMERA = 2;
    private byte[] byteArray;
    private EditText studentName, rollNumber, branch, contactNumber, referral;
    private String Name, base64a, base64b, RollNumber, Branch, referal, ContactNumber, imgUrl, gender;
    private CircleImageView profilePicture;
    private TextView buttonLoadImage, save;
    private Bitmap bmp, img;
    private int PICK_PHOTO_CODE = 1046;
    private RadioButton male, female , faceSmashYes , faceSmashNo;
    String face_smash_status;

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        AndroidNetworking.initialize(getApplicationContext());
        progress = findViewById(R.id.loadwall);
        loadPic = findViewById(R.id.loadPic);
        profilePicture = findViewById(R.id.profilePicture);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });
        buttonLoadImage = findViewById(R.id.galleryView);
        faceSmashYes = findViewById(R.id.yes);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                showPictureDialog();
            }
        });
        initUI();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // Call your camera here.
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(galleryIntent, GALLERY);
        }
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        openDialog = 0;
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {

            if (data != null) {
                Uri photoUri = data.getData();
                try {
                    loadPic.setVisibility(View.VISIBLE);
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    isHuman(selectedImage);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Profile.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            loadPic.setVisibility(View.VISIBLE);
            selectedImage = (Bitmap) data.getExtras().get("data");
            isHuman(selectedImage);

        }

    }


    public void isHuman(final Bitmap thumbnail) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(thumbnail);
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(highAccuracyOpts);

        Task<List<FirebaseVisionFace>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionFace>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionFace> faces) {
                                        // Task completed successfully
                                        // ...
                                        int counter = 0;
                                        for (FirebaseVisionFace face : faces) {

                                            List<FirebaseVisionPoint> faceContours = face.getContour(FirebaseVisionFaceContour.ALL_POINTS).getPoints();
                                            if (faceContours != null) {

                                                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, bs);
                                                byteArray = bs.toByteArray();
                                                bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                                img = getResizedBitmap(bmp, 150);
                                                pass = encodeTobase64(img);

                                                profilePicture.setImageBitmap(img);
                                                Toast.makeText(Profile.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                                                counter = 1;
                                                buttonLoadImage.setVisibility(View.GONE);
                                                loadPic.setVisibility(View.GONE);
                                            }

                                        }
                                        if (counter != 1) {
                                            loadPic.setVisibility(View.GONE);
                                            Toast.makeText(Profile.this, "Not a Human Image!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                        Toast.makeText(Profile.this, "Unexpected Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void initUI() {
//        SharedPreferences prefs = getSharedPreferences("number", Context.MODE_PRIVATE);
//        String check = prefs.getString("name", "gsbs");
//        SharedPreferences.Editor editor = prefs.edit();
//        String open = prefs.getString("Profile Dialog", "null");
        CautionDialog cautionDialog = new CautionDialog(Profile.this);

//        if (open.equals("1")) {
//            cautionDialog.show();
//            editor.putString("Profile Dialog", "0");
//            editor.apply();
//        }
//        if (!check.equals("gsbs")) {
////            cautionDialog.dismiss();
////            startActivity(new Intent(Profile.this, MainActivity.class));
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            finish();
//        }

        studentName = findViewById(R.id.studentName);
        rollNumber = findViewById(R.id.rollNumber);
        referral = findViewById(R.id.referal);
        branch = findViewById(R.id.branch);
        profilePicture = findViewById(R.id.profilePicture);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        contactNumber = findViewById(R.id.contactNumber);
//        final SharedPreferences sharedPreferences = getSharedPreferences("number", Context.MODE_PRIVATE);
////        contactNumber.setText(sharedPreferences.getString("numberMobile", "None").replace("+91 ", ""));
//        contactNumber.setText(sharedPreferences.getString("Phone", "None"));
//        contactNumber.setEnabled(false);
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                setdata();
            }

        });
    }

    public void setdata() {
        Name = String.valueOf(studentName.getText());
        RollNumber = String.valueOf(rollNumber.getText());
        Branch = String.valueOf(branch.getText());
        referal = String.valueOf(referral.getText());
        ContactNumber = contactNumber.getText().toString();

        if (male.isSelected())
            gender = "MALE";
        else if (female.isSelected())
            gender = "FEMALE";

        if (Name.length() == 0) {
            Toast.makeText(Profile.this, "Seems You Didn`t enter all the details", Toast.LENGTH_SHORT).show();
        } else {
            final SharedPreferences sharedPreferences = getSharedPreferences("number", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPreferences.edit();

            if (pass == "") {
                Toast.makeText(Profile.this, "Please select profile picture", Toast.LENGTH_SHORT).show();
            } else if (Name == "" || RollNumber == "" || Branch == "" || ContactNumber == "" || pass == "" || gender == "") {
                Toast.makeText(Profile.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (RollNumber.equals("0")) {
                Toast.makeText(Profile.this, "Please Enter Valid Roll No", Toast.LENGTH_SHORT).show();
            } else {
                editor.putString("name", Name);
                editor.putString("roll number", RollNumber);
                editor.putString("Branch", Branch);
                editor.putString("Phone", ContactNumber);
                editor.putString("Image", pass);
                editor.putString("Gender", gender);
                editor.putString("Referral", referal);
                editor.commit();
                progress.setVisibility(View.VISIBLE);
                String requestId = MediaManager.get().upload(byteArray)
                        .unsigned("xf7gsy1r")
                        .callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {
                            }

                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {
                            }

                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                System.out.println(resultData.get("url"));
                                imgUrl = String.valueOf(resultData.get("url"));
                                editor.putString("ImageURL", String.valueOf(resultData.get("url")));
                                editor.commit();
                                post(ContactNumber);
//                                finish();
                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {
                                Toast.makeText(Profile.this, "Error", Toast.LENGTH_SHORT).show();
                                Log.v("ErrorCloud", String.valueOf(error));
                            }

                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {
                            }
                        })
                        .dispatch(Profile.this);

            }
        }
    }

    public void post(final String ContactNumber) {
        try {
            if (faceSmashYes.isChecked())
                face_smash_status = "1";
            else
                face_smash_status = "0";
            if (male.isChecked())
                gender = "MALE";
            else
                gender = "FEMALE";
            // byte[] data = referal.getBytes("UTF-8");
            base64a = referal;
            if (base64a.equals(""))
                base64a = "0";
            byte[] data1 = imgUrl.getBytes("UTF-8");
            base64b = Base64.encodeToString(data1, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.baseUrl) + "/User",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences sharedPreferences = getSharedPreferences("number", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("ProfileCreated", "true");
                        editor.commit();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("failure"))
                            {
                                updateUser();
                            }
                            else
                            {
                                progress.setVisibility(View.GONE);
                            startActivity(new Intent(Profile.this,MainActivity.class));
                            finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sharedPreferences = getSharedPreferences("number", MODE_PRIVATE);
                String id = sharedPreferences.getString("fireBaseId", "12345");
                Map<String, String> params = new HashMap<String, String>();
                params.put("firebase_id", id);
                params.put("roll_number", RollNumber);
                params.put("branch", Branch);
                params.put("mobile", ContactNumber);
                params.put("referral_friend", referal);
                params.put("name", Name);
                params.put("gender", gender);
                params.put("face_smash_status", face_smash_status);
                params.put("image_url", imgUrl);
                return params;
            }

        };
        queue.add(stringRequest);

    }

    public void updateUser()
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.baseUrl) + "/User/Update",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.setVisibility(View.GONE);
                        startActivity(new Intent(Profile.this,MainActivity.class));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sharedPreferences = getSharedPreferences("number", MODE_PRIVATE);
                String id = sharedPreferences.getString("fireBaseId", "12345");

                Map<String, String> params = new HashMap<String, String>();
                params.put("firebase_id", id);
                params.put("roll_number", RollNumber);
                params.put("branch", Branch);
                params.put("name", Name);
                params.put("image_url", imgUrl);
                params.put("face_smash_status",face_smash_status);
                return params;
            }

        };
        queue.add(stringRequest);

    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
        Toast.makeText(this, "Please fill details to register", Toast.LENGTH_SHORT).show();
    }
}
