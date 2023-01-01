package com.heske;

import icybee.solver.*;
import icybee.solver.comparer.Comparer;
import icybee.solver.comparer.Dic5Comparer;
import icybee.solver.exceptions.BoardNotFoundException;
import icybee.solver.ranges.PrivateCards;
import icybee.solver.solver.CfrPlusRiverSolver;
import icybee.solver.solver.MonteCarolAlg;
import icybee.solver.solver.ParallelCfrPlusSolver;
import icybee.solver.solver.Solver;
import icybee.solver.trainable.DiscountedCfrTrainable;
import icybee.solver.utils.PrivateRangeConverter;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertTrue;

public class BasicTest {
    private static final Logger LOG = LoggerFactory.getLogger(BasicTest.class);
    static Comparer comparer = null;
    static Deck deck = null;

    Config loadConfig(String conf_name) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(conf_name).getFile());

        Config config = null;
        try {
            config = new Config(file.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return config;
    }

    @Before
    public void loadEnvironmentsTest() {
        String config_name = "yamls/rule_holdem_simple.yaml";
//        String config_name = "yamls/rule_shortdeck_simple.yaml";
        //String config_name = "yamls/rule_shortdeck_turnriversolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver_withallin.yaml";
        //String config_name = "yamls/rule_shortdeck_flopsolver.yaml";
        Config config = this.loadConfig(config_name);
        if (BasicTest.comparer == null) {
            try {
                BasicTest.comparer = SolverEnvironment.compairerFromConfig(config);
                BasicTest.deck = SolverEnvironment.deckFromConfig(config);
            } catch (Exception e) {
                e.printStackTrace();
                assertTrue(false);
            }
        }
    }

    @Test
    public void someTest() {
        String[] cardsStrings = {"2c", "2s", "3d", "3h", "4c", "5c", "6d", "7h", "8h", "9h", "Tc", "Jc", "Qc", "Kc", "Ac"};
        for (String someCardString : cardsStrings) {
            int someInt = Card.strCard2int(someCardString);
            LOG.info("card {} int {}", someCardString, someInt);
            // each card is unique in deck, e.g. 2c=0, 2s=3, 5c=12, Ac=48
        }
    }

    public String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);
        return sb.toString();
    }

    public Long getRandomEncodedHand() {
        String raw = "1111100000000000000000000000000000000000000000000000"; // 5 1's in 52 bits
        raw = "0100000000000000000000000000000000000000000000001111"; // 5 1's in 52 bits
        long longRepresentationOfCards = Long.parseLong(raw, 2);
        char[] characters = raw.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = (int)(Math.random() * characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        String bitString = new String(characters);
        long number = Long.parseLong(bitString, 2);
        return number;//note while there are only 2,598,960 different values with 5 1's, using this encoding strategy the range of values extends greater than 2,598,960, e.g. 11111 followed by 47 zeroes is 4362862139015168.
    }
//    Take a random number n between zero and threshold which will be the number of ones in the string (both ends inclusive)
//    Create an ArrayList list and add n ones to it.
//    Then add 9 minus n zeros to the same list
//    Call Collections.shuffle(list) to randomly shuffle the elements of the list
//    Convert the list to a String using a StringBuilder

    @Test
    public void comparerTest() {
        Map<Long, Integer> mapCardsToHandValue = Dic5Comparer.getCardslong2rank();
        Long longRepresentationOfCards = getRandomEncodedHand();
        //longs are every value from 1 to 2,598,960
//        long leftLimit = 0L;
//        long rightLimit = 2598960L;
//        long longRepresentationOfCards = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        int handValue = mapCardsToHandValue.get(longRepresentationOfCards);
        Card[] cards;
        try {
            cards = Card.long2boardCards(longRepresentationOfCards);
        } catch (BoardNotFoundException e) {
            throw new RuntimeException(e);
        }
        String binaryString = Long.toBinaryString(longRepresentationOfCards);
        binaryString = padLeftZeros(binaryString, 52);
        // there are 52 choose 5, or 2, 598 960 possible combinations of 5-card poker hands (52 x 51 x 50 x 49 x 48).
//0000000000000000000000000000000000000000000011110001,154 for 2c-3c-3d-3h-3s
//0000000000000000000000000000000000010001000100010001,9 for 2c-3c-4c-5c-6c
//0000000000000000000000000000001000000001000100010001,7462 for 2c-3c-4c-5c-7d
//0000000000000000000000000000000100010001000100010000,8 for 3c-4c-5c-6c-7c
//0001000100010001000100000000000000000000000000000000,1 for Ac-Jc-Kc-Qc-Tc
//0000101000000100000100000000000000000000000000000100,3653 for [2h, Tc, Jh, Kd, Ks]
//1111100000000000000000000000000000000000000000000000,11 for [Ks, Ac, Ad, Ah, As]
//0001000000100000000000000000010010000000100000000000,6451 for [4s, 6s, 7h, Qd, Ac]
//shdc
        LOG.info("cards {} {},{} for {}", cards, binaryString, handValue);
//            1948254208 1985 (first)
//            15032385539 3011 (last)
        //get min and max value hands:
        //starts with file (card5_dic_sorted.txt) of all possible 5 card hands.
        // puts each in a Map<Long,Integer> cardslong2rank = (Map<Long,Integer>)new HashMap<Long,Integer>();
        // which is an aggregate long rep of ranks and his int "rank" is hand value (quad A = 11, 23457.
        // e.g. first row in file is "2c-2d-2h-2s-3c,166", that is [Card("2c"), Card("2d"), Card("2h"), Card("2s"), Card("3c")]
        //  the long key is a sum of shifted numbers that create the int:  board_long += (Long.valueOf(1) << one_card);
        // e.g. a board of 0-1-2-3-4 which is what 2c-2d-2h-2s-3c is yields 31 where after each shift we got 1-2-4-8-16
        // 2c-2d-2h-2s-3d,166 is also a 31 with the same shifts

    }

    @Test
    public void cardCompareHigherTest() {
        try {
            List<Card> board = Arrays.asList(new Card("6c"), new Card("6d"), new Card("7c"), new Card("7d"), new Card("8s"));
            List<Card> private1 = Arrays.asList(new Card("6h"), new Card("6s"));
            List<Card> private2 = Arrays.asList(new Card("9c"), new Card("9s"));
            Comparer.CompareResult cr = BasicTest.comparer.compare(private1, private2, board);
            LOG.info("cardCompareHigherTest result {}", cr);
            assertTrue(cr == Comparer.CompareResult.LARGER);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void compare() {
        List<Card> board = Arrays.asList(new Card("6c"), new Card("6d"), new Card("7c"), new Card("7d"), new Card("8s"));
        List<Card> private1 = Arrays.asList(new Card("6h"), new Card("6s"));
        List<Card> private2 = Arrays.asList(new Card("9c"), new Card("9s"));
        try {
            Comparer.CompareResult cr = BasicTest.comparer.compare(private1, private2, board);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void compareTest() {
        try {
            List<Card> board = Arrays.asList(new Card("6c"), new Card("6d"), new Card("7c"), new Card("7d"), new Card("8s"));
            List<Card> private1 = Arrays.asList(new Card("6h"), new Card("6s"));
            List<Card> private2 = Arrays.asList(new Card("9c"), new Card("9s"));
            Comparer.CompareResult cr = BasicTest.comparer.compare(private1, private2, board);
//            LOG.info("compareTest result {}", cr);
            long startTime = System.currentTimeMillis();
            compare();
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            LOG.info("duration={} ms", duration);
//            assertTrue(cr == Comparer.CompareResult.LARGER);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void cardConvertTest() {
        LOG.info("cardConvertTest");
        try {
            Card card = new Card("6c");
            int card_int = Card.card2int(card);

            Card card_rev = new Card(Card.intCard2Str(card_int));
            int card_int_rev = Card.card2int(card_rev);
            LOG.info("cardConvertTest int {} to string {} to int {}", card_int, card_rev, card_int_rev);
            assert (card_int == card_int_rev);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}
