package plugins;

import campaign.lout_DiscoverEntityPlugin;
import campaign.lout_EntityDiscoveryScript;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import org.apache.log4j.Logger;
import world.procgen.lout_MoonGenerator;

public class lout_ModPlugin extends BaseModPlugin {
    public static Logger log = Global.getLogger(lout_ModPlugin.class);

    public static final boolean HIDE_SYSTEMS = Global.getSettings().getBoolean("lout_HideSystems");
    public static final boolean SPAWN_MOONS = Global.getSettings().getBoolean("lout_DoMoonSpawning");
//    public static final boolean SpawnBridges = Global.getSettings().getBoolean("lout_DoBridgeSpawning");

    @Override
    public void onApplicationLoad() throws ClassNotFoundException {
        try {
            Global.getSettings().getScriptClassLoader().loadClass("org.lazywizard.lazylib.ModUtils");
        } catch (ClassNotFoundException ex) {
            String message = System.lineSeparator()
                    + System.lineSeparator() + "LazyLib is required to run at least one of the mods you have installed."
                    + System.lineSeparator() + System.lineSeparator()
                    + "You can download LazyLib at http://fractalsoftworks.com/forum/index.php?topic=5444"
                    + System.lineSeparator();
            throw new ClassNotFoundException(message);
        }
    }

    @Override
    public void onNewGameAfterProcGen() {
        if (HIDE_SYSTEMS) {
            hideStarSystems();
        }

        if (SPAWN_MOONS) {
            //modify procgen systems
            new lout_MoonGenerator().generate(Global.getSector());
        }

//        if (SpawnBridges) {
//            new lout_WormholeBridgeGenerator().generate(Global.getSector());
//        }
    }

    public static void hideStarSystems() {
        for (SectorEntityToken token : Global.getSector().getHyperspace().getAllEntities()) {
            //only if the star anchor jump point is hidden for a system, will the system not appear on map.
            if (token instanceof JumpPointAPI && ((JumpPointAPI) token).isStarAnchor() && ((JumpPointAPI) token).getDestinationVisualEntity().getStarSystem().isProcgen()) {
                log.info(" - Star Jump Point - Location: " + token.getLocation().toString());
                token.setDiscoverable(true);
                token.setSensorProfile(2000f);
            }
        }
    }

    @Override
    public void onGameLoad(boolean isNewGame) {
        SectorAPI sector = Global.getSector();
        GenericPluginManagerAPI plugins = sector.getGenericPlugins();

        if (!plugins.hasPlugin(lout_DiscoverEntityPlugin.class)) {
            plugins.addPlugin(new lout_DiscoverEntityPlugin(), true);
        }

        addScripts();
    }

    public static void addScripts() {
        SectorAPI sector = Global.getSector();

        sector.addScript(lout_EntityDiscoveryScript.create());
    }
}
