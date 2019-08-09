/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Enemy.Boss.Phantom;

import com.dslayer.content.Enemy.Skeleton.*;
import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.math.MathUtils;
import com.dslayer.content.options.Difficulty;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Enemy.Boss.BaseBoss;
import com.dslayer.content.projectiles.Spells.ProjectileSpell;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Skills.DarkLaser;
import com.dslayer.content.Skills.FireBall;
import com.dslayer.content.Skills.ShadowGrab;
import com.dslayer.content.Skills.Skill;
import com.dslayer.content.Skills.Slash;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.options.LPC;
import com.dslayer.content.options.Multiplayer;
import com.dslayer.content.options.Options;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author ARustedKnight
 */

public class PhantomBoss extends BaseBoss{
    
    private final String phantomWalk = "Bosses/Phantom/PanthonFloat.png";
    private final String phantomDie = "Bosses/Phantom/PhantomDie.png";
    private final String phantomAttack = "Bosses/Phantom/PhantomAttack.png";
    private final String phantomPrepChannel = "Bosses/Phantom/PhantomPullCandle.png";
    private final String phantomChannel = "Bosses/Phantom/PhantomCandle.png";
    private final String phantomChannelAura = "particles/DarkPurpleOvalAura.png";
    protected enum WalkDirection{up,left,down,right};
    protected WalkDirection currentDirection;
    
    protected Skill skill2;
    
    protected BaseActor channeledSkill = null;
    protected boolean doneAttacking = false;
    
    public PhantomBoss(float x, float y, Stage s){
        
        super(x,y,s);
        //texture = new Texture(Gdx.files.internal(skeleMage));
        
        degreesToCastSkillAt = new ArrayList();
        
        pointsWorth = 15;
        
        maxHealth = 400;
        health = maxHealth;
        healthBar = new Rectangle(x, y, maxHealth , 5);
        currentDirection = WalkDirection.left;
        //setAnimation(this.walkAnimList.get(currentDirection.ordinal()));
        loadAnimationFromSheet(phantomWalk,1, 6, .3f, true);
        size = 80;
        setScale(Options.aspectRatio);
        setBoundaryPolygonLong(10);
        setMaxSpeed(50);
        setOrigin(getWidth() /2, getHeight() / 2);

        AttackRange = new Circle(x, y, 300* Options.aspectRatio);
        TargetRange = new Circle(x, y, 500* Options.aspectRatio);
        
        skill = new ShadowGrab();
        skill.setDamage(50);
        skill.isEnemy(true);
        
        skill2 = new DarkLaser();
        skill2.setDamage(750);
        skill2.setIndicatorTime(1.3f);
        skill2.isEnemy(true);
        
        moveTo = new Vector2();
        moveTo.x = MathUtils.random(Difficulty.worldWidth);
        moveTo.y = MathUtils.random(Difficulty.worldHeight);
        
        moveToRange = new Circle(moveTo.x, moveTo.y, 30);
        ignoreTracking = true;
        
        aura = new BaseActor(x,y,s);
        aura.loadTexture(phantomChannelAura);
        aura.addAction(Actions.fadeOut(.1f));
        aura.setWidth(this.getWidth() * 2f);
        aura.setHeight(this.getHeight() * 2f);
        
        targets = new ArrayList();
        if(Multiplayer.socket != null && Multiplayer.socket.connected() && Multiplayer.host){
            this.network_id = Integer.toString(Multiplayer.getNextID());
            JSONObject data = new JSONObject();
            try{
                data.put("id",this.network_id);
                data.put("x", x / Options.aspectRatio);
                data.put("y", y / Options.aspectRatio);
                data.put("type", type.Phantom.ordinal());
                Multiplayer.socket.emit("enemyCreated", data);
            }
            catch(Exception e){
                
            }
        }
    }
    
    @Override
    public void act(float dt){
        super.act(dt);
        if(isDying)
            return;
        aura.setPosition(this.getX() - (getWidth() / 2), this.getY()- (getHeight()/ 2));
        aura.setZIndex(this.getZIndex() - 1);
        if(attacking){
            if(isAnimationFinished() && !doneAttacking){
                if(skillToCast == 1){
                    doneAttacking = true;
                    for(BaseActor b : targets){
                        skill.cast(this, b, Skill.From.Enemy);
                    }
                }
                if(skillToCast == 2){
                    doneAttacking = true;
                    for(BaseActor b : targets){
                        float d = (float)(MathUtils.atan2(((b.getY() + (b.getHeight() / 2)) - (this.getY() + this.getHeight()) )
                        , (b.getX() + (b.getWidth() / 2)) - (this.getX() + this.getWidth())) * 180.0d / Math.PI);
                        degreesToCastSkillAt.add(d);
                        degreesToCastSkillAt.add(d + degreeVariation);
                        degreesToCastSkillAt.add(d - degreeVariation);
                    }
                    targets.clear();
                    for(float degrees : degreesToCastSkillAt){
                        channeledSkill = skill2.cast(this, new Vector2(this.getX() + (getWidth() / 2), this.getY() + (getHeight() / 2)),degrees, Skill.From.Enemy);
                    }
                    loadAnimationFromSheet(phantomChannel, 1, 6, .15f, true);
                    channeling = true;
                    degreesToCastSkillAt.clear();
                }
                canAttack = false;
            }
            if(doneAttacking ){
                if(channeling && channeledSkill != null && ((Skill)channeledSkill).isSkillComplete()){
                    attacking = false;
                    channeling = false;
                    targets.clear();
                    doneAttacking = false;
                    aura.addAction(Actions.fadeOut(1f));
                }else if(!channeling){
                    attacking = false;
                    channeling = false;
                    targets.clear();
                    doneAttacking = false;
                    
                }
                
            }
            return;
        }
        lookForTarget();
        moveTowardTarget2();
        lookForAttack();
        
        applyPhysics(dt);
    }
    
