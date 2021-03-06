package com.example.student.multimemo;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Video;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.student.multimemo.common.TitleBitmapButton;

// 동영상 선택  액티비티
public class VideoSelectionActivity extends AppCompatActivity {

	public static final String TAG = "VideoLoadingActivity";

	// 앨범에서 선택한 비디오의 URI
	String mAlbumVideoUri;

	ListView mVideoList;

	TextView mSelectedVideoTitle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video_loading_activity);

		setSelectVideoText();

        setBottomBtns();

      	Log.d(TAG, "Gallery data is loading.");
      	mVideoList = (ListView)findViewById(R.id.loading_listview);
    	Cursor c = getContentResolver().query(Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
    	final VideoCursorAdapter adapter = new VideoCursorAdapter(this, c);
    	mVideoList.setAdapter(adapter);

        // 아이템 클릭 리스너, 토스트 클릭 위치 셋
    	mVideoList.setOnItemClickListener(new OnItemClickListener() {
    		public void onItemClick(AdapterView parent, View v, int position, long id) {
    			try{
    				Uri uri = ContentUris.withAppendedId(Video.Media.EXTERNAL_CONTENT_URI, id); //개별 이미지에 대한 URI 생성
    				mAlbumVideoUri = uri.getPath();

    				String str = ((TextView)v).getText().toString();

    				mSelectedVideoTitle.setText(str);
    				mSelectedVideoTitle.setSelected(true);

    			}catch(Exception ie){
    				Log.d(TAG, ie.toString());
    			}

    		}
    	});
	}

	public void setSelectVideoText()
	{
		mSelectedVideoTitle = (TextView)findViewById(R.id.loading_selectedVideo);
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if (hasFocus) {
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath())));
		}

	}

	private void setBottomBtns(){

		TitleBitmapButton loading_okBtn = (TitleBitmapButton) findViewById(R.id.loading_okBtn);
		TitleBitmapButton loading_cancelBtn = (TitleBitmapButton) findViewById(R.id.loading_cancelBtn);

		loading_okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showParentActivity();

			}
        });

		loading_cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	class VideoCursorAdapter extends CursorAdapter {
		public VideoCursorAdapter(Context context, Cursor c) {
			super(context, c);
		}

		public void bindView(View view, Context context, Cursor cursor) {
			TextView videoTitleText = (TextView)view;

			long id = cursor.getLong(cursor.getColumnIndexOrThrow(Video.Media._ID));
			String str = cursor.getString(cursor.getColumnIndexOrThrow(Video.Media.TITLE));
			Uri uri = ContentUris.withAppendedId(Video.Media.EXTERNAL_CONTENT_URI, id); //개별 이미지에 대한 URI 생성
			Log.d(TAG, " id -> " + id + ", uri -> " + uri);


			try {
				videoTitleText.setText(str);


			} catch(Exception e) {}

		}

		public View newView(Context context, Cursor arg1, ViewGroup arg2) {
			TextView videoTitleText = new TextView(context);
			videoTitleText.setTextColor(Color.WHITE);


			videoTitleText.setPadding(10, 10, 10, 10);

			return videoTitleText;
		}

	}

	// 부모 액티비티로 돌아가기
	private void showParentActivity() {

		Intent intent = getIntent();

		if(mAlbumVideoUri != null){

			intent.putExtra(BasicInfo.KEY_URI_VIDEO, mAlbumVideoUri);

			setResult(RESULT_OK, intent);
		}
		finish();

	}
}