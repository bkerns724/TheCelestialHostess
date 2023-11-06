package CelestialHostess.cards;

import CelestialHostess.cards.abstracts.AbstractEasyCard;
import CelestialHostess.cards.tokens.FrostBlessing;
import CelestialHostess.util.Wiz;
import com.megacrit.cardcrawl.cards.purple.Vigilance;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static CelestialHostess.MainModfile.makeID;

public class SnowStorm extends AbstractEasyCard {
    public final static String ID = makeID(SnowStorm.class.getSimpleName());

    public SnowStorm() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 7;
        baseMagicNumber = magicNumber = 2;
        cardsToPreview = new FrostBlessing();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        Wiz.makeInHand(new FrostBlessing(), magicNumber);
    }

    @Override
    public void upp() {
        upgradeBlock(3);
    }

    @Override
    public String cardArtCopy() {
        return Vigilance.ID;
    }
}