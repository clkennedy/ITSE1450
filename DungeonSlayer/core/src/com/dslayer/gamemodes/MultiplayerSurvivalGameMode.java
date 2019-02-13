/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.gamemodes;

import com.atkinson.game.engine.BaseActor;
import com.atkinson.game.engine.BaseGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Enemy.Goblin.GoblinAssassin;
import com.dslayer.content.Enemy.Golem.BlueGolem;
import com.dslayer.content.Enemy.Skeleton.SkeletonArmored;
import com.dslayer.content.Enemy.Skeleton.SkeletonMage;
import com.dslayer.content.Enemy.Skeleton.SkeletonWarrior;
import com.dslayer.content.GameMessage.GameMessage;
import com.dslayer.content.Hero.Hero;
import com.dslayer.content.Objects.Potions.HealthPotion2;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Rooms.Dungeon.DungeonPanels;
import com.dslayer.content.Rooms.Dungeon.DungeonRoom;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.Skills.Skill;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.screens.MainMenuScreen;
import com.dslayer.content.screens.MutliplayerLobbyScreen;
import com.dslayer.content.screens.multiplayerRoomScreen;
import io.socket.emitter.Emitter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author ARustedKnight
 */
public class MultiplayerSurvivalGameMode extends GameMode{

    private boolean endGame = false;
    
    class skillInfo{
        public String id;
        public int targetX;
        public int targetY;
        public int skill;
    }
    
    class enemyInfo{
        public String id;
        public int X;
        public int Y;
        public int type;
    }
    
    class enemyAttack{
        public String id;
        public String player_id;
    }
    
    private HashMap<String, BaseActor> gameObjects;
    
    private HashMap<String, Player> OtherPlayers;
    private HashMap<String, String> OtherPlayersUserNames;
    private HashMap<String, Vector2> OtherPlayersMoveTo;
    private HashMap<String, Vector2> OtherPlayersOldPos;
    private HashMap<String, Integer> OtherPlayersHero;
    private HashMap<String, Float> OtherPlayersTimer;
    private HashMap<String, Label> PlayerPointLabels;
    
    private Table pointTable;
    
    private float potionRespawnInterval = 3f;
    private float potionRespawnTimer = 0;
    private float maxPotionsOnFeild = 6;
    
    private float maxNumOfEnemies = 6;
    private float spawnedEnemies = 0;
    private float spawnTimer = 3f;
    private float spawnTime = 0;
    
    private float GolemSpawnTimer = 40f;
    private float GolemSpawnTime = 0;
    
    private float GoblinSpawn = 15f;
    private float GoblinSpawnTimer = 0;
    
    private float increaseEnemyTimer = 10f;
    private float increaseEnemyTime = 0f;
    
    private boolean goSent = false;
    private GameMessage gm;
    private boolean reloadPlayers;
    private float updatePlayerTime = 0;
    private float updatePlayerTimer = 60/60;
    
    private float SyncEnemyTime = 0;
    private float SyncEnemyTimer = 5f;
    
    private List<skillInfo> heroCast;
    private List<enemyInfo> enemiesToSpawn;
    private List<enemyAttack> enemiesAttack;
    private List<enemyInfo> healthPotsToSpawn;
    
    private List<String> gameObjectToRemove;
    
    private float moveTimeElepased = 0;
    
    public MultiplayerSurvivalGameMode(Stage s){
        super(s);
        //System.out.println(Gdx.graphics.getHeight());
    }
    
