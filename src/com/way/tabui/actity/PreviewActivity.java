package com.way.tabui.actity;

import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import vv.PpviewClientInterface.PpviewClientInterface;
import vv.playlib.CPlayerEx;
import vv.playlib.FishEyeInfo;
import vv.playlib.OnPlayerCallbackListener;
import vv.playlib.OnVoiceTalkCallbackListener;
import vv.playlib.RecPlayerEx;
import vv.playlib.VVAudio;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.way.tabui.gokit.R;
import com.way.util.ToastUtil;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.view.decoderView.XmGlView;

/**
 * Created by Administrator on 2016/8/15.
 * 实时预览接口demo
 */
public class PreviewActivity extends Activity{
    private PpviewClientInterface ppviewClientInterface;
//    = PpviewClientInterface.getInstance();
    private Activity mActivity = this;
    private EditText dev_id,dev_pass;
    private Button start_preview,stop_preview;
    private Button btn_starttalk,btn_stoptalk,btn_downtalk;
    private Button btn_ptzup,btn_ptzdown,btn_ptzleft,btn_ptzright,snap_pic,btn_startrec,btn_stoprec,
            btn_startrecplay,btn_stoprecplay;

    private LinearLayout previewLayout;//预览窗口的layout
    private LinearLayout recLayout;//本地录像回放窗口的layout
    private GLSurfaceView previewSfv = null;//预览窗口的svf
    private GLSurfaceView recSfv = null;//录像回放的svf
    private ImageView snapImage;//截图显示的imageview


    private CPlayerEx cplayerEx = null;//VVSDK预览播放接口类
    private RecPlayerEx recPlayerEx = null;//本地录像回放接口类
    private VVAudio vvAudio;////音频控制类


    private int previewWidth,previewHeight;

    private boolean isPreView = false;//是否正在预览

    private long talkConnect = 0;//对讲connect句柄
    private boolean isTalk = false;//是否正在对讲
    private int p2ptalk = 4;//这个值需要在摄像头列表里获取，目前这里给定为4（私有单双工）
    private int chlId = 0;//摄像头通道号，这里给定为0

    private boolean isRec = false;//是否正在录像
    private boolean isRecPlay = false;//是否正在播放录像
    XmGlView glView;
    IXmSystem xmSystem;
  //vvsdk
//  	private PpviewClientInterface ppviewClientInterface;
      private String vv_url = "http://ppview.vveye.com:3000/webapi/client";
      private String bound_url = "http://ppview.vveye.com:3000/webapi/device";
      private String app_key = "C6460838-D5D0-0001-CB18-B1491FFB1DCE";
      private String app_pass = "H10Yz6fJS5oPX1Hj";
      private String p2p_svr = "nat.vveye.net";
      private int p2p_port = 8000;
      private String p2p_secret = "";
      private String event_url = "http://ppview.vveye.com:3000/webapi/page";
//      private Context mContext = this;
      private boolean isInit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
      
