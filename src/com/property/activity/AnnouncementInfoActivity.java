package com.property.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.property.base.BaseActivity;
import com.property.bean.InformDetails;
import com.way.tabui.gokit.R;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.ui.BindView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static org.kymjs.kjframe.ui.ViewInject.toast;

public class AnnouncementInfoActivity extends BaseActivity {

          @BindView(id = R.id.iv_back, click = true)
          private ImageView ivBack;
          @BindView(id = R.id.tv_back, click = true)
          private TextView tvBack;
          @BindView(id = R.id.tv_gonggao_detail_title)
          private TextView tvTitle;
          @BindView(id = R.id.tv_gonggao_detail_time)
          private TextView tvTime;
          @BindView(id = R.id.iv_gonggao_detail_img)
          private ImageView ivImg;
          @BindView(id = R.id.wv_gonggao_detail_content)
          private WebView wvContent;

          private KJBitmap bitmap;
          private InformDetails informDetails;

          @Override
          public void setRootView() {
                    setContentView(R.layout.activity_gonggao_detail);
          }

          @Override
          public void initData() {
                    super.initData();
                    bitmap = new KJBitmap();
                    //分配bean空间
                    informDetails = new InformDetails();
                    sendpost();
          }

          @Override
          public void widgetClick(View v) {
                    super.widgetClick(v);
                    switch (v.getId()) {
                              case R.id.iv_back:
                              case R.id.tv_back:
                                        finish();
                                        break;

                              default:
                                        break;
                    }
          }

          public void sendpost() {
                    BmobQuery<InformDetails> query = new BmobQuery<InformDetails>();
                    query.findObjects(new FindListener<InformDetails>() {
                              @Override
                              public void done(List<InformDetails> object, BmobException e) {
                                        if (object.size() == 0) {
                                                  toast("暂无信息！");
                                        } else {
                                                  toast("数据获取成功！");
                                                  informDetails = object.get(0);
                                                  tvTitle.setText(informDetails.getAnnouncement_info().getTitle());
                                                  tvTime.setText(informDetails.getAnnouncement_info().getUpdate_time());
                                                  //若图片地址为空，则隐藏ImageView
                                                  if (informDetails.getAnnouncement_info().getImg().equals("")) {
                                                            ivImg.setVisibility(View.GONE);
                                                  } else {
                                                            bitmap.display(ivImg, informDetails.getAnnouncement_info().getImg());
                                                  }
                                                  wvContent.loadData(informDetails.getAnnouncement_info().getContent(), "text/html; charset=UTF-8", null);
                                        }
                              }
                    });


          }
}
