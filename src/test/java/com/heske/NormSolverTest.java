package com.heske;

import icybee.solver.*;
import icybee.solver.compairer.Compairer;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class NormSolverTest {
    private static final Logger LOG = LoggerFactory.getLogger(NormSolverTest.class);
    static Compairer compairer = null;
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
        String config_name = "yamls/rule_shortdeck_simple.yaml";
        //String config_name = "yamls/rule_shortdeck_turnriversolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver_withallin.yaml";
        //String config_name = "yamls/rule_shortdeck_flopsolver.yaml";
        Config config = this.loadConfig(config_name);
        if (NormSolverTest.compairer == null) {
            try {
                NormSolverTest.compairer = SolverEnvironment.compairerFromConfig(config);
                NormSolverTest.deck = SolverEnvironment.deckFromConfig(config);
            } catch (Exception e) {
                e.printStackTrace();
                assertTrue(false);
            }
        }

    }

    @Test
    public void cardCompareHigherTest() {
        try {
            List<Card> board = Arrays.asList(new Card("6c"), new Card("6d"), new Card("7c"), new Card("7d"), new Card("8s"));
            List<Card> private1 = Arrays.asList(new Card("6h"), new Card("6s"));
            List<Card> private2 = Arrays.asList(new Card("9c"), new Card("9s"));
            Compairer.CompairResult cr = NormSolverTest.compairer.compair(private1, private2, board);
            LOG.info("cardCompareHigherTest result {}", cr);
            assertTrue(cr == Compairer.CompairResult.LARGER);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void cardCompareEqualTest() {
        try {
            List<Card> board = Arrays.asList(new Card("6c"), new Card("6d"), new Card("7c"), new Card("7d"), new Card("8s"));
            List<Card> private1 = Arrays.asList(new Card("8h"), new Card("7s"));
            List<Card> private2 = Arrays.asList(new Card("8d"), new Card("7h"));
            Compairer.CompairResult cr = NormSolverTest.compairer.compair(private1, private2, board);
            LOG.info("cardCompareEqualTest result {}", cr);
            assertTrue(cr == Compairer.CompairResult.EQUAL);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void cardCompareLowerTest() {
        try {
            List<Card> board = Arrays.asList(new Card("6c"), new Card("6d"), new Card("7c"), new Card("7d"), new Card("8s"));
            List<Card> private1 = Arrays.asList(new Card("6h"), new Card("7s"));
            List<Card> private2 = Arrays.asList(new Card("8h"), new Card("7h"));

            Compairer.CompairResult cr = NormSolverTest.compairer.compair(private1, private2, board);
            LOG.info("cardCompareLowerTest result {}", cr);
            assertTrue(cr == Compairer.CompairResult.SMALLER);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void getRankTest() {
        List<Card> board = Arrays.asList(new Card("8d"), new Card("9d"), new Card("9s"), new Card("Jd"), new Card("Jh"));
        List<Card> private_cards = Arrays.asList(new Card("6h"), new Card("7s"));
        int rank = NormSolverTest.compairer.get_rank(private_cards, board);
        LOG.info("getRankTest result {}", rank);
        assertTrue(rank == 687);
    }

    @Test
    public void printTreeTest() {
        String config_name = "yamls/rule_shortdeck_simple.yaml";
        //String config_name = "yamls/rule_shortdeck_turnriversolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver_withallin.yaml";
        //String config_name = "yamls/rule_shortdeck_flopsolver.yaml";
        Config config = this.loadConfig(config_name);
        GameTree game_tree = SolverEnvironment.gameTreeFromConfig(config, NormSolverTest.deck);
        LOG.info("The game tree :");
        try {
            game_tree.printTree(-1);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void printTreeLimitDepthTest() {
        String config_name = "yamls/rule_shortdeck_simple.yaml";
        //String config_name = "yamls/rule_shortdeck_turnriversolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver_withallin.yaml";
        //String config_name = "yamls/rule_shortdeck_flopsolver.yaml";
        Config config = this.loadConfig(config_name);
        GameTree game_tree = SolverEnvironment.gameTreeFromConfig(config, NormSolverTest.deck);
        System.out.println("printTreeLimitDepthTest The depth limit game tree :");
        LOG.info("printTreeLimitDepthTest The depth limit game tree :");
        try {
            game_tree.printTree(2);
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
            LOG.info("cardConvertTest int {} to string {} to int {}",card_int, card_rev, card_int_rev);
            assert (card_int == card_int_rev);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void cardsIntegerConvertTest() {
        Card[] board = {new Card("6c"), new Card("6d"), new Card("7c"), new Card("7d"), new Card("8s"), new Card("6h"), new Card("7s")};
        try {
            long board_int = Card.boardCards2long(board);
            Card[] board_cards = Card.long2boardCards(board_int);
            long board_int_rev = Card.boardCards2long(board_cards);
            LOG.info("cardsIntegerConvertTest board{} to int {} to string {} to int {}", board, board_int, board_cards, board_int_rev);
            assert (board_int == board_int_rev);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void cardsIntegerConvertNETest() {
        Card[] board1 = {new Card("6c"), new Card("6d"), new Card("7c"), new Card("7d"), new Card("8s"), new Card("6h"), new Card("7s")};
        Card[] board2 = {new Card("6c"), new Card("6d"), new Card("7c"), new Card("7d"), new Card("9s"), new Card("6h"), new Card("7s")};
        try {
            long board_int1 = Card.boardCards2long(board1);
            long board_int2 = Card.boardCards2long(board2);
            assertTrue(board_int1 != board_int2);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void compareEqualTest() {
        LOG.info("compareEqualTest");
        List<Card> board1_public = Arrays.asList(new Card("6c"), new Card("6d"), new Card("7c"), new Card("7d"), new Card("8s"));
        List<Card> board1_private = Arrays.asList(new Card("6h"), new Card("7s"));
        int[] board2_public = {(new Card("6c").getCardInt()), (new Card("6d").getCardInt()), (new Card("7c").getCardInt()), (new Card("7d").getCardInt()), (new Card("8s").getCardInt()),};
        int[] board2_private = {(new Card("6h").getCardInt()), (new Card("7s").getCardInt())};
        try {
            long board_int1 = compairer.get_rank(board1_private, board1_public);
            long board_int2 = compairer.get_rank(board2_private, board2_public);
            LOG.info("board1 {} = board2 {}",board_int1,board_int2);
            assertTrue(board_int1 == board_int2);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        LOG.info("end compareEqualTest");
    }

    @Test
    public void normSolverTest() throws Exception {
        System.out.println("normSolverTest");

        String config_name = "yamls/rule_shortdeck_simple.yaml";
        //String config_name = "yamls/rule_shortdeck_turnriversolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver_withallin.yaml";
        //String config_name = "yamls/rule_shortdeck_flopsolver.yaml";
        Config config = this.loadConfig(config_name);
        GameTree game_tree = SolverEnvironment.gameTreeFromConfig(config, NormSolverTest.deck);

//        String player1RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
//        String player2RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";

        String player1RangeStr = "87";
        String player2RangeStr = "87";
        /*
        case 'c': return 0; // 梅花
        case 'd': return 1; // 方块
        case 'h': return 2; // 红桃
        case 's': return 3; // 黑桃
         */

        int[] initialBoard = new int[]{
                Card.strCard2int("Kd"),
                Card.strCard2int("Jd"),
                Card.strCard2int("Td"),
                Card.strCard2int("7s"),
                Card.strCard2int("8s")
        };
        //       PrivateCards[] player1Range = PrivateRangeConverter.rangeStr2Cards(player1RangeStr,initialBoard);
        //      PrivateCards[] player2Range = PrivateRangeConverter.rangeStr2Cards(player2RangeStr,initialBoard);
        //PrivateCards[] player1Range = new PrivateCards[]{new PrivateCards(Card.strCard2int("As"),Card.strCard2int("Ad"),1)};

        PrivateCards[] player1Range = new PrivateCards[]{new PrivateCards(Card.strCard2int("As"), Card.strCard2int("Ad"), 1), new PrivateCards(Card.strCard2int("8h"), Card.strCard2int("7d"), 1)};
        PrivateCards[] player2Range = new PrivateCards[]{
                //new PrivateCards(Card.strCard2int("6d"),Card.strCard2int("8s"),1)
                new PrivateCards(Card.strCard2int("6d"), Card.strCard2int("7d"), 1), new PrivateCards(Card.strCard2int("6s"), Card.strCard2int("6d"), 1)};

        String logfile_name = "src/test/resources/outputs/outputs_log.txt";
        Solver solver = new CfrPlusRiverSolver(game_tree, player1Range, player2Range, initialBoard, NormSolverTest.compairer, NormSolverTest.deck
                , 100, true, 10, logfile_name, DiscountedCfrTrainable.class, MonteCarolAlg.NONE);
        Map train_config = new HashMap();
        solver.train(train_config);

        String strategy_json = solver.getTree().dumps(false).toJSONString();
        String strategy_fname = "src/test/resources/outputs/outputs_strategy.json";

        File output_file = new File(strategy_fname);
        FileWriter writer = new FileWriter(output_file);
        writer.write(strategy_json);
        writer.flush();
        writer.close();
        LOG.info("end normSolverTest");
    }

    @Test
    public void cfrSolverTest() throws Exception {
        System.out.println("solverTest");

        String config_name = "yamls/rule_shortdeck_simple.yaml";
        //String config_name = "yamls/rule_shortdeck_turnriversolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver_withallin.yaml";
        //String config_name = "yamls/rule_shortdeck_flopsolver.yaml";
        Config config = this.loadConfig(config_name);
        GameTree game_tree = SolverEnvironment.gameTreeFromConfig(config, NormSolverTest.deck);

        String player1RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
        String player2RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";

        // String player1RangeStr = "87";
        // String player2RangeStr = "87";
        /*
        case 'c': return 0; // 梅花
        case 'd': return 1; // 方块
        case 'h': return 2; // 红桃
        case 's': return 3; // 黑桃
         */

        int[] initialBoard = new int[]{Card.strCard2int("Kd"), Card.strCard2int("Jd"), Card.strCard2int("Td"), Card.strCard2int("7s"), Card.strCard2int("8s")};

        PrivateCards[] player1Range = PrivateRangeConverter.rangeStr2Cards(player1RangeStr, initialBoard);
        PrivateCards[] player2Range = PrivateRangeConverter.rangeStr2Cards(player2RangeStr, initialBoard);
        //PrivateCards[] player1Range = new PrivateCards[]{new PrivateCards(Card.strCard2int("As"),Card.strCard2int("Ad"),1)};

        /*
        PrivateCards[] player1Range = new PrivateCards[]{
                new PrivateCards(Card.strCard2int("As"),Card.strCard2int("Ad"),1)
                ,new PrivateCards(Card.strCard2int("8h"),Card.strCard2int("7d"),1)
        };
        PrivateCards[] player2Range = new PrivateCards[]{
                //new PrivateCards(Card.strCard2int("6d"),Card.strCard2int("8s"),1)
                new PrivateCards(Card.strCard2int("6d"),Card.strCard2int("7d"),1)
                ,new PrivateCards(Card.strCard2int("6s"),Card.strCard2int("6d"),1)
        };
         */

        String logfile_name = "src/test/resources/outputs/outputs_log.txt";
        Solver solver = new CfrPlusRiverSolver(game_tree, player1Range, player2Range, initialBoard, NormSolverTest.compairer
                , NormSolverTest.deck, 100, false, 10, logfile_name, DiscountedCfrTrainable.class, MonteCarolAlg.NONE);
        Map train_config = new HashMap();
        solver.train(train_config);

        /*
        String strategy_json = solver.getTree().dumps(false).toJSONString();
        String strategy_fname = "src/test/resources/outputs/outputs_strategy.json";
        File output_file = new File(strategy_fname);
        FileWriter writer = new FileWriter(output_file);
        writer.write(strategy_json);
        writer.flush();
        writer.close();
        System.out.println("end solverTest");
         */
        LOG.info("end cfrSolverTest");
    }

    @Test
    public void cfrTurnSolverTest() throws BoardNotFoundException, Exception {
        System.out.println("solverTest");

        //String config_name = "yamls/rule_shortdeck_simple.yaml";
        //String config_name = "yamls/rule_shortdeck_turnriversolver.yaml";
        String config_name = "yamls/rule_shortdeck_turnsolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver_withallin.yaml";
        //String config_name = "yamls/rule_shortdeck_flopsolver.yaml";
        Config config = this.loadConfig(config_name);
        GameTree game_tree = SolverEnvironment.gameTreeFromConfig(config, NormSolverTest.deck);

        String player1RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
        String player2RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";

        // String player1RangeStr = "87";
        // String player2RangeStr = "87";
        /*
        case 'c': return 0; // 梅花
        case 'd': return 1; // 方块
        case 'h': return 2; // 红桃
        case 's': return 3; // 黑桃
         */

        int[] initialBoard = new int[]{Card.strCard2int("Kd"), Card.strCard2int("Jd"), Card.strCard2int("Td"), Card.strCard2int("7s"),    };

        PrivateCards[] player1Range = PrivateRangeConverter.rangeStr2Cards(player1RangeStr, initialBoard);
        PrivateCards[] player2Range = PrivateRangeConverter.rangeStr2Cards(player2RangeStr, initialBoard);

        String logfile_name = "src/test/resources/outputs/outputs_log.txt";
        Solver solver = new CfrPlusRiverSolver(game_tree, player1Range, player2Range, initialBoard, NormSolverTest.compairer
                , NormSolverTest.deck, 100, false, 10, logfile_name
                , DiscountedCfrTrainable.class, MonteCarolAlg.NONE);
        Map train_config = new HashMap();
        solver.train(train_config);
        String strategy_json = solver.getTree().dumps(false).toJSONString();
        String strategy_fname = "src/test/resources/outputs/outputs_strategy.json";
        File output_file = new File(strategy_fname);
        FileWriter writer = new FileWriter(output_file);
        writer.write(strategy_json);
        writer.flush();
        writer.close();
        LOG.info("end cfrTurnSolverTest");
    }

    @Test
    public void parrallelCfrFlopSolverTest() throws BoardNotFoundException, Exception {
        System.out.println("solverTest");

        //String config_name = "yamls/rule_shortdeck_simple.yaml";
        //String config_name = "yamls/rule_shortdeck_turnriversolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver_withallin.yaml";
        String config_name = "yamls/rule_shortdeck_flopsolver.yaml";
        Config config = this.loadConfig(config_name);
        GameTree game_tree = SolverEnvironment.gameTreeFromConfig(config, NormSolverTest.deck);

        String player1RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
        String player2RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
        //String player2RangeStr = "KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";

        // String player1RangeStr = "87";
        // String player2RangeStr = "87";
        /*
        case 'c': return 0; // 梅花
        case 'd': return 1; // 方块
        case 'h': return 2; // 红桃
        case 's': return 3; // 黑桃
         */

        int[] initialBoard = new int[]{Card.strCard2int("Kd"), Card.strCard2int("Jd"), Card.strCard2int("Td"),};

        PrivateCards[] player1Range = PrivateRangeConverter.rangeStr2Cards(player1RangeStr, initialBoard);
        PrivateCards[] player2Range = PrivateRangeConverter.rangeStr2Cards(player2RangeStr, initialBoard);

        String logfile_name = "src/test/resources/outputs/outputs_log.txt";
        Solver solver = new ParallelCfrPlusSolver(game_tree, player1Range, player2Range, initialBoard, NormSolverTest.compairer
                , NormSolverTest.deck, 31, false, 10, logfile_name, DiscountedCfrTrainable.class
                , MonteCarolAlg.NONE, -1, 1, 0, 1, 0
        );
        Map train_config = new HashMap();
        solver.train(train_config);
        //String strategy_json = solver.getTree().dumps(false).toJSONString();
        //String strategy_fname = "src/test/resources/outputs/outputs_strategy.json";
        //File output_file = new File(strategy_fname);
        //FileWriter writer = new FileWriter(output_file);
        //writer.write(strategy_json);
        //writer.flush();
        //writer.close();
        //System.out.println("end solverTest");
        LOG.info("end parrallelCfrFlopSolverTest");
    }

    @Test
    public void cfrFlopSolverTest() throws BoardNotFoundException, Exception {
        System.out.println("solverTest");
        //String config_name = "yamls/rule_shortdeck_simple.yaml";
        //String config_name = "yamls/rule_shortdeck_turnriversolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver_withallin.yaml";
        String config_name = "yamls/rule_shortdeck_flopsolver.yaml";
        Config config = this.loadConfig(config_name);
        GameTree game_tree = SolverEnvironment.gameTreeFromConfig(config, NormSolverTest.deck);

        String player1RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
        String player2RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
        //String player2RangeStr = "KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";

        // String player1RangeStr = "87";
        // String player2RangeStr = "87";
        /*
        case 'c': return 0; // 梅花
        case 'd': return 1; // 方块
        case 'h': return 2; // 红桃
        case 's': return 3; // 黑桃
         */

        int[] initialBoard = new int[]{Card.strCard2int("Kd"), Card.strCard2int("Jd"), Card.strCard2int("Td"),};

        PrivateCards[] player1Range = PrivateRangeConverter.rangeStr2Cards(player1RangeStr, initialBoard);
        PrivateCards[] player2Range = PrivateRangeConverter.rangeStr2Cards(player2RangeStr, initialBoard);

        String logfile_name = "src/test/resources/outputs/outputs_log.txt";
        Solver solver = new CfrPlusRiverSolver(game_tree, player1Range, player2Range, initialBoard, NormSolverTest.compairer
                , NormSolverTest.deck, 31, false, 10, logfile_name, DiscountedCfrTrainable.class, MonteCarolAlg.NONE);
        Map train_config = new HashMap();
        solver.train(train_config);
        //String strategy_json = solver.getTree().dumps(false).toJSONString();
        //String strategy_fname = "src/test/resources/outputs/outputs_strategy.json";
        //File output_file = new File(strategy_fname);
        //FileWriter writer = new FileWriter(output_file);
        //writer.write(strategy_json);
        //writer.flush();
        //writer.close();
        LOG.info("end cfrFlopSolverTest");
    }

    @Test
    public void cfrFlopSolverPcsTest() throws BoardNotFoundException, Exception {
        System.out.println("solverTest");
        //String config_name = "yamls/rule_shortdeck_simple.yaml";
        //String config_name = "yamls/rule_shortdeck_turnriversolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver_withallin.yaml";
        String config_name = "yamls/rule_shortdeck_flopsolver.yaml";
        Config config = this.loadConfig(config_name);
        GameTree game_tree = SolverEnvironment.gameTreeFromConfig(config, NormSolverTest.deck);

        String player1RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
        String player2RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
        //String player2RangeStr = "KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";

        // String player1RangeStr = "87";
        // String player2RangeStr = "87";
        /*
        case 'c': return 0; // 梅花
        case 'd': return 1; // 方块
        case 'h': return 2; // 红桃
        case 's': return 3; // 黑桃
         */

        int[] initialBoard = new int[]{Card.strCard2int("Kd"), Card.strCard2int("Jd"), Card.strCard2int("Td"),};

        PrivateCards[] player1Range = PrivateRangeConverter.rangeStr2Cards(player1RangeStr, initialBoard);
        PrivateCards[] player2Range = PrivateRangeConverter.rangeStr2Cards(player2RangeStr, initialBoard);

        String logfile_name = "src/test/resources/outputs/outputs_log.txt";
        Solver solver = new CfrPlusRiverSolver(game_tree, player1Range, player2Range, initialBoard, NormSolverTest.compairer
                , NormSolverTest.deck, 1000, false, 100, logfile_name, DiscountedCfrTrainable.class
                , MonteCarolAlg.PUBLIC);
        Map train_config = new HashMap();
        solver.train(train_config);
        //String strategy_json = solver.getTree().dumps(false).toJSONString();
        //String strategy_fname = "src/test/resources/outputs/outputs_strategy.json";
        //File output_file = new File(strategy_fname);
        //FileWriter writer = new FileWriter(output_file);
        //writer.write(strategy_json);
        //writer.flush();
        //writer.close();
        LOG.info("end cfrFlopSolverPcsTest");
    }

    @Test
    public void parallelPcsCfrFlopSolverTest() throws BoardNotFoundException, Exception {
        System.out.println("solverTest");
        //String config_name = "yamls/rule_shortdeck_simple.yaml";
        //String config_name = "yamls/rule_shortdeck_turnriversolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver_withallin.yaml";
        String config_name = "yamls/rule_shortdeck_flopsolver.yaml";
        Config config = this.loadConfig(config_name);
        GameTree game_tree = SolverEnvironment.gameTreeFromConfig(config, NormSolverTest.deck);
        String player1RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
        String player2RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
        //String player2RangeStr = "KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
        // String player1RangeStr = "87";
        // String player2RangeStr = "87";
        /*
        case 'c': return 0; // 梅花
        case 'd': return 1; // 方块
        case 'h': return 2; // 红桃
        case 's': return 3; // 黑桃
         */
        int[] initialBoard = new int[]{Card.strCard2int("Kd"), Card.strCard2int("Jd"), Card.strCard2int("Td"),};
        PrivateCards[] player1Range = PrivateRangeConverter.rangeStr2Cards(player1RangeStr, initialBoard);
        PrivateCards[] player2Range = PrivateRangeConverter.rangeStr2Cards(player2RangeStr, initialBoard);
        String logfile_name = "src/test/resources/outputs/outputs_log.txt";
        Solver solver = new ParallelCfrPlusSolver(game_tree, player1Range, player2Range, initialBoard, NormSolverTest.compairer
                , NormSolverTest.deck, 1000, false, 100, logfile_name, DiscountedCfrTrainable.class
                , MonteCarolAlg.PUBLIC, -1, 1, 0, 1, 0);
        Map train_config = new HashMap();
        solver.train(train_config);
        //String strategy_json = solver.getTree().dumps(false).toJSONString();
        //String strategy_fname = "src/test/resources/outputs/outputs_strategy.json";
        //File output_file = new File(strategy_fname);
        //FileWriter writer = new FileWriter(output_file);
        //writer.write(strategy_json);
        //writer.flush();
        //writer.close();
        LOG.info("end parallelPcsCfrFlopSolverTest");
    }

    @Test
    public void parallelCfrTurnSolverTest() throws BoardNotFoundException, Exception {
        System.out.println("solverTest");
        //String config_name = "yamls/rule_shortdeck_simple.yaml";
        //String config_name = "yamls/rule_shortdeck_turnriversolver.yaml";
        String config_name = "yamls/rule_shortdeck_turnsolver.yaml";
        //String config_name = "yamls/rule_shortdeck_turnsolver_withallin.yaml";
        //String config_name = "yamls/rule_shortdeck_flopsolver.yaml";
        Config config = this.loadConfig(config_name);
        GameTree game_tree = SolverEnvironment.gameTreeFromConfig(config, NormSolverTest.deck);
        String player1RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
        //String player2RangeStr = "AA,KK,QQ,JJ,TT,99,88,77,66,AK,AQ,AJ,AT,A9,A8,A7,A6,KQ,KJ,KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
        String player2RangeStr = "KT,K9,K8,K7,K6,QJ,QT,Q9,Q8,Q7,Q6,JT,J9,J8,J7,J6,T9,T8,T7,T6,98,97,96,87,86,76";
        // String player1RangeStr = "87";
        // String player2RangeStr = "87";
        /*
        case 'c': return 0; // 梅花
        case 'd': return 1; // 方块
        case 'h': return 2; // 红桃
        case 's': return 3; // 黑桃
         */

        int[] initialBoard = new int[]{Card.strCard2int("Kd"), Card.strCard2int("Jd"), Card.strCard2int("Td"),};
        PrivateCards[] player1Range = PrivateRangeConverter.rangeStr2Cards(player1RangeStr, initialBoard);
        PrivateCards[] player2Range = PrivateRangeConverter.rangeStr2Cards(player2RangeStr, initialBoard);
        String logfile_name = "src/test/resources/outputs/outputs_log.txt";
        Solver solver = new ParallelCfrPlusSolver(game_tree, player1Range, player2Range, initialBoard, NormSolverTest.compairer
                , NormSolverTest.deck, 100, false, 10, logfile_name, DiscountedCfrTrainable.class
                , MonteCarolAlg.NONE, 2, 1, 0, 1, 0);
        Map train_config = new HashMap();
        solver.train(train_config);
        String strategy_json = solver.getTree().dumps(false).toJSONString();
        String strategy_fname = "src/test/resources/outputs/outputs_strategy.json";
        File output_file = new File(strategy_fname);
        FileWriter writer = new FileWriter(output_file);
        writer.write(strategy_json);
        writer.flush();
        writer.close();
        LOG.info("end parallelCfrTurnSolverTest");
    }
}
