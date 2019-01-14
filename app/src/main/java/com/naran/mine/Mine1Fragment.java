package com.naran.mine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.naran.Utils.LoginUtil;
import com.naran.Utils.StringUtil;
import com.naran.connector.OkHttpUtil;
import com.naran.favorite.Favorite1Activity;
import com.naran.navigation.SelectLanguageActivity;
import com.naran.useraccount.LoginActivity;
import com.naran.weather.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Mine1Fragment extends Activity {
    private Bitmap bmp;
    RelativeLayout btnLanguage;
    RelativeLayout upHImage;
    private ImageView mHead;
    private Button back;
    private Handler handler;
    private RelativeLayout favoriteLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mine1);

        initViews();
    }

    private void initViews() {
        handler = new Handler();
        favoriteLayout = (RelativeLayout)findViewById(R.id.favorite);
        favoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!LoginUtil.getInstance().isLogined){
                    startActivity(new Intent().setClass(Mine1Fragment.this, LoginActivity.class));
                }else {
                    startActivity(new Intent(Mine1Fragment.this, Favorite1Activity.class));
                }
            }
        });
        mHead = (ImageView) findViewById(R.id.imageView_head1);
        upHImage = (RelativeLayout) findViewById(R.id.rela_uphimage);
        upHImage.setOnClickListener(new OnClick());
        btnLanguage = (RelativeLayout) findViewById(R.id.language1);
        btnLanguage.setOnClickListener(new OnClick());
        back = (Button) findViewById(R.id.button_mine_back);
        back.setOnClickListener(new OnClick());
        if (LoginUtil.getInstance().isLogined) {
            loadHead();
        } else {
            startActivity(new Intent().setClass(Mine1Fragment.this, LoginActivity.class));
        }
    }

    private class OnClick implements View.OnClickListener {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.language1:
                    Intent intent = new Intent();
                    intent.putExtra("from", 1);
                    intent.setClass(Mine1Fragment.this, SelectLanguageActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.rela_uphimage:
                    if (LoginUtil.getInstance().isLogined) {
                        showImagePickDialog();
                    } else {
                        startActivity(new Intent(Mine1Fragment.this, LoginActivity.class));
                    }
                    break;
                case R.id.button_mine_back:
                    finish();
            }
        }
    }

    public void showImagePickDialog() {
        String title = "选取照片";
        String[] items = new String[]{"相机", "相册"};
        new AlertDialog.Builder(Mine1Fragment.this).setTitle(title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                // ѡ������
                                pickImageFromCamera();
                                break;
                            case 1:
                                // ѡ�����
                                pickImageFromAlbum();
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    public void pickImageFromCamera() {
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(), "head.jpg")));
        startActivityForResult(intent2, 2);
    }

    public void pickImageFromAlbum() {
        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent1, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// �ü�ͼƬ
                }
                break;
            case 2:

                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// �ü�ͼƬ
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    bmp = extras.getParcelable("data");
                    if (bmp != null) {
                        /**
                         * �ϴ�����������
                         */
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mHead.setImageBitmap(bmp);
                            }
                        });
                       // mHead.setImageBitmap(bmp);// ������SD����
                    }
                }

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                if (null == bos || null == bmp) {
                    return;
                }
                bmp.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
                byte[] bytes = bos.toByteArray();
                String s = Base64.encodeToString(bytes, Base64.DEFAULT);
                postImage(s);
//			Toast.makeText(MyData.this, ""+, 0).show();
                break;
        }

    }

    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY �ǿ�ߵı���
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY �ǲü�ͼƬ���
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void postImage(String base64Img) {
        Map<String, String> map = new HashMap<>();
        map.put("btyestr", base64Img);
        map.put("imgname", "head" + System.currentTimeMillis() + ".png");
        map.put("userid", LoginUtil.getInstance().userInfoModel.getID() + "");
        OkHttpUtil.postAsync(OkHttpUtil.UploadHeadImgs, map, new OkHttpUtil.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Toast.makeText(Mine1Fragment.this, request.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                Toast.makeText(Mine1Fragment.this, "上传成功！", Toast.LENGTH_SHORT).show();
                Log.e("requestSuccess", "result = " + result);
                JSONObject jb = new JSONObject(result);
                LoginUtil.getInstance().userInfoModel.setHeadImg(jb.optString("HeadImg"));
                loadHead();
            }
        });
    }

    private void loadHead() {

        if(StringUtil.isEmpty(LoginUtil.getInstance().userInfoModel.getHeadImg())){
            return;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request.Builder对象，设置参数，请求方式如果是Get，就不用设置，默认就是Get

        Request request = new Request.Builder()
                .url(LoginUtil.getInstance().userInfoModel.getHeadImg())
                .build();
        //3.创建一个Call对象，参数是request对象，发送请求
        Call call = okHttpClient.newCall(request);
        //4.异步请求，请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到从网上获取资源，转换成我们想要的类型
                byte[] Picture_bt = response.body().bytes();
                //通过handler更新UI

                final Bitmap bitmap = BitmapFactory.decodeByteArray(Picture_bt, 0, Picture_bt.length);
                //通过imageview，设置图片
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mHead.setImageBitmap(bitmap);
                    }
                });
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (LoginUtil.getInstance().isLogined) {
            loadHead();
        }
    }
}
