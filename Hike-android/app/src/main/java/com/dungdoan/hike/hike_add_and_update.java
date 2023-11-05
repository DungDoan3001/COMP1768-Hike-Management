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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class hike_add_and_update extends AppCompatActivity {

    private Calendar selectedDatetime;
    private CircleImageView hikeImage;
    private TextInputEditText hikeName,location, hikeDate, hikeLength, hikeDescription;
    private CheckBox confirmCheckbox;
    private RadioGroup levelRadioGr, parkingRadioGr;
    private Button addHikeBtn;
    private Uri imageUri;
    private ActionBar actionBar;

    // Internal use
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private String[] cameraPermission;
    private String[] storagePermission;
    private boolean isEditMode = false;
    String id, name, locationDb, date, description, level, parkingAvailable, length, createdAt, updatedAt;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_add_and_update);

        selectedDatetime = Calendar.getInstance();

        actionBar = getSupportActionBar();
        actionBar.setTitle("Add hike");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        hikeImage = findViewById(R.id.hike_input_image);
        hikeName = findViewById(R.id.hike_input_name);
        location = findViewById(R.id.hike_input_location);
        hikeDate = findViewById(R.id.hike_input_date);
        hikeLength = findViewById(R.id.hike_input_length);
        hikeDescription = findViewById(R.id.hike_input_description);
        parkingRadioGr = findViewById(R.id.hike_parking_radio_group);
        levelRadioGr = findViewById(R.id.hike_level_radio_group);
        addHikeBtn = findViewById(R.id.add_input_hike_btn);
        confirmCheckbox = findViewById(R.id.hike_confirm_check_box);

        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("IS_EDIT_MODE", false);

        hikeDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    showDateTimePicker();
                    return true;
                }
                return false;
            }
        });

        hikeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if(isEditMode) {
            actionBar.setTitle("Update hike");

            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            imageUri = Uri.parse(intent.getStringExtra("IMAGE"));
            locationDb = intent.getStringExtra("LOCATION");
            date = intent.getStringExtra("DATE");
            length = intent.getStringExtra("LENGTH");
            parkingAvailable = intent.getStringExtra("PARKING");
            level = intent.getStringExtra("LEVEL");
            description = intent.getStringExtra("DESCRIPTION");
            createdAt = intent.getStringExtra("CREATED_AT");
            updatedAt = intent.getStringExtra("UPDATED_AT");

            hikeName.setText(name);
            location.setText(locationDb);
            hikeDate.setText(date);
            hikeLength.setText(length);
            hikeDescription.setText(description);

            int levelRadioGrCount = levelRadioGr.getChildCount();
            for (int i = 0; i< levelRadioGrCount; i++) {
                RadioButton levelBtn = (RadioButton) levelRadioGr.getChildAt(i);
                if(levelBtn.getText().toString().equals(level)) {
                    levelBtn.setChecked(true);
                    break;
                }
            }

            int parkingRadioGrCount = parkingRadioGr.getChildCount();
            for (int i = 0; i< parkingRadioGrCount; i++) {
                RadioButton parkingBtn = (RadioButton) parkingRadioGr.getChildAt(i);
                if(parkingBtn.getText().toString().equals(parkingAvailable)) {
                    parkingBtn.setChecked(true);
                    break;
                }
            }

            if(imageUri.toString().equals("null")){
                //no image, set to default image
                hikeImage.setImageResource(R.drawable.image_vector);
            } else {
                hikeImage.setImageURI(imageUri);
            }
        }

        dbHelper = new DbHelper(this);

        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_MEDIA_IMAGES};
        storagePermission = new String[]{android.Manifest.permission.READ_MEDIA_IMAGES};

        hikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePickDialog();
            }
        });

        addHikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
                Intent mainIntent = new Intent(hike_add_and_update.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    public void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (DatePicker datePicker, int year, int month, int day) -> {
            selectedDatetime.set(Calendar.YEAR, year);
            selectedDatetime.set(Calendar.MONTH, month);
            selectedDatetime.set(Calendar.DAY_OF_MONTH, day);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (TimePicker timePicker, int hourOfDay, int minute) -> {
                selectedDatetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedDatetime.set(Calendar.MINUTE, minute);

                SimpleDateFormat format = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
                String selectDateTimeString = format.format(selectedDatetime.getTime());
                hikeDate.setText(selectDateTimeString);

            }, selectedDatetime.get(Calendar.HOUR_OF_DAY), selectedDatetime.get(Calendar.MINUTE), false);

            timePickerDialog.show();

        }, selectedDatetime.get(Calendar.YEAR), selectedDatetime.get(Calendar.MONTH), selectedDatetime.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void inputData() {
        name = hikeName.getText().toString().trim();
        locationDb = location.getText().toString().trim();
        length = hikeLength.getText().toString().trim();
        description = hikeDescription.getText().toString().trim();
        date = hikeDate.getText().toString().trim();

        int selectedLevelId = levelRadioGr.getCheckedRadioButtonId();
        if(selectedLevelId != -1) {
            RadioButton radioButton = findViewById(selectedLevelId);
            level = radioButton.getTag().toString();
        }

        int selectedParkingId = parkingRadioGr.getCheckedRadioButtonId();
        if(selectedParkingId != -1) {
            RadioButton radioButton = findViewById(selectedParkingId);
            parkingAvailable = radioButton.getTag().toString();
        }

        String timeStamp = String.valueOf(System.currentTimeMillis());

        boolean validateInput = validateInput();
        if(validateInput) {
            if(isEditMode) {
                dbHelper.updateHike(id,
                                    name,
                                    locationDb,
                                    date,
                                    length,
                                    parkingAvailable,
                                    level,
                                    description,
                                    String.valueOf(imageUri),
                                    createdAt,
                                    timeStamp);

                Toast.makeText(this, "Update Hike Successfully", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.insertHike(name,
                                    locationDb,
                                    date,
                                    length,
                                    parkingAvailable,
                                    level,
                                    description,
                                    String.valueOf(imageUri),
                                    timeStamp,
                                    timeStamp);
                Toast.makeText(this, "Successfully Add Hike: " + name, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput() {
        boolean validateResult = true;

        if(name.trim().isEmpty()) {
            hikeName.setError("Name of hike is required");
            validateResult = false;
        }

        if(locationDb.trim().isEmpty()) {
            location.setError("Location is required");
            validateResult = false;
        }

        if(date.trim().isEmpty()) {
            hikeDate.setError("Date is requied");
            validateResult = false;
        }

        if(length.trim().isEmpty()) {
            hikeLength.setError("Length is required");
            validateResult = false;
        } else if (!length.matches("^(-)?\\d+(\\.\\d+)?$")) {
            hikeLength.setError("Need to input valid length");
            validateResult = false;
        }

        if(levelRadioGr.getCheckedRadioButtonId() == - 1)
        {
            Toast.makeText(this, "Please select a level of the hike!", Toast.LENGTH_SHORT).show();
            validateResult = false;
        }

        if(parkingRadioGr.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(this, "Please select parking available!", Toast.LENGTH_SHORT).show();
            validateResult = false;
        }

        if(!confirmCheckbox.isChecked())
        {
            Toast.makeText(this, "Please confirm to our terms and conditions!", Toast.LENGTH_SHORT).show();
            validateResult = false;
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
           hikeImage.setImageURI(imageCropped);
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