package com.example.wordwithroom.room.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.wordwithroom.room.dao.WordDao;
import com.example.wordwithroom.room.data.Word;
import com.example.wordwithroom.room.database.WordDatabase;

import java.util.List;

/**
 * Singleton?
 * 1. dao is passing in from outside
 */
public class WordRepository {
    private WordDao wordDao;

    public WordRepository(Context context) {
        WordDatabase database = WordDatabase.getInstance(context.getApplicationContext());
        wordDao = database.getWordDao();
    }

    /**
     * Singleton part
     */
    private static WordRepository INSTANCE;

    //public static WordRepository getInstance(WordDao dao) { // for InjectorUtils
    public static WordRepository getInstance(Context context) { // injection could be problematic
        if (INSTANCE == null) {
            synchronized (WordRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WordRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<List<Word>> getAllWordsLive() {
        return wordDao.getAllWordsLive();
    }

    public LiveData<List<Word>> findWordsWithPattern(String pattern) {
        return wordDao.findWordsWithPattern("%" + pattern + "%");
    }

    public void insertWords(Word... words) {
        new InsertAsyncTask(wordDao).execute(words);
    }

    public void updateWords(Word... words) {
        new UpdateAsyncTask(wordDao).execute(words);
    }

    public void deleteWords(Word... words) {
        new DeleteAsyncTask(wordDao).execute(words);
    }

    public void deleteAllWords() {
        new DeleteAllAsyncTask(wordDao).execute();
    }

    static class InsertAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWords(words);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public UpdateAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.updateWords(words);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public DeleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.deleteWords(words);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordDao wordDao;

        public DeleteAllAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAllWords();
            return null;
        }
    }
}
