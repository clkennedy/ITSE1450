/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Skills;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Player.Player;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.projectiles.Spells.ProjectileSpell;
import com.dslayer.content.projectiles.Spells.Projectiles;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ARustedKnight
 */
public class Slash extends Skill{
    
    private String ico = "Icons/Slash.png";
    private String icoCD = "Icons/SlashCD.png";
    
    private float duration = .5f;
    private float durationTimer = 0;
    
    List<BaseActor> alreadyHit;
    
    public Slash(){
        super();
        setup();
    }
    
    public Slash(float x, float y, Stage s){
        super(x,y,s);
        setup();
    }
    
    private void setup(){
        /*setAnimation(Projectiles.getFireBallAnim());
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
        setBoundaryPolygonHalfWidth(12);
        setRotation(90);
        damage = 50;*/
    }
    @Override
    public void setupIcon(float x, float y){
        super.setupIcon(x, y);
        loadCastTexture();
    }
    public void act(float dt){
        
        super.act(dt);
        if(!isAction)
            return;
        getBoundaryPolygon();
        accelerateAtAngle(direction);
        applyPhysics(dt);
        
        if(from == Skill.From.Enemy){
            for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
                if(overlaps(player) && !alreadyHit.contains(player)){
                    ((Player)player).takeDamage((int)damage);
                    System.out.println(damage);
                    alreadyHit.add(player);
                }
            }
        }
        if(from == Skill.From.Player){
            for(BaseActor enemy: BaseActor.getList(this.getStage(), "com.dslayer.content.Enemy.BaseEnemy")){
                if(overlaps(enemy) && !alreadyHit.contains(enemy)){
                    ((BaseEnemy)enemy).takeDamage((int)damage);
                    alreadyHit.add(enemy);
                }
            }
        }
        durationTimer += dt;
        if(durationTimer > duration){
            remove();
        }
     }

    @Override
    public void cast(BaseActor caster, Vector2 target, Skill.From from) {
        float degrees = 0;
        if(!isEnemy){
            degrees= (float)(MathUtils.atan2((target.y - (caster.getY() + caster.getHeight()) )
                , target.x - (caster.getX() + caster.getWidth())) * 180.0d / Math.PI);
        }
        else{
            degrees = (float)(MathUtils.atan2((target.y - (caster.getY()) )
                , target.x - (caster.getX())) * 180.0d / Math.PI);
        }
        BaseActor b = new Slash(caster.getX() - caster.getWidth() /2,caster.getY() - caster.getHeight() /2 , 
                BaseActor.getMainStage()).isProjectile()
                .setProjectileSpeed(500).
                setProjectileRotation(degrees).
                setDirection(degrees)
                .setFrom(from).setDamage(damage);
                canCast = false;
    }
    
    public Slash isProjectile(){
        isAction = true;
        alreadyHit = new ArrayList<BaseActor>();
        setAnimation(Projectiles.getSlashAnim());
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
        setBoundaryPolygon(12);
        return this;
    }
    
    @Override
    protected void loadCDTexture() {
        //baIcon.loadTexture(icoCD);
        baIcon.setAnimation(Avatars.load(icoCD));
        baIcon.setSize(iconSize, iconSize);
    }
    @Override
    protected void loadCastTexture() {
        //baIcon.loadTexture(ico);
        baIcon.setAnimation(Avatars.load(ico));
        baIcon.setSize(iconSize, iconSize);
    }

}
