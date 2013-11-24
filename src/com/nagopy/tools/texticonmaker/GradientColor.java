package com.nagopy.tools.texticonmaker;

import java.awt.Color;

/**
 * グラデーション作成クラス.<br>
 * 参考：<a href=
 * "http://www.hm.aitai.ne.jp/~m-ito/rakugakika/applet/Gradation/index.html"
 * >http://www.hm.aitai.ne.jp/~m-ito/rakugakika/applet/Gradation/index.html</a>
 * 
 */
public class GradientColor {

	public Color color1;
	public Color color2;

	/**
	 * コンストラクタ.
	 * 
	 * @param color1
	 *            開始の色
	 * @param color2
	 *            終了の色
	 */
	public GradientColor(Color color1, Color color2) {
		this.color1 = color1;
		this.color2 = color2;
	}

	/**
	 * RGBで計算して色を返す.
	 * 
	 * @param total
	 *            分割する回数
	 * @param index
	 *            現在の位置
	 * @return {@link Color}
	 */
	public Color getByRgb(int total, int index) {
		int difR = (color2.getRed() - color1.getRed()) / total;
		int difG = (color2.getGreen() - color1.getGreen()) / total;
		int difB = (color2.getBlue() - color1.getBlue()) / total;

		return new Color(color1.getRed() + difR * index, color1.getGreen()
				+ difG * index, color1.getBlue() + difB * index);
	}

	/**
	 * HSBで計算して色を返す.
	 * 
	 * @param total
	 *            分割する回数
	 * @param index
	 *            現在の位置
	 * @return {@link Color}
	 */
	public Color getByHsb(int total, int index) {
		float aval[] = new float[3];
		float bval[] = new float[3];

		Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(),
				aval);
		Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(),
				bval);

		float difH = (bval[0] - aval[0]) / total;
		float difS = (bval[1] - aval[1]) / total;
		float difB = (bval[2] - aval[2]) / total;

		return Color.getHSBColor(aval[0] + difH * index,
				aval[1] + difS * index, aval[2] + difB * index);
	}

}
