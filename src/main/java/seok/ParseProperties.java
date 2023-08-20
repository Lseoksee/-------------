package seok;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import java.io.BufferedReader;
import java.awt.FileDialog;
import java.awt.Frame;

public class ParseProperties extends Main {

    public Properties properties;
    public HashMap<String, String> defmap;

    public ParseProperties() throws IOException {
        properties = new Properties();
        properties.load(new FileReader(propertiesfile));

        defmap = new HashMap<>();
        defmap.put("survival", "0");
        defmap.put("creative", "1");
        defmap.put("adventure", "2");
        defmap.put("spectator", "3");
        defmap.put("peaceful", "0");
        defmap.put("easy", "1");
        defmap.put("normal", "2");
        defmap.put("hard", "3");

        // gamemode, difficulty 치환
        String gamemode = properties.getProperty("gamemode");
        String difficulty = properties.getProperty("difficulty");
        properties.replace("gamemode", defmap.getOrDefault(gamemode, gamemode));
        properties.replace("difficulty", defmap.getOrDefault(difficulty, difficulty));
    }

    // properties 저장하기
    public void saveProperties(HashMap<String, Object> value) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(propertiesfile));
        String sp[] = new String[2];
        StringBuffer sb = new StringBuffer();

        String read;
        while ((read = br.readLine()) != null) {
            sp = read.split("=", 2);
            Object curline = value.get(sp[0]);
            if (curline != null) {
                sb.append(sp[0] + "=" + curline + "\n");
            } else {
                sb.append(read + "\n");
            }
        }
        br.close();

        FileOutputStream fos = new FileOutputStream(propertiesfile);
        fos.write(sb.toString().getBytes("utf-8"));
        fos.flush();
        fos.close();
    }

    // 불러온 값과 현제 값이 같은지 확인    
    public Boolean isModify(HashMap<String, Object> values) {
        for (Object object : values.keySet().toArray()) {
            if (!properties.get(object).equals(values.get(object).toString())) {
                return true;
            }
        }
        return false;
    }

    // 파일선택
    public static String filedia(Frame fr, String scanfile, String messege, int whatDialog, boolean allpath) {
        // 프레임, 파일, 메세지, 방식(0=불러오기,1저장하기), 전체경로?
        try {
            FileDialog fileDialogOpen = new FileDialog(fr, messege, whatDialog);
            fileDialogOpen.setFile(scanfile);
            fileDialogOpen.setDirectory(new File("").getAbsolutePath());
            fileDialogOpen.setVisible(true);
            String file = fileDialogOpen.getFile();
            if (allpath && file != null)
                return fileDialogOpen.getDirectory() + file;
            else
                return file;
        } catch (NullPointerException e) {
            return null;
        }
    }

    // String to 유니코드
    public static String escapeToUnicode(String input) {
        StringBuilder builder = new StringBuilder();

        for (char ch : input.toCharArray()) {
            if (ch < 128) {
                builder.append(ch);
            } else {
                builder.append("\\u").append(String.format("%04X", (int) ch));
            }
        }
        return builder.toString();
    }
}