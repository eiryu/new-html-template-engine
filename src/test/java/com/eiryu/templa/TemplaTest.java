package com.eiryu.templa;

import org.junit.Test;

import java.io.File;

/**
 * Created by eiryu on 2015/07/14.
 */
public class TemplaTest {

    @Test
    public void ファイルから入力() {
        Templa.process(new File("src/test/resources/input.txt"));
    }

    @Test
    public void 文字列から入力() {
        String input =
                "html lang:ja\n" +
                        "\thead\n" +
                        "\t\ttitle :test\n" +
                        "body\n" +
                        "\tdiv\n" +
                        "\t\tp";

        Templa.process(input);
    }
}
