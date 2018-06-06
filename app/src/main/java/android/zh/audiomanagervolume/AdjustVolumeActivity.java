package android.zh.audiomanagervolume;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;


public class AdjustVolumeActivity extends Activity implements OnClickListener {
    private Button play = null;
    private Button down = null;
    private Button up = null;

    private ProgressBar pb = null;
    private int maxVolume = 50; // 最大音量值
    private int curVolume = 20; // 当前音量值
    private int stepVolume = 0; // 每次调整的音量幅度

    private MediaPlayer mediaPlayer = null;// 播放器
    private AudioManager audioMgr = null; // Audio管理器，用了控制音量
    private AssetManager assetMgr = null; // 资源管理器

    private final String musicName = "hehe.mp3";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_volume);

        // 初始化播放器、音量数据等相关工作
        initPlayWork();
        // 初始化视图
        initUI();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        play = (Button) findViewById(R.id.play);
        down = (Button) findViewById(R.id.down);
        up = (Button) findViewById(R.id.up);

        play.setOnClickListener(this);
        down.setOnClickListener(this);
        up.setOnClickListener(this);
        // 设置进度条
        pb = (ProgressBar) findViewById(R.id.progress);
        pb.setMax(maxVolume);
        pb.setProgress(curVolume);
    }

    /**
     * 初始化播放器、音量数据等相关工作
     */
    private void initPlayWork() {
        audioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // 获取最大音乐音量
        maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取初始化音量
        curVolume = audioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        // 每次调整的音量大概为最大音量的1/6
        stepVolume = maxVolume / 6;

        mediaPlayer = new MediaPlayer();
        assetMgr = this.getAssets();
    }

    /**
     * 准备播放音乐
     *
     * @param music
     */
    private void prepareAndPlay() {
        try {
            // 打开指定音乐文件
            AssetFileDescriptor afd = assetMgr.openFd(musicName);
            mediaPlayer.reset();
            // 使用MediaPlayer加载指定的声音文件。
            mediaPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            // 准备声音
            mediaPlayer.prepare();
            // 播放
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调整音量
     */
    private void adjustVolume() {
        audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, curVolume,
                AudioManager.FLAG_PLAY_SOUND);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.play://按下播放按钮
                prepareAndPlay();
                break;
            case R.id.up://按下增大音量按钮
                curVolume += stepVolume;
                if (curVolume >= maxVolume) {
                    curVolume = maxVolume;
                }
                pb.setProgress(curVolume);
                break;
            case R.id.down://按下减小音量按钮
                curVolume -= stepVolume;
                if (curVolume <= 0) {
                    curVolume = 0;
                }
                pb.setProgress(curVolume);
                break;
            default:
                break;
        }
        // 调整音量
        adjustVolume();
    }
}