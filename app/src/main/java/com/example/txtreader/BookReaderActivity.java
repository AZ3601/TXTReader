package com.example.txtreader;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookReaderActivity extends AppCompatActivity {

    private TextView bookTitleTextView;
    private TextView bookContentTextView;
    private Button addToBookshelfButton;
    private int bookId;

    private int currentFontSize = 28; // 初始字号
    private int currentBackgroundColor = Color.WHITE; // 初始背景颜色

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);

        bookTitleTextView = findViewById(R.id.book_title);
        bookContentTextView = findViewById(R.id.book_content);
        addToBookshelfButton = findViewById(R.id.addToBookshelfButton);



        // 获取传递过来的书籍ID
        bookId = getIntent().getIntExtra("book_id", -1);
        if (bookId != -1) {
            fetchBookContent(bookId);
        } else {
            bookTitleTextView.setText("Error: Book ID not found");
        }

        // Set click listener for the button
        addToBookshelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBookshelf();
            }
        });

        // Set click listener for the screen to show/hide the button
        bookContentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAddToBookshelfButton();
            }
        });
    }

    private void toggleAddToBookshelfButton() {
        if (addToBookshelfButton.getVisibility() == View.VISIBLE) {
            addToBookshelfButton.setVisibility(View.GONE);
        } else {
            addToBookshelfButton.setVisibility(View.VISIBLE);
        }
    }

    private void addToBookshelf() {
        // 获取的用户ID
        SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(this);
        int userId = sharedPrefsManager.getUserId();
        // Call the backend API to add the book to the user's bookshelf
        BookService bookService = ApiClient.getClient().create(BookService.class);
        Call<Void> call = bookService.addToBookshelf(userId, bookId);
        showToast("userId=" + userId);
        showToast("bookId=" + bookId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    showToast("Book added to bookshelf");
                } else {
                    // Handle error
                    showToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                showToast("Failed to add book to bookshelf");
            }
        });
    }

    private void showToast(String message) {
        // Implement this method to show a toast message
        Toast.makeText(BookReaderActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void fetchBookContent(int bookId) {
        BookService apiService = ApiClient.getClient().create(BookService.class);
        Call<ResponseBody> call = apiService.getBookContent(bookId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String bookContent = response.body().string();
                        String bookname = getIntent().getStringExtra("book_name");
                        bookTitleTextView.setText(bookname); // 这里应该从API获取书籍标题
                        bookContentTextView.setText(bookContent);
                        bookContentTextView.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        bookTitleTextView.setText("Error: Failed to read book content");
                    }
                } else {
                    bookTitleTextView.setText("Error: Failed to fetch book content");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                bookTitleTextView.setText("Error: Network request failed");
            }
        });
    }

    // 字号调整按钮点击事件
    public void onFontSizeChange(View view) {
        Button button = (Button) view;
        if (button.getId() == R.id.btn_increase_font) {
            currentFontSize += 2; // 增加字号
        } else if (button.getId() == R.id.btn_decrease_font) {
            currentFontSize -= 2; // 减少字号
        }
        bookContentTextView.setTextSize(currentFontSize);
    }

    // 背景颜色调整按钮点击事件
    public void onBackgroundColorChange(View view) {
        if (currentBackgroundColor == Color.WHITE) {
            currentBackgroundColor = Color.BLACK;
            bookContentTextView.setTextColor(Color.WHITE); // 切换为白色字体
        } else {
            currentBackgroundColor = Color.WHITE;
            bookContentTextView.setTextColor(Color.BLACK); // 切换为黑色字体
        }
        bookContentTextView.setBackgroundColor(currentBackgroundColor);
    }
}
//package com.example.txtreader;
//
//
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import android.view.View;
//import android.widget.Toast;
//
//public class BookReaderActivity extends AppCompatActivity {
//
//    private TextView bookTitleTextView;
//    private TextView bookContentTextView;
//    private Button addToBookshelfButton;
//    private int bookId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_book_reader);
//
//        bookTitleTextView = findViewById(R.id.book_title);
//        bookContentTextView = findViewById(R.id.book_content);
//        // Initialize the button
//        addToBookshelfButton = findViewById(R.id.addToBookshelfButton);
//        // 获取传递过来的书籍ID
//        bookId = getIntent().getIntExtra("book_id", -1);
//        if (bookId != -1) {
//            fetchBookContent(bookId);
//        } else {
//            bookTitleTextView.setText("Error: Book ID not found");
//        }
//        // Set click listener for the button
//        addToBookshelfButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addToBookshelf();
//            }
//        });
//        // Set click listener for the screen to show/hide the button
//        bookContentTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggleAddToBookshelfButton();
//            }
//        });
//    }
//    private void toggleAddToBookshelfButton() {
//        if (addToBookshelfButton.getVisibility() == View.VISIBLE) {
//            addToBookshelfButton.setVisibility(View.GONE);
//        } else {
//            addToBookshelfButton.setVisibility(View.VISIBLE);
//        }
//    }
//    private void addToBookshelf() {
//        // 获取的用户ID
//        SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(this);
//        int userId = sharedPrefsManager.getUserId();
//        // Call the backend API to add the book to the user's bookshelf
//        BookService bookService = ApiClient.getClient().create(BookService.class);
//        Call<Void> call = bookService.addToBookshelf(userId, bookId);
//        showToast("userId="+userId);
//        showToast("bookId="+bookId);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    // Handle success
//                    showToast("Book added to bookshelf");
//                } else {
//                    // Handle error
//                    showToast(response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                // Handle failure
//                showToast("Failed to add book to bookshelf");
//            }
//        });
//    }
//    private void showToast(String message) {
//        // Implement this method to show a toast message
//        Toast.makeText(BookReaderActivity.this, message, Toast.LENGTH_SHORT).show();
//    }
//
//    private void fetchBookContent(int bookId) {
//        BookService apiService = ApiClient.getClient().create(BookService.class);
//        Call<ResponseBody> call = apiService.getBookContent(bookId);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    try {
//                        String bookContent = response.body().string();
//                        String bookname = getIntent().getStringExtra("book_name");
//                        bookTitleTextView.setText(bookname); // 这里应该从API获取书籍标题
//                        bookContentTextView.setText(bookContent);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        bookTitleTextView.setText("Error: Failed to read book content");
//                    }
//                } else {
//                    bookTitleTextView.setText("Error: Failed to fetch book content");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                t.printStackTrace();
//                bookTitleTextView.setText("Error: Network request failed");
//            }
//        });
//    }
//}