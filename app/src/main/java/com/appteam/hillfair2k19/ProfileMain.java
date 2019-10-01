package com.appteam.hillfair2k19;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileMain extends AppCompatActivity {

    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 10;
    TextView textView, save, name1, rollNumber1, referral, branch1, mobile1, reffaralDone;
    CircleImageView profilemain, buttonLoadImage;
    Bitmap selectedImage, img, bmp;
    String base64a, pass;
    SharedPreferences prefs;
    LinearLayout loadPic, progress;
    RelativeLayout sumbit;
    FirebaseVisionFaceDetectorOptions highAccuracyOpts =
            new FirebaseVisionFaceDetectorOptions.Builder()
                    .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
                    .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                    .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                    .build();
    private ClipboardManager myClipboard;
    private ClipData myClip;
    private byte[] byteArray;
    private String imageUrl;
    private int PICK_PHOTO_CODE = 1046;
    private int GALLERY = 1, CAMERA = 2;
    private String Name, ContactNumber, Branch, RollNumber, referal, img_Url, base64b;
    RadioButton faceSmashYes , faceSmashNo ;
    String face_smash_status;
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);
        progress = findViewById(R.id.loadwall);
        loadPic = findViewById(R.id.loadPic);
        faceSmashNo = findViewById(R.id.no);
        faceSmashYes = findViewById(R.id.yes);
        textView = findViewById(R.id.referral);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileMain.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();

            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = textView.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("COPY", text1);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getApplicationContext(), "Referral Copied", Toast.LENGTH_SHORT).show();
            }
        });
        initUI();
        SharedPreferences sharedPreferences = getSharedPreferences("number", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("fireBaseId", null);
        getProfile(id);

        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfile();
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

    public void changeProfile() {
        sumbit.setVisibility(View.VISIBLE);
        name1.setEnabled(true);
        rollNumber1.setEnabled(true);
        branch1.setEnabled(true);
        mobile1.setEnabled(true);
        SharedPreferences sharedPreferences = getSharedPreferences("number", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", String.valueOf(name1.getText()));
        editor.putString("branch", String.valueOf(branch1.getText()));
        editor.putString("rollNo", String.valueOf(rollNumber1.getText()));
        editor.commit();

        buttonLoadImage = findViewById(R.id.profilePicture);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showPictureDialog();
            }
        });

    }

    private void getProfile(String id) {
        AndroidNetworking.get(getResources().getString(R.string.baseUrl) + "/User/" + id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ProfileMain.this, String.valueOf(response), Toast.LENGTH_SHORT).show();
                        try {
                            name1.setText((String) (response.get("name")));
                            referral.setText((String) (response.get("referral_friend")));
                            rollNumber1.setText((String) (response.get("roll_number")));
                            branch1.setText((String) (response.get("branch")));
                            mobile1.setText((String) (response.get("mobile")));
                            reffaralDone.setText((String) (response.get("referral_friend")));
                            String image_url = (String) response.get("image_url");
                            face_smash_status = (String) response.getString("face_smash_status");
                            if (face_smash_status.equals("1"))
                                faceSmashYes.setChecked(true);
                            else
                                faceSmashNo.setChecked(true);
                            imageUrl = image_url;
                            if (image_url != null) {
                                Picasso.with(ProfileMain.this).load(image_url).resize(80, 80).centerCrop().into(profilemain);
                                BitmapDrawable drawable = (BitmapDrawable) profilemain.getDrawable();
                                if (drawable != null) {
                                    Bitmap bitmap = drawable.getBitmap();
                                    pass = encodeTobase64(bitmap);
                                }
                            }
                            progress.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // Handle error
                    }
                });
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
                    Toast.makeText(ProfileMain.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            loadPic.setVisibility(View.VISIBLE);
            selectedImage = (Bitmap) data.getExtras().get("data");
            isHuman(selectedImage);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("number", MODE_PRIVATE);
        sumbit.setVisibility(View.VISIBLE);
        name1.setEnabled(true);
        mobile1.setEnabled(true);
        name1.setText(sharedPreferences.getString("name", "Name"));
        rollNumber1.setEnabled(true);
        rollNumber1.setText(sharedPreferences.getString("rollNo", "Roll Number"));
        branch1.setEnabled(true);
        branch1.setText(sharedPreferences.getString("branch", "Branch"));
        buttonLoadImage = findViewById(R.id.profilePicture);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showPictureDialog();
            }
        });

    }
