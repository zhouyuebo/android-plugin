package mobcb.android.plugin.core;


import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LibCopyUtils {


    public static void copyPluginSoLib(Context context, String apkPath, String libs) {
        String arch = getCpuArch();

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(apkPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while (entries.hasMoreElements()) {

            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }

            String entryName = entry.getName();
            if (entryName.endsWith(".so") && entryName.contains(arch)) {
                InputStream in = null;
                OutputStream os = null;
                try {

                    in = zipFile.getInputStream(entry);
                    os = new FileOutputStream(new File(libs, entryName.substring(entryName.lastIndexOf("/") + 1)));
                    writeStream(os, in);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (os != null) {
                            os.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                    }
                }

            }
        }

        try {
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void writeStream(OutputStream os, InputStream in)
            throws IOException {
        byte[] buffer = new byte[1024];
        int c;
        while ((c = in.read(buffer, 0, 1024)) != -1) {
            os.write(buffer, 0, c);
        }
        os.flush();
    }

    private static String getCpuArch() {

        String cpuName = getCpuName();

        if (cpuName.toLowerCase().contains("arm")) {
            return "armeabi";
        } else if (cpuName.toLowerCase().contains("x86")) {
            return "x86";
        } else if (cpuName.toLowerCase().contains("mips")) {
            return "mips";
        } else {
            return "armeabi";
        }
    }

    private static String getCpuName() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            String text = reader.readLine();
            reader.close();
            String[] array = text.split(":\\s+", 2);
            if (array.length >= 2) {
                return array[1];
            }
            return array[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
