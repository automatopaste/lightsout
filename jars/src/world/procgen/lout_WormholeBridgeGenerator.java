package world.procgen;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.campaign.CampaignPlanet;
import com.fs.starfarer.campaign.CircularOrbit;
import com.fs.starfarer.campaign.NascentGravityWell;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class lout_WormholeBridgeGenerator implements SectorGeneratorPlugin {
    private static final Logger log = Global.getLogger(lout_WormholeBridgeGenerator.class);
    private static final int NUM_BRIDGES = Global.getSettings().getInt("lout_NumBridges");
    public static final String WORMHOLE_TAG = "lout_WormholeBridge";

    private Random random = new Random(Global.getSettings().hashCode());

    @Override
    public void generate(SectorAPI sector) {
        List<SectorEntityToken> wormholes = new ArrayList<>();

        log.info("Adding wormhole bridges to in-system entities...");

        List<StarSystemAPI> starSystems = sector.getStarSystems();
        Collections.shuffle(starSystems);
        List<StarSystemAPI> destSystems = sector.getStarSystems();
        Collections.shuffle(destSystems);

        int num = 1;
        for (StarSystemAPI system : starSystems) {
            if (!system.isProcgen() || system.getPlanets().isEmpty()) continue;

            PlanetAPI focus = system.getPlanets().get(random.nextInt(system.getPlanets().size()));
            float orbitRadius = (focus.getRadius() * 2f) + (random.nextFloat() * focus.getRadius());

            PlanetAPI destFocus = null;
            for (StarSystemAPI destSystem : destSystems) {
                if (!destSystem.isProcgen() || destSystem.getPlanets().isEmpty() || destSystem.equals(system)) continue;

                 destFocus = destSystem.getPlanets().get(random.nextInt(destSystem.getPlanets().size()));
                 break;
            }
            if (destFocus == null) continue;
            float destOrbitRadius = (destFocus.getRadius() * 2f) + (random.nextFloat() * destFocus.getRadius());

            JumpPointAPI point1 = Global.getFactory().createJumpPoint("lout_WormholeBridge_" + num, "Unstable Wormhole Bridge");
            OrbitAPI orbit = Global.getFactory().createCircularOrbit(destFocus, 0f, orbitRadius, orbitRadius * 0.5f);
            //point1.setAutoCreateEntranceFromHyperspace(false);
            point1.setStandardWormholeToStarOrPlanetVisual(destFocus);
            point1.addTag(WORMHOLE_TAG);
            //point1.setDiscoverable(true);
            //point1.setSensorProfile(2000f);

            JumpPointAPI.JumpDestination destination = new JumpPointAPI.JumpDestination(destFocus, "Unstable Wormhole Bridge Exit");
            point1.addDestination(destination);

            system.addEntity(point1);

            log.info("Added wormhole bridge from " + focus.getName() + " in the " + focus.getStarSystem().getName() + " system to " + destFocus.getName() + " in the " + destFocus.getStarSystem().getName());

            //JumpPointAPI destNascent = Global.getFactory().createJumpPoint("lout_WormholeBridgeExit_" + num, "Unstable Wormhole Bridge Nascent Entrance");
            //NascentGravityWellAPI destNascent = new NascentGravityWell((CampaignPlanet) destFocus,destOrbitRadius);

            num++;
            if (num > NUM_BRIDGES) {
                return;
            }
        }
    }
}
