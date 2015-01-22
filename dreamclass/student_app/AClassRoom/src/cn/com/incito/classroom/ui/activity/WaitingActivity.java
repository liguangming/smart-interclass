package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.GroupNumAdapter;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.ui.widget.MyAlertDialog;
import cn.com.incito.classroom.ui.widget.ProgressiveDialog;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.classroom.vo.LoginReqVo;
import cn.com.incito.classroom.vo.LoginRes2Vo;
import cn.com.incito.classroom.vo.LoginResVo;
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.core.NCoreSocket;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 用户登录等待界面 Created by popoy on 2014/7/28.
 */

public class WaitingActivity extends BaseActivity {
	public static final int STUDENT_LIST = 1;
	public static final int STUDENT_LOGIN = 2;
	public static final int STUDENT_CLEAR = 3;
	public static final int RANDOM_GROUP = 4;
	public static final int STUDENT_OFFLINE = 6;
	
	public int itemPosition;
	EditText et_stname;
	EditText et_stnumber;
	ImageButton btn_join;
	RadioGroup gender_group;
	RadioButton female;
	RadioButton male;
	GridView gv_group_member;
	LinearLayout llayout1;
	LinearLayout llayout;
	List<LoginRes2Vo> loginResList;
	GroupNumAdapter mAdapter;
	InputMethodManager imm;
	private ProgressiveDialog mProgressDialog;
	protected long mExitTime;
	/**
	 * 0只显示增加按钮，1显示姓名2显示姓名、学号、性别
	 */
	private int addState = 0;
	private RelativeLayout join_relative;
	private TextView join_text_notice;

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setContentView(R.layout.waiting);
		UIHelper.getInstance().setWaitingActivity(this);
		mProgressDialog = new ProgressiveDialog(this);
		mProgressDialog.setMessage(R.string.load_dialog_default_text);
		initViews();
		initListener();
		loginResList = new ArrayList<LoginRes2Vo>();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mAdapter = new GroupNumAdapter(WaitingActivity.this);
		et_stnumber.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		getGroupUserList();
	}

	private void initViews() {
		et_stnumber = (EditText) findViewById(R.id.et_stnumber);
		btn_join = (ImageButton) findViewById(R.id.btn_join);
		gender_group = (RadioGroup) findViewById(R.id.gender_group);
		female = (RadioButton) findViewById(R.id.female);
		male = (RadioButton) findViewById(R.id.male);
		gv_group_member = (GridView) findViewById(R.id.gv_group_member);
		llayout1 = (LinearLayout) findViewById(R.id.llayout1);
		llayout = (LinearLayout) findViewById(R.id.llayout);
		et_stname = (EditText) findViewById(R.id.et_stname);
		join_relative = (RelativeLayout) findViewById(R.id.join_relativelayout);
		join_text_notice = (TextView) findViewById(R.id.text_notice);
		join_relative.setPaddingRelative(200, 0, 0, 0);

	}

	private void initListener() {
		btn_join.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (addState == 0) {
					llayout1.setAnimation(AnimationUtils.loadAnimation(
							WaitingActivity.this, R.anim.push_bottom_in));
					gender_group.clearCheck();
					llayout1.setVisibility(View.VISIBLE);
					join_text_notice.setVisibility(View.GONE);
					join_relative.setPaddingRelative(0, 0, 0, 0);
					addState = 1;
				} else {
					if (validate()) {
						addState = 0;
						LoginRes2Vo groupNumberListRes = new LoginRes2Vo();
						groupNumberListRes.setSex(male.isChecked() ? "1" : "2");
						groupNumberListRes.setName(et_stname.getText()
								.toString());
						groupNumberListRes.setNumber(et_stnumber.getText()
								.toString());
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘

						registerStudent();
						mProgressDialog
								.setMessage(R.string.load_dialog_default_text);
						mProgressDialog.show();
						et_stnumber.setText("");
						et_stname.setText("");
						llayout1.setVisibility(View.GONE);
						join_text_notice.setVisibility(View.VISIBLE);
						join_relative.setPaddingRelative(200, 0, 0, 0);
					}

				}

			}
		});
		gv_group_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,View view, int position, long l) {
						itemPosition = position;

						if(NCoreSocket.getInstance().getChannel() != null){
							if(!mProgressDialog.isShowing()){
								mProgressDialog.show();
							}
							if (loginResList.get(position).isLogin() == false) {
								login(loginResList.get(position).getName(),
										loginResList.get(position).getNumber(),
										loginResList.get(position).getSex());
							} else {
								logout(loginResList.get(position).getName(),
										loginResList.get(position).getNumber(),
										loginResList.get(position).getSex());
							}
						}else{
							ToastHelper.showCustomToast(AppManager.getAppManager().currentActivity(), "还没有连接至服务器,请30s后重新操作");
						}
					}
				});
	}
	

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onBackPressed() {
		MyAlertDialog dialog = new MyAlertDialog(this);
		dialog.show();
	}

	/**
	 * 与后台服务建立连接，并实现登陆
	 *
	 * @param name
	 * @param number
	 * @param sex
	 */
	private void login(String name, String number, String sex) {
		LoginReqVo loginReqVo = new LoginReqVo();
		loginReqVo.setImei(MyApplication.deviceId);
		loginReqVo.setName(name);
		loginReqVo.setNumber(number);
		loginReqVo.setSex(sex);
		loginReqVo.setType("0");
		String json = JSON.toJSONString(loginReqVo);
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
		messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(json));
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime()+ ":WaitingActivity学生登录:");
		NCoreSocket.getInstance().sendMessage(messagePacking);
	}

	/**
	 * 取消登陆
	 *
	 * @param name
	 * @param number
	 * @param sex
	 */
	private void logout(String name, String number, String sex) {
		LoginReqVo loginReqVo = new LoginReqVo();
		loginReqVo.setImei(MyApplication.deviceId);
		loginReqVo.setName(name);
		loginReqVo.setNumber(number);
		loginReqVo.setSex(sex);
		loginReqVo.setType("1");
		String json = JSON.toJSONString(loginReqVo);
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
		messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(json));
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() +":WaitingActivity:学生退出");
		NCoreSocket.getInstance().sendMessage(messagePacking);
	}

	/**
	 * 检查新增的学生是否为重复录入(客户端检查)
	 */
	private boolean validate() {
		String stName = et_stname.getText().toString();
		String stNumber = et_stnumber.getText().toString();
		if (TextUtils.isEmpty(stName)) {
			ToastHelper.showCustomToast(getApplicationContext(),R.string.toast_stname_notnull);
			return false;
		}
		if(stName.contains(" ")){
			ToastHelper.showCustomToast(getApplicationContext(),R.string.toast_not_space);
			return false;
		}else if (stName.length() < 2) {
			ToastHelper.showCustomToast(getApplicationContext(),R.string.toast_stname_tooshort);
			return false;
		}
		if (TextUtils.isEmpty(stNumber)) {
			ToastHelper.showCustomToast(getApplicationContext(),R.string.toast_stnumber_notnull);
			return false;
		}
		if (loginResList.size() > Constants.STUDENT_MAX_NUM) {
			ToastHelper.showCustomToast(getApplicationContext(),R.string.toast_group_isfull);
			return false;
		}
		for (int i = 0; i < loginResList.size(); i++) {
			if (stNumber.equals(loginResList.get(i).getNumber())) {
				String msg = getResources().getString(
						R.string.toast_stname_repeat);
				ToastHelper.showCustomToast(getApplicationContext(),String.format(msg, loginResList.get(i).getNumber()));
				return false;
			}
		}
		if (!Utils.isNumberOrChinese(stName)) {
			ToastHelper.showCustomToast(getApplicationContext(),R.string.tost_name_is_not_english_chinese);
			return false;
		}

		// 判断是否已经选择男女
		if (!male.isChecked() && !female.isChecked()) {
			ToastHelper.showCustomToast(getApplicationContext(),R.string.toast_choose_sex);
			return false;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (addState == 1) {
			llayout1.setVisibility(View.GONE);
			join_text_notice.setVisibility(View.VISIBLE);
			join_relative.setPaddingRelative(200, 0, 0, 0);
			addState = 0;
			imm.hideSoftInputFromWindow(llayout.getWindowToken(), 0);
			return false;
		} else {
			return super.onTouchEvent(event);
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 登陆
			case STUDENT_LOGIN: {
				mProgressDialog.hide();
				JSONObject jsonObject = (JSONObject) msg.getData().getSerializable("data");
				MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":WaitingActivity:学生登录成功");
				if (!"0".equals(jsonObject.getString("code"))) {
					if ("-2".equals(jsonObject.getString("code"))) {
						String number = jsonObject.getString("data");
						ToastHelper.showCustomToast(WaitingActivity.this, "学号"+ number + "已注册");
					}
					return;
				} else if (jsonObject.getJSONObject("data") == null) {
				} else {
					LoginResVo loginResVo = JSON.parseObject(jsonObject.getJSONObject("data").toJSONString(),LoginResVo.class);
					if (loginResVo.getStudents() != null) {
						loginResList = loginResVo.getStudents();
					}
					((MyApplication) getApplication()).setLoginResVo(loginResVo);
					if (loginResList != null && loginResList.size() >= 0) {
						mAdapter.setDatas(loginResList);
						gv_group_member.setAdapter(mAdapter);
					}
					// addState = 0;
					gv_group_member.setSelection(itemPosition);
				}

				break;
			}
			// 获取分组
			case STUDENT_LIST: {
				mProgressDialog.hide();
				JSONObject jsonObject = (JSONObject) msg.getData().getSerializable("data");
				MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":WaitingActivity:得到所有学生信息");
				if (!"0".equals(jsonObject.getString("code"))) {
					return;
				} else if (jsonObject.getJSONObject("data") == null) {
				} else {
					LoginResVo loginResVo = JSON.parseObject(jsonObject.getJSONObject("data").toJSONString(),LoginResVo.class);
					if (loginResVo.getStudents() != null) {
						loginResList = loginResVo.getStudents();
					}
					((MyApplication) getApplication()).setLoginResVo(loginResVo);
					if (loginResList != null && loginResList.size() > 0) {
						mAdapter.setDatas(loginResList);
						gv_group_member.setAdapter(mAdapter);
					}
					llayout1.setVisibility(View.GONE);
				}
				break;
			}
			case STUDENT_CLEAR:
				if (loginResList != null) {
					for (LoginRes2Vo vo : loginResList) {
						vo.setLogin(false);
					}
					mAdapter.setDatas(loginResList);
				}
				break;
			case RANDOM_GROUP:
				loginResList = MyApplication.getInstance().getLoginResVo().getStudents();
				mAdapter.setDatas(MyApplication.getInstance().getLoginResVo().getStudents());
				gv_group_member.setAdapter(mAdapter);
				break;
			case STUDENT_OFFLINE:
				loginResList = MyApplication.getInstance().getLoginResVo().getStudents();
				mAdapter.setDatas(MyApplication.getInstance().getLoginResVo().getStudents());
				gv_group_member.setAdapter(mAdapter);
			}
			
		}
	};

	public void doResult(JSONObject jsonObject, int type) {
		android.os.Message message = new android.os.Message();
		message.what = type;
		Bundle data = new Bundle();
		data.putSerializable("data", jsonObject);
		message.setData(data);
		mHandler.sendMessage(message);
	}

	public void refreshStudents(JSONObject data) {

		// 马上刷新等待界面数据
		List<Student> studentList = JSONArray.parseArray(JSON.parseObject(data.toJSONString()).getString("students"),Student.class);
		MyApplication.Logger.debug("WaitingActivity:随机分组返回的数据:" + studentList.size());
		
		List<LoginRes2Vo> loginRes2Vos = new ArrayList<LoginRes2Vo>();
		
		if (studentList != null && studentList.size() > 0) {
			Iterator<Student> it = studentList.iterator();
			while (it.hasNext()) {
				Student s = it.next();
				
				LoginRes2Vo loginRes2Vo = new LoginRes2Vo();
				loginRes2Vo.setAvatar(s.getAvatar());
				loginRes2Vo.setId(s.getId() + "");
				loginRes2Vo.setLogin(s.isLogin());
				loginRes2Vo.setName(s.getName());
				loginRes2Vo.setNumber(s.getNumber());
				loginRes2Vo.setSex(s.getSex() + "");
				
				loginRes2Vos.add(loginRes2Vo);
			}
			MyApplication.getInstance().getLoginResVo().getStudents().clear();
			MyApplication.getInstance().getLoginResVo().setStudents(loginRes2Vos);
		}else{
			MyApplication.getInstance().getLoginResVo().getStudents().clear();
		}
		mHandler.sendEmptyMessage(RANDOM_GROUP);
	}

	public void clearStudent() {
		MyApplication.Logger.debug("WaitingActivity:随机分组返回的数据 clear学生调用");
		android.os.Message message = new android.os.Message();
		message.what = STUDENT_CLEAR;
		mHandler.sendMessage(message);
	}
	
	/**
	 * 注册成员
	 */
	private void registerStudent() {
		LoginReqVo loginReqVo = new LoginReqVo();
		loginReqVo.setImei(MyApplication.deviceId);
		loginReqVo.setName(et_stname.getText().toString());
		loginReqVo.setNumber(et_stnumber.getText().toString());
		loginReqVo.setSex(female.isChecked() ? "2" : "1");
		loginReqVo.setType("2");
		String json = JSON.toJSONString(loginReqVo);

		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
		messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(json));
		if(NCoreSocket.getInstance().getChannel() == null){
			ToastHelper.showCustomToast(this, "未连接至服务器,30s后重试!");
			return;
		}
		MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":WaitingActivity:启动注册学生");
		NCoreSocket.getInstance().sendMessage(messagePacking);
	}

	/**
	 * 获取组成员列表
	 */
	private void getGroupUserList() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.deviceId);
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_LIST);
		messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(jsonObject.toJSONString()));
		if(NCoreSocket.getInstance().getChannel() != null){
			MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + ":WaitingActivity启动获取组成员列表");
			NCoreSocket.getInstance().sendMessage(messagePacking);
		}
	}

	/**
	 * 改变本台pad所有学生的状态
	 */
	public void notifyStudentOffline() {
		List<LoginRes2Vo> students = MyApplication.getInstance().getLoginResVo().getStudents();
		if(students != null){
			for(LoginRes2Vo s : students){
				s.setLogin(false);
			}
		}
		mHandler.sendEmptyMessage(STUDENT_OFFLINE);
	}
}
