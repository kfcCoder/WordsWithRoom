package com.example.wordwithroom.room.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwithroom.R;
import com.example.wordwithroom.room.data.Word;
import com.example.wordwithroom.room.viewmodel.WordViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Word> allWords = new ArrayList<>();
    private boolean useCardVied;
    private WordViewModel wordViewModel;

    public MyAdapter(boolean useCardVied, WordViewModel wordViewModel) {
        this.useCardVied = useCardVied;
        this.wordViewModel = wordViewModel;
    }

    public void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        if(useCardVied) {
           itemView = inflater.inflate(R.layout.item_card_v2, parent, false);
        } else {
            itemView = inflater.inflate(R.layout.item_normal_v2, parent, false);
        }

        final MyViewHolder holder = new MyViewHolder(itemView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://tw.dictionary.search.yahoo.com/search;_ylt=AwrtXW9dcoVfFVUAlnR7rolQ;_ylc=X0kDQjY5MUVURXdMakppSG9FWlg0Vnh6Z0taTVRVd0xnQUFBQUQ4VFBnUQRfUwMxMzUxMjAwMzc5BF9yAzIEYWN0bgNjbGsEY3NyY3B2aWQDQjY5MUVURXdMakppSG9FWlg0Vnh6Z0taTVRVd0xnQUFBQUQ4VFBnUQRmcgMEZnIyA3NiLXRvcARncHJpZANFOHpTejJGbFNyZUF6c1kyY3dKX1dBBG5fcnNsdAMwBG5fc3VnZwM0BG9yaWdpbgN0dy5kaWN0aW9uYXJ5LnNlYXJjaC55YWhvby5jb20EcG9zAzAEcHFzdHIDBHBxc3RybAMwBHFzdHJsAzIEcXVlcnkDaGkEc2VjA3NlYXJjaARzbGsDYnV0dG9uBHQyA3NlYXJjaAR0NANidXR0b24EdF9zdG1wAzE2MDI1ODEyMDQEdnRlc3RpZAM-?ei=UTF-8&pvid=B691ETEwLjJiHoEZX4VxzgKZMTUwLgAAAAD8TPgQ&gprid=&fr=sfp&p="
                        + holder.textViewEnglish.getText());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.switchChineseInvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Word word = (Word) holder.itemView.getTag(R.id.word_for_view_holder);
                if(isChecked) {
                    holder.textViewChinese.setVisibility(View.GONE);
                    word.setChineseInvisible(true);
                    wordViewModel.updateWords(word);
                }
                else  {
                    holder.textViewChinese.setVisibility(View.VISIBLE);
                    word.setChineseInvisible(false);
                    wordViewModel.updateWords(word);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Word word = allWords.get(position);
        holder.textViewItem.setText(String.valueOf(position + 1));
        holder.textViewEnglish.setText(word.getWord());
        holder.textViewChinese.setText(word.getChineseMeaning());

        holder.itemView.setTag(R.id.word_for_view_holder, word); // can pass any object

        if(word.isChineseInvisible()) {
            holder.textViewChinese.setVisibility(View.GONE);
            holder.switchChineseInvisible.setChecked(true);
        } else {
            holder.textViewChinese.setVisibility(View.VISIBLE);
            holder.switchChineseInvisible.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return allWords.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItem, textViewEnglish, textViewChinese;
        Switch switchChineseInvisible;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewItem = itemView.findViewById(R.id.textViewNumber);
            textViewEnglish = itemView.findViewById(R.id.textViewEnglish);
            textViewChinese = itemView.findViewById(R.id.textViewChinese);
            switchChineseInvisible = itemView.findViewById(R.id.switchChineseInvisible);
        }
    }
}
