package campaign.intel;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.impl.campaign.intel.misc.BreadcrumbIntel;

public class lout_WormholeBridgeBreadcrumbIntel extends BreadcrumbIntel {
    public lout_WormholeBridgeBreadcrumbIntel(SectorEntityToken foundAt, SectorEntityToken target) {
        super(foundAt, target);

        String text;
        if (target.getStarSystem() != null) {
            text = "The wormhole bridge exits in hyperspace.";
        } else {
            text = "The wormhole bridge exits in the " + target.getStarSystem().getName() + " system.";
        }
    }
}
