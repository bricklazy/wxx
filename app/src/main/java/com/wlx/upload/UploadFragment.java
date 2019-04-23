package com.wlx.upload;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.wlx.application.R;
import com.wlx.base.BaseCallBack;
import com.wlx.base.BaseFragment;
import com.wlx.data.DataModel;
import com.wlx.utils.Base64;
import com.wlx.utils.Constants;
import com.wlx.utils.CustomToast;
import com.wlx.utils.OperateDialogUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;

/**
 * 上传数据
 */
public class UploadFragment extends BaseFragment {

    private int REQUEST_CODE = 1001;
    private DataModel dataModel = new DataModel();
    private EditText et_imei;
    private Button btnImei;
    private EditText et_msg;
    private RadioGroup rg;
    private RadioButton rbText;
    private Button btnSelImg;
    private Button btnSubmit;
    private Button btnReset;

    public static UploadFragment getIns() {
        UploadFragment uploadFragment = new UploadFragment();
        return uploadFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_upload;
    }

    @Override
    public void initView() {
        et_imei = view.findViewById(R.id.et_imei);
        btnImei = view.findViewById(R.id.btn_imei);
        et_msg = view.findViewById(R.id.et_msg);
        rg = view.findViewById(R.id.rg);
        rbText = view.findViewById(R.id.rb_text);
        btnSelImg = view.findViewById(R.id.btn_sel_img);
        btnSubmit = view.findViewById(R.id.btn_submit);
        btnReset = view.findViewById(R.id.btn_reset);

        if(!TextUtils.isEmpty(Constants.getIMEI(getContext()))){
            et_imei.setText(Constants.getIMEI(getContext()));
            et_imei.setSelection(Constants.getIMEI(getContext()).length());
        }

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId==R.id.rb_img){
                btnSelImg.setVisibility(View.VISIBLE);
                et_msg.setEnabled(false);
                et_msg.setText("");
            }else{
                btnSelImg.setVisibility(View.GONE);
                et_msg.setEnabled(true);
                et_msg.setText("");
            }
        });

        btnImei.setOnClickListener(v -> {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE);
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 123);
                CustomToast.showToast(getContext(), "没有获取到手机权限");
                return;
            }

            if(!TextUtils.isEmpty(Constants.getIMEI(getContext()))){
                et_imei.setText(Constants.getIMEI(getContext()));
                et_imei.setSelection(Constants.getIMEI(getContext()).length());
            }

        });

        btnSelImg.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                StringBuilder msg = new StringBuilder();
                boolean no_permission = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                if(!no_permission){
                    msg.append("相机");
                }

                boolean no_write_permission = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(!no_write_permission){
                    if(!no_permission){
                        msg.append("与");
                    }
                    msg.append("存储");
                }

                if(!no_permission && !no_write_permission){
                    OperateDialogUtil.getIns().showDelDialog(getContext(),"需要开启"+msg.toString()+"权限", new OperateDialogUtil.DialogCallBack() {

                        @Override
                        public void ok() {
                            //引导用户到设置中去进行设置
                            Intent intent = new Intent();
                            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package", getContext().getPackageName(), null));
                            startActivity(intent);
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                }else {
                    if (Build.VERSION.SDK_INT >= 23) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                    }
                }
                return;
            } else {

            }

            Matisse.from(getActivity())
                    .choose(MimeType.ofImage()) // 选择 mime 的类型
                    .capture(true)  // 开启相机，和 captureStrategy 一并使用否则报错
                    .captureStrategy(new CaptureStrategy(true,"com.wlx.application.wxx.fileprovider")) // 拍照的图片路径
                    .showSingleMediaType(true)
                    .countable(true)
                    .maxSelectable(1) // 图片选择的最多数量
                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.margin_150))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f) // 缩略图的比例
                    .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                    .forResult(REQUEST_CODE); // 设置作为标记的请求码
        });

        btnReset.setOnClickListener(v -> {
            reset();
        });

        btnSubmit.setOnClickListener(v -> {
            String imei = et_imei.getText().toString();
            String msg = et_msg.getText().toString();
            String msg_type = "1";
            if(rg.getCheckedRadioButtonId() == R.id.rb_img){
                msg_type = "2";
            }
            String imgName = (String) et_msg.getTag();
            if(TextUtils.isEmpty(imgName)){
                imgName = "";
            }
            dataModel.UploadMsg(imei, msg_type, msg, imgName, new BaseCallBack<Integer>(){

                @Override
                public void success(Integer code) {
                    if(code==0){
                        reset();
                        CustomToast.showToast(getContext(), "已提交");
                    }
                }

                @Override
                public void faild(String msg) {
                    CustomToast.showToast(getContext(), "提交失败:"+msg);
                }
            });
        });

    }

    private void reset() {
        rbText.setChecked(true);
        et_msg.setText("");
        et_msg.setTag(null);
    }

    /**
     *  根据Uri获取文件真实地址
     */
    public static String getRealFilePath(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String realPath = null;
        if (scheme == null)
            realPath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            realPath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        realPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        if (TextUtils.isEmpty(realPath)) {
            if (uri != null) {
                String uriString = uri.toString();
                int index = uriString.lastIndexOf("/");
                String imageName = uriString.substring(index);
                File storageDir;

                storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File file = new File(storageDir, imageName);
                if (file.exists()) {
                    realPath = file.getAbsolutePath();
                } else {
                    storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File file1 = new File(storageDir, imageName);
                    realPath = file1.getAbsolutePath();
                }
            }
        }
        return realPath;
    }

    /**
     * 将图片转换成Base64编码的字符串
     */
    public static String imageToBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = android.util.Base64.encodeToString(data, android.util.Base64.DEFAULT);
//            result = Base64.encode(data);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            for (int i = 0; i < mSelected.size(); i++) {
                Log.d("Matisse", "mSelected: " + getRealFilePath(getContext(), mSelected.get(i)));
                compress(getRealFilePath(getContext(), mSelected.get(i)));
            }
            Log.d("Matisse", "mSelected: " + mSelected);
        }
    }

    private void compress(String _Path)
    {
        System.out.println("_Path->" + _Path);
        String targetPath = _Path.substring(0 , _Path.lastIndexOf("/"));
        Luban.with(getContext())
                .load(_Path)
                .ignoreBy(100)
                .setTargetDir(targetPath)
                .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")))
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                        CustomToast.showToast(getContext(), "开始压缩");
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        et_msg.setTag(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("/")+1));
                        et_msg.setText(imageToBase64(file.getAbsolutePath()));
                        System.out.println("压缩后图片大小->" + file.length() / 1024 + "k");
                        System.out.println("getAbsolutePath->" + file.getAbsolutePath());
                        CustomToast.showToast(getContext(), "压缩完成开始转码");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        e.printStackTrace();
                    }
                }).launch();
    }


}
