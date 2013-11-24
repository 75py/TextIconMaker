package com.nagopy.tools.texticonmaker;

/**
 * 連番管理クラス.
 */
public class SerialNumber {

	private int number;
	private boolean enabled;

	/**
	 * コンストラクタ.<br>
	 * 0から開始。
	 */
	public SerialNumber() {
		this(0);
	}

	/**
	 * コンストラクタ.
	 * 
	 * @param start
	 *            開始番号
	 */
	public SerialNumber(int start) {
		this.number = start;
		this.enabled = true;
	}

	/**
	 * 連番が有効かどうかを返す.
	 * 
	 * @return 有効：true 無効：false
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * 連番の有効・無効を設定する.
	 * 
	 * @param enabled
	 *            有効：true 無効：false
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * 現在のカウントを取得する.
	 * 
	 * @return 現在のカウント
	 */
	public int getCurrent() {
		return this.number;
	}

	/**
	 * カウントを取得し、番号を一つ先に進める.
	 * 
	 * @return カウント
	 */
	public int next() {
		if (!isEnabled()) {
			throw new IllegalStateException("連番は無効です");
		}
		return number++;
	}

	/**
	 * カウントを取得し、番号を一つ先に進める.<br>
	 * 連番が無効になっている場合は""を返す。
	 * 
	 * @return カウント
	 */
	public String nextString() {
		return isEnabled() ? String.valueOf(next()) : "";
	}
}
