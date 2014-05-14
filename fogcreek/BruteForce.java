public class BruteForce {

    static final String available = "acdegilmnoprstuw";
    static final char[] letters = available.toCharArray();

    static final long TARGET_HASH = 910897038977002L;


    public static void main(String[] args) {
        bruteForce(TARGET_HASH);
    }

    static long hash(char[] s) {
        long h = 7;
        for(int i = 0; i < s.length; i++) {
            char c = s[i];
            h = (h * 37 + available.indexOf(c));
        }
        return h;
    }

    static void bruteForce(long targetHash) {
        long startMillis = System.currentTimeMillis();

        char[] res = new char[9];
        boolean found = false;

        BEGIN: for (char i0 : letters) {
            if (found) break;
            res[0] = i0;
            for (char i1 : letters) {
                res[1] = i1;
                for (char i2 : letters) {
                    res[2] = i2;
                    for (char i3 : letters) {
                        res[3] = i3;
                        for (char i4 : letters) {
                            res[4] = i4;
                            for (char i5 : letters) {
                                res[5] = i5;
                                for (char i6 : letters) {
                                    res[6] = i6;
                                    for (char i7 : letters) {
                                        res[7] = i7;
                                        for (char i8 : letters) {
                                            res[8] = i8;
                                            if (hash(res) == targetHash) {
                                                found = true;
                                                break BEGIN;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (found) {
            System.out.println("result: ");
            System.out.println(res);
        } else {
            System.out.println("no result");
        }

        long elapsedMillis = System.currentTimeMillis() - startMillis;
        System.out.println("elapsed (ms): " + elapsedMillis);
    }
}
