package com.example.memorygame;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

public class MusicManager {
	private static final String TAG = "MusicManager";
	public static final int MUSIC_PREVIOUS = -1;
	public static final int MUSIC_MENU = 0;
	public static final int MUSIC_GAME = 1;
	public static final int MUSIC_END_GAME = 2;
	public static final int MUSIC_CHRISTMAS = 3;

	private static HashMap<Integer, MediaPlayer> players = new HashMap();
	private static int currentMusic = -1;
	private static int previousMusic = -1;

	public static float getMusicVolume(Context context) {

		String volumeString = PreferenceManager.getDefaultSharedPreferences(
				context).getString(
				context.getString(R.string.key_pref_music_volume),
				context.getString(R.string.key_def_music_volume));
		return new Float(volumeString).floatValue();
	}

	public static void start(Context context, int music) {
		MediaPlayer mp;
		start(context, music, false);
	}

	public static void startagain(Context context, int music)
			throws IllegalStateException, IOException {
		startagain(context, music, false);
	}
	
	public static void startagainXmas(Context context, int music)
			throws IllegalStateException, IOException {
		startagainXmas(context, music, false);
	}
	
	public static void startagainHorror(Context context, int music)
			throws IllegalStateException, IOException {
		startagainHorror(context, music, false);
	}

