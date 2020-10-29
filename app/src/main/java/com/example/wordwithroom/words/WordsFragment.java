package com.example.wordwithroom.words;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.example.wordwithroom.R;
import com.example.wordwithroom.databinding.FragmentWordsBinding;
import com.example.wordwithroom.room.adapter.MyAdapter;
import com.example.wordwithroom.room.adapter.MyBindingAdapter;
import com.example.wordwithroom.room.data.Word;
import com.example.wordwithroom.room.viewmodel.WordViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

// WordsFragment to AddFragment
public class WordsFragment extends Fragment {
    private WordViewModel viewModel;
    private FragmentWordsBinding binding;
    private RecyclerView myRecyclerView;
    private MyAdapter adapter; // normal layout
    private MyBindingAdapter bindingAdapter; // card layout
    private FloatingActionButton floatingActionButton;
    private LiveData<List<Word>> filteredWords;

    private static final String VIEW_TYPE_SHP = "view_type_shp";
    private static final String IS_USING_CARD_VIEW = "is_using_card_view";

    public WordsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        SearchView searchView = (android.widget.SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(1000);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String pattern = newText.trim();

                filteredWords.removeObservers(requireActivity()); // 有多個觀察記得先移除再設定新觀察者
                filteredWords = viewModel.findWordsWithPattern(pattern);
                filteredWords.observe(requireActivity(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int temp = bindingAdapter.getItemCount();
                        adapter.setAllWords(words);
                        bindingAdapter.setAllWords(words);
                        if(temp != words.size()) {
                            adapter.notifyDataSetChanged();
                            bindingAdapter.notifyDataSetChanged();
                        }
                    }
                });

                return true; // 如果不需事件繼續往下傳遞就返回true
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clearData:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Clear Daata");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.deleteAllWords();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
                break;
            case R.id.switchViewType:
                SharedPreferences preferences = requireActivity().getSharedPreferences(VIEW_TYPE_SHP, Context.MODE_PRIVATE);
                boolean isUsingCardView = preferences.getBoolean(IS_USING_CARD_VIEW, false);
                SharedPreferences.Editor editor = preferences.edit();

                if (isUsingCardView) { // 切換前使用CardView
                    myRecyclerView.setAdapter(adapter);
                    editor.putBoolean(IS_USING_CARD_VIEW, false);
                } else {
                    myRecyclerView.setAdapter(bindingAdapter);
                    editor.putBoolean(IS_USING_CARD_VIEW, true);
                }
                editor.apply();
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_words, container, false);
        viewModel = ViewModelProviders.of(requireActivity()).get(WordViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myRecyclerView = binding.recyclerview;

        adapter = new MyAdapter(false, viewModel);
        bindingAdapter = new MyBindingAdapter(viewModel);

        myRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        //myRecyclerView.setAdapter(adapter);

        SharedPreferences preferences = requireActivity().getSharedPreferences(VIEW_TYPE_SHP, Context.MODE_PRIVATE);
        boolean isUsingCardView = preferences.getBoolean(IS_USING_CARD_VIEW, false);
        if(isUsingCardView) {
            myRecyclerView.setAdapter(bindingAdapter);
        } else {
            myRecyclerView.setAdapter(adapter);
        }

        filteredWords = viewModel.getAllWordsLive();
        filteredWords.observe(requireActivity(), new Observer<List<Word>>() { // 底層數據有變動才會觸發
            @Override
            public void onChanged(List<Word> words) {
                int temp = bindingAdapter.getItemCount();
                adapter.setAllWords(words);
                bindingAdapter.setAllWords(words);
                if(temp != words.size()) {
                    adapter.notifyDataSetChanged();
                    bindingAdapter.notifyDataSetChanged();
                }
            }
        });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_wordsFragment_to_addFragment);
            }
        });

        binding.setLifecycleOwner(requireActivity());
    }

    @Override
    public void onResume() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        super.onResume();
    }







}