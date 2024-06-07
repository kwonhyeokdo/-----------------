package smallcat;

import java.util.Arrays;
import java.io.UnsupportedEncodingException;

public class UrlDecoder {

    // 16진수 2자리를 ASCII코드로 나타내는 byte를, int로 변환
    private static int hex2int(byte b1, byte b2) {
        int digit;
        if (b1 >= 'A') {
            // 0xDF와의 &로 소문자를 대문자로 변환
            digit = (b1 & 0xDF) - 'A' + 10;
        } else {
            digit = (b1 - '0');
        }
        digit *= 16;
        if (b2 >= 'A') {
            digit += (b2 & 0xDF) - 'A' + 10;
        } else {
            digit += b2 - '0';
        }

        return digit;
    }

    public static String decode(String src, String enc) throws UnsupportedEncodingException {
        byte[] srcBytes = src.getBytes("ISO_8859_1");
        // 변환된 쪽이 길어질 일이 없기 땜누에, srcByte
        // 길이의 배열을 일단 확보.
        byte[] destBytes = new byte[srcBytes.length];

        int destIdx = 0;
        for (int srcIdx = 0; srcIdx < srcBytes.length; srcIdx++) {
            if (srcBytes[srcIdx] == (byte)'%') {
                destBytes[destIdx] = (byte)hex2int(srcBytes[srcIdx + 1],
                                                   srcBytes[srcIdx + 2]);
                srcIdx += 2;
            } else {
                destBytes[destIdx] = srcBytes[srcIdx];
            }
            destIdx++;
        }
        byte[] destBytes2 = Arrays.copyOf(destBytes, destIdx);

        return new String(destBytes2, enc);
    }

}
