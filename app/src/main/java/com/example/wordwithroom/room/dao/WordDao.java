package com.example.wordwithroom.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wordwithroom.room.data.Word;

import java.util.List;

@Dao
public interface WordDao {
    @Insert
    void insertWords(Word... words);

    @Update
    void updateWords(Word... words);

    @Delete
    void deleteWords(Word... words);

    @Query("DELETE FROM word")
    void deleteAllWords();

    @Query("SELECT * From word ORDER BY id DESC")
    //List<Word> getAllWords();
    LiveData<List<Word>> getAllWordsLive(); // LiveData executes on worker thread automatically

    @Query("SELECT * FROM word WHERE english_word LIKE :pattern ORDER BY id DESC")
    LiveData<List<Word>>findWordsWithPattern(String pattern);

}
