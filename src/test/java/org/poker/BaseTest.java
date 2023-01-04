package org.poker;

import icybee.solver.Card;

import java.util.Arrays;
import java.util.List;

public class BaseTest {
    List<Card> straightFlushWheel = Arrays.asList(new Card("2h"), new Card("3h"), new Card("4h"), new Card("5h"), new Card("Ah"));
    List<Card> royalFlush = Arrays.asList(new Card("Ah"), new Card("Kh"), new Card("Qh"), new Card("Jh"), new Card("Th"));
    List<Card> quadTwo = Arrays.asList(new Card("2h"), new Card("2c"), new Card("2d"), new Card("2s"), new Card("3d"));
    List<Card> quadAce = Arrays.asList(new Card("Ah"), new Card("Ac"), new Card("Ad"), new Card("As"), new Card("Kd"));
    List<Card> fullTwoThree = Arrays.asList(new Card("2h"), new Card("2c"), new Card("2d"), new Card("3s"), new Card("3d"));
    List<Card> fullAceKing = Arrays.asList(new Card("Ah"), new Card("Ac"), new Card("Ad"), new Card("Ks"), new Card("Kd"));
    List<Card> flushLow = Arrays.asList(new Card("2h"), new Card("3h"), new Card("4h"), new Card("5h"), new Card("7h"));
    List<Card> flushNut = Arrays.asList(new Card("Ah"), new Card("Kh"), new Card("Qh"), new Card("Jh"), new Card("9h"));
    List<Card> straightWheel = Arrays.asList(new Card("Ah"), new Card("2h"), new Card("3c"), new Card("4d"), new Card("5c"));
    List<Card> straightNut = Arrays.asList(new Card("Ah"), new Card("Kh"), new Card("Qc"), new Card("Jd"), new Card("Tc"));
    List<Card> tripTwo = Arrays.asList(new Card("2h"), new Card("2d"), new Card("2c"), new Card("3d"), new Card("4c"));
    List<Card> tripAce = Arrays.asList(new Card("Ah"), new Card("Ad"), new Card("Ac"), new Card("Kd"), new Card("Qc"));
    List<Card> twoPairBottom = Arrays.asList(new Card("2h"), new Card("2d"), new Card("3c"), new Card("3d"), new Card("4c"));
    List<Card> twoPairTop = Arrays.asList(new Card("Ah"), new Card("Ad"), new Card("Kc"), new Card("Kd"), new Card("Qc"));
    List<Card> onePairTwosLowKicker = Arrays.asList(new Card("2h"), new Card("2d"), new Card("3c"), new Card("4d"), new Card("5c"));
    List<Card> onePairAcesTopKicker = Arrays.asList(new Card("Ah"), new Card("Ad"), new Card("Kc"), new Card("Qd"), new Card("Jc"));
    List<Card> nutLow = Arrays.asList(new Card("2h"), new Card("3d"), new Card("4c"), new Card("5d"), new Card("7c"));
    List<Card> nutNoPair = Arrays.asList(new Card("Ah"), new Card("Kd"), new Card("Qc"), new Card("Jd"), new Card("9c"));

}
