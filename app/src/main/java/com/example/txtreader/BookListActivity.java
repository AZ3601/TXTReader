package com.example.txtreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity {
    private RecyclerView bookRecyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up RecyclerView
        bookRecyclerView = findViewById(R.id.bookRecyclerView);
        bookRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch and display books
        fetchBooks();
    }

    private void fetchBooks() {
        // Fetch books from your data source (e.g., API, local database)
        BookService bookService = ApiClient.getClient().create(BookService.class);
        Call<List<Book>> call = bookService.getAllBooks();

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    bookList = response.body();
                    // Set up adapter
                    bookAdapter = new BookAdapter(bookList, BookListActivity.this);
                    bookRecyclerView.setAdapter(bookAdapter);
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_list, menu);

        // Set up SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks(newText);
                return true;
            }
        });

        return true;
    }

    private void filterBooks(String query) {
        List<Book> filteredList = new ArrayList<>();
        for (Book book : bookList) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(book);
            }
        }
        bookAdapter.setBookList(filteredList);
        bookAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_bookshelf) {
            // Navigate to BookshelfActivity
            startActivity(new Intent(this, BookShelfActivity.class));
            return true;
        } else if (id == R.id.action_upload_book) {
            // Navigate to UploadBookActivity
            startActivity(new Intent(this, UploadBookActivity.class));
            return true;
        } else if (id == R.id.action_all_books) {
            // Navigate to BookListActivity (already on this page)
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}