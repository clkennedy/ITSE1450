/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Hero;

import com.atkinson.game.engine.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dslayer.content.Player.Player;
import com.dslayer.content.Skills.FireBall;
import com.dslayer.content.Skills.IceNova;
import com.dslayer.content.Skills.Skill;
import com.dslayer.content.options.Avatars;
import com.dslayer.content.projectiles.Spells.ProjectileSpell;

/**
 *
 * @author ARustedKnight
 */
public class ClassicHero extends Hero{
    final static public String DefaultPlayerUP = "Player\\Deafult Player\\Character_Up.png";
    final static public String DefaultPlayerDown = "Player\\Deafult Player\\Character_Down.png";
    final static public String DefaultPlayerLeft = "Player\\Deafult Player\\Character_Left.png";
    final static public String DefaultPlayerRight = "Player\\Deafult Player\\Character_Right.png";

    public ClassicHero(Animation<TextureRegion> animation) {
        super(animation);
    }
    
    public ClassicHero(){
        super();
        Down = Avatars.load(DefaultPlayerDown, 1, 4, .2f, true);
        Up = Avatars.load(DefaultPlayerUP, 1, 4, .2f, true);
        Left = Avatars.load(DefaultPlayerLeft, 1, 4, .2f, true);
        Right = Avatars.load(DefaultPlayerRight, 1, 4, .2f, true);
        setAnimation(Right);
        setBoundaryRectangle();
        heroName = "Classic Hero";
        
    }

    @Override
    public void attack(float MouseWorldX,float MouseWorldY, Player player ) {
        if(basicSkill.canCast()){
            Vector3 mousePos = BaseActor.getMainStage().getCamera().unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));
            basicSkill.cast(player, new Vector2(mousePos.x, mousePos.y), Skill.From.Player);
        }
        
    }
    @Override
    public void altAttack(float MouseWorldX,float MouseWorldY, Player player ) {
        if(altSkill.canCast()){
            Vector3 mousePos = BaseActor.getMainStage().getCamera().unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));
            altSkill.cast(player, new Vector2(mousePos.x, mousePos.y), Skill.From.Player);
        }
        
    }

    @Override
    public void setup() {
        basicSkill = new FireBall();
        basicSkill.setupIcon((BaseActor.getUiStage().getCamera().viewportWidth /2 - 30),(20));
        basicSkill.setCoolDown(1);
        basicSkill.setIconSize(40);
        
        altSkill = new IceNova();
        altSkill.setupIcon((BaseActor.getUiStage().getCamera().viewportWidth /2 + 30),(20));
        altSkill.setIconSize(40);
        //basicSkill.setIconPosition((int)(BaseActor.getUiStage().getCamera().viewportWidth /2),(int)(BaseActor.getUiStage().getCamera().viewportHeight /2));
    
    }
    
    
}
