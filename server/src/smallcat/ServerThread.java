package smallcat;

import static smallcat.SmallCat01.readLine;
import static smallcat.SmallCat01.DOCUMENT_ROOT;
import static smallcat.SmallCat01.ERROR_DOCUMENT;
import static smallcat.SmallCat01.SERVER_NAME;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class ServerThread implements Runnable{
    private Socket socket;

    public ServerThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        OutputStream output = null;

        try{
            InputStream input = socket.getInputStream();

            String line = null;
            String path = null;
            String ext = null;
            String host = null;

            while((line = readLine(input)) != null){
                if(line.equals("")){
                    break;
                }

                if(line.startsWith("GET")){
                    // path = line.split(" ")[1];
                    path = UrlDecoder.decode(line.split(" ")[1], "UTF-8");
                    String[] tmp = path.split("\\.");
                    ext = tmp[tmp.length - 1];
                }else if(line.startsWith("Host")){
                    host = line.substring("Host: ".length());
                }
            }

            if(path == null){
                return;
            }else if(path.endsWith("/")){
                path += "index.html";
                ext = "html";
            }

            output = new BufferedOutputStream(socket.getOutputStream());

            FileSystem fs = FileSystems.getDefault();
            Path pathObj = fs.getPath(DOCUMENT_ROOT + path);
            Path realPath = null;
            try{
                realPath = pathObj.toRealPath();
            }catch(NoSuchFileException e){
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
                return;
            }

            if(!realPath.startsWith(DOCUMENT_ROOT)){
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
                return;
            }else if(Files.isDirectory(realPath)){
                String location = "http://" + ((host != null) ? host : SERVER_NAME) + path + "/";
                SendResponse.sendMovePermanentlyResponse(output, location);
                return;
            }

            try(InputStream fis = new BufferedInputStream(Files.newInputStream(realPath))){
                SendResponse.sendOkResponse(output, fis, ext);
            }catch(FileNotFoundException e){
                SendResponse.sendNotFoundResponse(output, ERROR_DOCUMENT);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(output != null){
                    output.close();
                }
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
