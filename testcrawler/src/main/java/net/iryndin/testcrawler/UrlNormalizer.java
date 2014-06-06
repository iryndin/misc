package net.iryndin.testcrawler;

public class UrlNormalizer {

    public String normalize(String a) {
        a = a.toLowerCase().trim();

        if (!a.startsWith("http://") && ! a.startsWith("https://")) {
            a = "http://" + a;
        }
        while (a.endsWith("/")) {
            a = a.substring(0, a.length()-1);
        }
        int i = a.indexOf("?");
        if (i>-1) {
            a = a.substring(0, i);
        }
        i = a.indexOf("#");
        if (i>-1) {
            a = a.substring(0, i);
        }
        return a;
    }
}
