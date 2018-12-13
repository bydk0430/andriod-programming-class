package com.example.student.multimemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.example.student.multimemo.common.TitleBitmapButton;

import java.io.File;

// 음성 녹음  액티비티
public class VoiceRecordingActivity extends AppCompatActivity implements VoiceRecorder.OnStateChangedListener {

	public static final String TAG = "VoiceRecordingActivity";

	TextView mRecordingTimeText;
	TitleBitmapButton mStartStopBtn;
	TitleBitmapButton mCancelBtn;

	boolean isRecording;
	RemainingTimeCalculator mRemainingTimeCalculator;
    static final String AUDIO_3GPP = "audio/3gpp";
    static final String AUDIO_AMR = "audio/amr";
    static final String AUDIO_ANY = "audio/*";
    static final String ANY_ANY = "*/*";

    static final int BITRATE_AMR =  5900;
    static final int BITRATE_3GPP = 5900;

    String mRequestedType = AUDIO_AMR;
    VoiceRecorder mRecorder;
    boolean mSampleInterrupted = false;

    public static final int WARNING_INSERT_SDCARD = 1011;
    public static final int WARNING_DISK_SPACE_FULL = 1012;
    public static final int RECORDING_START = 1013;

    static final String STATE_FILE_NAME = "soundrecorder.state";
    static final String RECORDER_STATE_KEY = "recorder_state";
    static final String SAMPLE_INTERRUPTED_KEY = "sample_interrupted";
    static final String MAX_FILE_SIZE_KEY = "max_file_size";

    long mMaxFileSize = -1;

    WakeLock mWakeLock;

    long mRecordingTime;

    String mTimerFormat;
    Handler mHandler = new Handler();
    Runnable mUpdateTimer = new Runnable() {
        public void run() { updateTimerView(mRecordingTimeText); }
    };

    public int mRecordingState = 0;

    public static final int RECORDING_IDLE = 0;

