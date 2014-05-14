public class ReverseHash {

    static final String letters = "acdegilmnoprstuw";

    static final long TARGET_HASH = 910897038977002L;


    public static void main(String[] args) {
        String s = reverseHash(TARGET_HASH);
        System.out.println(s);
    }

    static long hash(String s) {
        long h = 7;
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            h = (h * 37 + letters.indexOf(c));
        }
        return h;
    }

    static String reverseHash(long hash) {
        String res = "";

        while (hash > 7) {
            long i = hash % 37;
            if (i >= letters.length()) {
                throw new IllegalStateException("no solution");
            }
            hash = (hash - i)/37;
            res = letters.charAt((int)i)+res;
        }

        return res;
    }
}
