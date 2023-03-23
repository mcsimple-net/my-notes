package com.improve.mynotes;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class},version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static  NoteDatabase intance;

    public abstract  NoteDao noteDao();

    public static  synchronized NoteDatabase getInstance(Context context){

        if(intance == null){

            intance = Room.databaseBuilder(context.getApplicationContext()
                    ,NoteDatabase.class,"note_database")
                    //.allowMainThreadQueries() --> not recommended
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();

        }

        return intance;

    }

    private static RoomDatabase.Callback roomCallback = new  RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            //new  PopulateDbAsynTask(intance).execute();

            NoteDao noteDao = intance.noteDao();

            ExecutorService executorService = Executors.newSingleThreadExecutor();

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    noteDao.insert(new Note("Title 1","Description 1"));
                    noteDao.insert(new Note("Title 2","Description 2"));
                    noteDao.insert(new Note("Title 3","Description 3"));
                    noteDao.insert(new Note("Title 4","Description 4"));
                    noteDao.insert(new Note("Title 5","Description 5"));

                }
            });

        }
    };

    /*
    public static class PopulateDbAsynTask extends AsyncTask<Void,Void,Void>{

        private NoteDao noteDao;

        private PopulateDbAsynTask(NoteDatabase database){

            noteDao = database.noteDao();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            noteDao.insert(new Note("Title 1","Description 1"));
            noteDao.insert(new Note("Title 2","Description 2"));
            noteDao.insert(new Note("Title 3","Description 3"));
            noteDao.insert(new Note("Title 4","Description 4"));
            noteDao.insert(new Note("Title 5","Description 5"));

            return null;
        }
    }

     */

}
