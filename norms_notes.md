
Card Storage

Card Representations
* 5-card hands are be stored in 64-bit Java long where the lower 52 bits represent each of the 52 possible cards in a standard deck with the presence of that card indicated by a 1 and the absence represented by a 0. 
  * The first 4 bits of the 52 are As, Ah, Ad, Ac, then Ks, Kh, Kd, Kc, .......2s, 2h, 2d, 2c in the order of spade-heard-diamond-club
  * e.g. 300239688826880L = 0001000100010001000100000000000000000000000000000000 = "Ac-Jc-Kc-Qc-Tc"
  * For a short deck, which removes the 2's through 4's, the lower 16 bits are always 0.

Hand Evaluator
* Called a "compairer" by the original author.
* The evaluator has a map with the key (Long: 5-card hand), value (Integer: hand rank, lower is better, ) 
  * hand ranks from 1 (royal flush) to 7462 (2-3-4-5-7 offsuit) 
  * ranks for worst possible straightFlush (9), quads (163), full-house (322), flush (815), straight (1609), twoPair 3325, onePair 6185, worst 7462
  * straight flushes 1-10, quads 11-166, full houses 167-322, flushes 323-1599, straights 1600-1609, trips 1610-2467, 2 pair 2468-3325, 1 pair 3326-6185, kicker only 6186-7462
  * the map contains 2,598,960 (52 choose 5) 5-card combos. Note that for short deck there are 376,992 (36 choose 5) 5-card combos.
    * Map<Long, Integer> mapCardsToHandValue = Dic5Compairer.getCardslong2rank();
  * The map is essentially a static cache and could potentially be stored as a minimal perfect hash, perhaps best as int array in Java.
  * to load the map from a text file (card5_dic_sorted.txt) with comma delimited pairs of hand, rank like: 2c-2d-3h-4d-8c,6175 takes 5.720 s
  * to load the map from a serialized instance


* org.junit.Test ShortDeckSolverTest.cfrSolverTest
  * Solver solver (game_tree, p1 range, p2 range, board, 100 iterations)
    * CFRPlusRiverSolver.train
      * for each iteration
        * for each player
          * CFRPlusRiverSolver.cfr(player,GameTreeNode,...,board)
            * handles action, showdown, chance, terminal
            * action: CFRPlusRiverSolver.ActionUtility(player,ActionNode,...,board)
              * current_strategy
                * if ActionNode==action
                  * regrets[action_id * node_player_private_cards.length + i] = all_action_utility[action_id][i] - payoffs[i]
                    * CFRTrainable.updateRegrets()