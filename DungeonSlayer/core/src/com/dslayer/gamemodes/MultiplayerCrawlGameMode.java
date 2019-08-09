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
import com.badlogic.gdx.utils.Json;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Enemy.Boss.BaseBoss;
import com.dslayer.content.Enemy.Goblin.GoblinAssassin;
import com.dslayer.content.Enemy.Golem.BlueGolem;
import com.dslayer.content.Enemy.Skeleton.SkeletonArmored;
import com.dslayer.content.Enemy.Skeleton.SkeletonMage;
import com.dslayer.content.Enemy.Skeleton.SkeletonWarrior;
import com.dslayer.content.Font.FontLoader;
import com.dslayer.content.GameMessage.GameMessage;
import com.dslayer.content.Hero.Hero;
import com.dslayer.content.Inventory.Items.BossKey;
import com.dslayer.content.Inventory.Items.Potions.HealthPotion;
import com.dslayer.content.LevelGenerator.LevelGenerator;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Rooms.Dungeon.DungeonRoom;
import com.dslayer.content.Rooms.Forest.ForestRoom;
import com.dslayer.content.Rooms.Room;
import com.dslayer.content.Rooms.RoomDoor;
import com.dslayer.content.Rooms.RoomPanels;
import com.dslayer.content.Skills.Skill;
import com.dslayer.content.Spawner.Spawner;
import com.dslayer.content.options.Difficulty;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.options.Options;
import com.dslayer.content.screens.MainMenuScreen;
import com.dslayer.content.screens.MultiplayerHeroSelectionScreen;
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
public class MultiplayerCrawlGameMode extends GameMode{

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
        public int attackType;
    }
    
    private HashMap<String, BaseActor> gameObjects;
    private List<JSONObject> gameObjectsToSpawn;
    private List<JSONObject> gameObjectsToSpawnQueue;
    
    private HashMap<String, Player> OtherPlayers;
    private HashMap<String, String> OtherPlayersUserNames;
    private HashMap<String, Vector2> OtherPlayersMoveTo;
    private HashMap<String, Vector2> OtherPlayersOldPos;
    private HashMap<String, Integer> OtherPlayersHero;
    private HashMap<String, Float> OtherPlayersTimer;
    
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
    private float updatePlayerTimer = .2f;
    
    private float SyncEnemyTime = 0;
    private float SyncEnemyTimer = 3f;
    
    private List<skillInfo> heroCast;
    private List<enemyInfo> enemiesToSpawn;
    private List<enemyAttack> enemiesAttack;
    private List<enemyAttack> bossAttack;
    private List<enemyInfo> healthPotsToSpawn;
    private List<enemyInfo> healthPotsToSpawnQueue;
    
    private List<String> gameObjectToRemove;
    private List<String> gameObjectToRemoveQueue;
    
    private float moveTimeElepased = 0;
    
    List<Room> nonBossRooms;
    Room bossRoom;
    BaseEnemy boss;
    
    Label points;
    private int dungeonWidth = 31;
    private int dungeonHeight = 31;
    
    private float pingHost = .22f;
    private float pingHostTimer = 0f;
    
    public boolean addingEnemy = false;
    public boolean spawningEnemies = false;
    public boolean loading = true;
    public boolean mapLoaded = false;
    public boolean spawnGathered = false;
    public boolean bossKeyFound = false;
    public Vector2 spawnPos;
    
    private LevelGenerator lg;
    
    public MultiplayerCrawlGameMode(Stage s){
        super(s);
        //System.out.println(Gdx.graphics.getHeight());
    }
    
    public MultiplayerCrawlGameMode(){
        this(BaseActor.getMainStage());
        
    }
    @Override
    public void setup() {
        gameObjects = new HashMap<String, BaseActor>();
        gameObjectsToSpawn = new ArrayList<JSONObject>();
        gameObjectsToSpawnQueue = new ArrayList();
        OtherPlayers = new HashMap<String, Player>();
        OtherPlayersHero = new HashMap<String, Integer>();
        OtherPlayersMoveTo = new HashMap<String, Vector2>();
        OtherPlayersOldPos = new HashMap<String, Vector2>();    
        OtherPlayersTimer = new HashMap<String, Float>();
        OtherPlayersUserNames = new HashMap<String, String>();
        
        pointTable = new Table();
        
        enemiesAttack = new ArrayList<enemyAttack>();
        bossAttack = new ArrayList<enemyAttack>();
        enemiesToSpawn = new ArrayList<enemyInfo>();
        healthPotsToSpawnQueue = new ArrayList<enemyInfo>();
        healthPotsToSpawn = new ArrayList<enemyInfo>();
        heroCast = new ArrayList<skillInfo>();
        gameObjectToRemove = new ArrayList<String>();
        gameObjectToRemoveQueue = new ArrayList<String>();
        
        setupSocketListeners();
        player = new Player(-100,-100, mainStage);
        player.setHero(Hero.getNewHero(Hero.heros.values()[MultiplayerHeroSelectionScreen.HeroSelectionIndex]));
        player.network_id = Multiplayer.myID;
        player.UserName = Multiplayer.myUserName;
        
        if(Multiplayer.host){
            lg = new LevelGenerator(dungeonWidth, dungeonHeight);
            lg.setDefaultSize(100);
            if(Difficulty.RoomType == Difficulty.RoomTypes.Dungeon){
                lg.setRoom(new DungeonRoom());
            }else if(Difficulty.RoomType == Difficulty.RoomTypes.Forest){
                lg.setRoom(new ForestRoom());
            }
            lg.generateMap();
            
            mapLoaded = true;
            
            Difficulty.worldHeight = lg.getPixelHeight();
            Difficulty.worldWidth = lg.getPixelWidth();
            
            Room spawnRoom = lg.getRandomNonBossRoom();
            nonBossRooms = lg.getNonBossRooms();
            bossRoom = lg.getBossRooms();
            
            //lg.draw(mainStage);
            
            Integer[][] ml = lg.getMapLayout();
            JSONArray map = new JSONArray();
            JSONArray mapRow;
            for(int row = 0; row < ml.length; row ++){
                mapRow = new JSONArray();
                for(int col = 0; col < ml[row].length; col ++){
                    mapRow.put(ml[row][col]);
                }
                map.put(mapRow);
            }
            JSONObject data = new JSONObject();
            try{
                data.put("mapData", map);
                Multiplayer.socket.emit("mapData", data);
            }catch(Exception e){
                System.out.println("Problem Sending Map Data");
            }
        
        spawnPos = Spawner.getSpawnLocation(lg);
        
        data = new JSONObject();
            try{
                data.put("spawnDataX", spawnPos.x / Options.aspectRatio);
                data.put("spawnDataY", spawnPos.y / Options.aspectRatio);
                Multiplayer.socket.emit("spawnData", data);
            }catch(Exception e){
                System.out.println("Problem Sending Spawn Data");
            }
        spawnGathered = true;
        for(int i = 0; i < nonBossRooms.size(); i++){
            Room r = nonBossRooms.get(i);
            int randNumOfEn = 0;
            if(r == spawnRoom){
                randNumOfEn = MathUtils.random(1, 4); 
            }else{
               randNumOfEn = MathUtils.random(2, 9); 
            }
            for(int j = 0; j < randNumOfEn; j ++){
                BaseEnemy b = Spawner.spawnRandomEnemy(r);
                if(MathUtils.randomBoolean(.6f)){
                    int randPots = MathUtils.random(0, 3);
                    for(int k = 0; k < randPots; k ++){
                        b.addToBackpack(new HealthPotion());
                    }
                }
                gameObjects.put(b.network_id, b);
            }
        }
       
        boss = Spawner.spawnRandomBoss(bossRoom);
        gameObjects.put(boss.network_id, boss);
        int rand = MathUtils.random(nonBossRooms.size() - 1);
        nonBossRooms.get(rand).getRandomEnemy().addToBackpack(new BossKey());
        
        player.setPosition(spawnPos.x,spawnPos.y);
        }
        BaseActor.setMainStage(mainStage);
        
        Difficulty.newGame();
        playMusic("8BitDungSurvival.mp3");
        
        gm = new GameMessage();
        gm.AddMessage("Welcome");
        points = new Label("", FontLoader.pointStyle);
        points.setAlignment(Align.left);
        
        float width = points.getWidth();
        pointTable.setWidth(100);
        pointTable.add(points);
        pointTable.row();
        pointTable.setHeight(50);
        pointTable.setPosition(pointTable.getWidth() / 2, BaseActor.getUiStage().getHeight() - pointTable.getHeight());
        BaseActor.getUiStage().addActor(pointTable);
        
        Multiplayer.socket.emit("getRoomPlayersGame");
        JSONObject data = new JSONObject();
        try{
            data.put("targetX", player.getX() / Options.aspectRatio);
            data.put("targetY", player.getY() / Options.aspectRatio);
            data.put("dir", player.dir.ordinal());
            Multiplayer.socket.emit("updateHeroPosition", data);
        }catch(Exception e){
            
        }
        reloadPlayers = true;
    }
    @Override
    public void update(float dt) {
        
        if(MutliplayerLobbyScreen.roomDestroyed || !Multiplayer.connected){
            MutliplayerLobbyScreen.roomDestroyed = false;
            multiplayerRoomScreen.rejoined = true;
            BaseGame.setActiveScreen(new multiplayerRoomScreen());
        }
        
        if(loading){
            pingHostTimer += dt;
            if(pingHostTimer > pingHost){
                Multiplayer.socket.emit("pingHostForLoadData");
                pingHostTimer = 0;
            }
            loading = !mapLoaded && !spawnGathered;
            return;
        }
        
        if(reloadPlayers){
            //Multiplayer.socket.emit("getRoomPlayersGame");
            reloadPlayers = false;
            if(lg != null){
                lg.draw(mainStage);
            }
            System.out.println("loadingPlayers");
            for(String id : OtherPlayers.keySet()){
                Player op;
                if(OtherPlayers.get(id) != null){
                    op = OtherPlayers.get(id);
                }else{
                    op = new Player(spawnPos.x* Options.aspectRatio, spawnPos.y* Options.aspectRatio, mainStage);
                }
                op.setPosition(spawnPos.x * Options.aspectRatio, spawnPos.y * Options.aspectRatio);
                op.isLocalPlayer = false;
                op.connected = true;
                op.network_id = id;
                op.UserName = OtherPlayersUserNames.get(id);
                //System.out.println(op.network_id);
                op.setHero(Hero.getNewHero(Hero.heros.values()[OtherPlayersHero.get(id)]));
                //mainStage.addActor(op);
                OtherPlayers.put(id, op);
                //BaseActor.getUiStage().addActor(pointTable);
            }
            
        }
        
        //Win Cond
        if(boss != null && boss.isDead())
        {
            if(!goSent){
                gm.AddMessage("Dungeon Cleared");
                gm.AddMessage("Congradulations");
                goSent = true;
            }
            if(boss.isAnimationFinished() && gm.isEmpty()){
                gameOver = true;
            }
            return;
        }
        
        //Death Condition
        boolean anyOtherPlayerAlive = false;
        for(Player b : OtherPlayers.values()){
            anyOtherPlayerAlive = anyOtherPlayerAlive || !b.isDead();
        }
        if(!anyOtherPlayerAlive && player.isDead() && !endGame){
            endGame = true;
            gm.AddMessage("All Party Members have Died");
            return;
        }
        
        
        for(Player b : OtherPlayers.values()){
            if(b.inventoryContains(BossKey.class) && !player.inventoryContains(BossKey.class)){
                player.addToBackpack(new BossKey());
            }
        }
        if(player.inventoryContains(BossKey.class)){
            points.setText("You Have a Boss Key!");
            for(Player b : OtherPlayers.values()){
                if(!b.inventoryContains(BossKey.class)){
                    b.addToBackpack(new BossKey());
                }
            }
        }
        if(gameObjectsToSpawnQueue.size() > 0){
            gameObjectsToSpawn.addAll(gameObjectsToSpawnQueue);
            gameObjectsToSpawnQueue.clear();
        }
        
        if(gameObjectsToSpawn.size() > 0){
            for(JSONObject spawn : gameObjectsToSpawn){
                BaseActor b = Spawner.spawnItemMultiplayer(spawn);
                gameObjects.put(b.network_id, b);
            }
            gameObjectsToSpawn.clear();
        }
        
        
        
        
        
        if(endGame && gm.isEmpty()){
            gameOver = true;
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
                f = MathUtils.clamp(f, 0, 1);
                float delta = MathUtils.clamp(f / updatePlayerTimer, 0, 1);
                p.updatePos(MathUtils.lerp(op.x, mv.x * Options.aspectRatio,  delta),
                         MathUtils.lerp(op.y, mv.y * Options.aspectRatio,  delta));
                 
                f = MathUtils.clamp(f, 0, 1);
                OtherPlayersTimer.put(id, f);
            }
        }
        
        if(player.isDead()){
            Player p = null;
            for(Player b : OtherPlayers.values()){
                if(!b.isDead()){
                    p = b;
                    break;
                }
            }
            player.alignCameraOnOtherActor(p);
        }
        
        for(skillInfo info: heroCast){
            OtherPlayers.get(info.id).cast((int)(info.targetX * Options.aspectRatio), (int)(info.targetY * Options.aspectRatio), info.skill);
        }
        heroCast.clear();
        
        if(!addingEnemy){
            if(healthPotsToSpawnQueue.size() > 1){
                enemiesToSpawn.addAll(healthPotsToSpawnQueue);
                healthPotsToSpawnQueue.clear();
            }
            spawningEnemies=true;
            for(enemyInfo enemy : enemiesToSpawn){
                BaseEnemy e = BaseEnemy.getNewEnemy(BaseEnemy.type.values()[enemy.type], enemy.X * Options.aspectRatio, enemy.Y * Options.aspectRatio);
                e.network_id = enemy.id;
                gameObjects.put(enemy.id, e);
            }
            enemiesToSpawn.clear();
            spawningEnemies = false;
        }
        
        for(enemyAttack enemy : bossAttack){
            if(Multiplayer.myID.equals(enemy.player_id)){
                ((BaseBoss)gameObjects.get(enemy.id)).setSkillToCast(enemy.attackType);
                //((BaseBoss)gameObjects.get(enemy.id)).collectTargets();
                ((BaseBoss)gameObjects.get(enemy.id)).attack(player);
            }
            else{
                BaseActor b = OtherPlayers.get(enemy.player_id);
                ((BaseBoss)gameObjects.get(enemy.id)).setSkillToCast(enemy.attackType);
                //((BaseBoss)gameObjects.get(enemy.id)).collectTargets();
                ((BaseBoss)gameObjects.get(enemy.id)).attack(OtherPlayers.get(enemy.player_id));
            }
        }
        bossAttack.clear();
        
        for(enemyAttack enemy : enemiesAttack){
            if(Multiplayer.myID.equals(enemy.player_id)){
                ((BaseEnemy)gameObjects.get(enemy.id)).attack(player);
            }
            else{
                BaseActor b = OtherPlayers.get(enemy.player_id);
                //((BaseEnemy)gameObjects.get(enemy.id)).target = OtherPlayers.get(enemy.player_id);
                if(gameObjects.get(enemy.id) != null){
                    ((BaseEnemy)gameObjects.get(enemy.id)).attack(OtherPlayers.get(enemy.player_id));
                }
            }
        }
        enemiesAttack.clear();
        
        updatePlayerTime += dt;
        if((updatePlayerTime > updatePlayerTimer && player.canMove()) || player.directionChanged()){
            updatePlayerTime = 0;
            JSONObject data = new JSONObject();
            try{
                data.put("targetX", player.getX() / Options.aspectRatio);
                data.put("targetY", player.getY() / Options.aspectRatio);
                data.put("dir", player.dir.ordinal());
                Multiplayer.socket.emit("updateHeroPosition", data);
            }catch(Exception e){
                 System.out.println("Failed to push player data " + e.getMessage());
            }
        }
        
        gameObjectToRemove.addAll(gameObjectToRemoveQueue);
        gameObjectToRemoveQueue.clear();
        
        List<String> objRemoved = new ArrayList();
        for(String id : gameObjectToRemove){
            BaseActor b = gameObjects.remove(id);
            //System.out.println("Trying to remove id: " + id);
            //System.out.println("Trying to remove Object: " + b);
            if(b != null && b instanceof BaseEnemy && b.isAnimationFinished()){
                b.remove();
                objRemoved.add(id);
            }else if(b!= null && !(b instanceof BaseEnemy)){
                 b.remove();
                 objRemoved.add(id);
            }
        }
        gameObjectToRemove.clear();
        //----------------------------------------------------------------------------------------------------------------------------------------------
       
        if(!Multiplayer.host)
            return;
        
        for(BaseActor b : Spawner.getNewlySpawnedItems()){
            gameObjects.put(b.network_id, b);
        }
        
        SyncEnemyTime += dt;
        if(SyncEnemyTime > SyncEnemyTimer){
            SyncEnemyTime = 0f;
            JSONArray enemies = new JSONArray();
            for(BaseActor b: gameObjects.values()){
                if(b instanceof BaseEnemy){
                    JSONObject d = new JSONObject();
                    try{
                        d.put("id", ((BaseEnemy) b).network_id);
                        d.put("curX", ((BaseEnemy) b).getX() / Options.aspectRatio);
                        d.put("curY", ((BaseEnemy) b).getY() / Options.aspectRatio);
                        d.put("tarX", ((BaseEnemy) b).moveTo.x / Options.aspectRatio);
                        d.put("tarY", ((BaseEnemy) b).moveTo.y / Options.aspectRatio);
                    }catch(Exception e){
                        System.out.println("Couldn't add enemey for Sync " + ((BaseEnemy) b).network_id + "| " + e.getMessage() + d.toString());
                    }
                    enemies.put(d);
                }
            }
            try{
                Multiplayer.socket.emit("syncEnemies", enemies);
            }catch(Exception e){
                 System.err.println("Failed to push Enemy data " + e.getMessage());
            }  
        }
    }

    private void setupSocketListeners() {
        Multiplayer.socket.on("healthPotionCreated", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        if(Multiplayer.host)
                                            return;
                                        enemyInfo en = new enemyInfo();
                                        en.id = data.getString("id");
                                        en.X = data.getInt("x");
                                        en.Y = data.getInt("y");
                                        healthPotsToSpawn.add(en);
                                    }catch(Exception e){
                                        System.out.println("Failed Health Pot Creation");
                                    }
                                }
                            }).on("GameItemSpawned", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        
                                    }catch(Exception e){
                                        System.out.println("Failed to Spawn Item");
                                    }
                                    gameObjectsToSpawnQueue.add(data);
                                }
                            }).on("healthPotionPickUp", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        System.out.println("Health Pot Picked up");
                                        String id = data.getString("id");
                                        System.out.println(id);
                                        gameObjectToRemoveQueue.add(id);
                                    }catch(Exception e){
                                        System.out.println("Failed Health Pot Pick Up " + e.getMessage());
                                    }
                                }
                            }).on("pingHostForLoadData", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    if(!Multiplayer.host){
                                        return;
                                    }
                                    if(spawnGathered){
                                        JSONObject dataSpawn = new JSONObject();
                                        try{
                                            dataSpawn.put("spawnDataX", spawnPos.x / Options.aspectRatio);
                                            dataSpawn.put("spawnDataY", spawnPos.y / Options.aspectRatio);
                                            Multiplayer.socket.emit("spawnData", dataSpawn);
                                        }catch(Exception e){
                                            System.out.println("Problem Sending Spawn Data After Ping for it");
                                        }
                                    }
                                    if(mapLoaded){
                                        Integer[][] ml = lg.getMapLayout();
                                        JSONArray map = new JSONArray();
                                        JSONArray mapRow;
                                        for(int row = 0; row < ml.length; row ++){
                                            mapRow = new JSONArray();
                                            for(int col = 0; col < ml[row].length; col ++){
                                                mapRow.put(ml[row][col]);
                                            }
                                            map.put(mapRow);
                                        }
                                        JSONObject dataMap = new JSONObject();
                                        try{
                                            dataMap.put("mapData", map);
                                            Multiplayer.socket.emit("mapData", dataMap);
                                        }catch(Exception e){
                                            System.out.println("Problem Sending Map Data After Pinged for it");
                                        }
                                    }
                                }
                            }).on("mapData", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    Integer[][] ml = new Integer[dungeonWidth][dungeonHeight];
                                    try{
                                        JSONArray map = (JSONArray)data.getJSONArray("mapData");
                                        for(int row = 0; row < map.length(); row ++){
                                            JSONArray mapRow = map.getJSONArray(row);
                                            for(int col = 0; col < mapRow.length(); col ++){
                                                ml[row][col] = mapRow.getInt(col);
                                            }
                                        }
                                        lg = new LevelGenerator(dungeonWidth, dungeonHeight);
                                        lg.setDefaultSize(100);
                                        if(Difficulty.RoomType == Difficulty.RoomTypes.Dungeon){
                                            lg.setRoom(new DungeonRoom());
                                        }else if(Difficulty.RoomType == Difficulty.RoomTypes.Forest){
                                            lg.setRoom(new ForestRoom());
                                        }
                                        lg.setMapLayout(ml);
                                        Difficulty.worldHeight = lg.getPixelHeight();
                                        Difficulty.worldWidth = lg.getPixelWidth();
                                        Difficulty.newGame();
                                        mapLoaded = true;
                                        loading = !mapLoaded && !spawnGathered;
                                    }catch(Exception e){
                                        System.out.println("Failed to get Map data " + e.getMessage());
                                    }
                                }
                            }).on("spawnData", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        spawnPos =new Vector2(data.getInt("spawnDataX") * Options.aspectRatio,data.getInt("spawnDataY")  * Options.aspectRatio);
                                        player.setPosition(spawnPos.x,spawnPos.y);
                                        JSONObject dataSend = new JSONObject();
                                        try{
                                            dataSend.put("targetX", player.getX() / Options.aspectRatio);
                                            dataSend.put("targetY", player.getY() / Options.aspectRatio);
                                            dataSend.put("dir", player.dir.ordinal());
                                            Multiplayer.socket.emit("updateHeroPosition", dataSend);
                                        }catch(Exception e){
                                             System.out.println("Failed to push player data after spawn " + e.getMessage());
                                        }
                                    }catch(Exception e){
                                        System.out.println("Failed to get spawn " + e.getMessage());
                                    }
                                    reloadPlayers = true;
                                    spawnGathered = true;
                                    loading = !mapLoaded && !spawnGathered;
                                }
                            }).on("enemyCreated", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        if(!Multiplayer.host){
                                            //while(spawningEnemies);
                                            
                                            addingEnemy = true;
                                            enemyInfo en = new enemyInfo();
                                            en.id = data.getString("id");
                                            en.X = data.getInt("x");
                                            en.Y = data.getInt("y");
                                            en.type = data.getInt("type");
                                            healthPotsToSpawnQueue.add(en);
                                        }
                                    }catch(Exception e){
                                        System.out.println("Failed Enemy Creation");
                                    }
                                    addingEnemy = false;
                                }
                            }).on("enemyTargetChange", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        if(Multiplayer.host)
                                            return;
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
                            }).on("bossAttack", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONObject data = (JSONObject) os[0];
                                    try{
                                        System.out.println("BossAttack");
                                        enemyAttack ea = new enemyAttack();
                                        System.out.println(data.toString());
                                        ea.id = data.getString("id");
                                        ea.player_id = data.getString("target");
                                        ea.attackType = data.getInt("skillCast");
                                        float d = (float)data.getDouble("degreeV");
                                        JSONArray t = data.getJSONArray("targets");
                                        List<BaseActor> targets = new ArrayList();
                                        for(int i = 0; i < t.length(); i ++){
                                            String id = (String)t.get(i);
                                            if(Multiplayer.myID.equals(id)){
                                                targets.add(player);
                                            }else{
                                                targets.add(OtherPlayers.get(id));
                                            }
                                        }
                                        ((BaseBoss)gameObjects.get(ea.id)).setTargets(targets);
                                        ((BaseBoss)gameObjects.get(ea.id)).setVariation(d);
                                        bossAttack.add(ea);
                                    }catch(Exception e){
                                        System.out.println("boss Attack Failed: " + e.getMessage() + ": " + e.getStackTrace()[0].getLineNumber());
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
                                        gameObjectToRemoveQueue.add(id);
                                    }catch(Exception e){
                                        System.out.println("Enemy failed to Die" + e.getMessage());
                                    }
                                }
                            }).on("syncEnemies", new Emitter.Listener() {
                                @Override
                                public void call(Object... os) {
                                    JSONArray array = (JSONArray) os[0];
                                    try{
                                        if(Multiplayer.host)
                                            return;
                                        for(int i = 0; i < array.length(); i++){
                                           JSONObject data = array.getJSONObject(i);
                                           String id = data.getString("id");
                                           int curX = data.getInt("curX");
                                           int curY = data.getInt("curY");
                                           int tarX = data.getInt("tarX");
                                           int tarY = data.getInt("tarY");
                                           gameObjects.get(id).setPosition(curX * Options.aspectRatio, curY* Options.aspectRatio);
                                           ((BaseEnemy)gameObjects.get(id)).moveTo = new Vector2(tarX* Options.aspectRatio, tarY* Options.aspectRatio);
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
                                        if(OtherPlayers.get(netID) == null){
                                           System.out.println("updateHeroPosition " + netID);
                                           // Multiplayer.socket.emit("getRoomPlayersGame");
                                        }else{
                                            OtherPlayers.get(netID).dir = Player.direction.values()[dir];
                                            OtherPlayersOldPos.put(netID,new Vector2(OtherPlayers.get(netID).getX(), 
                                                    OtherPlayers.get(netID).getY()) );
                                            OtherPlayersMoveTo.put(netID, new Vector2(tarX, tarY));
                                            OtherPlayersTimer.put(netID, 0f);
                                        }
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
                                        if(info.id.equals(player.network_id))
                                            return;
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
                                        if(id.equals(player.network_id))
                                            return;
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
                                        if(id.equals(player.network_id))
                                            return;
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
                                           System.out.println("getRoomPlayersGame " +id);
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
