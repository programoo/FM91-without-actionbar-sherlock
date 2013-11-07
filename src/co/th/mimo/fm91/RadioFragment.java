package co.th.mimo.fm91;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class RadioFragment extends Fragment 
{
	private String TAG = this.getClass().getSimpleName();
	private View viewMainFragment;
	private MediaPlayer player;
	private ImageButton playTg;
	private SeekBar volumeSeekbar;
	private AudioManager audioManager;
	private boolean isPlay =false;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initializeMediaPlayer();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.viewMainFragment = inflater.inflate(R.layout.radio_fragment,
				container, false);
		
		initControls();
		
		playTg = (ImageButton) this.viewMainFragment
				.findViewById(R.id.playRadioTg);
		
		if(!isPlay){
			playTg.setImageResource(R.drawable.play_img);
		}
		else{
			playTg.setImageResource(R.drawable.pause_img);
		}
		
		playTg.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!isPlay)
				{
					isPlay = true;
					startPlaying();
					playTg.setImageResource(R.drawable.pause_img);

				} else
				{
					isPlay = false;
					stopPlaying();
					playTg.setImageResource(R.drawable.play_img);

				}
			}
		});
		
		ImageButton minBtn = (ImageButton) this.viewMainFragment.findViewById(R.id.minSoundImgBtn);
		minBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				audioManager.setStreamVolume(
						AudioManager.STREAM_MUSIC, 0, 0);
				volumeSeekbar.setProgress(0);
			}
		});
		
		ImageButton maxBtn = (ImageButton) this.viewMainFragment.findViewById(R.id.maxSoundImgBtn);
		maxBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				audioManager.setStreamVolume(
						AudioManager.STREAM_MUSIC, 15, 0);
				volumeSeekbar.setProgress(15);

			}
		});
		return viewMainFragment;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
	}
	
	public void onStart()
	{
		super.onStart();
	}
	
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}
	
	public void reloadViewAfterRequestTaskComplete()
	{
	}
	
	private void initControls()
	{
		try
		{
	        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
			volumeSeekbar = (SeekBar) this.viewMainFragment.findViewById(R.id.seekBar1);
			audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
			volumeSeekbar.setMax(audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
			volumeSeekbar.setProgress(audioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC));
			
			volumeSeekbar
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
					{
						@Override
						public void onStopTrackingTouch(SeekBar arg0)
						{
						}
						
						@Override
						public void onStartTrackingTouch(SeekBar arg0)
						{
						}
						
						@Override
						public void onProgressChanged(SeekBar arg0,
								int progress, boolean arg2)
						{
							audioManager.setStreamVolume(
									AudioManager.STREAM_MUSIC, progress, 0);
						}
					});
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void startPlaying()
	{
		try
		{
			player.prepareAsync();
		} catch (IllegalStateException e)
		{
			e.printStackTrace();
		}
		
		player.setOnPreparedListener(new OnPreparedListener()
		{
			
			public void onPrepared(MediaPlayer mp)
			{
				player.start();
			}
		});
		
	}
	
	private void stopPlaying()
	{
		if (player.isPlaying())
		{
			player.stop();
			player.release();
			initializeMediaPlayer();
		}
		
	}
	
	private void initializeMediaPlayer()
	{
		player = new MediaPlayer();
		try
		{
			player.setDataSource("http://122.155.16.48:8955");
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (IllegalStateException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		player.setOnBufferingUpdateListener(new OnBufferingUpdateListener()
		{
			
			public void onBufferingUpdate(MediaPlayer mp, int percent)
			{
			}
		});
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
	}

	@Override
	public void onDestroy()
	{
		isPlay = false;
		stopPlaying();
		playTg.setImageResource(R.drawable.play_img);
		super.onDestroy();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}
	
}