        initSDK();
        initView();
        initPlaySDK();
    }

    private void initSDK(){
        ppviewClientInterface = PpviewClientInterface.getInstance();
        ppviewClientInterface.SetAppInfo(vv_url, app_key, app_pass, bound_url, p2p_svr,
                p2p_port, p2p_secret, event_url);
        isInit = true;
//        Toast.makeText(mContext,"initSDK",Toast.LENGTH_SHORT).show();
    }
    private void initView(){
        dev_id = (EditText)findViewById(R.id.dev_id);
        dev_pass = (EditText)findViewById(R.id.dev_pass);

        dev_id.setText("ZXSN6301986403");
        dev_pass.setText("888888");
        
//        xmSystem = XmSystem.getInstance();
        
        glView=new XmGlView(this,null);
        previewLayout = (LinearLayout)findViewById(R.id.main_ll);
        recLayout = (LinearLayout)findViewById(R.id.main_ll2);
        snapImage = (ImageView) findViewById(R.id.iv_catchpic);
        previewSfv = new GLSurfaceView(PreviewActivity.this);
        recSfv = new GLSurfaceView(PreviewActivity.this);
        
       
//        glView
        
//        LinearLayout.LayoutParams preview_lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams rec_lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        previewLayout.addView(  previewSfv,  LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
       // previewSfv
        recLayout.addView(recSfv,rec_lp);


        findViewById(R.id.button_back).setOnClickListener(onClickListener);
        start_preview = (Button)findViewById(R.id.start_preview);
        stop_preview = (Button)findViewById(R.id.stop_preview);
        btn_starttalk = (Button)findViewById(R.id.btn_starttalk);
        btn_stoptalk = (Button)findViewById(R.id.btn_stoptalk);
        btn_downtalk = (Button)findViewById(R.id.btn_downtalk);
        btn_ptzup = (Button)findViewById(R.id.btn_ptzup);
        btn_ptzdown = (Button)findViewById(R.id.btn_ptzdown);
        btn_ptzleft = (Button)findViewById(R.id.btn_ptzleft);
        btn_ptzright = (Button)findViewById(R.id.btn_ptzright);
        snap_pic = (Button)findViewById(R.id.snap_pic);
        btn_startrec = (Button)findViewById(R.id.btn_startrec);
        btn_stoprec = (Button)findViewById(R.id.btn_stoprec);
        btn_startrecplay = (Button)findViewById(R.id.btn_startrecplay);
        btn_stoprecplay = (Button)findViewById(R.id.btn_stoprecplay);

        start_preview.setOnClickListener(onClickListener);
        stop_preview.setOnClickListener(onClickListener);
        btn_starttalk.setOnClickListener(onClickListener);
        btn_stoptalk.setOnClickListener(onClickListener);
        snap_pic.setOnClickListener(onClickListener);
        btn_startrec.setOnClickListener(onClickListener);
        btn_stoprec.setOnClickListener(onClickListener);
        btn_startrecplay.setOnClickListener(onClickListener);
        btn_stoprecplay.setOnClickListener(onClickListener);

        btn_downtalk.setOnTouchListener(onTouchListener);
        btn_ptzup.setOnTouchListener(onTouchListener);
        btn_ptzdown.setOnTouchListener(onTouchListener);
        btn_ptzleft.setOnTouchListener(onTouchListener);
        btn_ptzright.setOnTouchListener(onTouchListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时必须停止预览和对讲，connect必须释放
        stopPreviewPlay();
        stopTalk();
    }

    private void initPlaySDK(){
        /**
         * 构造函数
         * @param mActivity
         * activity
         * @param index
         * 多路视频播放时的视频下标,单路播放填0
         * @param l
         * 播放时的回掉接口
         */
    	
//    	FishEyeInfo fishInfo=new FishEyeInfo();
//    	fishInfo.fishType=1;
        cplayerEx = new CPlayerEx(mActivity, 0, previewPlayerCallbackListener);
//       cplayerEx.setSurfaceview1(glView);
       
        
        cplayerEx.setSurfaceview1( previewSfv);
        
        //previewSfv
//        //初始化音频播放接口
//        myAudioPlayer = vvaudioplay.getInstance();
//        //让AudioTrack处于准备状态
//        myAudioPlayer.AudioPlayStart();

        /**
         * 初始化音频
         */
        vvAudio = VVAudio.getInstance();
        vvAudio.setVoiceTalkCallback(onVoiceTalkCallbackListener);

        /**
         * 本地录像播放构造函数
         * @param context
         * 上下文
         * @param callback
         * 每一帧的时间回调（用于定位seekbar）
         * @param l
         * 播放状态回调
         */
        recPlayerEx=new RecPlayerEx(mActivity, new PpviewClientInterface.OnRecPlayerCallback() {
          //  @Override
            //这里回调每一帧在文件中的位置
            public void onFrameTimeCallback(int i) {

            }
        }, recPlayerCallbackListener);

        recPlayerEx.setSurfaceview(recSfv);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
       // @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_back:
                    finish();
                    break;
                case R.id.start_preview:
                    //开始实时预览
                    startPreviewPlay();
                    break;
                case R.id.stop_preview:
                    //停止实时预览
                    stopPreviewPlay();
                    break;
                case R.id.btn_starttalk:
                    //开始实时对讲
                    startTalk();
                    break;
                case R.id.btn_stoptalk:
                    //停止实时对讲
                    stopTalk();
                    break;
                case R.id.snap_pic:
                    doCatchScreen();
                    break;
                case R.id.btn_startrec:
                    if(isPreView) {
                        doStartRec();
                    }
                    break;
                case R.id.btn_stoprec:
                    doStopRec();
                    break;
                case R.id.btn_startrecplay:
                    doStartRecPlay();
                    break;
                case R.id.btn_stoprecplay:
                    doStopRecPlay();
                    break;
            }
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
     //   @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                switch (v.getId()){
                    case R.id.btn_downtalk:
                        if (p2ptalk == 4&&isTalk) {
                            /**
                             * 设置当前对讲的播放模式
                             * @param mode
                             * 只录音，不播放的模式
                             * MODE_STARTREC，
                             * 只播放，不录音的模式
                             * MODE_STARTPLAY，
                             * 全双工模式，同时录音和播放
                             * MODE_RECANDPLAY
                             */
                            vvAudio.setTalkMode(VVAudio.MODE_STARTREC);
                            btn_downtalk.setText("松开结束");
                        }
                        break;
                    case R.id.btn_ptzup:
                        ptzCtrl(1);
                        break;
                    case R.id.btn_ptzdown:
                        ptzCtrl(2);
                        break;
                    case R.id.btn_ptzleft:
                        ptzCtrl(3);
                        break;
                    case R.id.btn_ptzright:
                        ptzCtrl(4);
                        break;
                }
            } else if (event.getAction()==MotionEvent.ACTION_UP) {
                switch (v.getId()){
                    case R.id.btn_downtalk:
                        if (p2ptalk == 4&&isTalk) {
                            /**
                             * 设置当前对讲的播放模式
                             * @param mode
                             * 只录音，不播放的模式
                             * MODE_STARTREC，
                             * 只播放，不录音的模式
                             * MODE_STARTPLAY，
                             * 全双工模式，同时录音和播放
                             * MODE_RECANDPLAY
                             */
                            vvAudio.setTalkMode(VVAudio.MODE_STARTPLAY);
                            btn_downtalk.setText("按住对讲");
                        }
                        break;
                    case R.id.btn_ptzup:
                    case R.id.btn_ptzdown:
                    case R.id.btn_ptzleft:
                    case R.id.btn_ptzright:
                        ptzCtrl(0);
                        break;
                }
            }

            return false;
        }
    };


    // 开始实时预览
    private void startPreviewPlay() {
        if(isPreView){
            return;
        }
        isPreView = true;

        String str_dev_id = dev_id.getText().toString().trim();
        String str_dev_user = "admin";
        String str_dev_pass = dev_pass.getText().toString().trim();

        Log.e("info", "startPlay url="
                + "    devid=" + str_dev_id + "    user=" + str_dev_user
                + "    pass=" + str_dev_pass);
        /**
         * 开始播放实时视屏
         * @param dev_id
         * 摄像头所属的设备id
         * @param user
         * 设备的用户名
         * @param pass
         * 设备的密码
         * @param chl_id
         * 摄像头的通道号
         * @param stream_id
         * 码流id
         * @return
         */
        cplayerEx.startPlay(str_dev_id,str_dev_user, str_dev_pass, chlId, 1);
        startPreviewAudio();
//        cplayerEx.startPlay(rtsp_url, dev_id, user, pass, index_id, frame, play_type, chl_id, stream_id, tag)
    }


    // 停止实时预览
    private void stopPreviewPlay() {

        if(!isPreView){
            return;
        }
        isPreView = false;
        /**
         * 停止播放视频
         */
        cplayerEx.stopPlay();
        stopPreviewAudio();
        doStopRec();

    }


    // 开始播放实时预览的音频
    private void startPreviewAudio() {
        /**
         * 设置音频是否开启（在播放完成，并且初始化音频库后调用）
         * @param status
         * 0=关闭   1=开启
         * @return
         */
        cplayerEx.setAudioStatus(1);
    }

    // 停止音频
    private void stopPreviewAudio() {
        /**
         * 设置音频是否开启（在播放完成，并且初始化音频库后调用）
         * @param status
         * 0=关闭   1=开启
         * @return
         */

        cplayerEx.setAudioStatus(0);
    }



    // 开启对讲
    private void startTalk() {
        createTalkConnector();
    }

    // 关闭对讲
    private void stopTalk() {
        if (isTalk&&talkConnect>0) {
            if (p2ptalk == 2 || p2ptalk == 4) {
                if (p2ptalk == 4) {
                    vvAudio.setTalkMode(VVAudio.MODE_RECANDPLAY);
                    /**
                     * 停止对讲（VV私有协议）
                     */
                    vvAudio.VVTalkStop();
                }
            }
            isTalk = false;
            //释放connect
            releaseTalkConnector();
            ToastUtil.ToastShow(mActivity,"关闭对讲成功");
        }
    }


    /**
     * 对讲之前创建connect
     */
    private void createTalkConnector(){
        if(talkConnect==0) {
            ppviewClientInterface.setOndevConnectCallback(talkConnectListener);
            talkConnect = ppviewClientInterface.createConnect(dev_id.getText().toString(), "admin",
                    dev_pass.getText().toString());
            Log.e("DEBUG", "talkConnect   " + talkConnect);
        }
    }

    /**结束对讲之后需要释放connect
     */
    private void releaseTalkConnector() {
        if (talkConnect != 0) {
            ppviewClientInterface.releaseConnect(talkConnect);
            talkConnect = 0;
            ppviewClientInterface.removeOndevConnectCallback(talkConnectListener);

        }
    }


    /**
     * @param mode
     * 云台控制，0-停止 1-上转 2-下转 3-左转 4-右转
     */
    private void ptzCtrl(int mode){
        if(isPreView){
            /**
             * 云镜控制(私有协议)
             * @param dev_id		设备ID
             * @param cid			通道号（从0开始）
             * @param sid			码流号（从0开始）
             * @param cmd			命令
            0：停止
            1：上转
            2：下转
            3：左转
            4：右转
            5：拉近
            6：拉远
            10：设置预置位
            11: 移动到预置位
            12: 删除预置位
            13: 设置看守位
            14: 删除看守位
            15：设置自动回复到看守位的闲置时间
            20：水平巡航
            21：垂直巡航
             * @param param1 (1~6，20~21)云镜速度（1~100)  (10~14)预置位编号(1~) (15)闲置时间秒
             * @param param2 （20~21）巡航次数（0表示无穷次）
             * @return
             */
            ppviewClientInterface.PlayerPtz(dev_id.getText().toString(),0,1,mode,50,0);
        }
    }



    // 截图
    private void doCatchScreen() {
        File m_file_store = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/catchPic/");
        if (!m_file_store.exists()) {
            m_file_store.mkdirs();
        }
        String strd = m_file_store.getAbsolutePath();
        final String filePath = strd + "/" + "jietu_jpg.jpg";
        int i_cp = -1;
        i_cp = cplayerEx.getCaptureFile(filePath);
        if (i_cp == 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            int i = previewWidth / previewHeight;
            if (i > 1) {
            } else {
                i = 1;
            }
            options.inSampleSize = i;
            Bitmap bm = BitmapFactory.decodeFile(filePath, options);
            if (bm != null) {
                snapImage.setImageBitmap(bm);
            }
        }

    }


    //开始录像

    private void doStartRec(){
        File m_file_store = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/catchPic/");
        if (!m_file_store.exists()) {
            m_file_store.mkdirs();
        }
        String strd = m_file_store.getAbsolutePath();
        String rec_fileName = strd + "/" + "rec_video.vveye";
        String rec_snapshotName = strd + "/" + "rec_video.jpg";
        /**
         * 开始本地录像
         * @param dev_id		设备ID
         * @param chl_id			通道号（从0开始）
         * @param rec_filename			录像文件保存的路径
         * @param rec_snapshot_filename	    录像缩略图保存的路径
         * @param max_sec 录像最长时长，秒，<=0，默认300秒
         * @return  0=成功
            -1=正在录像
            -2=正在开启播放过程
            -3=没在播放视频，无法录像
            -4=录像文件名为空
            -5=创建录像文件失败
         */
        int startResult = ppviewClientInterface.playerStartRec(dev_id.getText().toString(), chlId,
                rec_fileName, rec_snapshotName,0);

        if (startResult == 0) {
            isRec = true;
            ToastUtil.ToastShow(mActivity,"开始录像");
        } else {
            ToastUtil.ToastShow(mActivity,"录像失败");
        }
    }


    private void doStopRec() {
        if(getVideoState()==1){
            int stopResult = ppviewClientInterface.playerStopRec(dev_id.getText().toString(), chlId);
            if (stopResult == 0) {
                isRec = false;
                ToastUtil.ToastShow(mActivity,"关闭录像成功");
            } else {
                ToastUtil.ToastShow(mActivity,"关闭录像失败");
            }
        }
    }


    /**
     * 获取录像状态
     *
     * @return 0:没有在录像   1:在录像
     */
    private int getVideoState() {
        return ppviewClientInterface.playerIsRecording(dev_id.getText().toString(), chlId);
    }


    /**
     * 设置录像是否播放音频
     * @param status
     * true 播放 false 不播放
     */
    private void setRecAudioStatus(boolean status){
        /**
         * 设置是否开启音频
         * @param status
         * false=关闭 true=打开
         *
         */
        recPlayerEx.setIsPlayAudio(status);
    }

    private void doStartRecPlay() {
        File m_file_store = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/catchPic/");
        if (!m_file_store.exists()) {
            m_file_store.mkdirs();
        }
        String strd = m_file_store.getAbsolutePath();
        String rec_fileName = strd + "/" + "rec_video.vveye";
        String rec_snapshotName = strd + "/" + "rec_video.jpg";
        if (!TextUtils.isEmpty(rec_fileName)) {
            isRecPlay = true;
            /**
             * 开始视频回放
             * @param rec_filename
             * 文件名称，包括完整路径
             * @return
                >=0 = 成功开启，返回文件总的帧数（包括音频和视频）
                -1  = 播放器句柄错误
                -2  = 打开文件失败
             */
                int myAllSize=recPlayerEx.startRecPlayer(rec_fileName);
                setRecAudioStatus(true);
                if (myAllSize>=0) {

                }else{
                    //播放错误
                    ToastUtil.ToastShow(mActivity,"播放本地录像失败");
                }

        }
    }


    /**
     * 停止播放
     */
    private void doStopRecPlay() {
        if (isRecPlay) {
            /**
             * 停止本地录像播放
             */
            recPlayerEx.stopRecplayer();
            isRecPlay = false;
        }

    }


    private void doPauseRecVideo(boolean isPause){
        /**
         * 设置是否暂停播放
         * @param suspend
         * ture=暂停播放  false=开始播放
         */
        recPlayerEx.setSuspend(isPause);
    }
    //----------------------回调函数--------------------

    OnPlayerCallbackListener previewPlayerCallbackListener = new OnPlayerCallbackListener() {
       // @Override
        /**
         * 视屏播放状态改变回调
         * @param index
         * 多路视频播放时的视频下标（单路视频播放时为0）
         * @param status
         * 当前视屏播放状态 1=播放成功  2=正在缓冲  3=缓冲结束  -99=播放过程中数据被断开   其他=播放失败     (2和3只有在远程录像回放时才会出现)
         * @param tag
         * 标记，预留
         * @param progress
         * 缓冲进度，只有在status=2时才有效
         */
        public void OnPlayStatusChanged(int index, int status, String tag,int progress) {
            ToastUtil.ToastShow(mActivity,"OnPlayStatusChanged status:"+status);
            Log.e("DEBUG","OnPlayStatusChanged "+status);
        }

     //   @Override
        /**
         * 视频宽高回调（当播放成功后会受到此回调）
         * @param i
         * 多路视频播放时的视频下标（单路视频播放时为0）
         * @param width
         * 视屏宽度
         * @param height
         * 视屏高度
         */
        public void GetWidthAndHeight(int i, int width, int height) {
            previewWidth = width;
            previewHeight = height;
            Log.e("DEBUG","GetWidthAndHeight "+width+"   "+height);
        }

     //   @Override
        /**
         * 音频对讲状态回调（当开启音频对讲时会收到此回调）
         * @param index
         * 多路视频播放时的视频下标（单路视频播放时为0）
         * @param audio_status
         * 音频状态    0：没有音频 1：有音频
         * @param p2ptalk_status
         * 对讲状态     0-默认，无对讲；1=Onvif双工 2=私有双工 3=Onvif单工 4=私有单工
         * @param playHandler
         * 音频句柄
         * @param sendSize
         * 每次发送的数据大小
         */
        public void OnAudiosStatusChanged(int index, int audio_status,
                                          int p2ptalk_status, int playHandler, int sendSize) {
            if(audio_status==1){
                startPreviewAudio();
            }
        }

      //  @Override
        /**
         * 判断视频是否可以截图回调（播放成功后会调用此回调），收到此回调表示可以截图
         * @param index
         * 多路视频播放时的视频下标（单路视频播放时为0）
         */
        public void OnCaptureEnable(int index) {

        }
    };
    

    OnPlayerCallbackListener recPlayerCallbackListener = new OnPlayerCallbackListener() {
       // @Override
        public void OnPlayStatusChanged(int i, int i1, String s, int i2) {

        }

      //  @Override
        public void GetWidthAndHeight(int i, int i1, int i2) {

        }

      //  @Override
        public void OnAudiosStatusChanged(int i, int i1, int i2, int i3, int i4) {

        }

        //@Override
        public void OnCaptureEnable(int i) {

        }
    };


    OnVoiceTalkCallbackListener onVoiceTalkCallbackListener = new OnVoiceTalkCallbackListener() {

     //   @Override
        /**
         * 开启对讲的返回值
         * @param status
         * 1=成功
        -1=客户端句柄不存在
        -2=库未初始化
        -3=连接句柄不存在
        -4=发送数据失败
        -5=对讲已开启，不能重复开启
        -6=创建音频编码器失败
        -7=创建音频解码器失败
        -8=中断
        -9=超时
        -10=数据错误
        -20=设备端对讲插件不存在
        -21=设备端对讲插件打开失败
        -22=开启对讲失败，可能已被别人占用
        414=认证失败
        500=协议格式错误
         */
        public void OnVoiceTalkCallback(int status) {
            ToastUtil.ToastShow(mActivity,"onVoiceTalkCallbackListener "+status);
        }
    };


    PpviewClientInterface.OnDevConnectCallbackListener talkConnectListener = new PpviewClientInterface.OnDevConnectCallbackListener() {
      //  @Override
        /**
         * 连接设备返回的回调
         * @param msgid
         *			设备交互编码，256为连接设备，其它的则不是
         * @param connector
         *        连接句柄
         * @param result
         * 连接成功：1
        p2p连接超时：0
        p2p未初始化：-1
        p2p连接失败:-2
        连接中断:-3
        设备不在线:-4
         */
        public void on_connect_callback(int msgid, long connector, int result) {
            if (talkConnect == connector && !isTalk) {
                if(result!=1){
                    ToastUtil.ToastShow(mActivity,"创建对讲P2P失败！");
                    releaseTalkConnector();
                }else{
                    //开始对讲
                    isTalk = true;
                    if (p2ptalk == 2 || p2ptalk == 4) {
                        if (isTalk && talkConnect > 0) {
                            if (p2ptalk == 4) {
                                vvAudio.setTalkMode(VVAudio.MODE_STARTPLAY);
                            }

                            /**
                             * 开始对讲（vv私有协议）回调结果OnVoiceTalkCallbackListener
                             *
                             * @param connector
                             * P2P连接句柄
                             * @param chl_id
                             * 摄像头通道号
                             * @param devusr
                             * 设备用户名
                             * @param devpass
                             * 设备密码
                             * @return
                             */
                            vvAudio.VVTalkStart(talkConnect, chlId, dev_id.getText().toString(),
                                    dev_pass.getText().toString());
                            ToastUtil.ToastShow(mActivity,"开启对讲成功");
                        }
                    }
                }
            }
        }
    };
}
