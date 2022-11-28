


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