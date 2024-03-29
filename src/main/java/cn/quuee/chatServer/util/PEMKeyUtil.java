package cn.quuee.chatServer.util;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.Thread.currentThread;
public class PEMKeyUtil {
    public static String readKeyAsString(String keyLocation) throws Exception {
        URI uri = currentThread().getContextClassLoader().getResource(keyLocation).toURI();
        byte[] byteArray = Files.readAllBytes(Paths.get(uri));
        return new String(byteArray);
    }
}
