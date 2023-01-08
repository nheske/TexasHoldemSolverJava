package org.poker;

import icybee.solver.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class HandRankCache implements java.io.Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(HandRankCache.class);
    Map<Long, Integer> cardslong2rank = (Map<Long, Integer>) new HashMap<Long, Integer>();

    public static void main(String[] args) throws IOException {
        HandRankCache cache = new HandRankCache();
        cache.initializeFromText();
        cache.serializeLongIntHashmapToFile();
        cache.deserializeLongIntHashmapFromFile();
    }

    public Map<Long, Integer> getCardslong2rank() {
        return cardslong2rank;
    }

    public void initializeFromText() {
        ClassLoader classLoader = HandRankCache.class.getClassLoader();
        InputStream inputStream = null;
        long startTime = System.currentTimeMillis();
        try {
            File file = new File(classLoader.getResource("card5_dic_sorted.txt").getFile());
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            inputStream = new FileInputStream(file);
            String str;
            while (true) {
                if (!((str = bufferedReader.readLine()) != null)) break;
                String[] linesp = str.trim().split(",");
                String cards_str = linesp[0];
                String[] cards = cards_str.split("-");
                assert (cards.length == 5);
                Set<String> cards_set = new HashSet<>(Arrays.asList(cards));
                int rank = Integer.valueOf(linesp[1]);
                //cards2rank.put(cards_set,rank);
                long longRepresentationOfCards = Card.boardCards2long(cards);
                cardslong2rank.put(longRepresentationOfCards, rank);
            }
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            LOG.info("Hand Evaluator loaded from file in {} ms", duration);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void serializeLongIntHashmapToFile() {
        long startTime = System.currentTimeMillis();
        try {
            FileOutputStream fileOut = new FileOutputStream("LongIntHashmap.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(cardslong2rank);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in LongIntHashmap.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        LOG.info("LongIntHashmap serialized to file in {} ms", duration);
    }

    public void deserializeLongIntHashmapFromFile() throws IOException {
        long startTime = System.currentTimeMillis();
        ObjectInputStream ois;
        InputStream fis = null;
        Map<Long, Integer> cache = null;
        try {
            File initialFile = new File("LongIntHashmap.ser");
            if(initialFile.exists()){
                fis = new FileInputStream(initialFile);
                ois = new ObjectInputStream(fis);
                cache = (Map<Long, Integer>) ois.readObject();
            }
        } catch (OptionalDataException e) {
            if (!e.eof)
                throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }  finally {
            if (fis != null)
                fis.close();
        }
        if(cache != null){
            System.out.println("results = " + cache.size());
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        LOG.info("LongIntHashmap deserialized from file in {} ms", duration);
    }

}
