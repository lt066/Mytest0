package com.example.widget;

import com.example.db.MyDatabaseHelper;
import com.example.model.Words;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class DictProvider extends ContentProvider{
	
	private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int WORDS=1;
	private static final int WORD=2;
	private MyDatabaseHelper myDb;
	static{
		uriMatcher.addURI(Words.AUTHORITY, "words", WORDS);
		uriMatcher.addURI(Words.AUTHORITY, "word/#", WORD);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		myDb = new MyDatabaseHelper(this.getContext(), "myDict.db3", null, 1);
		return true;
	}
	

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = myDb.getReadableDatabase();
		int num=0;
		switch (uriMatcher.match(uri)) {
		case WORDS:
			return db.query("dict", projection, selection, selectionArgs, null, null, sortOrder);
		case WORD:
			long id = ContentUris.parseId(uri);
			String where = Words.Word._ID+"="+id;
			if(!selection.isEmpty())
			{
				where=where+" and "+selection;
			}
			return db.query("dict", projection, where, selectionArgs, null, null, sortOrder);

		default:
			break;
		}
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
		case WORDS:
			
			return "vnd.android.cursor.dir/com.example.mytest0.dict";

		case WORD:
			
			return "vnd.android.cursor.item/com.example.mytest0.dict";
		default:
			break;
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = myDb.getReadableDatabase();
		long rowId = db.insert("dict", Words.Word._ID, values);
		if(rowId>0)
		{
			Uri wordUri = ContentUris.withAppendedId(uri, rowId);
			getContext().getContentResolver().notifyChange(wordUri, null);
			return wordUri;
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = myDb.getReadableDatabase();
		int num=0;
		switch (uriMatcher.match(uri)) {
		case WORDS:
			num=db.delete("dict", selection, selectionArgs);
			break;
		case WORD:
			long id = ContentUris.parseId(uri);
			String where = Words.Word._ID+"="+id;
			if(!selection.isEmpty())
			{
				where=where+" and "+selection;
			}
			num=db.delete("dict", where, selectionArgs);
			break;

		default:
			break;
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return num;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=myDb.getReadableDatabase();
		int num=0;
		switch (uriMatcher.match(uri)) {
		case WORDS:
			num=db.update("dict", values, selection, selectionArgs);
			
			break;
			
		case WORD:
//			long id = ContentUris.parseId(uri);
			String id = "46";
			String where = "_id=?";
			String[] whereArgs={id};
			if(selection!=null && !selection.isEmpty())
			{
				where=where+" and "+selection;
			}
			if(selectionArgs!=null && selectionArgs.length!=0)
			{
				System.arraycopy(selectionArgs, 0, where, where.length(), where.length()+selection.length()-1);
			}
			num=db.update("dict", values, where, whereArgs);
			Log.d("≤‚ ‘", where);
			Log.d("≤‚ ‘", whereArgs[0]+"");
			break;

		default:
			Log.d("≤‚ ‘", "num2");
			break;
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return num;
	}

}
