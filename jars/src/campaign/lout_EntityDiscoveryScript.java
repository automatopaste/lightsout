package campaign;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.util.IntervalUtil;
import org.apache.log4j.Logger;
import world.procgen.lout_WormholeBridgeGenerator;

import java.util.List;
import java.util.Map;

public class lout_EntityDiscoveryScript extends BaseCampaignEventListener implements EveryFrameScript {
    public static final String STAR_DISCOVERY_MAP_KEY = "sstc_star_discovery";

    public static Logger log = Global.getLogger(lout_EntityDiscoveryScript.class);

    static final IntervalUtil tracker = new IntervalUtil(0.1f, 0.3f);

    public lout_EntityDiscoveryScript() {
        super(true);
    }

    //code liberated from nexerelin's SectorManager.java
    public static lout_EntityDiscoveryScript create() {
        lout_EntityDiscoveryScript script = getDiscoveryScript();
        if (script != null) {
            return script;
        }

        Map<String, Object> data = Global.getSector().getPersistentData();
        script = new lout_EntityDiscoveryScript();
        data.put(STAR_DISCOVERY_MAP_KEY, script);
        return script;
    }

    //code liberated from nexerelin's SectorManager.java
    public static lout_EntityDiscoveryScript getDiscoveryScript() {
        Map<String, Object> data = Global.getSector().getPersistentData();
        return (lout_EntityDiscoveryScript) data.get(STAR_DISCOVERY_MAP_KEY);
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {
        tracker.advance(amount);
        SectorAPI sector = Global.getSector();
        GenericPluginManagerAPI plugins = sector.getGenericPlugins();
        lout_DiscoverEntityPlugin plugin = (lout_DiscoverEntityPlugin) plugins.getPluginsOfClass(lout_DiscoverEntityPlugin.class).get(0);

        if (tracker.intervalElapsed()) {
            if (sector.getPlayerFleet().isInHyperspace()) {
                List<SectorEntityToken> entities = sector.getHyperspace().getAllEntities();

                for (SectorEntityToken entity : entities) {
                    if (entity instanceof JumpPointAPI) {
                        if (!((JumpPointAPI) entity).isStarAnchor()) {
                            continue;
                        }

                        if (entity.isDiscoverable() && entity.isVisibleToPlayerFleet()) {
                            log.info("Star made visible: " + entity.getName());

                            plugin.discoverEntity(entity);
                        }
                    }
                }
            } else {
                StarSystemAPI system = sector.getPlayerFleet().getStarSystem();
                for (SectorEntityToken token : system.getEntitiesWithTag(lout_WormholeBridgeGenerator.WORMHOLE_TAG)) {
                    if (token.isDiscoverable() && token.isVisibleToPlayerFleet()) {
                        log.info("Wormhole bridge discovered: " + token.getName());

                        plugin.discoverEntity(token);
                    }
                }
            }
        }
    }
}
