package icybee.solver;

import icybee.solver.comparer.Comparer;
import icybee.solver.comparer.Dic5Comparer;
import icybee.solver.solver.GameTreeBuildingSettings;

import java.io.IOException;

/**
 * Created by huangxuefeng on 2019/10/6.
 * This file contains the implemtation of the Texas Poker Solver Environment
 */
public class SolverEnvironment {
    Config config;
    Deck deck;

    Comparer comparer;
    GameTree game_tree = null;
    static SolverEnvironment se;

    public Comparer getCompairer() {
        return comparer;
    }

    public static SolverEnvironment getInstance(){
        return SolverEnvironment.se;
    }

    SolverEnvironment(Config config) throws ClassNotFoundException,IOException{
        this.config = config;
        this.deck = new Deck(config.ranks,config.suits);
        if(config.compairer_type.equals("Dic5Compairer")) {
            this.comparer = new Dic5Comparer(config.compairer_dic_dir,config.compairer_lines);
        }else{
            throw new ClassNotFoundException();
        }

        if(this.config.tree_builder){
            this.game_tree = new GameTree(this.config.tree_builder_json,this.deck);
        }
        if(this.config.solver_type.equals("cfrplus")){
            //solver = new CfrPlusRiverSolver(game_tree);
        }
        SolverEnvironment.se = this;
    }

    public static GameTree gameTreeFromConfig(Config config,Deck deck){
        try {
            return new GameTree(config.tree_builder_json, deck);
        }catch(IOException e){
            throw new RuntimeException();
        }
    }

    public static GameTree gameTreeFromParams(
            Deck deck,
            float oop_commit,
            float ip_commit,
            int current_round,
            int raise_limit,
            float small_blind,
            float big_blind,
            float stack,
            GameTreeBuildingSettings gameTreeBuildingSettings
            ){
        try {
            return new GameTree(deck,oop_commit,ip_commit,current_round,raise_limit,small_blind,big_blind,stack,gameTreeBuildingSettings);
        }catch(IOException e){
            throw new RuntimeException();
        }
    }

    public static GameTree gameTreeFromJson(String json_path,Deck deck){
        try {
            return new GameTree(json_path, deck);
        }catch(IOException e){
            throw new RuntimeException();
        }
    }

    public static Deck deckFromConfig(Config config){
        return new Deck(config.ranks,config.suits);
    }

    public static Comparer compairerFromFile(String compairer_type, String compairer_dic_dir, int compairer_lines)throws IOException{
        if(compairer_type.equals("Dic5Compairer")) {
            return new Dic5Comparer(compairer_dic_dir,compairer_lines);
        }else{
            throw new RuntimeException();
        }
    }
    public static Comparer compairerFromConfig(Config config)throws IOException{
        if(config.compairer_type.equals("Dic5Compairer")) {
            return new Dic5Comparer(config.compairer_dic_dir,config.compairer_lines);
        }else{
            throw new RuntimeException();
        }
    }
    public static Comparer compairerFromConfig(Config config, boolean verbose)throws IOException{
        if(config.compairer_type.equals("Dic5Compairer")) {
            return new Dic5Comparer(config.compairer_dic_dir,config.compairer_lines,verbose);
        }else{
            throw new RuntimeException();
        }
    }
}
