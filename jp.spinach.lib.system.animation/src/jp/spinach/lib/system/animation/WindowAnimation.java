package jp.spinach.lib.system.animation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;

/**
 * �E�B���h�E�̃A�j���[�V������`�悷��N���X�B �C���X�^���X�����ė��p����B
 * 
 * ���̃N���X�́A���g�Ƀr�b�g�}�b�v�ƕ`��̈�p��Rect�C���X�^���X���i�[����B
 * public���\�b�h�ł���A�j���[�V�����X�^�[�g�����s���邱�Ƃœ��I��Rect�C���X�^���X�̃J�^�`��ς���B
 * 
 * �`�掩�̂�View�N���X�ōs���K�v���L�邽�߁A���̃N���X�ŕ`��͍s��Ȃ��B
 * 
 * �܂��A���̃N���X���C���X�^���X������ہAView�̃R���X�g���N�^�ōs���K�v������B
 * ���r���[�̃��C���X���b�h�J�n��ɂ�Android�̃X���b�h�P�ꉻ�̊ϓ_����C���X�^���X���o���Ȃ��B
 * 
 * @author hiroki
 * 
 */
public class WindowAnimation {
	
	/**
	 * �����̃n���h���[�͈�x��1�܂ł����N���ł��Ȃ��B
	 * ���̐����S�ۂ��邽�߂̃t���O�B
	 */
	private boolean handlerFlg = false;
	
	/**
	 * �E�B���h�E�����ɊJ���Ă��邩��ێ�����t���O
	 */
	public boolean isOpen;

	/**
	 * �\������Bitmap��ݒ�B
	 */
	public Bitmap window;

	/**
	 * �ŏI�I�ȕ`��̈���i�[����B
	 */
	private Rect endRect;

	/**
	 * �A�j���[�V�����J�n���̕`��̈���i�[����B
	 * ��{�͓_
	 */
	private Rect startRect;

	/**
	 * �摜���̕`��T�C�Y��ݒ肷��B �X�v���C�g��1�P�ʂ̐ݒ�
	 */
	public Rect drawRect;

	/**
	 * ��ʏ�̕`��̈�B ���I�ɕω�����B
	 * View���ŕ`�ʂɗ��p����̂͂���
	 */
	public Rect viewRect;

	/**
	 * �v�Z�ɗ��p����p�x�B
	 */
	private int rot;

	/**
	 * �R���X�g���N�^�B �r�b�g�}�b�v���Z�b�g����B
	 * 
	 * @param res
	 * @param resourceId
	 */
	public WindowAnimation(Resources res, int resourceId) {
		window = BitmapFactory.decodeResource(res, resourceId);
	}

	/**
	 * �摜�ɑ΂��Ă̕`��̈�̐ݒ�B�X�v���C�g�̏������B
	 */
	public void initializeSprite(int left, int top, int width, int height) {
		drawRect = new Rect(left, top, width, height);
	}

	/**
	 * �I�[�v���E�B���h�E�A�j���[�V�����I�����̌�������ݒ肷��B
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
	 * �\���A�j���[�V�����n���h���[�̃L�b�N���\�b�h
	 * ���s���Ƀn���h���[���N���ςŖ������Ƃ�����
	 */
	public void startOpenAnimation() {
		if(handlerFlg){
			//��O�ł������邩�E�E�E�H
		}else{
			rot = 0;
			openAnimation.sendEmptyMessageDelayed(1, 5);
		}
	}
	
	/**
	 * �E�B���h�E��������A�j���[�V�����̃n���h���[�L�b�N���\�b�h
	 * ���s���Ƀn���h���[���N���ςŖ������Ƃ�����
	 */
	public void startCloseAnimation() {
		if(handlerFlg){
			//��O�ł������邩�E�E�E�H
		}else{
			rot = 45;
			closeAnimation.sendEmptyMessageDelayed(1, 5);
		}
	}

	/**
	 * �E�B���h�E�I�[�v���A�j���[�V�����n���h���[
	 * 0�x����45�x�܂ł̐����̒l���W���ɂ��ăA�j���[�V�������s���B
	 */
	private final Handler openAnimation = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			if (msg.what == 1) {
				
				handlerFlg = true;

				rot = rot + 1;

				float rad = (float) (rot * Math.PI / 180) * 2;
				float size = (float) Math.sin(rad);

				// ���A�����v�Z
				int width = endRect.right - endRect.left;
				int height = endRect.bottom - endRect.top;

				// �\�����A�����v�Z
				float vWidth = width * size;
				float vHeight = height * size;

				//�`��̈�̌v�Z
				viewRect = new Rect((int) (startRect.left - vWidth / 2),
						(int) (startRect.top - vHeight / 2),
						(int) (startRect.left + vWidth / 2),
						(int) (startRect.top + vHeight / 2));
				
				//45�x�܂ŏ����B
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
	 * �E�B���h�E�N���[�Y�A�j���[�V�����n���h���[
	 * 45�x����0�x�܂ł̐����̒l���W���ɂ��ăA�j���[�V�������s���B
	 */
	private final Handler closeAnimation = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			if (msg.what == 1) {
				
				handlerFlg = true;

				rot = rot - 1;

				float rad = (float) (rot * Math.PI / 180) * 2;
				float size = (float) Math.sin(rad);

				// ���A�����v�Z
				int width = endRect.right - endRect.left;
				int height = endRect.bottom - endRect.top;

				// �\�����A�����v�Z
				float vWidth = width * size;
				float vHeight = height * size;

				//�`��̈�̌v�Z
				viewRect = new Rect((int) (startRect.left - vWidth / 2),
						(int) (startRect.top - vHeight / 2),
						(int) (startRect.left + vWidth / 2),
						(int) (startRect.top + vHeight / 2));
				
				//45�x�܂ŏ����B
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
