package campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.listeners.DiscoverEntityPlugin;
import com.fs.starfarer.api.campaign.listeners.ListenerUtil;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.intel.MessageIntel;
import com.fs.starfarer.api.impl.campaign.intel.misc.WarningBeaconIntel;
import com.fs.starfarer.api.impl.campaign.procgen.SalvageEntityGenDataSpec;
import org.apache.log4j.Logger;
import world.procgen.lout_WormholeBridgeGenerator;

import java.awt.*;

/**
 * contains code copied directly from CoreDiscoverEntityPlugin, but is used to enable lout_StarDiscoverScript to
 * 'discover' a star jump point object that doesn't have a trigger to *be* discovered in the base game
 */
public class lout_DiscoverEntityPlugin implements DiscoverEntityPlugin {
    public static Logger log = Global.getLogger(lout_DiscoverEntityPlugin.class);

    public lout_DiscoverEntityPlugin() {
        log.info("Initialised entity discovery plugin...");
    }

    //code is copied directly from api CoreDiscoverEntityPlugin.java
    @Override
    public void discoverEntity(SectorEntityToken entity) {
        log.info("discovered some entity from plugin");

        entity.setDiscoverable(null);
        entity.setSensorProfile(null);

        if (entity.hasTag(Tags.WARNING_BEACON)) {
            WarningBeaconIntel intel = new WarningBeaconIntel(entity);
            Global.getSector().getIntelManager().addIntel(intel);
        } else if (entity.hasTag(lout_WormholeBridgeGenerator.WORMHOLE_TAG)) {
            Color c = Global.getSector().getPlayerFaction().getBaseUIColor();
            MessageIntel intel = new MessageIntel("Discovered: " + entity.getName(),
                    c, new String[] {entity.getName()}, c);
            intel.setSound("ui_discovered_entity");
            intel.setIcon(Global.getSettings().getSpriteName("intel", "discovered_entity"));
            Global.getSector().getCampaignUI().addMessage(intel);
        } else {
            Color c = Global.getSector().getPlayerFaction().getBaseUIColor();
            MessageIntel intel = new MessageIntel("Discovered: " + entity.getName(),
                    c, new String[] {entity.getName()}, c);
            intel.setSound("ui_discovered_entity");
            intel.setIcon(Global.getSettings().getSpriteName("intel", "discovered_entity"));
            Global.getSector().getCampaignUI().addMessage(intel);
        }



        float xp = 0;
        if (entity.hasDiscoveryXP()) {
            xp = entity.getDiscoveryXP();
        } else if (entity.getCustomEntityType() != null) {
            SalvageEntityGenDataSpec salvageSpec = (SalvageEntityGenDataSpec) Global.getSettings().getSpec(SalvageEntityGenDataSpec.class, entity.getCustomEntityType(), true);
            if (salvageSpec != null) {
                xp = salvageSpec.getXpDiscover();
            }
        }
        if (xp > 0) {
            Global.getSector().getPlayerPerson().getStats().addXP((long) xp);
        }

        ListenerUtil.reportEntityDiscovered(entity);
    }

    @Override
    public int getHandlingPriority(Object params) {
        log.info("checked plugin priority of some object " + params.getClass());

        return Integer.MAX_VALUE;
    }
}