String ImageCheck = "1";
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
                                                ImageCheck = "0";
                                                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, bs);
                                                byteArray = bs.toByteArray();
                                                bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                                img = getResizedBitmap(bmp, 150);
                                                pass = encodeTobase64(img);
                                                profilemain.setImageBitmap(img);
                                                loadPic.setVisibility(View.GONE);
                                                Toast.makeText(ProfileMain.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                                                counter = 1;

                                            }

                                        }
                                        if (counter != 1) {
                                            loadPic.setVisibility(View.GONE);
                                            Toast.makeText(ProfileMain.this, "Not a Human Image!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                        Toast.makeText(ProfileMain.this, "Unexpected Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
    }


    public Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void initUI() {
        name1 = findViewById(R.id.name1);
        referral = findViewById(R.id.referral);
        rollNumber1 = findViewById(R.id.rollNumber1);
        branch1 = findViewById(R.id.branch1);
        mobile1 = findViewById(R.id.mobile1);
        profilemain = findViewById(R.id.profilePicture);
        reffaralDone = findViewById(R.id.referralDone);
        sumbit = findViewById(R.id.submit);
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
            }
        });
    }

    public void setData() {
        Name = String.valueOf(name1.getText());
        RollNumber = String.valueOf(rollNumber1.getText());
        Branch = String.valueOf(branch1.getText());
        referal = String.valueOf(referral.getText());
        ContactNumber = mobile1.getText().toString();
        BitmapDrawable drawable = (BitmapDrawable) profilemain.getDrawable();
        if (drawable != null && pass == null) {
            Bitmap bitmap = drawable.getBitmap();
            pass = encodeTobase64(bitmap);
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        }
        if (Name.length() == 0) {
            Toast.makeText(ProfileMain.this, "Seems You Didn`t enter all the details", Toast.LENGTH_SHORT).show();
        } else {
            final SharedPreferences sharedPreferences = getSharedPreferences("number", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            if ( pass == "" || pass == null)
                Toast.makeText(ProfileMain.this, "Please select profile picture", Toast.LENGTH_SHORT).show();
            else if (Name == "" || RollNumber == "" || Branch == "" || ContactNumber == "" || pass == "" ) {
                Toast.makeText(ProfileMain.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (RollNumber.equals("0")) {
                Toast.makeText(ProfileMain.this, "Please Enter Valid Roll No", Toast.LENGTH_SHORT).show();
            } else {
                editor.putString("name", Name);
                editor.putString("roll number", RollNumber);
                editor.putString("Branch", Branch);
                editor.putString("Phone", ContactNumber);
                editor.putString("Image", pass);
                //editor.putString("Gender", gender);
                editor.commit();
                progress.setVisibility(View.VISIBLE);
                if (ImageCheck.equals("0"))
                {
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
                                    img_Url = String.valueOf(resultData.get("url"));
                                    post(ContactNumber);
                                    editor.putString("ImageURL", img_Url);
                                    editor.commit();
//                                startActivity(new Intent(Profile.this, MainActivity.class));
//                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                                editor.putString("ImageURL", String.valueOf(resultData.get("url")));
//                                editor.commit();
//                                finish();
                                }

                                @Override
                                public void onError(String requestId, ErrorInfo error) {
                                    Toast.makeText(ProfileMain.this, "Error", Toast.LENGTH_SHORT).show();
                                    Log.v("ErrorCloud", String.valueOf(error));
                                }

                                @Override
                                public void onReschedule(String requestId, ErrorInfo error) {
                                }
                            })
                            .dispatch(ProfileMain.this);
                }
                else
                {
                    if (img_Url == null)
                        img_Url = imageUrl;
                    post(ContactNumber);
                }

            }
        }
    }

    public void post(final String ContactNumber) {
        try {
            // byte[] data = referal.getBytes("UTF-8");
            base64a = referal;
            if (base64a.equals(""))
                base64a = "0";
            byte[] data1 = img_Url.getBytes("UTF-8");
            base64b = Base64.encodeToString(data1, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (faceSmashYes.isChecked())
            face_smash_status = "1";
        else
            face_smash_status = "0";
        SharedPreferences sharedPreferences = getSharedPreferences("number", Context.MODE_PRIVATE);
        final String id = sharedPreferences.getString("fireBaseId", null);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.baseUrl) + "/User/Update",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ProfileMain.this, response, Toast.LENGTH_LONG).show();
                        sumbit.setVisibility(View.GONE);
                        name1.setEnabled(false);
                        rollNumber1.setEnabled(false);
                        branch1.setEnabled(false);
                        mobile1.setEnabled(false);
                        buttonLoadImage = findViewById(R.id.profilePicture);
                        buttonLoadImage.setFocusable(false);//////////////////////////////////////////
                        getProfile(id);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileMain.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("firebase_id", id);
                params.put("roll_number", RollNumber);
                params.put("branch", Branch);
                params.put("name", Name);
                params.put("image_url", img_Url);
                params.put("face_smash_status",face_smash_status);
                return params;
            }

        };
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProfileMain.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}


