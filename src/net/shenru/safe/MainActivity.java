package net.shenru.safe;

import java.util.List;

import android.app.ListActivity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//获取开机运行有两中方法1：系统解析  2: 自己解析
		//
		List<ApplicationInfo> fetchInstalledApps = BootStartUtils.fetchInstalledApps(this);
		Toast.makeText(this, fetchInstalledApps.size() + "", 1).show();
		getListView().setAdapter(new MyBaseAdapter(fetchInstalledApps));
	}

	private class MyBaseAdapter extends BaseAdapter{

		private List<ApplicationInfo> lists;
		public MyBaseAdapter(List<ApplicationInfo> fetchInstalledApps) {
			lists = fetchInstalledApps;
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
			ApplicationInfo app = lists.get(position);
			PackageManager pm = MainActivity.this.getPackageManager();
			View view = View.inflate(MainActivity.this, R.layout.app_boot_item, null);
			ImageView iv = (ImageView) view.findViewById(R.id.icon);
			TextView nameView = (TextView) view.findViewById(R.id.name);
			CheckBox cb = (CheckBox) view.findViewById(R.id.sele);
			
			Drawable applicationIcon = pm.getApplicationIcon(app);
			CharSequence applicationLabel = pm.getApplicationLabel(app);
			iv.setImageDrawable(applicationIcon);
			nameView.setText(applicationLabel);
			return view;
		}
		
	}

}
