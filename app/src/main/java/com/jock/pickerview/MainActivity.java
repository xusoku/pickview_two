package com.jock.pickerview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jock.pickerview.dao.RegionDAO;
import com.jock.pickerview.view.OptionsPickerView;
import com.jock.pickerview.view.TimePickerView;
import com.jock.pickerview.view.TwoPickerView;

public class MainActivity extends Activity
{

	private TextView tvTime, tvOptions,tvTimae;
	TimePickerView pvTime;
	OptionsPickerView pvOptions;
	TwoPickerView tpvOptions;

	static ArrayList<RegionInfo> item1;

	static ArrayList<ArrayList<RegionInfo>> item2 = new ArrayList<ArrayList<RegionInfo>>();

	static ArrayList<ArrayList<ArrayList<RegionInfo>>> item3 = new ArrayList<ArrayList<ArrayList<RegionInfo>>>();

	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			System.out.println(System.currentTimeMillis());
			// 三级联动效果

			pvOptions.setPicker(item1, item2, item3, true);
			pvOptions.setCyclic(true, true, true);
			pvOptions.setSelectOptions(0, 0, 0);
			tvOptions.setClickable(true);
		};
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvTime = (TextView) findViewById(R.id.tvTime);
		tvTimae = (TextView) findViewById(R.id.tvTimae);
		tvOptions = (TextView) findViewById(R.id.tvOptions);


		// 时间选择器
		pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
		// 控制时间范围
//		 Calendar calendar = Calendar.getInstance();
//		 pvTime.setRange(calendar.get(Calendar.YEAR) - 20,
//		 calendar.get(Calendar.YEAR));
		pvTime.setTime(new Date());
		pvTime.setCyclic(false);
		pvTime.setCancelable(true);
		// 时间选择后回调
		pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				tvTime.setText(getTime(date));
			}
		});
		// 弹出时间选择器
		tvTime.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				pvTime.show();
			}
		});

		// 选项选择器
		pvOptions = new OptionsPickerView(this);

		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				System.out.println(System.currentTimeMillis());
				if (item1 != null && item2 != null && item3 != null)
				{
					handler.sendEmptyMessage(0x123);
					return;
				}
				item1 = (ArrayList<RegionInfo>) RegionDAO.getProvencesOrCity(1);
				for (RegionInfo regionInfo : item1)
				{
					item2.add((ArrayList<RegionInfo>) RegionDAO.getProvencesOrCityOnParent(regionInfo.getId()));

				}

				for (ArrayList<RegionInfo> arrayList : item2)
				{
					ArrayList<ArrayList<RegionInfo>> list2 = new ArrayList<ArrayList<RegionInfo>>();
					for (RegionInfo regionInfo : arrayList)
					{

						

						ArrayList<RegionInfo> q = (ArrayList<RegionInfo>) RegionDAO.getProvencesOrCityOnParent(regionInfo.getId());
						list2.add(q);

					}
					item3.add(list2);
				}

				handler.sendEmptyMessage(0x123);

			}
		}).start();
		// 设置选择的三级单位
		// pwOptions.setLabels("省", "市", "区");
		pvOptions.setTitle("选择城市");

		// 设置默认选中的三级项目
		// 监听确定选择按钮

		pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				// 返回的分别是三个级别的选中位置
				String tx = item1.get(options1).getPickerViewText() + item2.get(options1).get(option2).getPickerViewText() + item3.get(options1).get(option2).get(options3).getPickerViewText();
				tvOptions.setText(tx);

			}
		});
		// 点击弹出选项选择器
		tvOptions.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pvOptions.show();
			}
		});

		tvOptions.setClickable(false);


		tvTimae.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tpvOptions.show();
			}
		});


		// 选项选择器
		tpvOptions = new TwoPickerView(this);

		// pwOptions.setLabels("省", "市", "区");
//		tpvOptions.setTitle("选择城市");


		final ArrayList<String> list=new ArrayList<String>();
		list.add("11");
		list.add("22");
		list.add("33");
		list.add("44");


		final ArrayList<ArrayList<String>> arrayLists=new ArrayList<ArrayList<String>>();
		ArrayList<String> a=new ArrayList<String>();
		a.add("111");
		a.add("112");
		a.add("113");
		a.add("114");
		arrayLists.add(a);
		ArrayList<String> b=new ArrayList<String>();
		b.add("221");
		b.add("222");
		b.add("223");
		b.add("224");
		arrayLists.add(b);
		ArrayList<String> c=new ArrayList<String>();
		c.add("331");
		c.add("332");
		c.add("333");
		c.add("334");
		arrayLists.add(c);
		ArrayList<String> d=new ArrayList<String>();
		d.add("441");
//		d.add("442");
		d.add("443");
		d.add("444");
		arrayLists.add(d);

		tpvOptions.setPicker(list, arrayLists, false);
		tpvOptions.setCyclic(false);
		tpvOptions.setSelectOptions(0, 0);
		tpvOptions.setOnoptionsSelectListener(new TwoPickerView.OnOptionsSelectListener() {
			@Override
			public void onOptionsSelect(int options1, int option2) {

				tvTimae.setText(list.get(options1)+"  "+arrayLists.get(options1).get(option2));
			}
		});


	}

	public static String getTime(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}

}