    public static final int RECORDING_RUNNING = 1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.voice_recording_activity);

		setBottomBtns();
		init(savedInstanceState);
	}

	public void setBottomBtns()
	{
		isRecording = false;

		mRecordingTimeText = (TextView)findViewById(R.id.recording_recordingTimeText);
		mRecordingTimeText.setText("00:00");
		mStartStopBtn = (TitleBitmapButton)findViewById(R.id.recording_startstopBtn);
		mStartStopBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.btn_voice_record, 0, 0);
		mCancelBtn = (TitleBitmapButton)findViewById(R.id.recording_cancelBtn);

		mStartStopBtn.setText(R.string.audio_recording_start_title);
		mStartStopBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(isRecording)
				{
					stopVoiceRecording();
					isRecording = false;
				}
				else
				{
					startVoiceRecording();
					isRecording = true;
				}

			}
		});

		mCancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void init(Bundle icycle)
	{
		mRecorder = new VoiceRecorder();
        mRecorder.setOnStateChangedListener(this);
        mRemainingTimeCalculator = new RemainingTimeCalculator();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "MediGalaxy");


        if (icycle != null) {
            Bundle recorderState = icycle.getBundle(RECORDER_STATE_KEY);
            if (recorderState != null) {
                mRecorder.restoreState(recorderState);
                mSampleInterrupted = recorderState.getBoolean(SAMPLE_INTERRUPTED_KEY, false);
                mMaxFileSize = recorderState.getLong(MAX_FILE_SIZE_KEY, -1);
            }
        }

        mTimerFormat = "%02d:%02d";
        updateUi(mRecordingTimeText);
	}

	public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateUi(mRecordingTimeText);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mRecorder.sampleLength() == 0)
            return;

        Bundle recorderState = new Bundle();

        mRecorder.saveState(recorderState);
        recorderState.putBoolean(SAMPLE_INTERRUPTED_KEY, mSampleInterrupted);
        recorderState.putLong(MAX_FILE_SIZE_KEY, mMaxFileSize);

        outState.putBundle(RECORDER_STATE_KEY, recorderState);
    }


    //  레코더의 상태 변환시 불러오기
    public void onStateChanged(int state) {
        if (state == VoiceRecorder.PLAYING_STATE || state == VoiceRecorder.RECORDING_STATE) {
            mSampleInterrupted = false;
        }

        if (state == VoiceRecorder.RECORDING_STATE) {
            mWakeLock.acquire();
        } else {
            if (mWakeLock.isHeld())
                mWakeLock.release();
        }
        updateUi(mRecordingTimeText);
    }

    // 미디어플레이어가 에러났을때 불러오기
    public void onError(int error) {

        String message = null;
        switch (error) {
            case VoiceRecorder.SDCARD_ACCESS_ERROR:
                message = "SD카드 접근 오류입니다.";
                break;
            case VoiceRecorder.IN_CALL_RECORD_ERROR:
            case VoiceRecorder.INTERNAL_ERROR:
                message = "내부 오류입니다.";
                break;
        }

        if (message != null) {
            new AlertDialog.Builder(this)
                .setTitle(R.string.audio_recording_title)
                .setMessage(message)
                .setPositiveButton(R.string.confirm_btn, null)
                .setCancelable(false)
                .show();
        }
    }

    public void updateUi(TextView textView) {
    	updateTimerView(textView);

    }

    //  MM:SS 타이머 업데이트. 재생 됐을때 프로그레스바 업데이트
    private void updateTimerView(TextView textView) {
        Resources res = getResources();
        int state = mRecorder.state();

        boolean ongoing = state == VoiceRecorder.RECORDING_STATE || state == VoiceRecorder.PLAYING_STATE;

        long time = ongoing ? mRecorder.progress() : mRecorder.sampleLength();
        String timeStr = String.format(mTimerFormat, time/60, time%60);

        textView.setText(timeStr);
        mRecordingTime = time;

        if (state == VoiceRecorder.PLAYING_STATE) {
        } else if (state == VoiceRecorder.RECORDING_STATE) {
            updateTimeRemaining();
        }

        if (ongoing)
            mHandler.postDelayed(mUpdateTimer, 1000);
    }

    // 레코딩 상태일때 불러오기. 레코딩 얼마나 가능한지 찾기.만약 5분 미만일때 UI에서 카운트 다운 보여줌. 만약 시간이 다되면 레코딩 중단
    private void updateTimeRemaining() {
        long t = mRemainingTimeCalculator.timeRemaining();

        if (t <= 0) {
            mSampleInterrupted = true;

            int limit = mRemainingTimeCalculator.currentLowerLimit();
            switch (limit) {
                case RemainingTimeCalculator.DISK_SPACE_LIMIT:
                    break;
                case RemainingTimeCalculator.FILE_SIZE_LIMIT:
                    break;
                default:
                    break;
            }

            mRecorder.stop();
            return;
        }

        String timeStr = "";

        if (t < 60)
            timeStr = String.format("%d min available", t);
        else if (t < 540)
            timeStr = String.format("%d s available", t/60 + 1);

    }

	public void startVoiceRecording() {
		voiceRecordingStart();

		mStartStopBtn.setText(R.string.audio_recording_stop_title);
		mStartStopBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.btn_voice_stop, 0, 0);

		mRecordingTimeText.setText("00:00");
    }


	 public void voiceRecordingStart() {
    	mRemainingTimeCalculator.reset();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mSampleInterrupted = true;
            showDialog(WARNING_INSERT_SDCARD);


            stopAudioPlayback();

            if (AUDIO_AMR.equals(mRequestedType)) {
                mRemainingTimeCalculator.setBitRate(BITRATE_AMR);
                mRecorder.startRecording(MediaRecorder.OutputFormat.RAW_AMR, ".amr", this);
            } else if (AUDIO_3GPP.equals(mRequestedType)) {
                mRemainingTimeCalculator.setBitRate(BITRATE_3GPP);
                mRecorder.startRecording(MediaRecorder.OutputFormat.THREE_GPP, ".3gpp", this);
            } else {
                throw new IllegalArgumentException("Invalid output file type requested");
            }

            if (mMaxFileSize != -1) {
                mRemainingTimeCalculator.setFileSizeLimit(mRecorder.sampleFile(), mMaxFileSize);
            }


        } else if (!mRemainingTimeCalculator.diskSpaceAvailable()) {
            mSampleInterrupted = true;
            showDialog(WARNING_DISK_SPACE_FULL);
        } else {
            stopAudioPlayback();

            if (AUDIO_AMR.equals(mRequestedType)) {
                mRemainingTimeCalculator.setBitRate(BITRATE_AMR);
                mRecorder.startRecording(MediaRecorder.OutputFormat.RAW_AMR, ".amr", this);
            } else if (AUDIO_3GPP.equals(mRequestedType)) {
                mRemainingTimeCalculator.setBitRate(BITRATE_3GPP);
                mRecorder.startRecording(MediaRecorder.OutputFormat.THREE_GPP, ".3gpp", this);
            } else {
                throw new IllegalArgumentException("Invalid output file type requested");
            }

            if (mMaxFileSize != -1) {
                mRemainingTimeCalculator.setFileSizeLimit(mRecorder.sampleFile(), mMaxFileSize);
            }
        }

        mRecordingState = RECORDING_RUNNING;

    }

	 public void stopVoiceRecording() {
		 mRecorder.stop();

		 mStartStopBtn.setText(R.string.audio_recording_start_title);
		 mStartStopBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.btn_voice_record, 0, 0);

		 File tempFile = mRecorder.sampleFile();
		 saveRecording(tempFile);

		 mRecordingState = RECORDING_IDLE;
	 }

	 private void saveRecording(File tempFile) {

		 checkVoiceFolder();

		 String voiceName = "recorded";

		 try {
			 tempFile.renameTo(new File(BasicInfo.FOLDER_VOICE + voiceName));
			 setResult(RESULT_OK);
		 } catch(Exception ex) {
			 Log.e(TAG, "Exception in storing recording.", ex);
		 }


		 if (voiceName != null) {
			 Log.d(TAG, "Recording file saved to : " + voiceName);
		 }
	 }

	// 레코딩 폴더 확인
	public void checkVoiceFolder() {
    	File voiceFolder = new File(BasicInfo.FOLDER_VOICE);
		if(!voiceFolder.isDirectory()){
			Log.d(TAG, "creating voice folder : " + voiceFolder);

			voiceFolder.mkdirs();
		}
    }

    private void stopAudioPlayback() {
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");

        sendBroadcast(i);
    }

    class RemainingTimeCalculator {
        public static final int UNKNOWN_LIMIT = 0;
        public static final int FILE_SIZE_LIMIT = 1;
        public static final int DISK_SPACE_LIMIT = 2;

        //  어떤 제한을 먼저 할것인가
        private int mCurrentLowerLimit = UNKNOWN_LIMIT;

        private File mSDCardDirectory;

        //  레코딩하는 트레킹 파일 크기 상태
        private File mRecordingFile;
        private long mMaxBytes;

        // 어떤 파일을 큰지 비교
        private int mBytesPerSecond;

        // 어떤 프리 블럭이 나중에 봐뀌는지 시간
        private long mBlocksChangedTime;

        // 그 시간에 가능한 블럭 수
        private long mLastBlocks;

        //  어떤 사이즈의 파일이 나중에 변하는지 시간
        private long mFileSizeChangedTime;

        // 그 시간에 파일의 크기
        private long mLastFileSize;

        public RemainingTimeCalculator() {
            mSDCardDirectory = Environment.getExternalStorageDirectory();
        }

        // 불려왔을때, 계산기는 최소 두개의 상호아을 보여줌: 디스크 공간을 얼마나 사용하는지, 그 크기에 도달하는 시간이 얼마인지
        public void setFileSizeLimit(File file, long maxBytes) {
            mRecordingFile = file;
            mMaxBytes = maxBytes;
        }

        // 인터폴레이션 리셋
        public void reset() {
            mCurrentLowerLimit = UNKNOWN_LIMIT;
            mBlocksChangedTime = -1;
            mFileSizeChangedTime = -1;
        }

        // 얼마나(초 단위) 레코딩을 유지할수있는지 보여줌
        public long timeRemaining() {

            StatFs fs = new StatFs(mSDCardDirectory.getAbsolutePath());
            long blocks = fs.getAvailableBlocks();
            long blockSize = fs.getBlockSize();
            long now = System.currentTimeMillis();

            if (mBlocksChangedTime == -1 || blocks != mLastBlocks) {
                mBlocksChangedTime = now;
                mLastBlocks = blocks;
            }
            //  계산은 언제나 하나의 프리 블럭을 남기고, 남는 공간에 있는 블럭은 현재 작성하고있는건 추가하지않는다.
            //  이 마지막 블럭은 조금씩 사용한 우리가 파일을 닫고 플러쉬할때, 하지만 디스크에선 실행하지 않는다

            // mBlocksChangedTime에 이정도 시간이 있다
            long result = mLastBlocks*blockSize/mBytesPerSecond;

            result -= (now - mBlocksChangedTime)/1000;

            if (mRecordingFile == null) {
                mCurrentLowerLimit = DISK_SPACE_LIMIT;
                return result;
            }

            // 레코딩 파일이 셋 되면 계산기는 다음 mMaxBytes에 얼마나 걸리는지 기반으로 추산함
            mRecordingFile = new File(mRecordingFile.getAbsolutePath());
            long fileSize = mRecordingFile.length();
            if (mFileSizeChangedTime == -1 || fileSize != mLastFileSize) {
                mFileSizeChangedTime = now;
                mLastFileSize = fileSize;
            };

            long result2 = (mMaxBytes - fileSize)/mBytesPerSecond;
            result2 -= (now - mFileSizeChangedTime)/1000;
            result2 -= 1;

            mCurrentLowerLimit = result < result2
                ? DISK_SPACE_LIMIT : FILE_SIZE_LIMIT;

            return Math.min(result, result2);
        }

        //  FILE_SIZE_LIMIT,DISK_SPACE_LIMIT,UNKNOWN_LIMIT 중에 어떤 제한 먼저 발생하는지 나타냄
        //  이중 한개의 제한에 도달했을때 유저에게 올바른 메세지를 표현한다
        public int currentLowerLimit() {
            return mCurrentLowerLimit;
        }

        // 레코딩을 시작하는거를 도전하는 포인트가 있나
        public boolean diskSpaceAvailable() {
            StatFs fs = new StatFs(mSDCardDirectory.getAbsolutePath());
            // 한개의 프리 블럭을 유지
            return fs.getAvailableBlocks() > 1;
        }

        //  인터폴레이션에 있는 bit rate를 셋
        public void setBitRate(int bitRate) {
            mBytesPerSecond = bitRate/8;
        }
    }
}
