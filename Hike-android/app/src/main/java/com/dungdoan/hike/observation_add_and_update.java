package com.dungdoan.hike;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class observation_add_and_update extends AppCompatActivity {

    private CircleImageView observationImage;
    private Calendar selectedDateTime;
    private TextInputEditText nameInput, commentInput, timeInput;
    private CheckBox confirmCheckbox;
    private boolean isEditMode;
    private Button addObservation;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private String[] cameraPermission;
    private String[] storagePermission;
    private Uri imageUri;
    private ActionBar actionBar;
    String id, hikeId, name, comment, time, createdAt, updatedAt;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_add_and_update);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Add observation");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        selectedDateTime = Calendar.getInstance();

        observationImage = findViewById(R.id.observation_input_image);
        nameInput = findViewById(R.id.observation_input_name);
        commentInput = findViewById(R.id.observation_input_comment);
        timeInput = findViewById(R.id.observation_input_time);
        addObservation = findViewById(R.id.add_input_observation_btn);
        confirmCheckbox = findViewById(R.id.observation_confirm_check_box);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
        String defaultTime = format.format(new Date());
        timeInput.setText(defaultTime);

        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("IS_EDIT_MODE", false);
        hikeId = intent.getStringExtra("HIKE_ID");

        timeInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    showDateTimePicker();
                    return true;
                }
                return false;
            }
        });

        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if(isEditMode) {
            actionBar.setTitle("Update Observation");
            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            time = intent.getStringExtra("TIME");
            comment = intent.getStringExtra("COMMENT");
            imageUri = Uri.parse(intent.getStringExtra("IMAGE"));
            createdAt = intent.getStringExtra("CREATED_AT");
            updatedAt = intent.getStringExtra("UPDATED_AT");

            nameInput.setText(name);
            commentInput.setText(comment);
            timeInput.setText(time);

            if(imageUri.toString().equals("null")) {
                observationImage.setImageResource(R.drawable.image_vector);
            } else {
                observationImage.setImageURI(imageUri);
            }
        }

        dbHelper = new DbHelper(this);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES};
        storagePermission = new String[]{Manifest.permission.READ_MEDIA_IMAGES};

        observationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickDialog();
            }
        });

        addObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
                Intent intent = new Intent(observation_add_and_update.this, hike_details.class);
                intent.putExtra("HIKE_ID", hikeId);
                startActivity(intent);
            }
        });
    }

    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (DatePicker datePicker, int year, int month, int dayOfMonth) -> {
            selectedDateTime.set(Calendar.YEAR, year);
            selectedDateTime.set(Calendar.MONTH, month);
            selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (TimePicker timePicker, int hourOfDay, int minute) -> {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedDateTime.set(Calendar.MINUTE, minute);

                SimpleDateFormat format = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
                String selectedDateTimeString = format.format(selectedDateTime.getTime());
                timeInput.setText(selectedDateTimeString);
            }, selectedDateTime.get(Calendar.HOUR_OF_DAY), selectedDateTime.get(Calendar.MINUTE), false);

            timePickerDialog.show();
        }, selectedDateTime.get(Calendar.YEAR), selectedDateTime.get(Calendar.MONTH), selectedDateTime.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void inputData() {
        name = nameInput.getText().toString().trim();
        time = timeInput.getText().toString().trim();
        comment = commentInput.getText().toString().trim();

        String timeStamp = String.valueOf(System.currentTimeMillis());

        boolean validateInput = validateInput();
        if(validateInput) {
            if(isEditMode) {
                dbHelper.updateObservation(id,
                                           name,
                                           time,
                                           comment,
                                           String.valueOf(imageUri),
                                           createdAt,
                                           timeStamp,
                                           hikeId);
                Toast.makeText(this, "Update Observation Successfully", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.insertObservation(name,
                                           time,
                                           comment,
                                           String.valueOf(imageUri),
                                           timeStamp,
                                           timeStamp,
                                           hikeId);
                Toast.makeText(this,"Successfully Add an Observation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput() {
        boolean validateResult = true;

        if(name.trim().isEmpty()) {
            nameInput.setError("Observation title is required");
            validateResult = false;
        }

        if(!confirmCheckbox.isChecked()) {
            Toast.makeText(this, "Please confirm to our terms and conditions!", Toast.LENGTH_SHORT).show();
        }

        return validateResult;
    }

    private void imagePickDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick image from");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0) {
                    boolean cameraPermission = checkCameraPermission();
                    if(!cameraPermission) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (i == 1) {
                    boolean storagePermission = checkStoragePermission();
                    if(!storagePermission) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });

        builder.create().show();
    }

    private boolean checkCameraPermission() {
        boolean cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == (PackageManager.PERMISSION_GRANTED);

        return cameraPermission && storagePermission;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        pickFromCameraLauncher.launch(cameraIntent);
    }

    ActivityResultLauncher<Intent> pickFromCameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult activityResult) {
            int resultCode = activityResult.getResultCode();

            if(resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.imageSourceIncludeCamera = true;
                cropImageOptions.imageSourceIncludeGallery = true;
                CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(imageUri, cropImageOptions);
                cropImage.launch(cropImageContractOptions);

            }
        }
    });

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if(grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Camera & Storage permissions are required", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                if(grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Storage permission is required", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
        if(result.isSuccessful()) {
            Uri imageCropped = result.getUriContent();
            imageUri = imageCropped;
            observationImage.setImageURI(imageCropped);
        } else {
            Exception err = result.getError();
            Toast.makeText(this, "Error: " + err , Toast.LENGTH_SHORT).show();
        }
    });

    private boolean checkStoragePermission() {
        boolean storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return storagePermission;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        pickFromGalleryLauncher.launch(galleryIntent);
    }

    ActivityResultLauncher<Intent> pickFromGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult activityResult) {
            int resultCode = activityResult.getResultCode();

            if(resultCode == RESULT_OK) {
                Intent data = activityResult.getData();

                CropImageOptions cropImageOptions = new CropImageOptions();
                cropImageOptions.imageSourceIncludeGallery = true;
                cropImageOptions.imageSourceIncludeCamera = true;

                CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(data.getData(), cropImageOptions);
                cropImage.launch(cropImageContractOptions);
            }
        }
    });
}