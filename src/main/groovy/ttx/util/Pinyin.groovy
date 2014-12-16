package ttx.util

import net.sourceforge.pinyin4j.PinyinHelper

/**
 * Created by journey on 14-12-12.
 */
class Pinyin {
    static String toHintCode(String str) {
        StringBuffer b = new StringBuffer()
        for (char c : str) {
            def pinyins = PinyinHelper.toHanyuPinyinStringArray(c)
            if (pinyins) {
                b.append(pinyins[0][0])
            } else {
                b.append(c)
            }
        }
        b
    }
}
