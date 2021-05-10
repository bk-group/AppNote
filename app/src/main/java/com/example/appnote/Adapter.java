package com.example.appnote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {

    Context context;
    List<Note> notesList;
    List<Note> newNotesList;

    OnHandleClickListener onHandleClickListener;

    public Adapter(Context context, List<Note> notesList, OnHandleClickListener onHandleClickListener) {
        this.context = context;
        this.notesList = notesList;
        newNotesList = notesList;
        this.onHandleClickListener = onHandleClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(newNotesList.get(position).getTitle());
        holder.description.setText(newNotesList.get(position).getDescription());

        holder.layout.setOnClickListener(v -> onHandleClickListener.handClick(newNotesList.get(position)));
    }

    @Override
    public int getItemCount() {
        return newNotesList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;        //Dùng ViewHolder để giữ lại, để không cần
        RelativeLayout layout;              //findViewByID nữa mỗi lần View lên

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            layout = itemView.findViewById(R.id.noteLayout);

        }
    }



    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {

        //loc ket qua
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                newNotesList = notesList;
            } else {
                String search = constraint.toString().toLowerCase();     //luu tru chuoi tim kiem
                for (Note i : notesList) {
                    if (i.getTitle().toLowerCase().contains(search)) {
                        filteredList.add(i);
                    }
                }
                newNotesList = filteredList;
            }
            FilterResults results = new FilterResults();
            results.values = newNotesList;
            return results;
        }

        //de xuat ket qua
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            newNotesList = (List<Note>) results.values;
            notifyDataSetChanged();
        }
    };

    public interface OnHandleClickListener {
        void handClick(Note note);
    }

    public List<Note> getList() {
        return notesList;
    }

    public void removeItem(int position) {
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    public void unDo(Note item, int position) {
        notesList.add(position, item);
        notifyItemInserted(position);
    }

}
