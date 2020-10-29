package com.example.wordwithroom.room.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wordwithroom.room.data.Word;
import com.example.wordwithroom.room.repository.WordRepository;

import java.util.List;

/**
 * How to create table with init value ??
 */
public class WordViewModel extends AndroidViewModel {
    private WordRepository repository;

    public WordViewModel(@NonNull Application application) {
        super(application);
        repository = WordRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Word>> getAllWordsLive() {
        return repository.getAllWordsLive();
    }

    public LiveData<List<Word>> findWordsWithPattern(String patten) {
        return repository.findWordsWithPattern(patten);
    }

    public void insertWords(Word... words) {
        repository.insertWords(words);
    }

    public void updateWords(Word... words) {
        repository.updateWords(words);
    }

    public void deleteWords(Word... words) {
        repository.deleteWords(words);
    }

    public void deleteAllWords() {
        repository.deleteAllWords();
    }






}
