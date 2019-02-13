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
import com.dslayer.content.projectiles.Spells.ProjectileSpell;

/**
 *
 * @author ARustedKnight
 */
public class VikingHero extends Hero{
    final static public String DefaultPlayerUP = "Player\\Deafult Player\\Character_Up.png";
    final static public String DefaultPlayerDown = "Player\\Deafult Player\\Character_Down.png";
    final static public String DefaultPlayerLeft = "Player\\Deafult Player\\Character_Left.png";
    final static public String DefaultPlayerRight = "Player\\Deafult Player\\Character_Right.png";
    
    final static public String vikingHero = "Player/Viking/Bjorn.png";

    public VikingHero(Animation<TextureRegion> animation) {
        super(animation);
    }
    
    public VikingHero(){
        super();
        texture = new Texture(Gdx.files.internal(vikingHero));
        Down = Avatars.load(DefaultPlayerDown, 1, 4, .2f, true);
        Up = Avatars.load(DefaultPlayerUP, 1, 4, .2f, true);
        Left = Avatars.load(DefaultPlayerLeft, 1, 4, .2f, true);
        Right = Avatars.load(DefaultPlayerRight, 1, 4, .2f, true);
        setAnimation(Right);
        setBoundaryRectangle();
        heroName = "Björn";
        
        walkAnimList = LPC.LoadGroupFromFullSheet(texture, LPC.LPCGroupAnims.walk);
        castAnimList = LPC.LoadGroupFromFullSheet(texture, LPC.LPCGroupAnims.slash, .05f);
        dieAnim = LPC.LoadGroupFromFullSheet(texture, LPC.LPCGroupAnims.die).get(0);
        
    }

    @Override
    public void attack(float MouseWorldX,float MouseWorldY, Player player ) {
        if(basicSkill.canCast()){
            player.setCanMove(false);
            basicSkill.cast(player, new Vector2(MouseWorldX, MouseWorldY), Skill.From.Player);
            isAttacking = true;
            //waitToCast = true;
            //skillWaitedToCast = basicSkill;
            //this.caster = player;
            //this.target = new Vector2(MouseWorldX,MouseWorldY);
        }
    }
    @Override
    public void altAttack(float MouseWorldX,float MouseWorldY, Player player ) {
        if(altSkill.canCast()){
            altSkill.cast(player, new Vector2(MouseWorldX, MouseWorldY), Skill.From.Player);
            
        }
        
    }

    @Override
    public void setup(Player player) {
        player.setMaxHealth(120);
        player.setHealth(player.getMaxHealth());
        player.setDefaultDamageModifer(1.3f);
        basicSkill = new Slash();
        basicSkill.setDamage(35);
        basicSkill.setCoolDown(1);
        basicSkill.player = player;
        
        altSkill = new GroundSlam();
        altSkill.player = player;
        //basicSkill.setIconPosition((int)(BaseActor.getUiStage().getCamera().viewportWidth /2),(int)(BaseActor.getUiStage().getCamera().viewportHeight /2));
        if(player.isLocalPlayer){
        altSkill.setupIcon((BaseActor.getUiStage().getCamera().viewportWidth /2 + 30),(20));
        basicSkill.setupIcon((BaseActor.getUiStage().getCamera().viewportWidth /2 - 30),(20));
        basicSkill.setIconSize(40);
        altSkill.setIconSize(40);
        }
    }
    
    
}
