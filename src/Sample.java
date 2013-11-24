import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.nagopy.tools.texticonmaker.GradientColor;
import com.nagopy.tools.texticonmaker.TextIconMaker;

public class Sample {

	public static void main(String[] args) throws IOException {
		sample1();
		sample2();
		sample3();
	}

	private static void sample1() throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("test", "テスト");
		map.put("sample", "sample");
		map.put("icon", "アイコン");
		map.put("longtext", "あいうえおかきくけこ１２３４５あいうえお");
		map.put("shorttext", "aaa");
		map.put("test2", "テスト");

		TextIconMaker iconMaker = new TextIconMaker();
		iconMaker.font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
		iconMaker.remban.setEnabled(false);
		iconMaker.exportDir = "out/sample1/";
		for (Entry<String, String> entry : map.entrySet()) {
			iconMaker.createIcon(entry.getValue(), entry.getKey());
		}
	}

	private static void sample2() throws IOException {
		String[] strings = { "test\n改行", "test2", "アイコン作成テスト" };
		TextIconMaker iconMaker = new TextIconMaker();
		iconMaker.baseFileName = "";
		iconMaker.width = 100;
		iconMaker.height = 100;
		iconMaker.exportDir = "out/sample2/";
		iconMaker.font = new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 50);
		for (String text : strings) {
			iconMaker.createIcon(text);
		}
	}

	private static void sample3() throws IOException {
		String[] strings = { "test\n改行", "test2", "アイコン作成テスト", "サンプル", "日本",
				"アメリカ", "イギリス", "オバマ", "JAPAN" };
		TextIconMaker iconMaker = new TextIconMaker();
		iconMaker.baseFileName = "";
		iconMaker.width = 50;
		iconMaker.height = 30;
		iconMaker.exportDir = "out/sample3/";
		iconMaker.font = new Font(Font.SANS_SERIF, Font.ROMAN_BASELINE, 50);
		GradientColor gradientColor = new GradientColor(Color.RED, Color.PINK);
		for (int i = 0; i < strings.length; i++) {
			String text = strings[i];
			iconMaker.backgroundColor = gradientColor.getByHsb(strings.length,
					i);
			iconMaker.createIcon(text);
		}
	}

}
