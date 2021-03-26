import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String in = "";
        Scanner input = new Scanner(System.in);
        File currFile = new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents");
        System.getProperty("file.separator");

        while(in.compareTo("exit") != 0) {
            System.out.print(currFile.getAbsolutePath()+">");
            in = input.nextLine();
            Parser prs = new Parser(currFile);
            if(!prs.parse(in) && in.compareTo("exit") != 0){
                System.out.println("Couldn't execute command..use 'help' / 'args' for instructions");
            }
            else{
                currFile = prs.getDir();
            }
        }
    }
}