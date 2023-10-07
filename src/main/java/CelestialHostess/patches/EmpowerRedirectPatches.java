package CelestialHostess.patches;

import CelestialHostess.vfx.CustomEmpowerEffect;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class EmpowerRedirectPatches {

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class RedirectVars {
        public static SpireField<Boolean> shouldRedirect = new SpireField<>(() -> Boolean.FALSE);
        public static SpireField<Float> xR = new SpireField<>(() -> 0f);
        public static SpireField<Float> yR = new SpireField<>(() -> 0f);
    }

    public static void setRedirect(AbstractCard card, float x, float y) {
        RedirectVars.shouldRedirect.set(card, true);
        RedirectVars.xR.set(card, x);
        RedirectVars.yR.set(card, y);
    }

    public static void setRedirect(AbstractCard card, Hitbox h) {
        setRedirect(card, h.cX, h.cY);
    }

    public static void setRedirect(AbstractCard cardToRedirect, AbstractCard cardToEmpower) {
        setRedirect(cardToRedirect, cardToEmpower.hb.cX, cardToEmpower.hb.cY);
    }

    public static void resetRedirect(AbstractCard card) {
        RedirectVars.shouldRedirect.set(card, false);
        RedirectVars.xR.set(card, 0f);
        RedirectVars.yR.set(card, 0f);
    }

    public static boolean shouldRedirect(AbstractCard card) {
        return RedirectVars.shouldRedirect.get(card);
    }

    public static float getRedirectX(AbstractCard card) {
        return RedirectVars.xR.get(card);
    }

    public static float getRedirectY(AbstractCard card) {
        return RedirectVars.yR.get(card);
    }

    public static void getNewEffect(AbstractCard card) {
        AbstractDungeon.effectList.add(new CustomEmpowerEffect(getRedirectX(card), getRedirectY(card)));
        resetRedirect(card);
    }

    @SpirePatch(clz = Soul.class, method = "empower")
    public static class EmpowerRedirect {
        public static void Postfix(Soul __instance, AbstractCard card) {
            if (shouldRedirect(card)) {
                ReflectionHacks.setPrivate(__instance, Soul.class, "target", new Vector2(getRedirectX(card), getRedirectY(card)));
                //resetRedirect(card);
            }
        }
    }

    /*@SpirePatch2(clz = Soul.class, method = "update")
    public static class DoCirclesInTheCorrectSpotPls {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr m) throws CannotCompileException {
                    if (m.getClassName().equals(EmpowerEffect.class.getName())) {
                        m.replace("if (Professor.patches.EmpowerRedirectPatches.shouldRedirect(this.card)) {" +
                                    "$1 = Professor.patches.EmpowerRedirectPatches.getRedirectX(this.card);" +
                                    "$2 = Professor.patches.EmpowerRedirectPatches.getRedirectY(this.card);" +
                                "}" +
                                "$_ = $proceed($$);");
                    }
                }
            };
        }
    }*/

    @SpirePatch2(clz = Soul.class, method = "update")
    public static class DoCirclesInTheCorrectSpotPls {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(ArrayList.class.getName()) && m.getMethodName().equals("add")) {
                        m.replace("if (Professor.patches.EmpowerRedirectPatches.shouldRedirect(this.card)) {" +
                                    "Professor.patches.EmpowerRedirectPatches.getNewEffect(this.card);" +
                                "} else {" +
                                "$_ = $proceed($$);}");
                    }
                }
            };
        }
    }

}
