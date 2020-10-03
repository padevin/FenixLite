package com.yatechno.fenixlite;

import android.app.Activity;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import org.json.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.view.View;
import android.webkit.WebViewClient;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.DialogFragment;
import android.Manifest;
import android.content.pm.PackageManager;


public class MainActivity extends  Activity { 
	
	
	private LinearLayout linear1;
	private LinearLayout linear3;
	private LinearLayout araalan;
	private LinearLayout webalan;
	private TextView textview1;
	private LinearLayout linear6;
	private LinearLayout linear7;
	private LinearLayout linear8;
	private EditText edittext1;
	private ImageView imageview1;
	private LinearLayout linear10;
	private LinearLayout linear11;
	private ProgressBar prog;
	private WebView webview1;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
			|| checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
			}
			else {
				initializeLogic();
			}
		}
		else {
			initializeLogic();
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		araalan = (LinearLayout) findViewById(R.id.araalan);
		webalan = (LinearLayout) findViewById(R.id.webalan);
		textview1 = (TextView) findViewById(R.id.textview1);
		linear6 = (LinearLayout) findViewById(R.id.linear6);
		linear7 = (LinearLayout) findViewById(R.id.linear7);
		linear8 = (LinearLayout) findViewById(R.id.linear8);
		edittext1 = (EditText) findViewById(R.id.edittext1);
		imageview1 = (ImageView) findViewById(R.id.imageview1);
		linear10 = (LinearLayout) findViewById(R.id.linear10);
		linear11 = (LinearLayout) findViewById(R.id.linear11);
		prog = (ProgressBar) findViewById(R.id.prog);
		webview1 = (WebView) findViewById(R.id.webview1);
		webview1.getSettings().setJavaScriptEnabled(true);
		webview1.getSettings().setSupportZoom(true);
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (edittext1.getText().toString().contains(".")) {
					if (edittext1.getText().toString().contains("http://") || edittext1.getText().toString().contains("https://")) {
						webview1.loadUrl(edittext1.getText().toString());
					}
					else {
						webview1.loadUrl("http://".concat(edittext1.getText().toString()));
					}
					araalan.setVisibility(View.GONE);
					webalan.setVisibility(View.VISIBLE);
					edittext1.setText("");
				}
				else {
					webview1.loadUrl("https://www.google.com/search?q=".concat(edittext1.getText().toString()));
					araalan.setVisibility(View.GONE);
					webalan.setVisibility(View.VISIBLE);
					edittext1.setText("");
				}
				SketchwareUtil.hideKeyboard(getApplicationContext());
			}
		});
		
		webview1.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
				final String _url = _param2;
				linear10.setVisibility(View.VISIBLE);
				super.onPageStarted(_param1, _param2, _param3);
			}
			
			@Override
			public void onPageFinished(WebView _param1, String _param2) {
				final String _url = _param2;
				linear10.setVisibility(View.GONE);
				super.onPageFinished(_param1, _param2);
			}
		});
	}
	
	private void initializeLogic() {
		
		webview1.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				try {
					DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
					String cookies = CookieManager.getInstance().getCookie(url);
					request.addRequestHeader("cookie", cookies);
					request.addRequestHeader("User-Agent", userAgent);
					request.setDescription("Dosya indiriliyor...");
					request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
					request.allowScanningByMediaScanner(); request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
					
					java.io.File aatv = new java.io.File(Environment.getExternalStorageDirectory().getPath() + "/Download");
					if(!aatv.exists()){if (!aatv.mkdirs()){ Log.e("TravellerLog ::","Hata oluştu izinleri verin!");}}
					
					request.setDestinationInExternalPublicDir("/Download", URLUtil.guessFileName(url, contentDisposition, mimetype));
					
					DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
					manager.enqueue(request);
					showMessage("Dosya indiriliyor....");
					
					//Notif if success
					BroadcastReceiver onComplete = new BroadcastReceiver() {
						public void onReceive(Context ctxt, Intent intent) {
							showMessage("Indirme Tamamlandı!");
							unregisterReceiver(this);
						}};
					registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
				} catch (Exception e){
					showMessage(e.toString());
				}
			}
			
		});
		final android.widget.ProgressBar prog = new android.widget.ProgressBar(this,null, android.R.attr.progressBarStyleHorizontal);
		
		prog.setPadding(0,0,0,0);
		
		prog.setIndeterminate(false);
		
		prog.setFitsSystemWindows(true);
		
		prog.setProgress(0);
		
		prog.setScrollBarStyle(android.widget.ProgressBar.SCROLLBARS_OUTSIDE_INSET);
		
		prog.setMax(100);
		
		ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		prog.setLayoutParams(vlp);
		
		linear10.addView(prog);
		
		webview1.setWebChromeClient(new WebChromeClient() {
			
			@Override public void onProgressChanged(WebView view, int newProgress) {
				
				prog.setProgress(newProgress);
				
			}
			
		});
		
		
		
		webview1.getSettings().setBuiltInZoomControls(true);webview1.getSettings().setDisplayZoomControls(false);
		
		 webview1.getSettings().setJavaScriptEnabled(true);
		
		 webview1.getSettings().setAppCacheMaxSize(5*1024*1024); 
		webview1.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath()); 
		webview1.getSettings().setAllowFileAccess(true);
		webview1.getSettings().setAppCacheEnabled(true);
		webview1.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webview1.getSettings().setLoadWithOverviewMode(true);
		webview1.getSettings().setUseWideViewPort(true);
		webview1.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webview1.getSettings().setDomStorageEnabled(true);
		webview1.getSettings().setSaveFormData(true);
		
		WebSettings webSettings = webview1.getSettings(); 
		webSettings.setJavaScriptEnabled(true); 
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			webSettings.setAllowFileAccessFromFileURLs(true); 
			webSettings.setAllowUniversalAccessFromFileURLs(true);
		}
		
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		if (webview1.canGoBack()) {
			webview1.goBack();
		}
		else {
			webalan.setVisibility(View.GONE);
			araalan.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onPostCreate(Bundle _savedInstanceState) {
		super.onPostCreate(_savedInstanceState);
		_GradientDrawable(linear1, "#0542FF", "#009BFF");
		_setRadius(linear6, 35, "#2196f3");
		webalan.setVisibility(View.GONE);
		linear10.setVisibility(View.GONE);
		Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); w.setStatusBarColor(0xFF008375); w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}
	private void _GradientDrawable (final View _view, final String _color1, final String _color2) {
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
		int clr[] = new int[]{ Color.parseColor(_color1), Color.parseColor(_color2) };
		gd.setColors(clr);
		gd.setCornerRadius(0);
		_view.setBackground(gd);
	}
	
	
	private void _setRadius (final View _view, final double _radius, final String _color) {
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
		gd.setColor(Color.parseColor(_color));
		gd.setCornerRadius((int)_radius);
		_view.setBackground(gd);
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}
