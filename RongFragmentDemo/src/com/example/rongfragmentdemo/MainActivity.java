package com.example.rongfragmentdemo;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.UserInfoProvider;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ConnectionStatusListener;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

/**
 * 
 * @author AM  模拟了两个用户实现用户信息提供者 建议大家自己拿到例子后重新 配置 appkey 和 token 不然很多开发者用一个 appkey 和 token 会经常被顶号 
 * 因为是主要做 会话列表 会话界面 和 用户头像的 示例 为了避免代码 过于冗余 这边没有对 群组(聚合等) 位置消息 和 语音通话 做实现 如果有报错 请参考官网 Demo 实现
 *
 */
public class MainActivity extends Activity implements OnClickListener, UserInfoProvider {

	protected static final String TAG = "MainActivity";

	private static final String token1 = "4rnspHMw6ruF/ha//z5/YbDS8NWRd4boTj2Vy4QL3GdXZhpbxVBu95Rcuww/pJdcKLu+G5cq0LCM1uI9uTLY0A==";
	// username = 联通 userid = 10010 头像 =
	// http://img5.imgtn.bdimg.com/it/u=3461135921,2635786312&fm=21&gp=0.jpg

	private static final String token2 = "bugmIZWR5JGzPHNoNp47EEGFC6hW/OOiwJwgWU0oTvPK1cxi0MjsRwRj4jyW+UFZDt0alvCqoDsBSVHlDVJA1g==";
	// username = 移动 userid = 10086 头像 =
	// http://img02.tooopen.com/Download/2010/5/22/20100522103223994012.jpg

	private Button mUser1, mUser2, mLoadFragment1, mLoadFragment2,mChat,mCustomerService,mListener;
	
	private String mUserId;
	
	private List<Friend> userIdList;

	protected boolean temp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init();
		userIdList = new ArrayList<Friend>();
		userIdList.add(new Friend("10010","联通","http://img4.imgtn.bdimg.com/it/u=2047908622,1571760504&fm=21&gp=0.jpg"));
		userIdList.add(new Friend("10086","移动","http://img02.tooopen.com/Download/2010/5/22/20100522103223994012.jpg"));
		
		//此处把客服头像 和 昵称 设置了 
		userIdList.add(new Friend("KEFU1426079728044", "客服服务", "http://www.jf258.com/uploads/2013-07-11/135409968.jpg"));
		RongIM.setUserInfoProvider(this, false);
	}

	private void init() {
		mUser1 = (Button) findViewById(R.id.connect_10010);
		mUser2 = (Button) findViewById(R.id.connect_10086);
		mLoadFragment1 = (Button) findViewById(R.id.load1);
		mLoadFragment2 = (Button) findViewById(R.id.load2);
		mChat = (Button) findViewById(R.id.chat);
		mCustomerService = (Button) findViewById(R.id.customerservice);
		mListener = (Button) findViewById(R.id.listener);
		mUser1.setOnClickListener(this);
		mUser2.setOnClickListener(this);
		mLoadFragment1.setOnClickListener(this);
		mLoadFragment2.setOnClickListener(this);
		mChat.setOnClickListener(this);
		mCustomerService.setOnClickListener(this);
		mListener.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.connect_10010) {
			connectRongServer(token1);
		} else if (v.getId() == R.id.connect_10086) {
			connectRongServer(token2);
		} else if (v.getId() == R.id.load1) {
			startActivity(new Intent(MainActivity.this,
					LoadConversationListFragment1.class));
		} else if(v.getId() == R.id.load2){
			startActivity(new Intent(MainActivity.this,
					LoadConversationListFragment2.class));
		}else if(v.getId() == R.id.chat){
			if(mUserId!=null&&RongIM.getInstance()!=null)
				//此处聊天是写死的 实际开发中 大家应该写成动态的 
			RongIM.getInstance().startPrivateChat(MainActivity.this,mUserId.equals("10010") ? "10086" : "10010" , mUserId.equals("10010") ? "联通" : "移动");
		}else if(v.getId() == R.id.customerservice){
			//客服 id 是您在融云开发者平台 客服 功能模块的获取
			if(RongIM.getInstance() != null)
			RongIM.getInstance().startCustomerServiceChat(MainActivity.this, "KEFU1426079728044", "客服服务");
		}else if(v.getId() == R.id.listener){
			if (temp) {
				Log.e(TAG, "回调已走");
				RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new ConnectionStatusListener() {
					
					@Override
					public void onChanged(ConnectionStatus arg0) {
						Log.e(TAG, "网络状态已经变化");
					}
				});
			}
		}
	}

	/**
	 * 连接融云服务器
	 * 
	 * @param token
	 */
	private void connectRongServer(String token) {
		RongIM.connect(token, new ConnectCallback() {

			@Override
			public void onSuccess(String userId) {
				mUserId = userId;
				if (userId.equals("10010")) {
					mUser1.setText("用户1连接服务器成功");
					mUser2.setClickable(false);
					mUser2.setTextColor(Color.GRAY);
					Toast.makeText(MainActivity.this, "connet server success",
							Toast.LENGTH_SHORT).show();
					;
				} else {
					mUser2.setText("用户2连接服务器成功");
					mUser1.setClickable(false);
					mUser1.setTextColor(Color.GRAY);
					Toast.makeText(MainActivity.this, "connet server success",
							Toast.LENGTH_SHORT).show();
					;
				}
				Log.e(TAG, "connect success userid is :" + userId);
				temp = true;
			}

			@Override
			public void onError(ErrorCode errorCode) {
				Log.e(TAG,
						"connect failure errorCode is :" + errorCode.getValue());
			}

			@Override
			public void onTokenIncorrect() {
				Log.e(TAG, "token is error , please check token and appkey ");
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		RongIM.getInstance().logout();
		mUser1 = null;
		mUser2 = null;
	}

	private Long firstClickTime = (long) 0;

	/**
	 * 当用户点击返回键的时候
	 */
	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - firstClickTime <= 2000) {
			super.onBackPressed();
			return;
		}
		firstClickTime = System.currentTimeMillis();
		Toast.makeText(getApplicationContext(), "再按一次返回键退出应用",
				Toast.LENGTH_SHORT).show();
		return;
	}

	@Override
	public UserInfo getUserInfo(String userId) {
		for (Friend i : userIdList) {
			if (i.getUserId().equals(userId)) {
				Log.e(TAG, i.getPortraitUri());
				return new UserInfo(i.getUserId(),i.getUserName(),Uri.parse(i.getPortraitUri()));
			}
		}
		return null;
	}
}
