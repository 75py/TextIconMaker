package com.nagopy.tools.texticonmaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.imageio.ImageIO;

public class TextIconMaker {

	/**
	 * 保存ファイルの拡張子.<br>
	 * ピリオド＋拡張子
	 */
	public String extension;

	/** {@link SerialNumber} */
	public SerialNumber serialNumber;

	/** 背景色 */
	public Color backgroundColor;

	/** 文字色 */
	public Color textColor;

	/** 自動生成ファイル名 */
	public String baseFileName;

	/** フォント */
	public Font font;

	/** 幅 */
	public Integer width;

	/** 高さ */
	public Integer height;

	/** ファイル作成ディレクトリ */
	public String exportDir;

	/** 全ての文字が収まるよう自動でリサイズするかどうか */
	public Boolean autoResizeTextSize;

	/**
	 * コンストラクタ.<br>
	 * 設定値を変更したい場合は、フィールドを直接操作してください。
	 */
	public TextIconMaker() {
		extension = ".png";
		baseFileName = "test";
		serialNumber = new SerialNumber(1);
		backgroundColor = Color.WHITE;
		textColor = Color.BLACK;
		font = new Font(Font.SERIF, Font.PLAIN, 9);
		width = 50;
		height = 50;
		exportDir = "out/";
		autoResizeTextSize = true;
	}

	/**
	 * 文字アイコンを作成する.<br>
	 * ファイル名は自動生成。
	 * 
	 * @param text
	 *            アイコンの文字列
	 */
	public void createIcon(String text) throws IOException {
		createIcon(text, makeFileName());
	}

	/**
	 * 文字アイコンを作成する.
	 * 
	 * @param text
	 *            アイコンの文字列
	 * @param fileName
	 *            保存ファイル名。拡張子は自動で付与されるため不要。
	 */
	public void createIcon(String text, String fileName) throws IOException {
		Image image = createImage(width, height);
		Graphics graphics = image.getGraphics();

		// 背景色を設定
		setBackgroundColor(graphics, backgroundColor, 0, 0, width, height);

		// 文字色、文字を設定
		graphics.setColor(textColor);
		graphics.setFont(font);
		drawString((Graphics2D) graphics, text, 0, 0, width, height);

		// ファイルに保存
		save(image, fileName.endsWith(extension) ? fileName : fileName
				+ extension);
	}

	/**
	 * 自動生成のファイル名を作成するメソッド.
	 * 
	 * @return {@link TextIconMaker#baseFileName} + {@link TextIconMaker#serialNumber}
	 *         + {@link TextIconMaker#extension}
	 */
	private String makeFileName() {
		return baseFileName + serialNumber.nextString() + extension;
	}

	/**
	 * 文字を描画する.
	 * 
	 * @param graphics
	 *            {@link Graphics}
	 * @param str
	 *            描画する文字列
	 * @param x
	 *            描画開始X座標
	 * @param y
	 *            描画開始Y座標
	 * @param width
	 *            幅
	 * @param height
	 *            高さ
	 */
	private void drawString(Graphics graphics, String str, int x, int y,
			int width, int height) {
		String[] strs = str.split("\n");
		int drawPosY = 0;
		for (String s : strs) {
			drawPosY = drawStringMultiLine(graphics, s, x, drawPosY, width,
					height);

			// -1が返る＝文字が全て収まらなかった場合
			if (drawPosY == -1) {
				if (autoResizeTextSize) {
					graphics.setFont(new Font(font.getName(), font.getStyle(),
							graphics.getFont().getSize() - 1));
					drawString(graphics, str, x, drawPosY, width, height);
					return;
				} else {
					// 自動リサイズがオフの場合はここで終了
					break;
				}
			}

			if (drawPosY < height) {
				continue;
			} else {
				break;
			}
		}
	}

	/**
	 * 下記URLより。<br>
	 * <a
	 * href="http://ameblo.jp/blueskyame/entry-10989489344.html">http://ameblo
	 * .jp/blueskyame/entry-10989489344.html</a>
	 */
	private int drawStringMultiLine(Graphics graphics, String str, int x,
			int y, int width, int height) {
		Graphics2D graphics2d = (Graphics2D) graphics;
		AttributedString attributedString = new AttributedString(str);
		attributedString.addAttribute(TextAttribute.FONT, graphics2d.getFont());
		attributedString.addAttribute(TextAttribute.FOREGROUND,
				graphics2d.getColor());
		attributedString.addAttribute(TextAttribute.BACKGROUND,
				graphics2d.getBackground());

		AttributedCharacterIterator attributedCharacterIterator = attributedString
				.getIterator();

		FontRenderContext fontRenderContext = graphics2d.getFontRenderContext();

		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(
				attributedString.getIterator(), fontRenderContext);

		// 幅
		float formatWidth = width;

		// 描画位置
		float drawPosX = x;
		float drawPosY = y;

		int beginIndex = attributedCharacterIterator.getBeginIndex();
		int endIndex = attributedCharacterIterator.getEndIndex();

		lineMeasurer.setPosition(beginIndex);
		while (lineMeasurer.getPosition() < endIndex) {

			TextLayout layout = lineMeasurer.nextLayout(formatWidth);

			// 矩形領域からはみ出るので、処理を中断する
			if (drawPosY + layout.getAscent() + layout.getDescent()
					+ layout.getLeading() > y + height) {
				return -1;
				// break;
			}

			drawPosY += layout.getAscent();
			if (layout.isLeftToRight()) {
				drawPosX = x;
			} else {
				drawPosX = x + formatWidth - layout.getAdvance();
			}

			layout.draw(graphics2d, drawPosX, drawPosY);

			drawPosY += layout.getDescent() + layout.getLeading();
		}

		// 最後に描いたY座標を返す
		return (int) drawPosY;
	}

	/**
	 * {@link Image}を作成する.
	 * 
	 * @param width
	 *            幅
	 * @param height
	 *            高さ
	 * @return {@link Image}インスタンス
	 */
	private Image createImage(int width, int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
	}

	/**
	 * 背景色を設定する.
	 * 
	 * @param graphics
	 *            {@link Graphics}
	 * @param color
	 *            {@link Color}
	 * @param x
	 *            対象X座標
	 * @param y
	 *            対象Y座標
	 * @param width
	 *            幅
	 * @param height
	 *            高さ
	 */
	private void setBackgroundColor(Graphics graphics, Color color, int x,
			int y, int width, int height) {
		Graphics2D g2 = (Graphics2D) graphics;
		g2.setBackground(color);
		g2.clearRect(x, y, width, height);
	}

	/**
	 * {@link Image}をファイルに保存する.
	 * 
	 * @param image
	 * @param fileName
	 * @throws IOException
	 */
	private void save(Image image, String fileName) throws IOException {
		// ディレクトリを作成
		Files.createDirectories(Paths.get(exportDir));

		Path path = Paths.get(exportDir, fileName);
		// 作成済みのファイルがあれば削除
		Files.deleteIfExists(path);

		BufferedImage bufferedImage = createBufferedImage(image);
		if (!ImageIO.write(bufferedImage, "PNG", path.toFile())) {
			throw new IOException("フォーマットが対象外");
		}
	}

	/**
	 * {@link BufferedImage}を作成する.
	 * 
	 * @param image
	 *            {@link Image}
	 * @return {@link BufferedImage}
	 */
	private BufferedImage createBufferedImage(Image image) {
		BufferedImage bimg = new BufferedImage(image.getWidth(null),
				image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics g = bimg.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimg;
	}

}
