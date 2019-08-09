/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Hero;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import static com.dslayer.content.Hero.ClassicHero.ClassicHero;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Skills.*;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.options.LPC;
import com.dslayer.content.options.Options;
import com.dslayer.content.projectiles.Spells.ProjectileSpell;

/**
 *
 * @author ARustedKnight
 */
public class ArcherHero extends Hero{
    
    final static public String archerHero = "Player/Archer/Arya.png";

    public ArcherHero(Animation<TextureRegion> animation) {
        super(animation);
    }
    
    public ArcherHero(){
        super();
        texture = new Texture(Gdx.files.internal(archerHero));
        
        heroName = "Ayra";
        
        walkAnimList = LPC.LoadGroupFromFullSheet(texture, LPC.LPCGroupAnims.walk, .08f);
        castAltAnimList = LPC.LoadGroupFromFullSheet(texture, LPC.LPCGroupAnims.slash, .1f);
        castBasicAnimList = LPC.LoadGroupFromFullSheet(texture, LPC.LPCGroupAnims.shoot, .03f);
        dieAnim = LPC.LoadGroupFromFullSheet(texture, LPC.LPCGroupAnims.die).get(0);
        setAnimation(playRight());
        setBoundaryRectangle();
    }

    @Override
    public void attack(float MouseWorldX,float MouseWorldY, Player player ) {
        if(basicSkill.canCast()){
            player.setCanMove(false);
            //basicSkill.cast(player, new Vector2(MouseWorldX, MouseWorldY), Skill.From.Player);
            castAnimList = castBasicAnimList;
            isAttacking = true;
            waitToCast = true;
            skillWaitedToCast = basicSkill;
            this.caster = player;
            this.target = new Vector2(MouseWorldX,MouseWorldY);
        }
    }
    @Override
    public void altAttack(float MouseWorldX,float MouseWorldY, Player player ) {
        if(altSkill.canCast()){
            //altSkill.cast(player, new Vector2(MouseWorldX, MouseWorldY), Skill.From.Player);
            player.setCanMove(false);
            isAttacking = true;
            castAnimList = castAltAnimList;
            waitToCast = true;
            skillWaitedToCast = altSkill;
            this.caster = player;
            this.target = new Vector2(MouseWorldX,MouseWorldY);
        }
        
    }

    @Override
    public void setup(Player player) {
        basicSkill = new ArrowShot();
        basicSkill.setDamage(35);
        basicSkill.setCoolDown(.5f);
        basicSkill.player = player;
        
        altSkill = new BearTrap();
        altSkill.player = player;
        //basicSkill.setIconPosition((int)(BaseActor.getUiStage().getCamera().viewportWidth /2),(int)(BaseActor.getUiStage().getCamera().viewportHeight /2));
        if(player.isLocalPlayer){
        altSkill.setupIcon((BaseActor.getUiStage().getCamera().viewportWidth /2 + 30*Options.aspectRatio) ,(20));
        basicSkill.setupIcon((BaseActor.getUiStage().getCamera().viewportWidth /2 - 30*Options.aspectRatio),(20));
        basicSkill.setIconSize(40);
        altSkill.setIconSize(40);
        }
    }
    
    
}
