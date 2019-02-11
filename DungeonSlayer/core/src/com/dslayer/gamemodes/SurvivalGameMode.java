/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.gamemodes;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.dslayer.content.Enemy.Golem.BlueGolem;
import com.dslayer.content.Enemy.Skeleton.SkeletonMage;
import com.dslayer.content.Enemy.Skeleton.SkeletonWarrior;
import com.dslayer.content.GameMessage.GameMessage;
import com.dslayer.content.Objects.Potions.HealthPotion2;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Rooms.DungeonPanels;
import com.dslayer.content.Rooms.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.screens.MainMenuScreen;
import java.util.List;

/**
 *
 * @author ARustedKnight
 */
public class SurvivalGameMode extends GameMode{
    
    private float potionRespawnInterval = 3f;
    private float potionRespawnTimer = 0;
    private float maxPotionsOnFeild = 6;
    
    private float maxNumOfEnemies = 6;
    private float spawnedEnemies = 0;
    private float spawnTimer = 3f;
    private float spawnTime = 0;
    
    private float GolemSpawnTimer = 75f;
    private float GolemSpawnTime = 0;
    
    private float increaseEnemyTimer = 10f;
    private float increaseEnemyTime = 0f;
    
    private boolean goSent = false;
    
    Label points;
    
    private GameMessage gm;
    
    public SurvivalGameMode(Stage s){
        super(s);
        //System.out.println(Gdx.graphics.getHeight());
    }
    
    public SurvivalGameMode(){
        this(BaseActor.getMainStage());
    }
    @Override
    public void setup() {
        
        Room dr = new DungeonRoom();
        dr.generateRoom(30,40);
        
        Difficulty.worldHeight = dr.getRoomHeight();
        Difficulty.worldWidth = dr.getRoomWidth();
        Difficulty.newGame();
        
        dr.Draw(mainStage);
        Table pointTable = new Table();
        player = new Player(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight), mainStage);
        Label u = new Label(player.hero.getName() +": ", MainMenuScreen.pointStyle);
        u.setAlignment(Align.left);
        pointTable.add(u);
        points = new Label(Integer.toString(player.getPoints()), MainMenuScreen.pointStyle);
        points.setAlignment(Align.right);
        
        float width = u.getWidth() + points.getWidth();
        pointTable.setWidth(width);
        pointTable.add(points);
        pointTable.row();
        pointTable.setHeight(u.getHeight());
        pointTable.setPosition(pointTable.getWidth() / 2, BaseActor.getUiStage().getHeight() - pointTable.getHeight());
        BaseActor.getUiStage().addActor(pointTable);
        gm = new GameMessage();
        gm.AddMessage("Welcome");
    }
    @Override
    public void update(float dt) {
        if(player.isDead())
        {
            if(!goSent){
                gm.AddMessage("Game Over");
                gm.AddMessage("Total Points: " + player.getPoints());
                goSent = true;
            }
            if(player.isAnimationFinished() && gm.isEmpty()){
                gameOver = true;
            }
            return;
        }
        
        points.setText( Integer.toString(player.getPoints()));
        
        potionRespawnTimer += dt;
        List<BaseActor> hPots = BaseActor.getList(mainStage, "com.dslayer.content.Objects.Potions.HealthPotion2");
        
        if(potionRespawnTimer > potionRespawnInterval){
            potionRespawnTimer = 0;
            if(hPots.size() < maxPotionsOnFeild && MathUtils.randomBoolean(.8f)){
                BaseActor a =new HealthPotion2(MathUtils.random(Difficulty.worldWidth - (DungeonPanels.defaultSize * 2)) + DungeonPanels.defaultSize, 
                        MathUtils.random(Difficulty.worldHeight - (DungeonPanels.defaultSize * 2))+ DungeonPanels.defaultSize, 
                        mainStage);
                        ((HealthPotion2)a).enableDespawnTimer(30);
            }
        }
        
        List<BaseActor> enemies = BaseActor.getList(mainStage, "com.dslayer.content.Enemy.BaseEnemy");
        
        spawnTime += dt;
        if(spawnTime > spawnTimer && enemies.size() < maxNumOfEnemies ){
            BaseActor b;
            if(MathUtils.randomBoolean(.4f)){
                b = new SkeletonMage(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight), mainStage);
           }
            else{
                b = new SkeletonWarrior(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight), mainStage);
            }
            spawnTime= 0;
            while(b.getX() > Difficulty.worldWidth || b.getX() < 0 ||
                b.getY() > Difficulty.worldHeight || b.getY() < 0 ){
                b.centerAtPosition(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight));
            }
        }
        
        GolemSpawnTime += dt;
        if(GolemSpawnTime > GolemSpawnTimer ){
            BaseActor b = null;
            if(MathUtils.randomBoolean(.8f)){
                b = new BlueGolem(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight), mainStage);
                if(b != null){
                    gm.AddMessage("Blue Golem Appeared");
                    while(b.getX() > Difficulty.worldWidth || b.getX() < 0 ||
                        b.getY() > Difficulty.worldHeight || b.getY() < 0 ){
                        b.centerAtPosition(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight));
                    }
                }
            }
            GolemSpawnTime = 0;
        }
        
        increaseEnemyTime += dt;
        if(increaseEnemyTime > increaseEnemyTimer){
            maxNumOfEnemies += 2;
            increaseEnemyTime= 0;
        }
        
    }

    
    
}