	public static void start(Context context, int music, boolean force) {
		if (!force && currentMusic > -1) {
			// already playing some music and not forced to change
			return;
		}
		if (music == MUSIC_PREVIOUS) {
			Log.d(TAG, "Using previous music [" + previousMusic + "]");
			music = previousMusic;
		}
		if (currentMusic == music) {
			// already playing this music
			return;
		}
		if (currentMusic != -1) {
			previousMusic = currentMusic;
			Log.d(TAG, "Previous music was [" + previousMusic + "]");
			// playing some other music, pause it and change
			pause();
		}
		currentMusic = music;
		Log.d(TAG, "Current music is now [" + currentMusic + "]");
		MediaPlayer mp = players.get(music);
		if (mp != null) {
			if (!mp.isPlaying()) {
				mp.start();
			}
		} else {
			if (music == MUSIC_MENU) {
				mp = MediaPlayer.create(context, R.raw.secondbounce);
			} else if (music == MUSIC_GAME) {
				mp = MediaPlayer.create(context, R.raw.secondbounce);
			} else if (music == MUSIC_END_GAME) {
				mp = MediaPlayer.create(context, R.raw.secondbounce);
			} else {
				Log.e(TAG, "unsupported music number - " + music);
				return;
			}
			players.put(music, mp);
			float volume = getMusicVolume(context);
			Log.d(TAG, "Setting music volume to " + volume);
			mp.setVolume(volume, volume);
			if (mp == null) {
				Log.e(TAG, "player was not created successfully");
			} else {
				try {
					mp.setLooping(true);
					mp.start();
				} catch (Exception e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}
	}

	public static void startagain(Context context, int music, boolean force)
			throws IllegalStateException, IOException {
		if (!force && currentMusic > -1) {
			// already playing some music and not forced to change
			return;
		}
		if (music == MUSIC_PREVIOUS) {
			Log.d(TAG, "Using previous music [" + previousMusic + "]");
			music = previousMusic;
		}
		if (currentMusic == music) {
			// already playing this music
			return;
		}
		if (currentMusic != -1) {
			previousMusic = currentMusic;
			Log.d(TAG, "Previous music was [" + previousMusic + "]");
			// playing some other music, pause it and change
			pause();
		}
		currentMusic = music;
		Log.d(TAG, "Current music is now [" + currentMusic + "]");
		MediaPlayer mp = players.get(music);
		MediaPlayer yp;
		yp = MediaPlayer.create(context, R.raw.secondbounce);
		String uriPath = "android.resource://com.example.memorygame/raw/secondbounce";
		Uri uri = Uri.parse(uriPath);

		if (mp != null) {
			if (!mp.isPlaying()) {
				mp.reset();
				mp.setDataSource(context, uri);
				mp.prepare();
				mp.setLooping(true);
				mp.start();
			}
		} else {
			if (music == MUSIC_MENU) {
				mp = MediaPlayer.create(context, R.raw.secondbounce);
			} else if (music == MUSIC_GAME) {
				mp = MediaPlayer.create(context, R.raw.secondbounce);
			} else if (music == MUSIC_END_GAME) {
				mp = MediaPlayer.create(context, R.raw.secondbounce);
			} else {
				Log.e(TAG, "unsupported music number - " + music);
				return;
			}
			players.put(music, mp);
			float volume = getMusicVolume(context);
			Log.d(TAG, "Setting music volume to " + volume);
			mp.setVolume(volume, volume);
			if (mp == null) {
				Log.e(TAG, "player was not created successfully");
			} else {
				try {
					mp.setLooping(true);
					mp.start();
				} catch (Exception e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}
	}
	
	public static void startagainXmas(Context context, int music, boolean force)
			throws IllegalStateException, IOException {
		if (!force && currentMusic > -1) {
			// already playing some music and not forced to change
			return;
		}
		if (music == MUSIC_PREVIOUS) {
			Log.d(TAG, "Using previous music [" + previousMusic + "]");
			music = previousMusic;
		}
		if (currentMusic == music) {
			// already playing this music
			return;
		}
		if (currentMusic != -1) {
			previousMusic = currentMusic;
			Log.d(TAG, "Previous music was [" + previousMusic + "]");
			// playing some other music, pause it and change
			pause();
		}
		currentMusic = music;
		Log.d(TAG, "Current music is now [" + currentMusic + "]");
		MediaPlayer mp = players.get(music);
		//MediaPlayer yp;
		//yp = MediaPlayer.create(context, R.raw.secondbounce);
		String uriPath = "android.resource://com.example.memorygame/raw/xmas";
		Uri uri = Uri.parse(uriPath);

		if (mp != null) {
			if (!mp.isPlaying()) {
				mp.reset();
				mp.setDataSource(context, uri);
				mp.prepare();
				mp.setLooping(true);
				mp.start();
			}
		} else {
			if (music == MUSIC_MENU) {
				mp = MediaPlayer.create(context, R.raw.xmas);
			} else if (music == MUSIC_GAME) {
				mp = MediaPlayer.create(context, R.raw.xmas);
			} else if (music == MUSIC_END_GAME) {
				mp = MediaPlayer.create(context, R.raw.xmas);
			} else {
				Log.e(TAG, "unsupported music number - " + music);
				return;
			}
			players.put(music, mp);
			float volume = getMusicVolume(context);
			Log.d(TAG, "Setting music volume to " + volume);
			mp.setVolume(volume, volume);
			if (mp == null) {
				Log.e(TAG, "player was not created successfully");
			} else {
				try {
					mp.setLooping(true);
					mp.start();
				} catch (Exception e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}
	}
	
	public static void startagainHorror(Context context, int music, boolean force)
			throws IllegalStateException, IOException {
		if (!force && currentMusic > -1) {
			// already playing some music and not forced to change
			return;
		}
		if (music == MUSIC_PREVIOUS) {
			Log.d(TAG, "Using previous music [" + previousMusic + "]");
			music = previousMusic;
		}
		if (currentMusic == music) {
			// already playing this music
			return;
		}
		if (currentMusic != -1) {
			previousMusic = currentMusic;
			Log.d(TAG, "Previous music was [" + previousMusic + "]");
			// playing some other music, pause it and change
			pause();
		}
		currentMusic = music;
		Log.d(TAG, "Current music is now [" + currentMusic + "]");
		MediaPlayer mp = players.get(music);
		//MediaPlayer yp;
		//yp = MediaPlayer.create(context, R.raw.secondbounce);
		String uriPath = "android.resource://com.example.memorygame/raw/horror";
		Uri uri = Uri.parse(uriPath);

		if (mp != null) {
			if (!mp.isPlaying()) {
				mp.reset();
				mp.setDataSource(context, uri);
				mp.prepare();
				mp.setLooping(true);
				mp.start();
			}
		} else {
			if (music == MUSIC_MENU) {
				mp = MediaPlayer.create(context, R.raw.horror);
			} else if (music == MUSIC_GAME) {
				mp = MediaPlayer.create(context, R.raw.horror);
			} else if (music == MUSIC_END_GAME) {
				mp = MediaPlayer.create(context, R.raw.horror);
			} else {
				Log.e(TAG, "unsupported music number - " + music);
				return;
			}
			players.put(music, mp);
			float volume = getMusicVolume(context);
			Log.d(TAG, "Setting music volume to " + volume);
			mp.setVolume(volume, volume);
			if (mp == null) {
				Log.e(TAG, "player was not created successfully");
			} else {
				try {
					mp.setLooping(true);
					mp.start();
				} catch (Exception e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}
	}

	public static void pause() {
		Collection<MediaPlayer> mps = players.values();
		for (MediaPlayer p : mps) {
			if (p.isPlaying()) {
				p.pause();
			}
		}
		// previousMusic should always be something valid
		if (currentMusic != -1) {
			previousMusic = currentMusic;
			Log.d(TAG, "Previous music was [" + previousMusic + "]");
		}
		currentMusic = -1;
		Log.d(TAG, "Current music is now [" + currentMusic + "]");
	}

	public static void stop() {
		Collection<MediaPlayer> mps = players.values();
		for (MediaPlayer p : mps) {
			if (p.isPlaying()) {
				p.stop();
			}
		}
		// previousMusic should always be something valid
		if (currentMusic != -1) {
			previousMusic = currentMusic;
			Log.d(TAG, "Previous music was [" + previousMusic + "]");
		}
		currentMusic = -1;
		Log.d(TAG, "Current music is now [" + currentMusic + "]");
	}

	public static void updateVolumeFromPrefs(Context context) {
		try {
			float volume = getMusicVolume(context);
			Log.d(TAG, "Setting music volume to " + volume);
			Collection<MediaPlayer> mps = players.values();
			for (MediaPlayer p : mps) {
				p.setVolume(volume, volume);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	public static void release() {
		Log.d(TAG, "Releasing media players");
		Collection<MediaPlayer> mps = players.values();
		for (MediaPlayer mp : mps) {
			try {
				if (mp != null) {
					if (mp.isPlaying()) {
						mp.stop();
					}
					mp.release();
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
		mps.clear();
		if (currentMusic != -1) {
			previousMusic = currentMusic;
			Log.d(TAG, "Previous music was [" + previousMusic + "]");
		}
		currentMusic = -1;
		Log.d(TAG, "Current music is now [" + currentMusic + "]");
	}
}