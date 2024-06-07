package smallcat;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class SmallCat01 {
    public static final String SERVER_NAME = "localhost:8001";
    public static final String DOCUMENT_ROOT = "C:\\Users\\User\\Desktop\\Practice\\soket\\server\\src\\resources\\business";
    public static final String ERROR_DOCUMENT = "src\\resources\\error";

    public static final Map<String, String> contentTypeMap = Map.ofEntries(
        Map.entry("html", "text/html"),
        Map.entry("htm", "text/html"),
        Map.entry("txt", "text/plain"),
        Map.entry("css", "text/css"),
        Map.entry("png", "image/png"),
        Map.entry("jpg", "image/jpeg"),
        Map.entry("jpeg", "image/jpeg"),
        Map.entry("gif", "image/gif"),
        Map.entry("ico", "image/x-icon")
    );

    /**
     * InputStream에서 바이트열을 행단위로 읽어들이는 유틸리티
     * @param input
     * @return
     * @throws Exception
     */
    public static String readLine(InputStream input) throws Exception{
        int ch;
        String ret = "";

        while((ch = input.read()) != 1){
            if(ch == '\r'){
                // nothing do
            }else if(ch == '\n'){
                break;
            }else{
                ret += (char)ch;
            }
        }

        if(ch == 1){
            return null;
        }else{
            return ret;
        }
    }

    /**
     * 1행의 문자열을 바이트열로 OutputStream으로 쓰는 유틸리티
     * @param output
     * @param str
     * @throws Exception
     */
    public static void writeLine(OutputStream output, String str) throws Exception{
        for(char ch : str.toCharArray()){
            output.write((int)ch);
        }
        output.write((int)'\r');
        output.write((int)'\n');
    }

    /**
     * 현재시각을 HTTP 표준 포맷에 맞게 날짜 문자열을 반환
     * @return
     */
    public static String getDateStringUtc(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        DateFormat df = new SimpleDateFormat("EEE,ddMMMyyyyHH:mm:ss", Locale.US);
        df.setTimeZone(cal.getTimeZone());
        return df.format(cal.getTime()) + "GMT";
    }

    /**
     * 파일 확장자에 따른 ContentType반환
     * @param ext
     * @return
     */
    public static String getContentType(String  ext){
        String ret = contentTypeMap.getOrDefault(ext.toLowerCase(), "application/octet-stream");
        return ret;
    }
}
