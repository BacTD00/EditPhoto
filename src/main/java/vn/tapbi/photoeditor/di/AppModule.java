package vn.tapbi.photoeditor.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Provides
    @Singleton
    public SharedPreferences provideSharedPreference(Application context) {
        return PreferenceManager.getDefaultSharedPreferences(context);

    }

//    @Provides
//    @Singleton
//    public MessageDatabase provideRoomDb(Application context) {
//        return Room.databaseBuilder(context, MessageDatabase.class, Constant.DB_NAME).fallbackToDestructiveMigration().addMigrations(MessageDatabase.MIGRATION_1_2).build();
////        return Room.databaseBuilder(context.getApplicationContext(), MessageDatabase.class, Constant.DB_NAME).fallbackToDestructiveMigration().addMigrations(MessageDatabase.MIGRATION_1_2).build();
//    }
//
//    @Provides
//    @Singleton
//    public MessageDao provideMessageThreadDao(MessageDatabase db) {
//        return db.getMessageDao();
//    }

//    @Provides
//    @Singleton
//    public MessageRepository getMessageRepository(){
//        return new MessageRepository();
//    }
}
