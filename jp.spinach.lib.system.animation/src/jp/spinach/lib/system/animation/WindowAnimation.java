package jp.spinach.lib.system.animation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;

/**
 * ウィンドウのアニメーションを描画するクラス。 インスタンス化して利用する。
 * 
 * このクラスは、自身にビットマップと描画領域用のRectインスタンスを格納する。
 * publicメソッドであるアニメーションスタートを実行することで動的にRectインスタンスのカタチを変える。
 * 
 * 描画自体はViewクラスで行う必要が有るため、このクラスで描画は行わない。
 * 
 * また、このクラスをインスタンス化する際、Viewのコンストラクタで行う必要がある。
 * ※ビューのメインスレッド開始後にはAndroidのスレッド単一化の観点からインスタンス化出来ない。
 * 
 * @author hiroki
 * 
 */
public class WindowAnimation {
	
	/**
	 * 内部のハンドラーは一度に1つまでしか起動できない。
	 * その制約を担保するためのフラグ。
	 */
	private boolean handlerFlg = false;
	
	/**
	 * ウィンドウが既に開いているかを保持するフラグ
	 */
	public boolean isOpen;

	/**
	 * 表示するBitmapを設定。
	 */
	public Bitmap window;

	/**
	 * 最終的な描画領域を格納する。
	 */
	private Rect endRect;

	/**
	 * アニメーション開始時の描画領域を格納する。
	 * 基本は点
	 */
	private Rect startRect;

	/**
	 * 画像内の描画サイズを設定する。 スプライトの1単位の設定
	 */
	public Rect drawRect;

	/**
	 * 画面上の描画領域。 動的に変化する。
	 * View側で描写に利用するのはここ
	 */
	public Rect viewRect;

	/**
	 * 計算に利用する角度。
	 */
	private int rot;

	/**
	 * コンストラクタ。 ビットマップをセットする。
	 * 
	 * @param res
	 * @param resourceId
	 */
	public WindowAnimation(Resources res, int resourceId) {
		window = BitmapFactory.decodeResource(res, resourceId);
	}

	/**
	 * 画像に対しての描画領域の設定。スプライトの初期化。
	 */
	public void initializeSprite(int left, int top, int width, int height) {
		drawRect = new Rect(left, top, width, height);
	}

	/**
	 * オープンウィンドウアニメーション終了時の見え方を設定する。
	 * 
	 * @param top
	 * @param left
	 * @param width
	 * @param height
	 */
	public void setViewRect(int left, int top, int right, int bottom) {
		startRect = new Rect((left + right) / 2, (top + bottom) / 2,
				(left + right) / 2, (top + bottom) / 2);
		endRect = new Rect(left, top, right, bottom);

		viewRect = new Rect((left + right) / 2, (top + bottom) / 2,
				(left + right) / 2, (top + bottom) / 2);
	}

	/**
	 * 表示アニメーションハンドラーのキックメソッド
	 * 実行時にハンドラーが起動済で無いことが条件
	 */
	public void startOpenAnimation() {
		if(handlerFlg){
			//例外でも投げるか・・・？
		}else{
			rot = 0;
			openAnimation.sendEmptyMessageDelayed(1, 5);
		}
	}
	
	/**
	 * ウィンドウが消えるアニメーションのハンドラーキックメソッド
	 * 実行時にハンドラーが起動済で無いことが条件
	 */
	public void startCloseAnimation() {
		if(handlerFlg){
			//例外でも投げるか・・・？
		}else{
			rot = 45;
			closeAnimation.sendEmptyMessageDelayed(1, 5);
		}
	}

	/**
	 * ウィンドウオープンアニメーションハンドラー
	 * 0度から45度までの正弦の値を係数にしてアニメーションを行う。
	 */
	private final Handler openAnimation = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			if (msg.what == 1) {
				
				handlerFlg = true;

				rot = rot + 1;

				float rad = (float) (rot * Math.PI / 180) * 2;
				float size = (float) Math.sin(rad);

				// 幅、高さ計算
				int width = endRect.right - endRect.left;
				int height = endRect.bottom - endRect.top;

				// 表示幅、高さ計算
				float vWidth = width * size;
				float vHeight = height * size;

				//描画領域の計算
				viewRect = new Rect((int) (startRect.left - vWidth / 2),
						(int) (startRect.top - vHeight / 2),
						(int) (startRect.left + vWidth / 2),
						(int) (startRect.top + vHeight / 2));
				
				//45度まで処理。
				if (rot < 45) {
					sendEmptyMessageDelayed(1, 5);
				}else{
					handlerFlg = false;
					isOpen = true;
				}
			} else {
				super.dispatchMessage(msg);
			}
		}
	};
	
	/**
	 * ウィンドウクローズアニメーションハンドラー
	 * 45度から0度までの正弦の値を係数にしてアニメーションを行う。
	 */
	private final Handler closeAnimation = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			if (msg.what == 1) {
				
				handlerFlg = true;

				rot = rot - 1;

				float rad = (float) (rot * Math.PI / 180) * 2;
				float size = (float) Math.sin(rad);

				// 幅、高さ計算
				int width = endRect.right - endRect.left;
				int height = endRect.bottom - endRect.top;

				// 表示幅、高さ計算
				float vWidth = width * size;
				float vHeight = height * size;

				//描画領域の計算
				viewRect = new Rect((int) (startRect.left - vWidth / 2),
						(int) (startRect.top - vHeight / 2),
						(int) (startRect.left + vWidth / 2),
						(int) (startRect.top + vHeight / 2));
				
				//45度まで処理。
				if (rot > 0) {
					sendEmptyMessageDelayed(1, 5);
				}else{
					handlerFlg = false;
					isOpen = false;
				}
			} else {
				super.dispatchMessage(msg);
			}
		}
	};

}
