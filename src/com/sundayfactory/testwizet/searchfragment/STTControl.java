package com.sundayfactory.testwizet.searchfragment;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

public class STTControl {
	private Context mContext;
	private SpeechRecognizer mRecognizer;
	private ArrayList<String> mResult;	
	private STTListiner mListiner;
	public boolean isStart = false;
	public interface STTListiner{
		public void onStart();
		public void onReadly();
		public void onResult(ArrayList<String> mResult);
		public void onEnd();
	}
	private RecognitionListener mRecognitionListener = new RecognitionListener() {
		
		@Override
		public void onRmsChanged(float rmsdB) {
			// TODO Auto-generated method stub
			isStart = true;
		}
		
		@Override
		public void onResults(Bundle results) {
			mResult = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
			
			mListiner.onResult(mResult);
		
		
		}
		
		@Override
		public void onReadyForSpeech(Bundle params) {
			Log.i("park", "onReadyForSpeech");
			isStart = true;
			mListiner.onReadly();
			Toast.makeText(mContext, "Ready for Speech", Toast.LENGTH_SHORT).show();
			
		}
		
		@Override
		public void onPartialResults(Bundle partialResults) {
			// TODO Auto-generated method stub
			Log.i("park", "onPartialResults");

		}
		
		@Override
		public void onEvent(int eventType, Bundle params) {
			Log.i("park", "onEvent");
		}
		
		@Override
		public void onError(int error) {
			mListiner.onEnd();
			String msg = "[" + error+"]";
			switch(error){
			case SpeechRecognizer.ERROR_AUDIO:
				msg = "오디오 입력 중 오류가 발생했습니다.";
				break;
			case SpeechRecognizer.ERROR_CLIENT:
				msg = "단말에서 오류가 발생했습니다.";
				break;
			case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
				msg = "권한이 없습니다.";
				break;
			case SpeechRecognizer.ERROR_NETWORK:
			case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
				msg = "네트워크 오류가 발생했습니다.";
				break;
			case SpeechRecognizer.ERROR_NO_MATCH:
				msg = "일치하는 항목이 없습니다.";
				break;
			case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
				msg = "음성인식 서비스가 과부하 되었습니다.";
				break;
			case SpeechRecognizer.ERROR_SERVER:
				msg = "서버에서 오류가 발생했습니다.";
				break;
			case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
				msg = "입력이 없습니다.";
				break;
				
		}
		isStart = false;
		if(msg != null)		//오류 메시지가 null이 아니면 메시지 출력
			Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
			
		}
		
		@Override
		public void onEndOfSpeech() {
			Log.i("park", "onEndOfSpeech");
			endSTT();
			mListiner.onEnd();
		}
		
		@Override
		public void onBufferReceived(byte[] buffer) {
			// TODO Auto-generated method stub
			Log.i("park", "onBufferReceived");
		}
		
		@Override
		public void onBeginningOfSpeech() {
			Log.i("park", "onBeginningOfSpeech");
		}
	};
	
	public STTControl(Context mContext , STTListiner mListiner) {
		this.mContext = mContext;
		this.mListiner = mListiner;
	}
	public void startSTT(){
		Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);            //음성인식 intent생성
		i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mContext.getPackageName());    //데이터 설정
		//i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());                            //음성인식 언어 설정
		mRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);                //음성인식 객체
		mRecognizer.setRecognitionListener(mRecognitionListener);                                        //음성인식 리스너 등록
		mRecognizer.startListening(i);
		mListiner.onStart();
	}
	public void endSTT(){
		mRecognizer.stopListening();
		isStart = false;
	}
	
}
