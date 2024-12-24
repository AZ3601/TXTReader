package com.example.txtreader;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookShelfActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<Book> books;
    private int userId;
    private Button manageButton;
    private boolean isManageMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_shelf);


        recyclerView = findViewById(R.id.main_shelf_view); //id在对应的layout下才找得到
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置recyclerview子视图排列方式

        // 获取的用户ID
        SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(this);
        userId = sharedPrefsManager.getUserId();
        Toast.makeText(BookShelfActivity.this, "User ID ="+userId, Toast.LENGTH_SHORT).show();
        // Set up manage button
        manageButton = findViewById(R.id.manageButton);
        manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleManageMode();
            }
        });
        if (userId != -1) {
            fetchUserBookshelf(userId,false);
        } else {
            Toast.makeText(BookShelfActivity.this, "User ID not found", Toast.LENGTH_SHORT).show();

        }
    }

    private void fetchUserBookshelf(int userId,boolean isManageMode) {
        BookService bookService = ApiClient.getClient().create(BookService.class);
        Call<List<Book>> call = bookService.getUserBookshelf(userId);

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                     books = response.body();
                    if (books != null && !books.isEmpty()) {
                        // Set adapter
                        if(adapter==null) {
                            adapter = new BookAdapter(books, BookShelfActivity.this); //这里直接由bookList改成books了，观察是否兼容
                            adapter.setActivity(BookShelfActivity.this);
                            showToast("adapter==null");
                        } else{
                            adapter.setBookList(books);
                        }
                        adapter.setManageMode(isManageMode);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


                    } else {
                        Toast.makeText(BookShelfActivity.this,"No books found in user's bookshelf",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BookShelfActivity.this,"Failed to load user's bookshelf",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Toast.makeText(BookShelfActivity.this,"Error: " + t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void removeFromBookshelf(Book book) {
        // Call the backend API to remove the book from the user's bookshelf
        BookService bookService = ApiClient.getClient().create(BookService.class);
        Call<Void> call = bookService.removeFromBookshelf(userId, book.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    showToast("Book removed from bookshelf");
                    // Refresh the bookshelf
                    fetchUserBookshelf(userId,isManageMode);
                } else {
                    // Handle error
                    showToast("Failed to remove book from bookshelf: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                showToast("Failed to remove book from bookshelf: " + t.getMessage());
            }
        });
    }
    private void showToast(String message) {
        // Implement this method to show a toast message
        Toast.makeText(BookShelfActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void toggleManageMode() {
        isManageMode = !isManageMode;
        if (isManageMode) {
            manageButton.setText("Done");
        } else {
            manageButton.setText("Manage");
        }
        // Refresh the adapter to show/hide the remove button
        adapter.setManageMode(isManageMode);
        adapter.notifyDataSetChanged();
        //fetchUserBookshelf(userId,isManageMode);
    }
}
//package com.example.txtreader;
//
//import android.os.Bundle;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import java.util.ArrayList;
//import java.util.List;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class BookShelfActivity extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    private BookAdapter adapter;
//    private List<Book> bookList;
//    private int userId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.book_shelf);
//        // 获取传递的用户ID
//        userId = getIntent().getIntExtra("user_id", -1);
//
//        recyclerView = findViewById(R.id.main_shelf_view); //id在对应的layout下才找得到
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置recyclerview子视图排列方式
//        BookService bookService = ApiClient.getClient().create(BookService.class);
//
//        Call<List<Book>> call = bookService.getUserBookshelf(1); // 假设用户ID为1
//        call.enqueue(new Callback<List<Book>>() {
//            @Override
//            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
//                if (response.isSuccessful()) {
//                    List<Book> books = response.body();
//                    if (books != null && !books.isEmpty()) {
//                        //StringBuilder sb = new StringBuilder();
//                        // Initialize book list
////                        bookList = new ArrayList<>();
////                        bookList.add(new Book(1,"Book 1", "Author 1", "https://example.com/cover1.jpg",""));
////                        bookList.add(new Book(2,"Book 2", "Author 2", "https://example.com/cover2.jpg",""));
////                        bookList.add(new Book(3,"Book 3", "Author 3", "https://example.com/cover3.jpg",""));
//
////                        for (Book book : books) {
////                            sb.append("Title: ").append(book.getTitle()).append("\n");
////                            sb.append("Author: ").append(book.getAuthor()).append("\n\n");
////                        }
////                        textView.setText(sb.toString());
//                        // Set adapter
//                          adapter = new BookAdapter(books); //这里直接由bookList改成books了，观察是否兼容
//                          recyclerView.setAdapter(adapter);
//                    } else {
//                        //此处和if差不多
//                        adapter = new BookAdapter(books);
//                        recyclerView.setAdapter(adapter);
//                    }
//                } else {
//                    //作警告
//                    Toast.makeText(BookShelfActivity.this,"Failed to load user's bookshelf",Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Book>> call, Throwable t) {
//               //是否可报错？
//                Toast.makeText(BookShelfActivity.this,"Error: " + t.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }
//}
//
////package com.example.txtreader;
////
////import android.os.Bundle;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.recyclerview.widget.LinearLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////import java.util.ArrayList;
////import java.util.List;
////
////public class BookShelfActivity extends AppCompatActivity {
////    private RecyclerView recyclerView;
////    private BookAdapter adapter;
////    private List<Book> bookList;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.book_shelf);
////
////        recyclerView = findViewById(R.id.main_shelf_view); //id在对应的layout下才找得到
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////
////        // Initialize book list
////        bookList = new ArrayList<>();
////        bookList.add(new Book(1,"Book 1", "Author 1", "https://example.com/cover1.jpg",""));
////        bookList.add(new Book(2,"Book 2", "Author 2", "https://example.com/cover2.jpg",""));
////        bookList.add(new Book(3,"Book 3", "Author 3", "https://example.com/cover3.jpg",""));
////
////        // Set adapter
////        adapter = new BookAdapter(bookList);
////        recyclerView.setAdapter(adapter);
////    }
////}
