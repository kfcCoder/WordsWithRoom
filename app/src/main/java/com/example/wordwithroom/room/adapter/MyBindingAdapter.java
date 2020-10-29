package com.example.wordwithroom.room.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwithroom.R;
import com.example.wordwithroom.databinding.ItemCardV2Binding;
import com.example.wordwithroom.room.data.Word;
import com.example.wordwithroom.room.viewmodel.WordViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyBindingAdapter extends RecyclerView.Adapter<MyBindingAdapter.ViewHolder>
        implements View.OnClickListener {
    private List<Word> allWords = new ArrayList<>();
    private WordViewModel viewModel;
    private ItemCardV2Binding binding;

    public MyBindingAdapter(WordViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card_v2, parent, false);
        ViewHolder holder = new ViewHolder(view);
        binding = holder.getBinding();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Word word = allWords.get(position);
        binding.setWord(word);
        binding.textViewNumber.setText(String.valueOf(position + 1));
        holder.itemView.setOnClickListener(this);

        // 因為視圖可回收, 因此在重新綁定之前記得先將監聽器歸零
        binding.switchChineseInvisible.setOnCheckedChangeListener(null);

        if (word.isChineseInvisible()) {
            binding.textViewChinese.setVisibility(View.GONE);
            binding.switchChineseInvisible.setChecked(true);
        } else {
            binding.textViewChinese.setVisibility(View.VISIBLE);
            binding.switchChineseInvisible.setChecked(false);
        }

        binding.switchChineseInvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.textViewChinese.setVisibility(View.GONE);
                    word.setChineseInvisible(true);
                    viewModel.updateWords(word);
                } else {
                    binding.textViewChinese.setVisibility(View.VISIBLE);
                    word.setChineseInvisible(false);
                    viewModel.updateWords(word);
                }
            }
        });

        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return allWords.size();
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = new ViewHolder(v);
        Uri uri = Uri.parse("https://tw.dictionary.search.yahoo.com/search;_ylt=AwrtXW9dcoVfFVUAlnR7rolQ;_ylc=X0kDQjY5MUVURXdMakppSG9FWlg0Vnh6Z0taTVRVd0xnQUFBQUQ4VFBnUQRfUwMxMzUxMjAwMzc5BF9yAzIEYWN0bgNjbGsEY3NyY3B2aWQDQjY5MUVURXdMakppSG9FWlg0Vnh6Z0taTVRVd0xnQUFBQUQ4VFBnUQRmcgMEZnIyA3NiLXRvcARncHJpZANFOHpTejJGbFNyZUF6c1kyY3dKX1dBBG5fcnNsdAMwBG5fc3VnZwM0BG9yaWdpbgN0dy5kaWN0aW9uYXJ5LnNlYXJjaC55YWhvby5jb20EcG9zAzAEcHFzdHIDBHBxc3RybAMwBHFzdHJsAzIEcXVlcnkDaGkEc2VjA3NlYXJjaARzbGsDYnV0dG9uBHQyA3NlYXJjaAR0NANidXR0b24EdF9zdG1wAzE2MDI1ODEyMDQEdnRlc3RpZAM-?ei=UTF-8&pvid=B691ETEwLjJiHoEZX4VxzgKZMTUwLgAAAAD8TPgQ&gprid=&fr=sfp&p="
                + binding.textViewEnglish.getText());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        v.getContext().startActivity(intent);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemCardV2Binding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ItemCardV2Binding getBinding() {
            return binding;
        }
    }

}