package ezz.abdulaziz.project2_popularmovies_stage2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_BACKDROP_PATH;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_MOVIE_ID;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_MOVIE_TITLE;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_OVERVIEW;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_POPULARITY;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_POSTER_PATH;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_RELEASE_DATE;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_TIMESTAMP;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_VOTE_AVERAGE;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLM_VOTE_COUNT;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLR_AUTHOR;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLR_CONTENT;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLR_REVIEW_ID;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLR_URL;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLT_NAME;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLT_SIZE;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLT_SOURCE;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.CLT_TYPE;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.TBL_MOVIE;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.TBL_REVIEW;
import static ezz.abdulaziz.project2_popularmovies_stage2.database.DBContract.DBSchema.TBL_TRAILER;

/**
 * Created by EZZ on 2018-04-03.
 */

public class DBHelper extends SQLiteOpenHelper
{

	private static final String DATABASE_NAME = "mymoviedb.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		final String CREATE_MOVIE_TABLE =
				"CREATE TABLE " + TBL_MOVIE + " (" +
						_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
						CLM_MOVIE_ID + " INTEGER NOT NULL, " +
						CLM_VOTE_COUNT + " INTEGER NOT NULL, " +
						CLM_VOTE_AVERAGE + " REAL NOT NULL, " +
						CLM_POPULARITY + " REAL NOT NULL, " +
						CLM_MOVIE_TITLE + " TEXT NOT NULL, " +
						CLM_OVERVIEW + " TEXT NOT NULL, " +
						CLM_POSTER_PATH + " TEXT NOT NULL, " +
						CLM_BACKDROP_PATH + " TEXT NOT NULL, " +
						CLM_RELEASE_DATE + " TEXT NOT NULL, " +
						CLM_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
						"); ";

		final String CREATE_REVIEW_TABLE =
				"CREATE TABLE " + TBL_REVIEW + " (" +
						_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
						CLR_REVIEW_ID + " TEXT NOT NULL, " +
						CLM_MOVIE_ID + " INTEGER NOT NULL, " +
						CLR_AUTHOR + " TEXT, " +
						CLR_CONTENT + " TEXT NOT NULL, " +
						CLR_URL + " TEXT NOT NULL " +
						"); ";

		final String CREATE_TRAILER_TABLE =
				"CREATE TABLE " + TBL_TRAILER + " (" +
						_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
						CLT_NAME + " TEXT NOT NULL, " +
						CLT_SIZE + " TEXT, " +
						CLM_MOVIE_ID + " INTEGER NOT NULL, " +
						CLT_SOURCE + " TEXT NOT NULL, " +
						CLT_TYPE + " TEXT NOT NULL " +
						"); ";

		db.execSQL(CREATE_MOVIE_TABLE);
		db.execSQL(CREATE_REVIEW_TABLE);
		db.execSQL(CREATE_TRAILER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TBL_MOVIE);
		db.execSQL("DROP TABLE IF EXISTS " + TBL_REVIEW);
		db.execSQL("DROP TABLE IF EXISTS " + TBL_TRAILER);
		onCreate(db);
	}

}
