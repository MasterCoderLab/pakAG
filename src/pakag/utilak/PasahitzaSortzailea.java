package pakag.utilak;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PasahitzaSortzailea klaseak pasahitz seguruak automatikoki sortzen ditu.
 * Gutxienez 8 karaktere, letra larria, letra xehea, zenbakia eta karaktere berezia izango ditu.
 */
public class PasahitzaSortzailea {

    private static final String LARRIAK = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String XEHEAK = "abcdefghijklmnopqrstuvwxyz";
    private static final String ZENBAKIAK = "0123456789";
    private static final String BEREZIAK = "!@#$%&*?";
    private static final String GUZTIAK = LARRIAK + XEHEAK + ZENBAKIAK + BEREZIAK;

    private static final SecureRandom random = new SecureRandom();

    /**
     * Pasahitz berri bat sortzen du.
     *
     * @return sortutako pasahitza
     */
    public static String sortu() {
        int luzera = 10;

        List<Character> karaktereak = new ArrayList<>();

        karaktereak.add(ausazkoKarakterea(LARRIAK));
        karaktereak.add(ausazkoKarakterea(XEHEAK));
        karaktereak.add(ausazkoKarakterea(ZENBAKIAK));
        karaktereak.add(ausazkoKarakterea(BEREZIAK));

        while (karaktereak.size() < luzera) {
            karaktereak.add(ausazkoKarakterea(GUZTIAK));
        }

        Collections.shuffle(karaktereak, random);

        StringBuilder pasahitza = new StringBuilder();
        for (Character c : karaktereak) {
            pasahitza.append(c);
        }

        return pasahitza.toString();
    }

    /**
     * Emandako karaktere multzotik karaktere bat ausaz aukeratzen du.
     *
     * @param multzoa karaktereen multzoa
     * @return ausazko karakterea
     */
    private static char ausazkoKarakterea(String multzoa) {
        return multzoa.charAt(random.nextInt(multzoa.length()));
    }
}