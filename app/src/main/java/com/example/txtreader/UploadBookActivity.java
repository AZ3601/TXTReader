package com.example.txtreader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.core.app.ActivityCompat;
import java.io.File;
import androidx.core.content.ContextCompat;

public class UploadBookActivity extends AppCompatActivity {

    private EditText editTextBookTitle;
    private EditText editTextAuthor;
    private Button buttonUploadCover;
    private Button buttonUploadContent;
    private Button buttonSubmit;
    private static final int REQUEST_CODE = 1;

    private Uri coverImageUri;
    private Uri contentFileUri;

    private ActivityResultLauncher<Intent> coverImagePickerLauncher;
    private ActivityResultLauncher<Intent> contentFilePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_book);

        editTextBookTitle = findViewById(R.id.editTextBookTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        buttonUploadCover = findViewById(R.id.buttonUploadCover);
        buttonUploadContent = findViewById(R.id.buttonUploadContent);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Initialize ActivityResultLaunchers
        coverImagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            coverImageUri = result.getData().getData();
                            Toast.makeText(UploadBookActivity.this, "Cover image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        contentFilePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            contentFileUri = result.getData().getData();
                            Toast.makeText(UploadBookActivity.this, "Content file selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        buttonUploadCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker(coverImagePickerLauncher, "image/*");
            }
        });

        buttonUploadContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker(contentFilePickerLauncher, "text/plain");
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 请求权限
                if (ContextCompat.checkSelfPermission(UploadBookActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(UploadBookActivity.this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UploadBookActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO}, REQUEST_CODE);
                } else {
                    // 权限已授予，执行相关操作
                    Toast.makeText(UploadBookActivity.this, "权限已授予，执行相关操作", Toast.LENGTH_SHORT).show();
                    submitBook();

                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限已授予，执行相关操作
                submitBook();
            } else {
                // 权限被拒绝
                Toast.makeText(UploadBookActivity.this, "权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void openFilePicker(ActivityResultLauncher<Intent> launcher, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        launcher.launch(intent);
    }

    private void submitBook() {
        String bookTitle = editTextBookTitle.getText().toString();
        String author = editTextAuthor.getText().toString();

        if (bookTitle.isEmpty() || author.isEmpty() || coverImageUri == null || contentFileUri == null) {
            Toast.makeText(this, "Please fill all fields and select files", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create RequestBody instances of book title and author
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), bookTitle);
        RequestBody authorBody = RequestBody.create(MediaType.parse("text/plain"), author);

        // Create MultipartBody.Part instances for cover image and content file
        File coverImageFile = new File(coverImageUri.getPath());
        RequestBody coverImageBody = RequestBody.create(MediaType.parse("image/*"), coverImageFile);
        MultipartBody.Part coverImagePart = MultipartBody.Part.createFormData("coverImage", coverImageFile.getName(), coverImageBody);

        File contentFile = new File(contentFileUri.getPath());
        RequestBody contentFileBody = RequestBody.create(MediaType.parse("text/plain"), contentFile);
        MultipartBody.Part contentFilePart = MultipartBody.Part.createFormData("contentFile", contentFile.getName(), contentFileBody);

        // Create Retrofit instance and call the API
        BookService apiService = ApiClient.getClient().create(BookService.class);
        Call<Void> call = apiService.uploadBook(title, authorBody, coverImagePart, contentFilePart);
        Toast.makeText(UploadBookActivity.this, "3", Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UploadBookActivity.this, "Book uploaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadBookActivity.this, "Failed to upload book", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UploadBookActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UploadBookActivity", "Error: " + t.getMessage());
            }
        });
    }
}