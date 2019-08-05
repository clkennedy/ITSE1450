/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Skills;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dslayer.content.Enemy.BaseEnemy;
import com.dslayer.content.Player.Menu.EscapeMenu;
import com.dslayer.content.Player.Player;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.options.Options;
import com.dslayer.content.projectiles.Spells.ProjectileSpell;
import com.dslayer.content.projectiles.Spells.Projectiles;
import static com.dslayer.content.projectiles.Spells.Projectiles.IceNova;

/**
 *
 * @author ARustedKnight
 */
public class IceNova extends Skill{
    
    private String ico = "Icons/IceNova.png";
    private String icoCD = "Icons/IceNovaCD.png";
    
    private float duration = 16;
    private float durationTimer = 0;
    
    private float tick = 1f;
    private float ticktimer = 2;
    
    private float radius = 15;
    
    private BaseActor caster;
    
    public IceNova(){
        super();
        skillCooldown = 27f;
        setup();
    }
    
    public IceNova(float x, float y, Stage s){
        super(x,y,s);
        skillCooldown = 32f;
        setup();
    }
    
    public void setCaster(BaseActor caster){
        this.caster = caster;
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
        
        if(!isCast && this.getStage().getCamera().frustum.pointInFrustum(this.getX(), this.getY(), 0)){
            skillSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/IceStorm.wav"));
            skillSound.play(Options.soundVolume);
            EscapeMenu.addSoundToPause(skillSound);
            skillHit = Gdx.audio.newSound(Gdx.files.internal("Sounds/BasicDamage.mp3"));
            isCast = true;
        }
        
        this.centerAtActor(caster);
        getBoundaryPolygon();
        setRotation( (getRotation() + 1 > 350)? 0 : getRotation() + 1);
        ticktimer += dt;
        if(ticktimer > tick){
            if(from == Skill.From.Enemy){
                for(BaseActor player: BaseActor.getList(this.getStage(), "com.dslayer.content.Player.Player")){
                    if(overlaps(player)){
                        ((Player)player).takeDamage((int)damage);
                        if(skillHit != null && this.getStage().getCamera().frustum.pointInFrustum(this.getX(), this.getY(), 0)){
                            skillHit.play(Options.soundVolume);
                        }
                    }
                }
            }
            if(from == Skill.From.Player){
                for(BaseActor enemy: BaseActor.getList(this.getStage(), "com.dslayer.content.Enemy.BaseEnemy")){
                    if(overlaps(enemy)){
                        ((BaseEnemy)enemy).takeDamage((int)damage, player);
                        if(skillHit != null && this.getStage().getCamera().frustum.pointInFrustum(this.getX(), this.getY(), 0)){
                            skillHit.play(Options.soundVolume);
                        }
                    }
                }
            }
            ticktimer = 0;
        }
        durationTimer += dt;
        if(durationTimer > duration){
            skillSound.stop();
            EscapeMenu.removeSoundToPause(skillSound);
            remove();
        }
        
     }
    
    @Override
    public BaseActor cast(BaseActor caster, Vector2 target, Skill.From from) {
        //float degrees = (float)(MathUtils.atan2((target.y - caster.y ), target.x - caster.x) * 180.0d / Math.PI);
                Skill b = new IceNova(caster.getX() ,caster.getY() , BaseActor.getMainStage()).isProjectile()
                        .setFrom(from);
                  ((IceNova)b).setCaster(caster);
                canCast = false;
                ((IceNova)b).caster = caster;
                if(from == Skill.From.Player){
                    ((Skill)b).player = ((Player)caster);
                }
                return b;
        }
    
    public IceNova isProjectile(){
        isAction = true;
        loadAnimationFromSheetWithTrim(IceNova, 10, 10, .05f, true, 14);
        setSize(400* Options.aspectRatio,400* Options.aspectRatio);
        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);
        setBoundaryPolygon(12);
        setRotation(MathUtils.random(360));
        setZIndex(1500);
        damage = 20;
        return this;
    }
    
    @Override
    protected void loadCDTexture() {
        //baIcon.loadTexture(icoCD);
        baIcon.loadTexture(icoCD);
        baIcon.setSize(iconSize* Options.aspectRatio, iconSize* Options.aspectRatio);
    }
    @Override
    protected void loadCastTexture() {
        //baIcon.loadTexture(ico);
        baIcon.loadTexture(ico);
        baIcon.setSize(iconSize* Options.aspectRatio, iconSize* Options.aspectRatio);
    }

    @Override
    public BaseActor cast(BaseActor arg0, Vector2 target,float degrees, From arg2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BaseActor cast(BaseActor arg0, BaseActor arg1, From arg2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
}
