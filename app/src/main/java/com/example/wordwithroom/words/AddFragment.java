package com.example.wordwithroom.words;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.wordwithroom.R;
import com.example.wordwithroom.room.data.Word;
import com.example.wordwithroom.room.viewmodel.WordViewModel;


public class AddFragment extends Fragment {

    public AddFragment() {
        // Required empty public constructor
    }

    private Button buttonSubmit;
    private EditText editTextEnglish, editTextChinese;
    private WordViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final FragmentActivity activity = requireActivity();
        viewModel = ViewModelProviders.of(activity).get(WordViewModel.class);

        buttonSubmit = activity.findViewById(R.id.button);
        editTextEnglish = activity.findViewById(R.id.editTextTextEnglish);
        editTextChinese = activity.findViewById(R.id.editTextTextChinese);

        buttonSubmit.setEnabled(false);

        editTextEnglish.requestFocus(); // 獲取焦點才能彈出鍵盤
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextEnglish, 0); // 強制顯示鍵盤

        TextWatcher textWatcher = new TextWatcher() { // 監聽EditText的內容變化
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String english = editTextEnglish.getText().toString().trim();
                String chinese = editTextChinese.getText().toString().trim();
                buttonSubmit.setEnabled(!english.isEmpty() && !chinese.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editTextEnglish.addTextChangedListener(textWatcher);
        editTextChinese.addTextChangedListener(textWatcher);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String english = editTextEnglish.getText().toString().trim();
                String chinese = editTextChinese.getText().toString().trim();
                Word word = new Word(english, chinese);
                viewModel.insertWords(word);

                NavController controller = Navigation.findNavController(v);
                controller.navigateUp(); // 回到上一頁

                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });
    }


}