    @Override
    public void attack(BaseActor player){
        attacking = true;
        setSpeed(0);
        
        if(skillToCast == 1){
            loadAnimationFromSheet(phantomAttack, 1, 6, .2f, false);
        }
        if(skillToCast == 2){
            loadAnimationFromSheet(phantomPrepChannel, 1, 10, .15f, false);
            aura.addAction(Actions.fadeIn(2));
        }
        //setSize(size, size);
        target = player;
    }
    
    private void lookForAttack(){
        if(Multiplayer.socket != null && Multiplayer.socket.connected() && !Multiplayer.host)
            return;
        
        ArrayList<BaseActor> players = BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player");
        for(BaseActor player: players){
                if((_room != null && !_room.isActorInRoom(player)) || ((Player)player).isDead()){
                    continue;
                }
                targetInRange = true;
                targets.add(player);
                chaseTarget = false;
        }
        if(!canAttack || !targetInRange){
            targets.clear();
            return;
        }
        skillToCast = MathUtils.random(2, 2);
        attack(null);
        degreeVariation = MathUtils.random(25, 35);
        if(skillToCast == 2){
            degreesToCastSkillAt.clear();
        }
        JSONArray tars = new JSONArray();
        for(BaseActor b : targets){
            tars.put(b.network_id);
        }
        if(Multiplayer.socket != null && Multiplayer.socket.connected() && Multiplayer.host){
            JSONObject data = new JSONObject();
            try{
            //System.out.println(player.network_id);
            data.put("id", this.network_id);
            data.put("target", Multiplayer.myID);
            data.put("skillCast", skillToCast);
            data.put("degreeV", degreeVariation);
            data.put("targets", tars);
            Multiplayer.socket.emit("bossAttack", data);
            }
            catch(Exception e){
                   System.out.println("Failed to push enemy Attack: Skeleton Warrior");
            }
        }
    }
    
    private void moveTowardTarget(){
        
        setAcceleration(100);
        if(moveTo.y > getY()){
            accelerateAtAngle(90);
            currentDirection = WalkDirection.up;
        }
        if(moveTo.y < getY()){
            accelerateAtAngle(270);
            currentDirection = WalkDirection.down;
        }
        if(moveTo.x < getX()){
            accelerateAtAngle(180);
            if(currentDirection != WalkDirection.left){
                flipAnim();
            }
            currentDirection = WalkDirection.left;
        }
        if(moveTo.x > getX()){
            accelerateAtAngle(0);
            if(currentDirection != WalkDirection.right){
                flipAnim();
            }
            currentDirection = WalkDirection.right;
        }
        
        
        loadAnimationFromSheet(phantomWalk,1, 6, .3f, true);
        //setSize(size, size);
    }
    
    protected void moveTowardTarget2(){
        if(!canMove)
            return;
        setAcceleration(100 * Options.aspectRatio);
        if(Intersector.overlaps(moveToRange, getBoundaryPolygon().getBoundingRectangle())){
            return;
        }
        float degrees = (float)Math.toDegrees( MathUtils.atan2((moveTo.y - getY()), moveTo.x - getX()));
        accelerateAtAngle(degrees);
        
        
        if(degrees > 45 && degrees <= 135)
            currentDirection = WalkDirection.up;
        if(degrees > -135 && degrees <= -45)
            currentDirection = WalkDirection.down;
        if(degrees > -45 && degrees <= 45){
            if(currentDirection != WalkDirection.right){
                flipAnim();
            }
            currentDirection = WalkDirection.right;
        }
        if((degrees >= -180 && degrees <= -135) || (degrees >= 135 && degrees <= 180)){
            if(currentDirection != WalkDirection.left){
                flipAnim();
            }
            currentDirection = WalkDirection.left;
        }
            
        
        loadAnimationFromSheet(phantomWalk,1, 6, .3f, true);
        //setSize(size, size);
    }
    @Override
    public void die() {
        setSpeed(0);
        //setAnimationWithReset(dieAnim);
        //resetAnim();
        loadAnimationFromSheet(phantomDie, 1, 10, .2f, false);
        //setSize(size, size);
        
        isDying = true;
    }
    
}
