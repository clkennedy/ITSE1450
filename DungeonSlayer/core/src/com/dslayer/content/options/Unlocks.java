/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.options;

import com.atkinson.game.engine.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.options.Options;
import com.dslayer.content.options.Progress;
import com.dslayer.content.screens.MainMenuScreen;
import java.util.ArrayList;

/**
 *
 * @author ARustedKnight
 */
public class Unlocks extends BaseScreen{
    
    static BaseActor mainmenu;
    
    static int currentMenuIndex = 0;
    static int currentMenu = 0;
    
    private static Object[] PlayerUnlocks = {};
    public static boolean[] PlayerUnlocked = {};
    
    private static Object[] EnemyUnlocks = {};
    public static boolean[] EnemyUnlocked = {};
    
    private static Object[] CollectableUnlocks = {};
    public static boolean[] CollectableUnlocked = {};
    
    private static Object[] BulletUnlocks = {};
    public static boolean[] BulletUnlocked = {};
    
    private static Object[] AllUnlocks = {};
    public static int[] scoreToUnlock = {};
    
    public static Avatar currentAvatar = (Avatar)PlayerUnlocks[0];
    public static Avatar currentEnemyAvatar = (Avatar)EnemyUnlocks[0];
    public static Avatar currentCollectableAvatar = (Avatar)CollectableUnlocks[0];
    public static Avatar currentBulletAvatar = (Avatar)BulletUnlocks[0];
    
    private static int highScore = 0;
    
    private ArrayList<BaseActor> avatars;
    private ArrayList<BaseActor> enemyAvatars;
    private ArrayList<BaseActor> collectableAvatars;
    private ArrayList<BaseActor> bulletAvatars;
    
    BaseActor aBox;
    BaseActor eBox;
    BaseActor cBox;
    BaseActor bBox;
    
    private static float scale = .5f;
    
    private static boolean[] cheat = {false,false, false, false, false, false, false, false} ;
    
    public void initialize()
    {
        
        float rowY = Gdx.graphics.getHeight();
        
        avatars = new ArrayList();
        enemyAvatars = new ArrayList();
        collectableAvatars = new ArrayList();
        bulletAvatars = new ArrayList();
        
        float bw = 0;
        
        while(bw < Gdx.graphics.getWidth()){
            //BaseActor bGround =new Sky(bw, 0, mainStage, "sky.png");
            //bGround.setSpeed(0);
            //bw+=bGround.getWidth();
        }
        
        BaseActor sAvatar = new BaseActor(0,0, mainStage);
        sAvatar.loadTexture( "selectavatar.png" );
        sAvatar.setSize((sAvatar.getWidth() / 2)*Options.aspectRatio, (sAvatar.getHeight() /2)*Options.aspectRatio);
        rowY -= sAvatar.getHeight();
        sAvatar.setPosition(10, rowY);
        
        BaseActor avatar;
        int row = 0;
        int temph = 0;
        float w = 0;
        float h = 0;
        for(int i = 0; i < PlayerUnlocked.length ; i ++){
            avatar = new BaseActor(0, 0, mainStage);
            avatar.setAnimation(((Avatar)PlayerUnlocks[i]).getAnim());
            w = ((Avatar)EnemyUnlocks[i]).getWidth() * scale;
            w = (w<50) ? w : 50;
            h = ((Avatar)EnemyUnlocks[i]).getHeight() * scale;
            h = (h<50) ? h : 50;
            avatar.setSize(w*Options.aspectRatio, h*Options.aspectRatio);
            avatar.setOrigin(avatar.getWidth() / 2, avatar.getHeight()/ 2);
            avatar.setPosition((i  * (avatar.getWidth() + 20)) + 50, rowY - avatar.getHeight() - 20);
            
            if(!PlayerUnlocked[i]){
                avatar.setOpacity(0);
            }
            else if(currentAvatar == PlayerUnlocks[i]){
                aBox = new BaseActor(0,0, mainStage);
                aBox.loadTexture( "box.png" );
                aBox.setSize((140* scale)*Options.aspectRatio, (130 * scale)*Options.aspectRatio);
                aBox.setOrigin(aBox.getWidth() / 2, aBox.getHeight()/ 2);
                aBox.centerAtActor(avatar);
                temph = (int) aBox.getHeight() +20 ;
                //System.err.println(box);
            }
            else{
                avatar.addListener(new Hover(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    setCurrentActor(event, x, y);
                }

            });
            }
            avatars.add(avatar);
        }
        
