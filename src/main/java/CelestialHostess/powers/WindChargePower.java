package CelestialHostess.powers;

import CelestialHostess.MainModfile;
import CelestialHostess.patches.PowerOrbitPatches;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WindChargePower extends AbstractPower implements PowerOrbitPatches.OrbitPower {
    public static final String POWER_ID = MainModfile.makeID(WindChargePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static int EFFECT = 1;

    public WindChargePower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.loadRegion("blur");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            this.description = DESCRIPTIONS[0] + EFFECT + (EFFECT == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2]);
        } else {
            this.description = DESCRIPTIONS[3] + amount + DESCRIPTIONS[4] + EFFECT + (EFFECT == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2]);
        }
    }

    @Override
    public void onExhaust(AbstractCard card) {
        onSpecificTrigger();
    }

    @Override
    public void onSpecificTrigger() {
        flash();
        addToBot(new DrawCardAction(EFFECT));
        AbstractPower preserve = owner.getPower(ChargePreservationPower.POWER_ID);
        if (preserve != null && preserve.amount > 0) {
            preserve.onSpecificTrigger();
        } else {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public int orbAmount() {
        return amount;
    }

    @Override
    public Color orbColor() {
        return Color.CHARTREUSE;
    }
}