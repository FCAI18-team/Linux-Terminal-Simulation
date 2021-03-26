import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;


public class Terminal {
    private File currdir = null;
    private String defpath = "C:\\Users\\"+System.getProperty("user.name")+"\\Documents";
    public Terminal(File dir){
        currdir = new File(dir.getAbsolutePath());
    }
    private String getAbs(String path){
        Path p = Paths.get(path);
        String tmp = path;
        if(!p.isAbsolute()) path = currdir.getAbsolutePath()+'\\'+tmp;
        return path;
    }
    public String cd(String destination) {
        boolean flag = false;
        if (destination.length() == 3){
            flag = true;
        }
        else if (destination.equals("."))
        {
            destination = currdir.getAbsolutePath();
            flag = true;
        }
        else if(destination.equals("~"))
        {
            destination = defpath;
            flag = true;
        }
        else if (destination.equals("..")) {
            try {
                File parent = new File(currdir.getParent());
                if (!parent.exists()) {
                } else {
                    destination = currdir.getParent();
                    flag = true;
                }
            }
            catch(NullPointerException n) { }
        }
        else{
            try {
                File child = new File(getAbs(destination));
                File parent = child.getParentFile();
                File[] exist = ls(parent.getAbsolutePath());
                for (File f : exist) {
                    if (f.getAbsolutePath().compareTo(child.getAbsolutePath()) == 0) {
                        flag = true;
                        break;
                    }
                }
            }
            catch (NullPointerException n){
            }
        }
        if(!flag) {
            System.out.println("Directory not found.");
            return currdir.getAbsolutePath();
        }
        File direc = new File(getAbs(destination));
        if(!direc.exists()) {
            System.out.println("Directory not found.");
            return currdir.getAbsolutePath();
        }
        currdir = direc;
        return currdir.getAbsolutePath();
    }
    public File[] ls(String curr){
        File direc = new File(curr);
        return direc.listFiles();
    }
    public boolean cp(String[] args) throws IOException {
        File source = new File(getAbs(args[0]));
        File dest = new File(getAbs(args[1])+'\\'+source.getName());
        InputStream is = null;
        OutputStream os = null;
        boolean flag = false;
        try {
            File parent = new File(getAbs(args[1]));
            if(source.exists() && parent.exists()) {
                flag = true;
                is = new FileInputStream(source);
                os = new FileOutputStream(dest);
                byte[] buff = new byte[4096];
                int len;
                while ((len = is.read(buff)) > 0) {
                    os.write(buff, 0, len);
                }
            }
        } catch (IOException e){
           try {
               is.close();
               os.close();
           }
           catch (NullPointerException n)
           {}
        }
        return flag;
    }
    public ArrayList cat(String path) throws IOException {
        File source = new File(getAbs(path));
        ArrayList<String> FileContents = new ArrayList<>();
        if (source.exists()) {
            Scanner input = new Scanner(source);
            while (input.hasNextLine()) {
                FileContents.add(input.nextLine());
            }
        }
        else {
            FileContents.add("No file exists with that name");
            return FileContents;
        }

        return FileContents;
    }
    public boolean more(String path)throws IOException{
        File source = new File(getAbs(path));
        if(source.exists()) {
            Scanner input = new Scanner(source);
            Scanner userInput = new Scanner(System.in);
            boolean flag = false;
            String userChoice = "";
            while (input.hasNextLine()) {
                for (int i = 0; i < 10; i++) {
                    if (input.hasNextLine())
                        System.out.println(input.nextLine());
                    else {
                        flag = true;
                        break;
                    }
                }
                if(!flag) {
                    System.out.println("Do you want to continue?(Y/N)");
                    userChoice = userInput.nextLine();
                    if (userChoice.compareToIgnoreCase("n") == 0 )
                        break;
                }
                else
                    break;
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean mkdir(String path){
        File direc = new File(getAbs(path));
        return direc.mkdir();
    }
    public boolean rmdir(String path) {
        try {
            File rmv = new File(getAbs(path));
            String[] files = rmv.list();
            if (files.length > 0) return false;
            rmv.delete();
        }
        catch (NullPointerException n)
        {
            return false;
        }
        return true;
    }
    public boolean mv(String[] args) throws IOException {
        boolean ret = cp(args);
        return ret && rm(args[0]);
    }
    public boolean rm(String sourcePath) {
        File rmv = new File(getAbs(sourcePath));
        System.gc();
        return rmv.delete();
    }
    public ArrayList args(String command){
        ArrayList<String> line = new ArrayList<>();
        if(command.compareTo("cd") == 0)
           line.add("arg1: Directory path.");
        if(command.compareTo("ls") == 0)
            line.add("No need for arguments.");
        if(command.compareTo("cp") == 0)
            line.add("arg1: SourcePath, arg2: DestinationPath");
        if(command.compareTo("cat") == 0)
            line.add("arg1: FileName");
        if(command.compareTo("more") == 0)
            line.add("arg1: FileName");
        if(command.compareTo("mkdir") == 0)
            line.add("arg1: Directory path/name");
        if(command.compareTo("rmdir") == 0)
            line.add("arg1: Directory path/name");
        if(command.compareTo("mv") == 0)
            line.add("arg1: SourcePath, arg2: DestinationDirectory");
        if(command.compareTo("rm") == 0)
            line.add("arg1: FileName");
        if(command.compareTo("args") == 0)
            line.add("arg1: CommandName");
        if(command.compareTo("date") == 0)
            line.add("No need for arguments");
        if(command.compareTo("help") == 0)
            line.add("No need for arguments");
        if(command.compareTo("pwd") == 0)
            line.add("No need for arguments");
        if(command.compareTo("clear") == 0)
            line.add("No need for arguments");
        if(command.compareTo("exit") == 0)
            line.add("No need for arguments");
        return line;
    }
    public ArrayList date(){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime currDate = LocalDateTime.now();
        String date = dateFormat.format(currDate);
        ArrayList<String> line = new ArrayList<>();
        line.add(date);
        return line;
    }
    public ArrayList help(){
        ArrayList<String> line = new ArrayList<>();
        line.add("cd : Change current directory");
        line.add("ls : list all files in current directory");
        line.add("cd : Change current directory");
        line.add("ls : list all files in current directory");
        line.add("cp : Copy file from directory to another");
        line.add("cat : Show contents of a file");
        line.add("more : View text files displaying 1 screen at a time");
        line.add("mkdir : Create new directory");
        line.add("rmdir : Delete Directory");
        line.add("mv : Move file from directory to another");
        line.add("rm : Delete file from directory");
        line.add("args :  List any command arguments");
        line.add("date : Current Date/time");
        line.add("clear : Clear terminal screen");
        line.add("pwd : Prints absolute path for current directory");
        line.add("exit : Stop all");
        return line;
    }
    public void clear() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }
    public ArrayList pwd() {
        ArrayList<String> line = new ArrayList<>();
        line.add(currdir.getAbsolutePath());
        return line;
    }
    public void writeList(File[] list , String target) {
        ArrayList<String> lines = new ArrayList<>();
        String path = getAbs(target);
        for (int i = 0; i < list.length; i++) {
            lines.add(list[i].getName());
        }
        try {
            PrintWriter writer = new PrintWriter(new File(path));
            for(int i=0;i<lines.size();i++)
            {
                writer.write(lines.get(i));
                writer.write("\n");
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void appendList(File[] list , String target) {
        ArrayList<String> lines = new ArrayList<>();
        String path = getAbs(target);
        for (int i = 0; i < list.length; i++) {
            lines.add(list[i].getName());
        }
       try {
           PrintWriter writer = new PrintWriter(new FileOutputStream(new File(path),true));
           for(int i=0;i<lines.size();i++)
           {
               writer.append(lines.get(i));
               writer.append("\n");
           }
           writer.close();
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }
    }
    public void writeString(ArrayList<String> lines , String target) {
        String path = getAbs(target);
        try {
            PrintWriter writer = new PrintWriter(new File(path));
            for(int i=0;i<lines.size();i++)
            {
                writer.write(lines.get(i));
                writer.write("\n");
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void appendString(ArrayList<String> lines , String target) {
        String path = getAbs(target);
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File(path),true));
            for(int i=0;i<lines.size();i++)
            {
                writer.write(lines.get(i));
                writer.write("\n");
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
