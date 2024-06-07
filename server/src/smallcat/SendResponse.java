package smallcat;

import static smallcat.SmallCat01.writeLine;
import static smallcat.SmallCat01.getDateStringUtc;
import static smallcat.SmallCat01.getContentType;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class SendResponse {

    public static void sendNotFoundResponse(OutputStream output, String errorDocument) throws Exception {
        writeLine(output, "HTTP/1.1 404 Not Found");
        writeLine(output, "Date:" + getDateStringUtc());
        writeLine(output, "Server:SmallCat/0.2");
        writeLine(output, "Connection: close");
        writeLine(output, "Content-Type: text/html");
        writeLine(output, "");

        try (InputStream fis = new BufferedInputStream(new FileInputStream(errorDocument + "/404.html"))) {
            int ch;
            while((ch = fis.read()) != -1){
                output.write(ch);
            }
        }
    }

    public static void sendMovePermanentlyResponse(OutputStream output, String location) throws Exception {
        writeLine(output, "HTTP/1.1 301 Moved Permanently");
        writeLine(output, "Date:" + getDateStringUtc());
        writeLine(output, "Server:SmallCat/0.2");
        writeLine(output, "Location:" + location);
        writeLine(output, "Connection: close");
        writeLine(output, "");
    }

    public static void sendOkResponse(OutputStream output, InputStream fis, String ext) throws Exception {
        writeLine(output, "HTTP/1.1 200 OK");
        writeLine(output, "Date:" + getDateStringUtc());
        writeLine(output, "Server:SmallCat/0.2");
        writeLine(output, "Connection: close");
        writeLine(output, "Content-Type:" + getContentType(ext));
        writeLine(output, "");

        int ch;
        while((ch = fis.read()) != -1){
            output.write(ch);
        }
    }

}
