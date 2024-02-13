import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    static StringBuilder temp = new StringBuilder();
    static Date date = new Date();
    public static String savePath = "D:/Games/savegames/";
    public static String zipFile = "D:/Games/savegames/zip.zip";

    public static void main(String[] args) {
        List<String> folders = Arrays.asList("src", "res", "savegames", "temp", "src/main", "src/test", "res/drawables", "res/vectors", "res/icons");
        List<String> files = Arrays.asList("src/main/Main.java", "src/main/Utils.java", "temp/temp.txt");
        folders.forEach(f -> folderCreation(f));
        files.forEach(f -> filesCreation(f));
        System.out.println(temp);
        tempWrite(temp);

        GameProgress gameProgress1 = new GameProgress(1, 0, 1, 0);
        GameProgress gameProgress2 = new GameProgress(2, 1, 2, 1);
        GameProgress gameProgress3 = new GameProgress(3, 2, 3, 2);

        saveGame(savePath + "save1.dat", gameProgress1);
        saveGame(savePath + "save2.dat", gameProgress2);
        saveGame(savePath + "save3.dat", gameProgress3);

        zipFiles(zipFile, savePath);
        deleteSaves(zipFile, savePath);

    }

    //Создание папок
    public static void folderCreation(String folderName) {
        File file = new File("D:/Games/" + folderName);
        if (file.mkdirs()) {
            temp.append("Directory " + file.getAbsolutePath() + " is created " + date + "\n");
        }
    }

    //Создание файлов
    public static void filesCreation(String fileName) {
        File file = new File("D:/Games/" + fileName);
        try {
            if (file.createNewFile()) {
                temp.append("File " + file.getName() + " is created " + date + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Запись в файл temp
    public static void tempWrite(StringBuilder sb) {
        try (FileWriter writer = new FileWriter("D:/Games/temp/temp.txt")) {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Сохранение
    public static void saveGame(String savePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(savePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Архивирование
    private static void zipFiles(String zipPath, String filesToZip) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath))) {
            File dirZipPath = new File(filesToZip);
            File dirFilesToZip = new File(zipPath);
            for (File file : dirZipPath.listFiles()) {
                if (!file.getName().equals(dirFilesToZip.getName())) {
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    fis.close();

                    ZipEntry entry = new ZipEntry(file.getName());
                    zout.putNextEntry(entry);
                    zout.write(buffer);
                    zout.closeEntry();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Удаление
    public static void deleteSaves(String zip, String path) {
        File zipFile = new File(zip);
        File pathFiles = new File(path);
        for (File file : pathFiles.listFiles()) {
            if (!file.getName().equals(zipFile.getName())) {
                file.delete();
            }
        }
    }
}