        rowY-=temph;
        
        BaseActor eAvatar = new BaseActor(0,0, mainStage);
        eAvatar.loadTexture( "selectenemy.png" );
        eAvatar.setSize((eAvatar.getWidth() / 2)*Options.aspectRatio, (eAvatar.getHeight() /2)*Options.aspectRatio);
        rowY -= eAvatar.getHeight();
        eAvatar.setPosition(10, rowY);
        
        BaseActor enemyAvatar;
        row = 1;
        
        for(int i = 0; i < EnemyUnlocked.length ; i ++){
            enemyAvatar = new BaseActor(0, 0, mainStage);
            enemyAvatar.setAnimation(((Avatar)EnemyUnlocks[i]).getAnim());
            w = ((Avatar)EnemyUnlocks[i]).getWidth() * scale;
            w = (w<50) ? w : 50;
            h = ((Avatar)EnemyUnlocks[i]).getHeight() * scale;
            h = (h<50) ? h : 50;
            enemyAvatar.setSize(w*Options.aspectRatio, h*Options.aspectRatio);
            enemyAvatar.setOrigin(enemyAvatar.getWidth() / 2, enemyAvatar.getHeight()/ 2);
            enemyAvatar.setPosition((i * (enemyAvatar.getWidth() + 20)) + 50, rowY - enemyAvatar.getHeight() - 20);
            
            if(!EnemyUnlocked[i]){
                enemyAvatar.setOpacity(0);
            }
            else if(currentEnemyAvatar == EnemyUnlocks[i]){
                eBox = new BaseActor(0,0, mainStage);
                eBox.loadTexture( "box.png" );
                eBox.setSize((140* scale)*Options.aspectRatio, (130 * scale)*Options.aspectRatio);
                eBox.setOrigin(eBox.getWidth() / 2, eBox.getHeight()/ 2);
                eBox.centerAtActor(enemyAvatar);
                temph = (int)eBox.getHeight() +20 ;
                //System.err.println(box);
            }
            else{
                enemyAvatar.addListener(new Hover(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    setCurrentEnemyActor(event, x, y);
                }

            });
            }
            enemyAvatars.add(enemyAvatar);
        }
        rowY-=temph;
        
        BaseActor cAvatar = new BaseActor(0,0, mainStage);
        cAvatar.loadTexture( "selectcollectable.png" );
        cAvatar.setSize((cAvatar.getWidth() / 2)*Options.aspectRatio, (cAvatar.getHeight() /2)*Options.aspectRatio);
        rowY -= cAvatar.getHeight();
        cAvatar.setPosition(10, rowY);
        
        BaseActor collectableAvatar;
        row = 1;
        for(int i = 0; i < CollectableUnlocked.length ; i ++){
            collectableAvatar = new BaseActor(0, 0, mainStage);
            collectableAvatar.setAnimation(((Avatar)CollectableUnlocks[i]).getAnim());
            collectableAvatar.setSize(50*Options.aspectRatio, 50*Options.aspectRatio);
            collectableAvatar.setOrigin(collectableAvatar.getWidth() / 2, collectableAvatar.getHeight()/ 2);
            collectableAvatar.setPosition((i * (collectableAvatar.getWidth() + 20)) + 50, rowY - collectableAvatar.getHeight() - 20);
            
            if(!CollectableUnlocked[i]){
                collectableAvatar.setOpacity(0);
            }
            else if(currentCollectableAvatar == CollectableUnlocks[i]){
                cBox = new BaseActor(0,0, mainStage);
                cBox.loadTexture( "box.png" );
                cBox.setSize((140* scale)*Options.aspectRatio, (130 * scale)*Options.aspectRatio);
                cBox.setOrigin(cBox.getWidth() / 2, cBox.getHeight()/ 2);
                cBox.centerAtActor(collectableAvatar);
                temph = temph = (int)cBox.getHeight() +20 ;
                //System.err.println(box);
            }
            else{
                collectableAvatar.addListener(new Hover(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    setCurrentCollectableActor(event, x, y);
                }

            });
            }
            collectableAvatars.add(collectableAvatar);
        }
        rowY-=temph;
        
        BaseActor bAvatar = new BaseActor(0,0, mainStage);
        bAvatar.loadTexture( "selectbullet.png" );
        bAvatar.setSize((bAvatar.getWidth() / 2)*Options.aspectRatio, (bAvatar.getHeight() /2)*Options.aspectRatio);
        rowY -= cAvatar.getHeight();
        bAvatar.setPosition(10, rowY);
        
        BaseActor bulletAvatar;
        row = 1;
        for(int i = 0; i < BulletUnlocked.length ; i ++){
            bulletAvatar = new BaseActor(0, 0, mainStage);
            bulletAvatar.setAnimation(((Avatar)BulletUnlocks[i]).getAnim());
            bulletAvatar.setSize(50*Options.aspectRatio, 50*Options.aspectRatio);
            bulletAvatar.setOrigin(bulletAvatar.getWidth() / 2, bulletAvatar.getHeight()/ 2);
            bulletAvatar.setPosition((i * (bulletAvatar.getWidth() + 20)) + 50, rowY - bulletAvatar.getHeight() - 40);
            
            if(!BulletUnlocked[i]){
                bulletAvatar.setOpacity(0);
            }
            else if(currentBulletAvatar == BulletUnlocks[i]){
                bBox = new BaseActor(0,0, mainStage);
                bBox.loadTexture( "box.png" );
                bBox.setSize((140* scale)*Options.aspectRatio, (130 * scale)*Options.aspectRatio);
                bBox.setOrigin(bBox.getWidth() / 2, bBox.getHeight()/ 2);
                bBox.centerAtActor(bulletAvatar);
                temph = temph = (int)bBox.getHeight() +20 ;
                //System.err.println(box);
            }
            else{
                bulletAvatar.addListener(new Hover(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    setCurrentBulletActor(event, x, y);
                }

            });
            }
            bulletAvatars.add(bulletAvatar);
        }
        
        
        
        mainmenu = new BaseActor(0, 0, mainStage);
        mainmenu.loadTexture("mainmenu.png");
        mainmenu.setSize((mainmenu.getWidth() / 3)*Options.aspectRatio, (mainmenu.getHeight() /3)*Options.aspectRatio);
        mainmenu.setOriginX(mainmenu.getWidth() / 2);
        mainmenu.setOriginY(mainmenu.getHeight()/ 2);
        mainmenu.setBoundaryRectangle();
        mainmenu.getBoundaryPolygon();
        mainmenu.centerAtPosition(Gdx.graphics.getWidth() - (mainmenu.getWidth() / 2) - (50*Options.aspectRatio),50*Options.aspectRatio);
        mainmenu.addListener(new Hover(){
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                event.getListenerActor().remove();
                Progress.Save();
                BaseGame.setActiveScreen(new MainMenuScreen());
            }
        
        });
        currentMenu =-1;
        descaleSelected();
    }
    
    public static int getCurrentAvatarIndex(){
        for(int i = 0; i < PlayerUnlocks.length; i++){
            if(PlayerUnlocks[i] == currentAvatar){
                return i;
            }
        }
        return -1;
    }
    
    public static int getCurrentEnemyIndex(){
        for(int i = 0; i < EnemyUnlocks.length; i++){
            if(EnemyUnlocks[i] == currentEnemyAvatar){
                return i;
            }
        }
        return -1;
    }
    
    public static int getCurrentCollectableIndex(){
        for(int i = 0; i < CollectableUnlocks.length; i++){
            if(CollectableUnlocks[i] == currentCollectableAvatar){
                return i;
            }
        }
        return -1;
    }
    
    public static int getCurrentBulletIndex(){
        for(int i = 0; i < BulletUnlocks.length; i++){
            if(BulletUnlocks[i] == currentBulletAvatar){
                return i;
            }
        }
        return -1;
    }
    
    public static void setSelects(int aIndex, int eIndex, int cIndex, int bIndex){
        
        if(aIndex < 0 || !PlayerUnlocked[aIndex]){
            aIndex = 0;
        }
        currentAvatar = (Avatar)PlayerUnlocks[aIndex];
        
        if(eIndex < 0 || !EnemyUnlocked[eIndex]){
            eIndex = 0;
        }
        currentEnemyAvatar = (Avatar)EnemyUnlocks[eIndex];
        if(cIndex < 0 || !CollectableUnlocked[cIndex]){
            cIndex = 0;
        }
        currentCollectableAvatar = (Avatar)CollectableUnlocks[cIndex];
        if(bIndex < 0 || !BulletUnlocked[bIndex]){
            bIndex = 0;
        }
        currentBulletAvatar = (Avatar)BulletUnlocks[bIndex];
    }
    
    public static void reset(){
        highScore = 0;
        for(int i = 1; i < PlayerUnlocked.length; i++){
            PlayerUnlocked[i] = false;
        }
        currentAvatar = (Avatar)PlayerUnlocks[0];
        for(int i = 1; i < EnemyUnlocked.length; i++){
            EnemyUnlocked[i] = false;
        }
        currentEnemyAvatar = (Avatar)EnemyUnlocks[0];
        for(int i = 1; i < CollectableUnlocked.length; i++){
            CollectableUnlocked[i] = false;
        }
        currentCollectableAvatar = (Avatar)CollectableUnlocks[0];
        for(int i = 1; i < BulletUnlocked.length; i++){
            BulletUnlocked[i] = false;
        }
        currentBulletAvatar = (Avatar)BulletUnlocks[0];
    }
    
    public static ArrayList<BaseActor> Unlock(int score){
        
        if(cheat[7]){
            for(int j = 0; j < PlayerUnlocks.length; j ++){
                PlayerUnlocked[j] = true;
            }
            for(int j = 0; j < EnemyUnlocks.length; j ++){
                EnemyUnlocked[j] = true;
            }
            for(int j = 0; j < CollectableUnlocks.length; j ++){
                CollectableUnlocked[j] = true;
            }
            for(int j = 0; j < BulletUnlocked.length; j ++){
                BulletUnlocked[j] = true;
            }
        }
        
        if(score < highScore){
            return null;
        }
        
        ArrayList<BaseActor> unlockList = new ArrayList();
        
        for( int i = 0; i < AllUnlocks.length; i++){
            if(scoreToUnlock[i] < score){
                for(int j = 0; j < PlayerUnlocks.length; j ++){
                    if(AllUnlocks[i] == PlayerUnlocks[j] && !PlayerUnlocked[j]){
                        PlayerUnlocked[j] = true;
                        BaseActor unlocked = new BaseActor();
                        unlocked.setAnimation(((Avatar)PlayerUnlocks[j]).getAnim());
                        unlocked.setSize(((Avatar)PlayerUnlocks[j]).getWidth(), ((Avatar)PlayerUnlocks[j]).getHeight());
                        unlockList.add(unlocked);
                    }
                }
                
                for(int j = 0; j < EnemyUnlocks.length; j ++){
                    if(AllUnlocks[i] == EnemyUnlocks[j] && !EnemyUnlocked[j]){
                        EnemyUnlocked[j] = true;
                        BaseActor unlocked = new BaseActor();
                        unlocked.setAnimation(((Avatar)EnemyUnlocks[j]).getAnim());
                        unlocked.setSize(((Avatar)EnemyUnlocks[j]).getWidth(), ((Avatar)EnemyUnlocks[j]).getHeight());
                        unlockList.add(unlocked);
                    }
                }
                
                for(int j = 0; j < CollectableUnlocks.length; j ++){
                    if(AllUnlocks[i] == CollectableUnlocks[j] && !CollectableUnlocked[j]){
                        CollectableUnlocked[j] = true;
                        BaseActor unlocked = new BaseActor();
                        unlocked.setAnimation(((Avatar)CollectableUnlocks[j]).getAnim());
                        unlocked.setSize(((Avatar)CollectableUnlocks[j]).getWidth(), ((Avatar)CollectableUnlocks[j]).getHeight());
                        unlockList.add(unlocked);
                    }
                }
                for(int j = 0; j < BulletUnlocks.length; j ++){
                    if(AllUnlocks[i] == BulletUnlocks[j] && !BulletUnlocked[j]){
                        BulletUnlocked[j] = true;
                        BaseActor unlocked = new BaseActor();
                        unlocked.setAnimation(((Avatar)BulletUnlocks[j]).getAnim());
                        unlocked.setSize(((Avatar)BulletUnlocks[j]).getWidth(), ((Avatar)BulletUnlocks[j]).getHeight());
                        unlockList.add(unlocked);
                    }
                }
            }
        }
        
        highScore = score;
        return unlockList;
    }
    
    public static int getHighScore(){
        return highScore;
    }
    
    private void setCurrentActor(InputEvent event, float x, float y){
        for(int i = 0; i < avatars.size(); i++){
            if(avatars.get(i) == event.getListenerActor()){
                currentAvatar = (Avatar)PlayerUnlocks[i];
            }
        }
        BaseGame.setActiveScreen( new Unlocks());
    }
    
    private void setCurrentEnemyActor(InputEvent event, float x, float y){
        for(int i = 0; i < enemyAvatars.size(); i++){
            if(enemyAvatars.get(i) == event.getListenerActor()){
                currentEnemyAvatar = (Avatar)EnemyUnlocks[i];
            }
        }
        BaseGame.setActiveScreen( new Unlocks());
    }
    
    private void setCurrentCollectableActor(InputEvent event, float x, float y){
        for(int i = 0; i < collectableAvatars.size(); i++){
            if(collectableAvatars.get(i) == event.getListenerActor()){
                currentCollectableAvatar = (Avatar)CollectableUnlocks[i];
            }
        }
        BaseGame.setActiveScreen( new Unlocks());
    }
    private void setCurrentBulletActor(InputEvent event, float x, float y){
        for(int i = 0; i < bulletAvatars.size(); i++){
            if(bulletAvatars.get(i) == event.getListenerActor()){
                currentBulletAvatar = (Avatar)BulletUnlocks[i];
            }
        }
        BaseGame.setActiveScreen( new Unlocks());
    }
    
    public void update(float dt) {
        //mainStage.act(dt);
        if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)){
            if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
                if(cheat[0] && cheat[1] && !cheat[2]){
                    cheat[2] = true;
                }
                else if(!cheat[0]){
                    cheat[0] = true;
                }
                else{
                    for(int i = 0; i < cheat.length; i++){
                        cheat[i] = false;
                    }
                }
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
                if(cheat[0] && cheat[1]&& cheat[2]&& !cheat[3]){
                    cheat[3] = true;
                }
                else if(cheat[0] && !cheat[1]){
                    cheat[1] = true;
                }
                else{
                    for(int i = 0; i < cheat.length; i++){
                        cheat[i] = false;
                    }
                }
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
                if(cheat[0] && cheat[1]&& cheat[2]&& cheat[3]&&!cheat[4]){
                    cheat[4] = true;
                }
                else{
                    for(int i = 0; i < cheat.length; i++){
                        cheat[i] = false;
                    }
                }
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
                if(cheat[0] && cheat[1]&& cheat[2]&& cheat[3]&& cheat[4]&& !cheat[5]){
                    cheat[5] = true;
                }
                else{
                    for(int i = 0; i < cheat.length; i++){
                        cheat[i] = false;
                    }
                }
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
                if(cheat[0] && cheat[1]&& cheat[2]&& cheat[3]&& cheat[4]&& cheat[5]&& !cheat[6]){
                    cheat[6] = true;
                }
                else{
                    for(int i = 0; i < cheat.length; i++){
                        cheat[i] = false;
                    }
                }
            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
                if(cheat[0] && cheat[1]&& cheat[2]&& cheat[3]&& cheat[4]&& cheat[5]&& cheat[6]&& !cheat[7]){
                    cheat[7] = true;
                    Unlock(0);
                    BaseGame.setActiveScreen(new MainMenuScreen());
                }
                else{
                    for(int i = 0; i < cheat.length; i++){
                        cheat[i] = false;
                    }
                }
            }
            else{
                for(int i = 0; i < cheat.length; i++){
                    cheat[i] = false;
                }
            }
        }
        
    }
    @Override
    public boolean keyDown(int keyCode) {
        if(keyCode == Input.Keys.ENTER && currentMenu == 4){
            BaseGame.setActiveScreen(new MainMenuScreen());
        }
        if(keyCode == Input.Keys.UP){
            descaleSelected();
            currentMenu --;
            if(currentMenu < 0){
                currentMenu = 0;
            }
            descaleSelected();
            scaleSelected();
        }
        if(keyCode == Input.Keys.DOWN){
            
            currentMenu ++;
            if(currentMenu > 4){
                currentMenu = 4;
            }
            descaleSelected();
            scaleSelected();
        }
        if(keyCode == Input.Keys.RIGHT){
                currentMenuIndex++;
            scaleSelected();
        }
        if(keyCode == Input.Keys.LEFT){
            currentMenuIndex--;
            if(currentMenuIndex < 0){
                currentMenuIndex = 0;
            }
            scaleSelected();
        }
        if(keyCode == Input.Keys.ESCAPE){
            BaseGame.setActiveScreen(new MainMenuScreen());
        }
        return true;
    }
    public void descaleSelected(){
        aBox.setScale(1f);
        eBox.setScale(1f);
        cBox.setScale(1f);
        bBox.setScale(1f);
        if(currentMenuIndex < 0) return;
        switch(currentMenu){
            case 0:
                for(int i = 0; i < PlayerUnlocks.length; i ++){
                    if(PlayerUnlocks[i] == currentAvatar){
                        currentMenuIndex = i;
                        break;
                    }
                }
            break;
            case 1:
                for(int i = 0; i < EnemyUnlocks.length; i ++){
                    if(EnemyUnlocks[i] == currentEnemyAvatar){
                        currentMenuIndex = i;
                        break;
                    }
                }
            break;
            case 2:
                for(int i = 0; i < CollectableUnlocks.length; i ++){
                    if(CollectableUnlocks[i] == currentCollectableAvatar){
                        currentMenuIndex = i;
                        break;
                    }
                }
            break;
            case 3:
                for(int i = 0; i < BulletUnlocks.length; i ++){
                    if(BulletUnlocks[i] == currentBulletAvatar){
                        currentMenuIndex = i;
                        break;
                    }
                }
            break;
            case 4:
                mainmenu.setScale(1f);
            break;
        }
    }
    public void scaleSelected(){
        switch(currentMenu){
            case 0:
                if(currentMenuIndex > lastUnlocked(PlayerUnlocked)){
                    currentMenuIndex = lastUnlocked(PlayerUnlocked);
                }
                aBox.setScale(1.2f);
                aBox.centerAtActor(avatars.get(currentMenuIndex));
                currentAvatar = (Avatar)PlayerUnlocks[currentMenuIndex];
                //BaseGame.setActiveScreen(new Unlocks());
            break;
            case 1:
                if(currentMenuIndex > lastUnlocked(EnemyUnlocked)){
                    currentMenuIndex = lastUnlocked(EnemyUnlocked);
                }
                eBox.setScale(1.2f);
                eBox.centerAtActor(enemyAvatars.get(currentMenuIndex));
                currentEnemyAvatar = (Avatar)EnemyUnlocks[currentMenuIndex];
                //BaseGame.setActiveScreen(new Unlocks());
            break;
            case 2:
                if(currentMenuIndex > lastUnlocked(CollectableUnlocked)){
                    currentMenuIndex = lastUnlocked(CollectableUnlocked);
                }
                cBox.setScale(1.2f);
                cBox.centerAtActor(collectableAvatars.get(currentMenuIndex));
                currentCollectableAvatar = (Avatar)CollectableUnlocks[currentMenuIndex];
                //BaseGame.setActiveScreen(new Unlocks());
            break;
            case 3:
                if(currentMenuIndex > lastUnlocked(BulletUnlocked)){
                    currentMenuIndex = lastUnlocked(BulletUnlocked);
                }
                bBox.setScale(1.2f);
                bBox.centerAtActor(bulletAvatars.get(currentMenuIndex));
                currentBulletAvatar = (Avatar)BulletUnlocks[currentMenuIndex];
                //BaseGame.setActiveScreen(new Unlocks());
            break;
            case 4:
                mainmenu.setScale(1.2f);
            break;
        }
        
    }
    private int lastUnlocked(boolean[] b){
        for(int i = 0; i < b.length; i ++){
            if(!b[i]){
                return i - 1;
            }
        }
        return b.length - 1;
    }
}
