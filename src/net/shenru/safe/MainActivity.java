package net.shenru.safe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	GridView gv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gv = new GridView(this);
		gv.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		gv.setNumColumns(2);
		gv.setAdapter(new MainAdapter());

		setContentView(gv);

		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					//手机优化 
					Intent oit = new Intent(getContext(), OptimaActivity.class);
					startActivity(oit);
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					break;
				case 7:
					break;
				case 8:
					break;
				}
			}
		});
	}

	private class Module {

		public String name;
		public int img;

		public Module(String name, int img) {
			super();
			this.name = name;
			this.img = img;
		}

	}

	private class MainAdapter extends BaseAdapter {

		private List<Module> lists;

		public MainAdapter() {
			super();
			Module m1 = new Module("手机优化", R.drawable.ic_launcher);
			Module m2 = new Module("恶意拦截", R.drawable.ic_launcher);
			Module m3 = new Module("手机清理", R.drawable.ic_launcher);
			Module m4 = new Module("手机防盗", R.drawable.ic_launcher);
			Module m5 = new Module("手机杀毒", R.drawable.ic_launcher);
			Module m6 = new Module("节电管理", R.drawable.ic_launcher);
			Module m7 = new Module("软件管家", R.drawable.ic_launcher);
			Module m8 = new Module("安全市场", R.drawable.ic_launcher);

			lists = new ArrayList<MainActivity.Module>();
			lists.add(m1);
			lists.add(m2);
			lists.add(m3);
			lists.add(m4);
			lists.add(m5);
			lists.add(m6);
			lists.add(m7);
			lists.add(m8);

		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Module module = lists.get(position);

			LinearLayout ll = new LinearLayout(getContext());
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.setGravity(Gravity.CENTER_HORIZONTAL);

			ImageView iv = new ImageView(getContext());
			TextView tv = new TextView(getContext());

			iv.setImageResource(module.img);
			tv.setText(module.name);

			ll.addView(iv, new LinearLayout.LayoutParams(-2, -2));
			ll.addView(tv, new LinearLayout.LayoutParams(-2, -2));

			Display display = getContext().getWindow().getWindowManager().getDefaultDisplay();
			ll.setLayoutParams(new AbsListView.LayoutParams(display.getWidth() / 3, display.getWidth() / 5));
			return ll;
		}

	}

	private MainActivity getContext() {
		return this;
	}
}
