package com.example.activity;

import com.example.db.MyDatabaseHelper;
import com.example.model.Words;
import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DictResolverActivity extends BaseActivity implements OnClickListener{
	private Button insert_button;
	private Button delete_button;
	private Button update_button;
	private Button show_button;
	private EditText save_text;
	private TextView show_text;
	private ContentResolver contentResolver;
	private MyDatabaseHelper myDb;
	private SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dict_resolver);
		initView();
	}
	
	private void initView() {
		insert_button=(Button) findViewById(R.id.insert);
		delete_button=(Button) findViewById(R.id.delete);
		update_button=(Button) findViewById(R.id.update);
		show_button=(Button) findViewById(R.id.show);
		save_text=(EditText) findViewById(R.id.edit_text);
		show_text=(TextView) findViewById(R.id.show_text);
		
		insert_button.setOnClickListener(this);
		delete_button.setOnClickListener(this);
		update_button.setOnClickListener(this);
		show_button.setOnClickListener(this);
		
		contentResolver=getContentResolver();
		myDb=new MyDatabaseHelper(this, "myDict.db3", null, 1);
		db=myDb.getReadableDatabase();
		
	}

	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id=v.getId();
		switch (id) {
		case R.id.insert:
			String word = save_text.getText().toString();
			if(!word.isEmpty())
			{
				ContentValues values = new ContentValues();
				values.put(Words.Word.WORD, word);
				values.put(Words.Word.DETAIL, "detl:"+word);
				contentResolver.insert(Words.Word.DICT_CONTENT_URI, values);
				
			}
			break;
		case R.id.delete:
			contentResolver.delete(Words.Word.DICT_CONTENT_URI, null, null);
			break;
		case R.id.update:
			String word_update = save_text.getText().toString();
			if(!word_update.isEmpty())
			{
				ContentValues values = new ContentValues();
				values.put(Words.Word.WORD, word_update);
				values.put(Words.Word.DETAIL, word_update);
				showToastMsgShort(""+contentResolver.update(Uri.withAppendedPath(Words.Word.WORD_CONTENT_URI,"/2"), values,null, null));
			}
			break;
		case R.id.show:
//			Cursor cursor = contentResolver.query(Words.Word.DICT_CONTENT_URI, null, "word like ? or detail like ?",
//					new String[]{"%word%","%detl%"}, null);
			Cursor cursor = contentResolver.query(Words.Word.DICT_CONTENT_URI, null, null,
					null, null);
			String text="";
			while(cursor.moveToNext())
			{
					text=text+cursor.getString(1)+cursor.getString(2)+"\n";
					Log.d("≤‚ ‘", "_id="+cursor.getString(0));

			}
			show_text.setText(text);
			break;

		default:
			break;
		}
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "Conproviderπ¶ƒ‹";
	}

}
