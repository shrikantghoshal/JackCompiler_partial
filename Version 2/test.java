import java.util.*;
import java.io.*;

public class test {

    public static List<String> removeCommentsTest(String inputFile) throws IOException {
        FileReader fread = new FileReader(inputFile);
        List<String> charCode;

        String content = null;
        StringBuilder stringBuild = new StringBuilder();
        String separateLine = System.getProperty("line.separator"); // Java API
        LineNumberReader lineRead = new LineNumberReader(fread);
        content = lineRead.readLine();
        while (content != (null)) {
            int inLineCom = content.indexOf("//");
            if (inLineCom != -1)
                content = content.substring(0, inLineCom); // Delete the inline coment from the starting double-slash

            stringBuild.append(content);
            stringBuild.append(separateLine);
            content = lineRead.readLine();

        }
        String rmvMultilineCom = stringBuild.toString();
        String noComments = rmvMultilineCom.replaceAll("/\\*(?:.|[\\n\\r])*?\\*/", " ");

        List<String> conversionStrList = new ArrayList<String>(Arrays.asList(noComments.split("\n")));
        for (int count = 0; count < conversionStrList.size(); count++) {          //iterate through lines
            charCode = (Arrays.asList(conversionStrList.get(count).split("(?!^)")));
            System.out.println(charCode);
        }
        lineRead.close();
        fread.close();

        return conversionStrList;
    }

    public static void main(String args[]) throws IOException {

        List<String> store = new ArrayList<String>();
        try {
            store = removeCommentsTest(args[0]);

            for(int i = 0; i< store.size();i++){
                System.out.println(store.get(i));
            }
        } catch (IOException FileError) {
            System.out.print("fIle error");
        }

    }

}