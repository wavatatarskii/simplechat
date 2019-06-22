import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ChatWriter {

    public void write_to_chat(String message)
    {
        File newDir = new File("X:/Logs");
        File newFile = new File("X:/Logs/log.txt");
        newDir.mkdir();
        if(newFile.exists())
            System.out.println("Файл существует");
        else
        {
            System.out.println("Файл еще не создан");
        try
        {
            boolean created = newFile.createNewFile();
            if(created)
                System.out.println("Файл создан");
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }}
        try(FileWriter writer = new FileWriter(newFile, true))
        {
            writer.append(message+"\r\n");

            writer.close();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
}
