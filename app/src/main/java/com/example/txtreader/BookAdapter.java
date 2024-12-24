package com.example.txtreader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private List<Book> bookList;
    private Context context;
    private boolean isManageMode=false; // 添加 isManageMode 变量,默认不显示
    private BookShelfActivity activity;


    public BookAdapter(List<Book> bookList,Context context) {
        this.bookList = bookList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthor());
        // Load book cover image using a library like Glide or Picasso
        // Example using Glide:
        // Glide.with(holder.itemView.getContext()).load(book.getCoverUrl()).into(holder.bookCover);

        // Set click listener for each book item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start BookContentActivity and pass the book ID
                Intent intent = new Intent(holder.itemView.getContext(), BookReaderActivity.class); //不确定context对不对
                intent.putExtra("book_id", book.getId());
                intent.putExtra("book_name",book.getTitle());
                holder.itemView.getContext().startActivity(intent);
            }
        });
        // Show/hide the remove from bookshelf button based on the manage mode
        if (isManageMode&&activity!=null) {
            holder.removeFromBookshelfButton.setVisibility(View.VISIBLE);

        } else {
            holder.removeFromBookshelfButton.setVisibility(View.GONE);
        }

        // Set click listener for the remove from bookshelf button
        holder.removeFromBookshelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.removeFromBookshelf(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public void setManageMode(boolean isManageMode) {
        this.isManageMode = isManageMode; // 添加 setManageMode 方法
    }
    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
    public void setActivity(BookShelfActivity a) {
        this.activity = a;
    }

    //定义viewholder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bookTitle;
        public TextView bookAuthor;
        public ImageView bookCover;
        public Button removeFromBookshelfButton;

        public ViewHolder(View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookCover = itemView.findViewById(R.id.bookCover);
            removeFromBookshelfButton= itemView.findViewById(R.id.removeFromBookshelfButton);
        }
    }
}
