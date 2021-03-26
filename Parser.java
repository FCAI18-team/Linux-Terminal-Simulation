import java.io.File;
import java.io.IOException;

public class Parser {
    private String[] args = {"","",""};
    private String cmd = "";
    private String[] cmdList =
            {"cd", "ls", "cp", "cat", "more", "mkdir", "rmdir", "mv", "rm", "args", "date", "help", "pwd", "clear", "exit"};
    private File currdir = null;
    private Terminal term = null;
    private String defpath = "C:\\Users\\"+System.getProperty("user.name")+"\\Documents";
    public Parser(File curr){
        currdir = new File (curr.getAbsolutePath());
        term = new Terminal(currdir);
    }
    public boolean parse(String input) throws IOException, InterruptedException {
        int idx = 0;
        for(int i = 0; i < input.length(); ++i) {
            if (input.charAt(i) == ' ') {
                idx = i;
                break;
            } else {
                cmd += input.charAt(i);
            }
        }
        boolean flag = false;
        for(String s : cmdList){
            if(s.equals(cmd)){
                flag = true;
                break;
            }
        }
        if(!flag){
            System.out.println("can't execute command, check command syntax or spelling.");
            return false;
        }
        int cnt = 0;
        char oper = '+';
        for(int i = 0; i < input.length(); ++i){
            if(input.charAt(i) == ' ')
                cnt++;
            else if(input.charAt(i) == '|' || input.charAt(i) == '>') {
                oper = input.charAt(i);
                idx = i;
            }
            else if(cnt != 0)
                args[cnt-1] += input.charAt(i);
        }
        if(cmd.equals("cd")){
            if(cnt == 0) {
                currdir = new File(term.cd(defpath));
                return true;
            }
            else if(cnt == 1) {
                currdir = new File(term.cd(args[0]));
                return true;
            }
        }
        else if(cmd.equals("ls")){
            if (cnt == 0) {
                for (File f : term.ls(currdir.getAbsolutePath()))
                    System.out.println(f.getName());
                return true;
            }
            else if (oper == '>' && input.charAt(idx-1) != '>'){
                    File[] list = term.ls(currdir.getAbsolutePath());
                    term.writeList(list, args[cnt-1]);
                    return true;
            }
            else if(oper == '>' && input.charAt(idx) == '>') {
                    File[] list = term.ls(currdir.getAbsolutePath());
                    term.appendList(list, args[cnt-1]);
                    return true;
            }
            else if(oper == '|')
            {
                if (args[cnt-1].equals("more"))
                {
                    // create a tmp text file >> list in it >> call more(tmp.txt) >> delete tmp.txt after the process
                    File[] list = term.ls(currdir.getAbsolutePath());
                    term.writeList(list, "tmp.txt");
                    term.more("tmp.txt");
                    term.rm("tmp.txt");
                    return true;
                }
            }
        }
        else if(cmd.equals("cp")){
            if (cnt == 2)
                return term.cp(args);
            else {}
        }
        else if(cmd.equals("cat")){
            if (cnt == 1){

                for(int i=0;i<term.cat(args[0]).size();i++)
                {
                    System.out.println(term.cat(args[0]).get(i));
                }
                return true;
            }
            else if(oper == '>' && input.charAt(idx-1) != '>')
            {
                term.writeString(term.cat(args[0]),args[cnt-1]);
                return true;

            }
            else if(oper == '>' && input.charAt(idx) == '>') {
                term.appendString(term.cat(args[0]),args[cnt-1]);
                return true;
            }
            else if(oper == '|')
            {
                if (args[cnt-1].equals("more")) {
                    term.writeString(term.cat(args[0]), "tmp.txt");
                    term.more("tmp.txt");
                    term.rm("tmp.txt");
                    return true;
                }
            }
        }
        else if(cmd.equals("more")){
            if (cnt == 1){
                return term.more(args[0]);
            }
            else {}
        }
        else if(cmd.equals("mkdir")){
            if(cnt == 1){
                return term.mkdir(args[0]);
            }
            else {}
        }
        else if(cmd.equals("rmdir")){
            if(cnt == 1){
                return term.rmdir(args[0]);
            }
            else {}
        }
        else if(cmd.equals("mv")){
            if(cnt == 2){
                return term.mv(args);
            }
            else {}
        }
        else if(cmd.equals("rm")){
            if(cnt == 1){
                return term.rm(args[0]);
            }
            else {}
        }
        else if(cmd.equals("args")){
            if(cnt == 1){
                System.out.println(term.args(args[0]).get(0));
                return true;
            }
            else if(oper == '>' && input.charAt(idx-1) != '>')
            {
                term.writeString(term.args(args[0]),args[cnt-1]);
                return true;

            }
            else if(oper == '>' && input.charAt(idx) == '>') {
                term.appendString(term.args(args[0]),args[cnt-1]);
                return true;
            }
        }
        else if(cmd.equals("date")){
            if(cnt == 0){
                System.out.println(term.date().get(0));
                return true;
            }
            else if(oper == '>' && input.charAt(idx-1) != '>')
            {
                term.writeString(term.date(),args[cnt-1]);
                return true;

            }
            else if(oper == '>' && input.charAt(idx) == '>') {
                term.appendString(term.date(),args[cnt-1]);
                return true;
            }
        }
        else if(cmd.equals("help")){
            if(cnt == 0){
                for(int i=0;i<term.help().size();i++)
                {
                    System.out.println(term.help().get(i));
                }
                return true;
            }
            else if(oper == '>' && input.charAt(idx-1) != '>')
            {
                term.writeString(term.help(),args[cnt-1]);
                return true;
            }
            else if(oper == '>' && input.charAt(idx) == '>') {
                term.appendString(term.help(),args[cnt-1]);
                return true;
            }
            else if(oper == '|')
            {
                if (args[cnt-1].equals("more")) {
                    term.writeString(term.help(),"tmp.txt");
                    term.more("tmp.txt");
                    term.rm("tmp.txt");
                    return true;
                }
            }
        }
        else if(cmd.equals("pwd")){
            if(cnt == 0){
                    System.out.println(term.pwd().get(0));
                return true;
            }
            else if(oper == '>' && input.charAt(idx-1) != '>')
            {
                term.writeString(term.pwd(),args[cnt-1]);
                return true;

            }
            else if(oper == '>' && input.charAt(idx) == '>') {
                term.appendString(term.pwd(),args[cnt-1]);
                return true;
            }
        }
        else if(cmd.equals("clear")){
            if(cnt == 0){
                term.clear();
                return true;
            }
            else {}
        }
        return false;
    }
    public File getDir(){
        return currdir;
    }
}