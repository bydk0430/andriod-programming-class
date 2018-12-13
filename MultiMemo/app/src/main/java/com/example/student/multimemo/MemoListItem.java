package com.example.student.multimemo;


public class MemoListItem {


	// 데디터 정렬
	private String[] mData;

	//	아이템 ID
	private String mId;

	//	아이템이 선택가능하면 true
	private boolean mSelectable = true;

		//Initialize with icon and data array 아이콘과 데디터 정렬 초기설정
	public MemoListItem(String itemId, String[] obj) {
		mId = itemId;
		mData = obj;
	}

		//	스트링관련 초기설정
	public MemoListItem(String memoId, String memoDate, String memoText,
			String id_handwriting, String uri_handwriting,
			String id_photo, String uri_photo,
			String id_video, String uri_video,
			String id_voice, String uri_voice
			)
	{
		mId = memoId;
		mData = new String[10];
		mData[0] = memoDate;
		mData[1] = memoText;
		mData[2] = id_handwriting;
		mData[3] = uri_handwriting;
		mData[4] = id_photo;
		mData[5] = uri_photo;
		mData[6] = id_video;
		mData[7] = uri_video;
		mData[8] = id_voice;
		mData[9] = uri_voice;
	}

	// 아이템이 선택가능하면 ture
	public boolean isSelectable() {
		return mSelectable;
	}

	// 선택가능 플레그 셋
	public void setSelectable(boolean selectable) {
		mSelectable = selectable;
	}

	public String getId() {
		return mId;
	}

	public void setId(String itemId) {
		mId = itemId;
	}


	//	데이터 정렬 겟
	public String[] getData() {
		return mData;
	}

	// 데이터 겟
	public String getData(int index) {
		if (mData == null || index >= mData.length) {
			return null;
		}

		return mData[index];
	}

	//  데이터 정렬 셋
	public void setData(String[] obj) {
		mData = obj;
	}


	// 입력 객체와 비교
	public int compareTo(MemoListItem other) {
		if (mData != null) {
			Object[] otherData = other.getData();
			if (mData.length == otherData.length) {
				for (int i = 0; i < mData.length; i++) {
					if (!mData[i].equals(otherData[i])) {
						return -1;
					}
				}
			} else {
				return -1;
			}
		} else {
			throw new IllegalArgumentException();
		}

		return 0;
	}

}
