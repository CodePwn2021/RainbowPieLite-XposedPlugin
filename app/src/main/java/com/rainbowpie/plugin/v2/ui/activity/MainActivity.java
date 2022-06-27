package com.rainbowpie.plugin.v2.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.rainbowpie.plugin.v2.ui.FlexibleScrollView;
import com.rainbowpie.plugin.v2.Utils;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity { 

	private boolean isModuleActivate() {
		return false;
	}

	public void openBrowser(String uri) {
        Uri uris = Uri.parse(uri);
        Intent intent = new Intent(Intent.ACTION_VIEW, uris);
        startActivity(intent);
    }

	private void ClickVersion(View view) {
		new AlertDialog.Builder(MainActivity.this)
			.setTitle("加入群组")
			.setMessage("请选择你要加入的群组")
			.setNegativeButton("QQ", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(MainActivity.this, "正在跳转到QQ", Toast.LENGTH_SHORT).show();
					openBrowser("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D6xoG0L2wJIz_jDE1UVx6ylSF5oLf2w1q");
				}
			})
			.setPositiveButton("Telegram", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(MainActivity.this, "正在跳转到浏览器", Toast.LENGTH_SHORT).show();
					openBrowser("https://t.me/rainbowpielite");
				}
			})
			.setNeutralButton("取消", null)
			.create().show();
	}

	private void ClickDonate(View view) {
		new AlertDialog.Builder(MainActivity.this)
			.setTitle("捐赠")
			.setMessage("确定要跳转到浏览器？")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(MainActivity.this, "正在跳转到浏览器", Toast.LENGTH_SHORT).show();
					openBrowser("https://wtf.wtf.cn/donate.html");
				}
			})
			.setNegativeButton("取消", null)
			.create().show();
	}

	private void ClickHelp(View view) {
		new AlertDialog.Builder(MainActivity.this)
			.setTitle("帮助")
			.setMessage("帮助？没有帮助。\n提示：如果你选择手动下载RN更新包，请解压到RainbowPieFiles文件夹里的ReactNativeFiles文件夹，一定要覆盖掉")
			.setPositiveButton("确定", null)
			.create().show();
	}

	private void ClickAbout(View view) {
		new AlertDialog.Builder(MainActivity.this)
			.setTitle("关于模块")
			.setMessage("Hook：*DELETED*\nRN：Mintraspberry\n模块UI：雪鹅鹅\n特别提醒：严禁以任何形式贩卖、商用本模块，否则开发者有权追究法律责任。")
			.setPositiveButton("确定", null)
			.create().show();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		try {
			String moduleVer = Utils.moduleVersionName;
			String moduleExpire = Utils.expireTimeString;
			GradientDrawable inRed=new GradientDrawable();
			inRed.setColor(0xffFF6A6A);
			inRed.setCornerRadius(20f);
			GradientDrawable inGreen=new GradientDrawable();
			inGreen.setColor(0xff00B170);
			inGreen.setCornerRadius(20f);
			GradientDrawable inNormal=new GradientDrawable();
			inNormal.setColor(0xfff0f0f0);
			inNormal.setCornerRadius(20f);
			DisplayMetrics displayMetrics=new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			int widthPixels=displayMetrics.widthPixels;//屏幕宽度
			int heightPixels=displayMetrics.heightPixels;//屏幕高度
			FlexibleScrollView sv=new FlexibleScrollView(getApplicationContext());
			sv.setVerticalScrollBarEnabled(false);
			LinearLayout background=new LinearLayout(getApplicationContext());
			background.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, heightPixels));
			background.setGravity(Gravity.CENTER | Gravity.TOP);
			// background.setBackgroundColor(0xffffffff);
			background.setOrientation(1);
			LinearLayout bn1=new LinearLayout(getApplicationContext());
			bn1.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, new Double(heightPixels * 0.12).intValue()));
			bn1.setGravity(Gravity.CENTER);
			LinearLayout bl1=new LinearLayout(getApplicationContext());
			bl1.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.95).intValue(), new Double(heightPixels * 0.11).intValue()));
			LinearLayout leftImageLayout1=new LinearLayout(getApplicationContext());
			leftImageLayout1.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.234).intValue(), new Double(heightPixels * 0.11).intValue()));
			leftImageLayout1.setGravity(Gravity.CENTER);
			ImageView img1=new ImageView(getApplicationContext());
			img1.setImageBitmap(getImageFromAssetsFile(getApplicationContext(), "ic_extension.png"));
			leftImageLayout1.addView(img1);
			img1.setColorFilter(0xffFFFFFF);
			LinearLayout rightTextLayout1=new LinearLayout(getApplicationContext());
			rightTextLayout1.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.65).intValue(), new Double(heightPixels * 0.11).intValue()));
			rightTextLayout1.setGravity(Gravity.CENTER | Gravity.LEFT);
			rightTextLayout1.setOrientation(1);
			TextView moduleLoad=new TextView(getApplicationContext());
			moduleLoad.setTextColor(0xffFFFFFF);
			moduleLoad.setTextSize(15f);
			TextView moduleMethod=new TextView(getApplicationContext());
			moduleMethod.setTextColor(0xffe0e0e0);
			moduleMethod.setText("支持无极/应用转生/LSPosed等框架");
			moduleMethod.setTextSize(10f);

			LinearLayout bn2=new LinearLayout(getApplicationContext());
			bn2.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, new Double(heightPixels * 0.12).intValue()));
			bn2.setGravity(Gravity.CENTER);
			LinearLayout bl2=new LinearLayout(getApplicationContext());
			bl2.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.95).intValue(), new Double(heightPixels * 0.11).intValue()));
			bl2.setBackgroundDrawable(inNormal);
			LinearLayout leftImageLayout2=new LinearLayout(getApplicationContext());
			leftImageLayout2.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.234).intValue(), new Double(heightPixels * 0.11).intValue()));
			leftImageLayout2.setGravity(Gravity.CENTER);
			ImageView img2=new ImageView(getApplicationContext());
			img2.setImageBitmap(getImageFromAssetsFile(getApplicationContext(), "ic_update.png"));
			img2.setColorFilter(0xff1E90FF);
			LinearLayout rightTextLayout2=new LinearLayout(getApplicationContext());
			rightTextLayout2.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.65).intValue(), new Double(heightPixels * 0.11).intValue()));
			rightTextLayout2.setGravity(Gravity.CENTER | Gravity.LEFT);
			rightTextLayout2.setOrientation(1);
			TextView moduleVersion=new TextView(getApplicationContext());
			moduleVersion.setText("当前版本号：" + moduleVer);
			moduleVersion.setTextColor(0xff000000);
			moduleVersion.setTextSize(15f);
			TextView moduleUpdate=new TextView(getApplicationContext());
			moduleUpdate.setTextColor(0xffa0a0a0);
			moduleUpdate.setText("模块过期时间：" + moduleExpire + "\n过期后模块将无法使用\n提示：点击可加入群组，及时获取更新");
			moduleUpdate.setTextSize(10f);
			leftImageLayout2.addView(img2);
			rightTextLayout2.addView(moduleVersion);
			rightTextLayout2.addView(moduleUpdate);
			bl2.addView(leftImageLayout2);
			bl2.addView(rightTextLayout2);
			bn2.addView(bl2);

			bn2.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						ClickVersion(view);
					}
				});
			LinearLayout bn3=new LinearLayout(getApplicationContext());
			bn3.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, new Double(heightPixels * 0.12).intValue()));
			bn3.setGravity(Gravity.CENTER);
			LinearLayout bl3=new LinearLayout(getApplicationContext());
			bl3.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.95).intValue(), new Double(heightPixels * 0.11).intValue()));
			bl3.setBackgroundDrawable(inNormal);
			LinearLayout leftImageLayout3=new LinearLayout(getApplicationContext());
			leftImageLayout3.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.234).intValue(), new Double(heightPixels * 0.11).intValue()));
			leftImageLayout3.setGravity(Gravity.CENTER);
			ImageView img3=new ImageView(getApplicationContext());
			img3.setImageBitmap(getImageFromAssetsFile(getApplicationContext(), "ic_attach_money.png"));
			img3.setColorFilter(0xff1E90FF);
			LinearLayout rightTextLayout3=new LinearLayout(getApplicationContext());
			rightTextLayout3.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.65).intValue(), new Double(heightPixels * 0.11).intValue()));
			rightTextLayout3.setGravity(Gravity.CENTER | Gravity.LEFT);
			rightTextLayout3.setOrientation(1);
			TextView donationText=new TextView(getApplicationContext());
			donationText.setText("捐赠");
			donationText.setTextColor(0xff000000);
			donationText.setTextSize(15f);
			TextView donationIntro=new TextView(getApplicationContext());
			donationIntro.setTextColor(0xffa0a0a0);
			donationIntro.setText("帮服务器续上一秒");
			donationIntro.setTextSize(10f);
			leftImageLayout3.addView(img3);
			rightTextLayout3.addView(donationText);
			rightTextLayout3.addView(donationIntro);
			bl3.addView(leftImageLayout3);
			bl3.addView(rightTextLayout3);
			bn3.addView(bl3);

			bn3.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						ClickDonate(view);
					}
				});

			LinearLayout simpleBn1=new LinearLayout(getApplicationContext());
			simpleBn1.setGravity(Gravity.CENTER);
			simpleBn1.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, new Double(heightPixels * 0.06).intValue()));
			LinearLayout simpleBl1=new LinearLayout(getApplicationContext());
			simpleBl1.setGravity(Gravity.CENTER | Gravity.LEFT);
			simpleBl1.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.95).intValue(), new Double(heightPixels * 0.05).intValue()));
			// simpleBl1.setBackgroundColor(0xffffffff);
			LinearLayout simpleLeftLayout1=new LinearLayout(getApplicationContext());
			simpleLeftLayout1.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.234).intValue(), new Double(heightPixels * 0.05).intValue()));
			simpleLeftLayout1.setGravity(Gravity.CENTER);
			simpleLeftLayout1.setPadding(15, 15, 15, 15);
			ImageView simpleImg1=new ImageView(getApplicationContext());
			simpleImg1.setImageBitmap(getImageFromAssetsFile(getApplicationContext(), "ic_help_outline.png"));
			simpleImg1.setColorFilter(0xff1E90FF);
			LinearLayout simpleRightLayout1=new LinearLayout(getApplicationContext());
			simpleRightLayout1.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.65).intValue(), new Double(heightPixels * 0.05).intValue()));
			simpleRightLayout1.setGravity(Gravity.CENTER | Gravity.LEFT);
			TextView simpleText1=new TextView(getApplicationContext());
			simpleText1.setText("帮助");
			simpleText1.setTextSize(12.5f);
			simpleText1.setTextColor(0xff000000);
			simpleRightLayout1.addView(simpleText1);
			simpleLeftLayout1.addView(simpleImg1);
			simpleBl1.addView(simpleLeftLayout1);
			simpleBl1.addView(simpleRightLayout1);
			simpleBn1.addView(simpleBl1);

			simpleBn1.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						ClickHelp(view);
					}
				});

			/*
			 LinearLayout simpleBn2=new LinearLayout(getApplicationContext());
			 simpleBn2.setGravity(Gravity.CENTER);
			 simpleBn2.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, new Double(heightPixels * 0.06).intValue()));
			 LinearLayout simpleBl2=new LinearLayout(getApplicationContext());
			 simpleBl2.setGravity(Gravity.CENTER | Gravity.LEFT);
			 simpleBl2.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.95).intValue(), new Double(heightPixels * 0.05).intValue()));
			 // simpleBl2.setBackgroundColor(0xffffffff);
			 LinearLayout simpleLeftLayout2=new LinearLayout(getApplicationContext());
			 simpleLeftLayout2.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.234).intValue(), new Double(heightPixels * 0.05).intValue()));
			 simpleLeftLayout2.setGravity(Gravity.CENTER);
			 simpleLeftLayout2.setPadding(15, 15, 15, 15);
			 ImageView simpleImg2=new ImageView(getApplicationContext());
			 simpleImg2.setImageBitmap(getImageFromAssetsFile(getApplicationContext(), "ic_bug_report.png"));
			 simpleImg2.setColorFilter(0xff1E90FF);
			 LinearLayout simpleRightLayout2=new LinearLayout(getApplicationContext());
			 simpleRightLayout2.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.65).intValue(), new Double(heightPixels * 0.05).intValue()));
			 simpleRightLayout2.setGravity(Gravity.CENTER | Gravity.LEFT);
			 TextView simpleText2=new TextView(getApplicationContext());
			 simpleText2.setText("日志");
			 simpleText2.setTextSize(12.5f);
			 simpleText2.setTextColor(0xff000000);
			 simpleRightLayout2.addView(simpleText2);
			 simpleLeftLayout2.addView(simpleImg2);
			 simpleBl2.addView(simpleLeftLayout2);
			 simpleBl2.addView(simpleRightLayout2);
			 simpleBn2.addView(simpleBl2);
			 */

			LinearLayout simpleBn3=new LinearLayout(getApplicationContext());
			simpleBn3.setGravity(Gravity.CENTER);
			simpleBn3.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, new Double(heightPixels * 0.06).intValue()));
			LinearLayout simpleBl3=new LinearLayout(getApplicationContext());
			simpleBl3.setGravity(Gravity.CENTER | Gravity.LEFT);
			simpleBl3.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.95).intValue(), new Double(heightPixels * 0.05).intValue()));
			// simpleBl3.setBackgroundColor(0xffffffff);
			LinearLayout simpleLeftLayout3=new LinearLayout(getApplicationContext());
			simpleLeftLayout3.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.234).intValue(), new Double(heightPixels * 0.05).intValue()));
			simpleLeftLayout3.setGravity(Gravity.CENTER);
			simpleLeftLayout3.setPadding(15, 15, 15, 15);
			ImageView simpleImg3=new ImageView(getApplicationContext());
			simpleImg3.setImageBitmap(getImageFromAssetsFile(getApplicationContext(), "ic_info_outline.png"));
			simpleImg3.setColorFilter(0xff1E90FF);
			LinearLayout simpleRightLayout3=new LinearLayout(getApplicationContext());
			simpleRightLayout3.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.65).intValue(), new Double(heightPixels * 0.05).intValue()));
			simpleRightLayout3.setGravity(Gravity.CENTER | Gravity.LEFT);
			TextView simpleText3=new TextView(getApplicationContext());
			simpleText3.setText("关于");
			simpleText3.setTextSize(12.5f);
			simpleText3.setTextColor(0xff000000);
			simpleRightLayout3.addView(simpleText3);
			simpleLeftLayout3.addView(simpleImg3);
			simpleBl3.addView(simpleLeftLayout3);
			simpleBl3.addView(simpleRightLayout3);
			simpleBn3.addView(simpleBl3);

			simpleBn3.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						ClickAbout(view);
					}
				});

			/*
			 LinearLayout simpleBn4=new LinearLayout(getApplicationContext());
			 simpleBn4.setGravity(Gravity.CENTER);
			 simpleBn4.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, new Double(heightPixels * 0.06).intValue()));
			 LinearLayout simpleBl4=new LinearLayout(getApplicationContext());
			 simpleBl4.setGravity(Gravity.CENTER | Gravity.LEFT);
			 simpleBl4.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.95).intValue(), new Double(heightPixels * 0.05).intValue()));
			 // simpleBl4.setBackgroundColor(0xffffffff);
			 LinearLayout simpleLeftLayout4=new LinearLayout(getApplicationContext());
			 simpleLeftLayout4.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.234).intValue(), new Double(heightPixels * 0.05).intValue()));
			 simpleLeftLayout4.setGravity(Gravity.CENTER);
			 simpleLeftLayout4.setPadding(15, 15, 15, 15);
			 ImageView simpleImg4=new ImageView(getApplicationContext());
			 simpleImg4.setImageBitmap(getImageFromAssetsFile(getApplicationContext(), "ic_settings.png"));
			 simpleImg4.setColorFilter(0xff1E90FF);
			 LinearLayout simpleRightLayout4=new LinearLayout(getApplicationContext());
			 simpleRightLayout4.setLayoutParams(new LinearLayout.LayoutParams(new Double(widthPixels * 0.65).intValue(), new Double(heightPixels * 0.05).intValue()));
			 simpleRightLayout4.setGravity(Gravity.CENTER | Gravity.LEFT);
			 TextView simpleText4=new TextView(getApplicationContext());
			 simpleText4.setText("设置");
			 simpleText4.setTextSize(12.5f);
			 simpleText4.setTextColor(0xff000000);
			 simpleRightLayout4.addView(simpleText4);
			 simpleLeftLayout4.addView(simpleImg4);
			 simpleBl4.addView(simpleLeftLayout4);
			 simpleBl4.addView(simpleRightLayout4);
			 simpleBn4.addView(simpleBl4);
			 */

			LinearLayout blank=new LinearLayout(getApplicationContext());
			blank.setLayoutParams(new LinearLayout.LayoutParams(widthPixels, new Double(heightPixels * 0.35).intValue()));  // 0.35
			if (!isModuleActivate()) {
				bl1.setBackgroundDrawable(inRed);
				moduleLoad.setText("模块未激活");
			} else {
				bl1.setBackgroundDrawable(inGreen);
				moduleLoad.setText("模块已激活");
			}

			rightTextLayout1.addView(moduleLoad);
			rightTextLayout1.addView(moduleMethod);
			bl1.addView(leftImageLayout1);
			bl1.addView(rightTextLayout1);
			bn1.addView(bl1);
			background.addView(bn1);
			background.addView(bn2);
			background.addView(bn3);
			background.addView(simpleBn1);
			// background.addView(simpleBn2);
			background.addView(simpleBn3);
			// background.addView(simpleBn4);
			background.addView(blank);
			sv.addView(background);
			setContentView(sv);
        } catch (Exception e) {

		}
    }
	public static Bitmap getImageFromAssetsFile(Context context, String fileName) {   
		Bitmap image = null;   
		AssetManager am = context.getResources().getAssets();   
		try {   
			InputStream is = am.open(fileName);   
			image = BitmapFactory.decodeStream(is);   
			is.close();   
		} catch (IOException e) {   
			e.printStackTrace();   
		}   
		return image;   
	}
} 
