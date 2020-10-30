package com.example.wordwithroom.words;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.google.android.material.snackbar.Snackbar;

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
    private List<Word> allWords;
    private boolean undoAcion;
    private DividerItemDecoration dividerItemDecoration;

    private static final String VIEW_TYPE_SHP = "view_type_shp";
    private static final String IS_USING_CARD_VIEW = "is_using_card_view";

    public WordsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_words, container, false);
        viewModel = ViewModelProviders.of(requireActivity()).get(WordViewModel.class);
        return binding.getRoot();
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

                filteredWords.removeObservers(getViewLifecycleOwner()); // 有多個觀察記得先移除再設定新觀察者
                filteredWords = viewModel.findWordsWithPattern(pattern);
                filteredWords.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int temp = bindingAdapter.getItemCount();
                        allWords = words;

                        //adapter.setAllWords(words);
                        adapter.submitList(words);
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
                builder.setTitle("Clear Data");

                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
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

                if (isUsingCardView) { // 切過去使用NormalView
                    myRecyclerView.setAdapter(adapter);
                    myRecyclerView.addItemDecoration(dividerItemDecoration);
                    editor.putBoolean(IS_USING_CARD_VIEW, false);
                } else { // 切過去使用CardView
                    myRecyclerView.setAdapter(bindingAdapter);
                    myRecyclerView.removeItemDecoration(dividerItemDecoration);
                    editor.putBoolean(IS_USING_CARD_VIEW, true);
                }
                editor.apply();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myRecyclerView = binding.recyclerview;

        adapter = new MyAdapter(false, viewModel);
        bindingAdapter = new MyBindingAdapter(viewModel);

        myRecyclerView.setItemAnimator(new DefaultItemAnimator(){
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) myRecyclerView.getLayoutManager();
                if(linearLayoutManager != null) {
                    int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    for (int i = firstPosition; i <= lastPosition; i++) {
                        MyAdapter.MyViewHolder holder = (MyAdapter.MyViewHolder)
                                myRecyclerView.findViewHolderForAdapterPosition(i);
                        if(holder != null) {
                            holder.textViewItem.setText(String.valueOf(i + 1));
                        }
                    }
                }

            }
        });

        myRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        //myRecyclerView.setAdapter(adapter);

        dividerItemDecoration = new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);

        SharedPreferences preferences = requireActivity().getSharedPreferences(VIEW_TYPE_SHP, Context.MODE_PRIVATE);
        boolean isUsingCardView = preferences.getBoolean(IS_USING_CARD_VIEW, false);
        if(isUsingCardView) {
            myRecyclerView.setAdapter(bindingAdapter);
        } else {
            myRecyclerView.setAdapter(adapter);
            myRecyclerView.addItemDecoration(dividerItemDecoration);
        }



        filteredWords = viewModel.getAllWordsLive();
        filteredWords.observe(getViewLifecycleOwner(), new Observer<List<Word>>() { // 底層數據有變動才會觸發
            @Override
            public void onChanged(List<Word> words) { // View重新載入就會新增新的owner, 避免重複的observer
                int temp = bindingAdapter.getItemCount();
                //adapter.setAllWords(words);
                bindingAdapter.setAllWords(words);

                allWords = words;

                if(temp != words.size()) {
                    if(temp < words.size() && !undoAcion) {
                        myRecyclerView.smoothScrollBy(0, -200);
                    }
                    undoAcion = false;

                    adapter.submitList(words);
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Word wordToDelete = allWords.get(viewHolder.getAdapterPosition());
                viewModel.deleteWords(wordToDelete);

                Snackbar.make(getView(), "A Word Deleted", Snackbar.LENGTH_SHORT)
                        .setAction("Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                undoAcion = true;
                                viewModel.insertWords(wordToDelete);
                            }
                        })
                .show();
            }

            Drawable icon = ContextCompat.getDrawable(requireActivity(),R.drawable.ic_delete);
            Drawable background = new ColorDrawable(Color.LTGRAY);
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;

                int iconLeft,iconRight,iconTop,iconBottom;
                int backTop,backBottom,backLeft,backRight;
                backTop = itemView.getTop();
                backBottom = itemView.getBottom();
                iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) /2;
                iconBottom = iconTop + icon.getIntrinsicHeight();
                if (dX > 0) {
                    backLeft = itemView.getLeft();
                    backRight = itemView.getLeft() + (int)dX;
                    background.setBounds(backLeft,backTop,backRight,backBottom);
                    iconLeft = itemView.getLeft() + iconMargin ;
                    iconRight = iconLeft + icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);
                } else if (dX < 0){
                    backRight = itemView.getRight();
                    backLeft = itemView.getRight() + (int)dX;
                    background.setBounds(backLeft,backTop,backRight,backBottom);
                    iconRight = itemView.getRight()  - iconMargin;
                    iconLeft = iconRight - icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);
                } else {
                    background.setBounds(0,0,0,0);
                    icon.setBounds(0,0,0,0);
                }
                background.draw(c);
                icon.draw(c);
            }
        }).attachToRecyclerView(myRecyclerView);

        binding.setLifecycleOwner(requireActivity());
    }





    @Override
    public void onResume() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        super.onResume();
    }








}