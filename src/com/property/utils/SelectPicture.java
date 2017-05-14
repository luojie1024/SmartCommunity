package com.property.utils;

import java.io.File;

import com.way.tabui.gokit.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SelectPicture {
	public static final int TAKE_PICTURE = 33;
	public static final int SELECT_PICTURE = 313;
	public static final int TAKE_SELECT_PICTURE = 333;
	/* 用来标识请求裁剪图片后的activity */
	public static final int CAMERA_CROP_DATA = 3022;
	/* 用来标识请求照相功能的activity */
	public static final int CAMERA_WITH_DATA = 3023;
	// 照相机拍照得到的图片
	private File mCurrentPhotoFile;
	private String mFileName;
	private String imagePath;
	public static String FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "android" + "/"
			+ "data" + "/" + "picture/";
	private File sdcardTempFile = new File(FILEPATH, "tmp_pic_" + SystemClock.currentThreadTimeMillis() + ".jpg");
	private Context context;
	private Activity activity;

	public SelectPicture(Activity activity) {
		this.context = activity;
		this.activity = activity;
	}

	public void showSelectIcon() {
		final IconSelectDialog dialog = new IconSelectDialog(context);
		dialog.show();
		Button take = (Button) dialog.findViewById(R.id.btn_photo_take);
		take.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// // 选择拍照
				// Intent cameraintent = new
				// Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// // 指定调用相机拍照后照片的储存路径
				// cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
				// Uri.fromFile(sdcardTempFile));
				// startActivityForResult(cameraintent, TAKE_PICTURE);
				doPickPhotoAction();
				dialog.cancel();
			}
		});

		Button select = (Button) dialog.findViewById(R.id.btn_photo_select);
		select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 从相册中去获取
				try {
					Intent i = new Intent(Intent.ACTION_PICK);
					i.setType("image/*");
					activity.startActivityForResult(i, SELECT_PICTURE);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(context, "没有找到照片", Toast.LENGTH_SHORT).show();
				}
				dialog.cancel();
			}
		});

		Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

	}

	public String onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return null;
		}
		switch (requestCode) {
		case SELECT_PICTURE:
			Uri uri = data.getData();
			String currentFilePath = getPath(uri);
			if (!TextUtils.isEmpty(currentFilePath)) {
				activity.startActivityForResult(
						new Intent(context, CropImageActivity.class).putExtra("PATH", currentFilePath),
						CAMERA_CROP_DATA);
			} else {
				Toast.makeText(context, "未在存储卡中找到这个文件", Toast.LENGTH_SHORT).show();
			}
			return null;
		case TAKE_PICTURE:
			startPhotoZoom(Uri.fromFile(sdcardTempFile)); // 选择拍照
			return null;
		case CAMERA_WITH_DATA:
			Log.d("image", "将要进行裁剪的图片的路径是 = " + mCurrentPhotoFile.getPath());
			activity.startActivityForResult(
					new Intent(context, CropImageActivity.class).putExtra("PATH", mCurrentPhotoFile.getPath()),
					CAMERA_CROP_DATA);
			return null;
		case CAMERA_CROP_DATA:
			imagePath = data.getStringExtra("PATH");
			return imagePath;
		// Log.d("image", "裁剪后得到的图片的路径是 = " + imagePath);
		// if (!TextUtils.isEmpty(imagePath)) {
		// // 从缓存中获取图片，很重要否则会导致页面闪动
		// Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		// // 缓存中没有则从网络和SD卡获取
		// if (bitmap == null) {
		// // ibAdd.setImageResource(R.drawable.image_loading);
		// if (imagePath.indexOf("http://") != -1) {
		// // 图片的下载
		// AbImageLoader.getInstance(context).display(image, imagePath);
		// } else if (imagePath.indexOf("/") == -1) {
		// // 索引图片
		// try {
		// int res = Integer.parseInt(imagePath);
		// image.setImageDrawable(context.getResources().getDrawable(res));
		// } catch (Exception e) {
		// // ibAdd.setImageResource(R.drawable.image_error);
		// }
		// } else {
		// Bitmap mBitmap = AbFileUtil.getBitmapFromSD(new File(imagePath),
		// AbImageUtil.SCALEIMG, 150,
		// 150);
		// if (mBitmap != null) {
		// image.setImageBitmap(mBitmap);
		// } else {
		// // 无图片时显示
		// }
		// }
		// } else {
		// // 直接显示
		// image.setImageBitmap(bitmap);
		// }
		// } else {
		// // 无图片时显示
		// }
		default:
			return null;
		}
	}

	/**
	 * 从照相机获取
	 */
	private void doPickPhotoAction() {
		String status = Environment.getExternalStorageState();
		// 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakePhoto();
		} else {
			Toast.makeText(context, "没有可用的存储卡", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 拍照获取图片
	 */
	protected void doTakePhoto() {
		try {
			mFileName = System.currentTimeMillis() + ".jpg";
			mCurrentPhotoFile = new File(FILEPATH, mFileName);// 自定义路径
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
			Log.e("拍摄照片路径", mCurrentPhotoFile.getPath() + "!!!" + mCurrentPhotoFile.getAbsolutePath());
			activity.startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			Toast.makeText(context, "未找到系统相机程序", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		Log.v(this.toString(), uri + "aaa");
		if (uri == null && TextUtils.isEmpty(uri.getAuthority())) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}

	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		activity.startActivityForResult(intent, TAKE_SELECT_PICTURE);
	}

}