    public MultiplayerSurvivalGameMode(){
        this(BaseActor.getMainStage());
        
    }
    @Override
    public void setup() {
        Multiplayer.host = true;
        gameObjects = new HashMap<String, BaseActor>();
        OtherPlayers = new HashMap<String, Player>();
        OtherPlayersHero = new HashMap<String, Integer>();
        OtherPlayersMoveTo = new HashMap<String, Vector2>();
        OtherPlayersOldPos = new HashMap<String, Vector2>();    
        OtherPlayersTimer = new HashMap<String, Float>();
        PlayerPointLabels = new HashMap<String, Label>();
        OtherPlayersUserNames = new HashMap<String, String>();
        
        pointTable = new Table();
        
        enemiesAttack = new ArrayList<enemyAttack>();
        enemiesToSpawn = new ArrayList<enemyInfo>();
        healthPotsToSpawn = new ArrayList<enemyInfo>();
        heroCast = new ArrayList<skillInfo>();
        gameObjectToRemove = new ArrayList<String>();
        
        setupSocketListeners();
        
        Room dr = new DungeonRoom();
        dr.generateRoom(30,40);
        
        Difficulty.worldHeight = dr.getRoomHeight();
        Difficulty.worldWidth = dr.getRoomWidth();
        Difficulty.newGame();
        
        dr.Draw(mainStage);
        
        Multiplayer.socket.emit("getRoomPlayersGame");
        
        player = new Player(100, 100, mainStage);
        player.network_id = Multiplayer.myID;
        player.UserName = Multiplayer.myUserName;
        
        Label u = new Label(Multiplayer.myUserName +": ", MainMenuScreen.pointStyle);
        u.setAlignment(Align.left);
        pointTable.add(u);
        Label p = new Label(Integer.toString(player.getPoints()), MainMenuScreen.pointStyle);
        p.setAlignment(Align.right);
        PlayerPointLabels.put(Multiplayer.myID, p);
        
        float width = u.getWidth() + p.getWidth();
        pointTable.setWidth(width);
        pointTable.add(p);
        pointTable.row();
        pointTable.setHeight(u.getHeight());
        pointTable.setPosition(pointTable.getWidth() / 2, BaseActor.getUiStage().getHeight() - pointTable.getHeight());
        BaseActor.getUiStage().addActor(pointTable);
        //player = new Player(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight), mainStage);
        JSONObject data = new JSONObject();
        try{
            data.put("targetX", player.getX());
            data.put("targetY", player.getY());
            data.put("dir", player.dir.ordinal());
            Multiplayer.socket.emit("updateHeroPosition", data);
        }catch(Exception e){
            
        }
        /*if(Multiplayer.host){
            BaseActor s = new SkeletonWarrior(200, 200, mainStage);
            gameObjects.put(s.network_id, s);
            
            BaseActor a = new HealthPotion2(300, 100, mainStage);
            gameObjects.put(a.network_id, a);
        }*/
        
        gm = new GameMessage();
        gm.AddMessage("Welcome");
        BaseActor.setMainStage(mainStage);
    }
    @Override
    public void update(float dt) {
        
        if(MutliplayerLobbyScreen.roomDestroyed || !Multiplayer.connected){
            MutliplayerLobbyScreen.roomDestroyed = false;
            multiplayerRoomScreen.rejoined = true;
            BaseGame.setActiveScreen(Multiplayer.baseScreen);
        }
        
        if(reloadPlayers){
            reloadPlayers = false;
            //System.out.println("loadingPlayers");
            for(String id : OtherPlayers.keySet()){
                Player op = new Player(100, 100, mainStage);
                op.isLocalPlayer = false;
                op.network_id = id;
                op.UserName = OtherPlayersUserNames.get(id);
                op.setHero(Hero.getNewHero(Hero.heros.values()[OtherPlayersHero.get(id)]));
                //mainStage.addActor(op);
                OtherPlayers.put(id, op);
                
                Label u = new Label(op.UserName +": ", MainMenuScreen.pointStyle);
                u.setAlignment(Align.left);
                pointTable.add(u);
                Label p = new Label(Integer.toString(op.getPoints()), MainMenuScreen.pointStyle);
                PlayerPointLabels.put(op.network_id, p);

                pointTable.add(p);
                pointTable.row();
                
                float width = u.getWidth() + p.getWidth();
                pointTable.setHeight(pointTable.getHeight() + u.getHeight());
                if(pointTable.getWidth() < width){
                    pointTable.setWidth(width);
                }
                pointTable.setPosition(pointTable.getWidth() / 2, BaseActor.getUiStage().getHeight() - pointTable.getHeight());
                //BaseActor.getUiStage().addActor(pointTable);
            }
        }
        
        boolean anyOtherPlayerAlive = false;
        for(Player b : OtherPlayers.values()){
            anyOtherPlayerAlive = anyOtherPlayerAlive || !b.isDead();
        }
        
        if(!anyOtherPlayerAlive && player.isDead() && !endGame){
            endGame = true;
            gm.AddMessage("All Party Members have Died");
            Player p = player;
            for(Player b : OtherPlayers.values()){
                if(b.getPoints() > p.getPoints()){
                    p = b;
                }
            }
            gm.AddMessage("HighestScore was " + p.UserName +": " + p.getPoints());
            return;
        }
        if(endGame && gm.isEmpty()){
            gameOver = true;
        }
        
        for(String id : gameObjectToRemove){
            gameObjects.remove(id).remove();
        }
        gameObjectToRemove.clear();
        
        for(String id : PlayerPointLabels.keySet()){
            if(Multiplayer.myID.equals(id)){
                PlayerPointLabels.get(id).setText(Integer.toString(player.getPoints()));
            }else{
                if(OtherPlayers.get(id) != null)
                PlayerPointLabels.get(id).setText(Integer.toString(OtherPlayers.get(id).getPoints()));
            }
        }
        
        for(String id: OtherPlayers.keySet()){
            Player p = OtherPlayers.get(id);
            Vector2 mv = OtherPlayersMoveTo.get(id);
            Vector2 op = OtherPlayersOldPos.get(id);
            Float f = OtherPlayersTimer.get(id);
            if(p!= null && mv != null && op != null && f != null){
                //MathUtils.lerp(p.getX(), mv.x, p.getMaxSpeed() * f)
                //p.updatePos(mv.x, mv.y);
                f += dt;
                float delta = MathUtils.clamp(updatePlayerTimer / f, 0, 1);
                 p.updatePos(MathUtils.lerp(op.x, mv.x,  delta),
                         MathUtils.lerp(op.y, mv.y,  delta));
                 
                f = MathUtils.clamp(f, 0, 1);
                OtherPlayersTimer.put(id, f);
            }
        }
        if(player.isDead()){
            Player p = player;
            for(Player b : OtherPlayers.values()){
                if(!b.isDead()){
                    p = b;
                    break;
                }
            }
            player.alignCameraOnOtherActor(p);
        }
        
        for(skillInfo info: heroCast){
            OtherPlayers.get(info.id).cast(info.targetX, info.targetY, info.skill);
        }
        heroCast.clear();
        
        for(enemyInfo enemy : enemiesToSpawn){
            BaseEnemy e = BaseEnemy.getNewEnemy(BaseEnemy.type.values()[enemy.type], enemy.X, enemy.Y);
            e.network_id = enemy.id;
            if(e instanceof BlueGolem){
                gm.AddMessage("Blue Golem Appeared");
            }
            if(e instanceof SkeletonArmored){
                gm.AddMessage("An Armored Skeleton has Risen");
            }
            if(e instanceof GoblinAssassin){
                gm.AddMessage("Goblin Assassin has Scurried in");
            }
            gameObjects.put(enemy.id, e);
        }
        enemiesToSpawn.clear();
        
        
        for(enemyInfo enemy : healthPotsToSpawn){
            BaseActor e = new HealthPotion2(enemy.X, enemy.Y, mainStage);
            e.network_id = enemy.id;
            gameObjects.put(enemy.id, e);
        }
        healthPotsToSpawn.clear();
        
        for(enemyAttack enemy : enemiesAttack){
            if(Multiplayer.myID.equals(enemy.player_id)){
                ((BaseEnemy)gameObjects.get(enemy.id)).attack(player);
            }
            else{
                BaseActor b = OtherPlayers.get(enemy.player_id);
                //((BaseEnemy)gameObjects.get(enemy.id)).target = OtherPlayers.get(enemy.player_id);
                ((BaseEnemy)gameObjects.get(enemy.id)).attack(OtherPlayers.get(enemy.player_id));
            }
        }
        enemiesAttack.clear();
        
        
        updatePlayerTime += dt;
        if((updatePlayerTime > updatePlayerTimer && player.canMove()) || player.directionChanged() || player.isMoving()){
            updatePlayerTime = 0;
            JSONObject data = new JSONObject();
            try{
                data.put("targetX", player.getX());
                data.put("targetY", player.getY());
                data.put("dir", player.dir.ordinal());
                Multiplayer.socket.emit("updateHeroPosition", data);
            }catch(Exception e){
                 System.out.println("Failed to push player data " + e.getMessage());
            }
        }
        /*if(player.isDead())
        {
            if(!goSent){
                gm.AddMessage("Game Over");
                goSent = true;
            }
            if(player.isAnimationFinished()){
                gameOver = true;
            }
            return;
        }*/
        if(!Multiplayer.host)
            return;
        
        SyncEnemyTime += dt;
        if(SyncEnemyTime > SyncEnemyTimer){
            SyncEnemyTime = 0f;
            JSONArray enemies = new JSONArray();
            for(BaseActor b: gameObjects.values()){
                if(b instanceof BaseEnemy){
                    JSONObject d = new JSONObject();
                    try{
                        d.put("id", ((BaseEnemy) b).network_id);
                        d.put("curX", ((BaseEnemy) b).getX());
                        d.put("curY", ((BaseEnemy) b).getY());
                        d.put("tarX", ((BaseEnemy) b).moveTo.x);
                        d.put("tarY", ((BaseEnemy) b).moveTo.y);
                    }catch(Exception e){
                        System.out.println("Couldn't add enemey for Sync " + ((BaseEnemy) b).network_id + "| " + e.getMessage() + d.toString());
                    }
                    enemies.put(d);
                }
            }
            try{
                Multiplayer.socket.emit("syncEnemies", enemies);
            }catch(Exception e){
                 System.err.println("Failed to push player data " + e.getMessage());
            }  
        }
        
        potionRespawnTimer += dt;
        List<BaseActor> hPots = BaseActor.getList(mainStage, "com.dslayer.content.Objects.Potions.HealthPotion2");
        
        if(potionRespawnTimer > potionRespawnInterval){
            potionRespawnTimer = 0;
            if(hPots.size() < maxPotionsOnFeild && MathUtils.randomBoolean(.8f)){
                BaseActor a =new HealthPotion2(MathUtils.random(Difficulty.worldWidth - (DungeonPanels.defaultSize * 2)) + DungeonPanels.defaultSize, 
                        MathUtils.random(Difficulty.worldHeight - (DungeonPanels.defaultSize * 2))+ DungeonPanels.defaultSize, 
                        mainStage);
                        ((HealthPotion2)a).enableDespawnTimer(30);
                gameObjects.put(a.network_id, a);
            }
        }
        
        List<BaseActor> enemies = BaseActor.getList(mainStage, "com.dslayer.content.Enemy.BaseEnemy");
        
        spawnTime += dt;
        if(spawnTime > spawnTimer && enemies.size() < maxNumOfEnemies ){
            BaseActor b;
            if(MathUtils.randomBoolean(.4f)){
                b = new SkeletonMage(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight), mainStage);
           }
            else if(MathUtils.randomBoolean(.95f)){
                b = new SkeletonWarrior(MathUtils.random(DungeonPanels.defaultSize,Difficulty.worldWidth - DungeonPanels.defaultSize), 
                        MathUtils.random(DungeonPanels.defaultSize,Difficulty.worldHeight - DungeonPanels.defaultSize), mainStage);
            }
            else{
                 b = new SkeletonArmored(MathUtils.random(DungeonPanels.defaultSize,Difficulty.worldWidth - DungeonPanels.defaultSize), 
                        MathUtils.random(DungeonPanels.defaultSize,Difficulty.worldHeight - DungeonPanels.defaultSize), mainStage);
                 gm.AddMessage("An Armored Skeleton has Risen");
            }
            spawnTime= 0;
            while(b.getX() > Difficulty.worldWidth || b.getX() < 0 ||
                   b.getY() > Difficulty.worldHeight || b.getY() < 0 ){
                b.centerAtPosition(MathUtils.random(Difficulty.worldWidth), MathUtils.random(Difficulty.worldHeight));
            }
            gameObjects.put(b.network_id, b);
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
                gameObjects.put(b.network_id, b);
            }
            GolemSpawnTime = 0;
        }
        
        GoblinSpawnTimer += dt;
        if(GoblinSpawnTimer > GoblinSpawn){
            GoblinSpawnTimer = 0;
            BaseActor b = null;
            if(MathUtils.randomBoolean(.1f)){
                b = new GoblinAssassin(MathUtils.random(DungeonPanels.defaultSize,Difficulty.worldWidth - DungeonPanels.defaultSize), 
                            MathUtils.random(DungeonPanels.defaultSize,Difficulty.worldHeight - DungeonPanels.defaultSize), mainStage);
            }
            if(b != null){
                    gm.AddMessage("Goblin Assassin has Scurried in");
                    while(b.getX() > Difficulty.worldWidth || b.getX() < 0 ||
                        b.getY() > Difficulty.worldHeight || b.getY() < 0 ){
                        b.setPosition(MathUtils.random(DungeonPanels.defaultSize,Difficulty.worldWidth - DungeonPanels.defaultSize), 
                            MathUtils.random(DungeonPanels.defaultSize,Difficulty.worldHeight - DungeonPanels.defaultSize));
                    }
                }
        }
        
        increaseEnemyTime += dt;
        if(increaseEnemyTime > increaseEnemyTimer){
            maxNumOfEnemies += 2;
            increaseEnemyTime= 0;
        }
        
    }

    private void setupSocketListeners() {
        Multiplayer.socket.on("healthPotionCreated", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        enemyInfo en = new enemyInfo();
                                        en.id = data.getString("id");
                                        en.X = data.getInt("x");
                                        en.Y = data.getInt("y");
                                        healthPotsToSpawn.add(en);
                                    }catch(Exception e){
                                        System.out.println("Failed Health Pot Creation");
                                    }
                                }
                            }).on("healthPotionPickUp", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        String id = data.getString("id");
                                        gameObjectToRemove.add(id);
                                    }catch(Exception e){
                                        System.out.println("Failed Health Pot Pick Up " + e.getMessage());
                                    }
                                }
                            }).on("enemyCreated", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        enemyInfo en = new enemyInfo();
                                        en.id = data.getString("id");
                                        en.X = data.getInt("x");
                                        en.Y = data.getInt("y");
                                        en.type = data.getInt("type");
                                        enemiesToSpawn.add(en);
                                    }catch(Exception e){
                                        System.out.println("Failed Enemy Creation");
                                    }
                                }
                            }).on("enemyTargetChange", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        String netID = data.getString("id");
                                        int tarX = data.getInt("x");
                                        int tarY = data.getInt("y");
                                        ((BaseEnemy)gameObjects.get(netID)).moveTo = new Vector2(tarX, tarY);
                                    }catch(Exception e){
                                        System.out.println("Failed to Update Enemy Target: " + e.getMessage());
                                    }
                                }
                            }).on("enemyAttack", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        enemyAttack ea = new enemyAttack();
                                        ea.id = data.getString("id");
                                        ea.player_id = data.getString("target");
                                        enemiesAttack.add(ea);
                                    }catch(Exception e){
                                        System.out.println("enemy Attack Failed" + e.getMessage());
                                    }
                                }
                            }).on("enemyDamageTaken", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        String id = data.getString("id");
                                        int damage = data.getInt("damage");
                                        ((BaseEnemy)gameObjects.get(id)).multiplayerTakeDamage(damage);
                                    }catch(Exception e){
                                        System.out.println("Enemy failed Damage Taken" + e.getMessage());
                                    }
                                }
                            }).on("enemyDied", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        String id = data.getString("id");
                                        gameObjects.remove(id);
                                    }catch(Exception e){
                                        System.out.println("Enemy failed to Die" + e.getMessage());
                                    }
                                }
                            }).on("syncEnemies", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONArray array = (JSONArray) os[0];
                                    try{
                                        for(int i = 0; i < array.length(); i++){
                                           JSONObject data = array.getJSONObject(i);
                                           String id = data.getString("id");
                                           int curX = data.getInt("curX");
                                           int curY = data.getInt("curY");
                                           int tarX = data.getInt("tarX");
                                           int tarY = data.getInt("tarY");
                                           gameObjects.get(id).setPosition(curX, curY);
                                           ((BaseEnemy)gameObjects.get(id)).moveTo = new Vector2(tarX, tarY);
                                        }
                                    }catch(Exception e){
                                        System.out.println("Failed to Sync Enemies" + e.getMessage());
                                    }
                                }
                            }).on("updateHeroPosition", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        String netID = data.getString("id");
                                        int tarX = data.getInt("targetX");
                                        int tarY = data.getInt("targetY");
                                        int dir = data.getInt("dir");
                                        OtherPlayers.get(netID).dir = Player.direction.values()[dir];
                                        OtherPlayersOldPos.put(netID,new Vector2(OtherPlayers.get(netID).getX(), 
                                                OtherPlayers.get(netID).getY()) );
                                        OtherPlayersMoveTo.put(netID, new Vector2(tarX, tarY));
                                        OtherPlayersTimer.put(netID, 0f);
                                    }catch(Exception e){
                                        System.out.println("Failed to update Hero Position" + e.getMessage());
                                    }
                                }
                            }).on("heroCast", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        skillInfo info = new skillInfo();
                                        info.id = data.getString("id");
                                        info.targetX= data.getInt("targetX");
                                        info.targetY = data.getInt("targetY");
                                        info.skill = data.getInt("skill");
                                        heroCast.add(info);
                                    }catch(Exception e){
                                        System.out.println("GameObject Failed to cast" + e.getMessage());
                                    }
                                }
                            }).on("heroDamageTaken", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        String id = data.getString("id");
                                        int damage = data.getInt("damage");
                                        OtherPlayers.get(id).multiplayerTakeDamage(damage);
                                    }catch(Exception e){
                                        System.out.println("GameObject Failed to cast" + e.getMessage());
                                    }
                                }
                            }).on("heroRecover", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        String id = data.getString("id");
                                        int recover = data.getInt("recover");
                                        OtherPlayers.get(id).multiplayerRecover(recover);
                                    }catch(Exception e){
                                        System.out.println("GameObject Failed to cast" + e.getMessage());
                                    }
                                }
                            }).on("heroAddPoints", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        String id = data.getString("id");
                                        int points = data.getInt("points");
                                        OtherPlayers.get(id).multiplayerAddPoints(points);
                                    }catch(Exception e){
                                        System.out.println("GameObject Failed to cast" + e.getMessage());
                                    }
                                }
                            }).on("getRoomPlayersGame", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    //System.out.println("Getting Players");
                                    JSONArray array = (JSONArray) os[0];
                                    try{
                                        for(int i = 0; i < array.length(); i++){
                                           JSONObject data = array.getJSONObject(i);
                                           String id = data.getString("id");
                                           String userName = data.getString("userName");
                                           int heroid = data.getInt("hero");
                                           if(id != Multiplayer.myID){
                                               OtherPlayersHero.put(id,heroid);
                                               OtherPlayersUserNames.put(id,userName);
                                               OtherPlayers.put(id,null);
                                           }
                                           reloadPlayers = true;
                                        }
                                    }catch(Exception e){
                                        System.out.println("Failed to get Players in room: " + e.getMessage());
                                    }
                                }
                            }).on("playerDisconnectedGame", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                       String id = data.getString("id");
                                       String userName = data.getString("userName");
                                       OtherPlayers.get(id).remove();
                                       OtherPlayers.remove(id);
                                       OtherPlayersHero.remove(id);
                                       gm.AddMessage(userName + " has Disconnected");
                                       System.out.println(userName + " has Disconnected");
                                    }catch(Exception e){
                                        System.out.println("player DC");
                                    }
                                }
                            });
    }

    
    
}
