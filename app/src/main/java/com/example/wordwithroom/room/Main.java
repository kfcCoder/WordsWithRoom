package com.example.wordwithroom.room;

public class Main {
/*
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mBinding;

    WordViewModel mViewModel;

    WordDatabase mWordDatabase;
    WordDao mWordDao;

    LiveData<List<Word>> mAllWordsLive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, activity_main);

        mViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        mWordDatabase = WordDatabase.getInstance(this);

        mWordDao = mWordDatabase.getWordDao();

        final MyAdapter adapterNormal = new MyAdapter(false, mViewModel);
        final MyAdapter adapterCard = new MyAdapter(true, mViewModel);

        mBinding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mBinding.recyclerView.setAdapter(adapterCard);
                } else {
                    mBinding.recyclerView.setAdapter(adapterNormal);
                }
            }
        });

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(adapterNormal);

        mAllWordsLive = mWordDao.getAllWordsLive();
        mAllWordsLive.observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                int size = adapterNormal.getItemCount();

                if(size != words.size()) {
                    adapterNormal.setAllWords(words);
                    adapterNormal.notifyDataSetChanged();

                    adapterCard.setAllWords(words);
                    adapterCard.notifyDataSetChanged();
                }
            }
        });

        mBinding.buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] english = {
                  "Hello",
                  "World",
                  "Android",
                  "Studio",
                  "Room",
                  "Basic",
                  "Database",
                  "Persistence",
                  "Library",
                  "Project",
                  "Fun",
                  "Cool"
                };

                String[] chinese = {
                  "你好",
                  "世界",
                  "安卓",
                  "工坊",
                  "房間",
                  "基本",
                  "資料庫",
                  "持久化",
                  "函式庫",
                  "計畫",
                  "有趣",
                  "酷"
                };

                for (int i = 0; i < english.length; i++) {
                    Word word = new Word(english[i], chinese[i]);
                    mViewModel.insertWords(word);
                }
            }
        });

        mBinding.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.deleteAllWords();
            }
        });

    }
}
*/
}
