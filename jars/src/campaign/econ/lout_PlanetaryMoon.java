package campaign.econ;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;

public class lout_PlanetaryMoon extends BaseMarketConditionPlugin {
    private static final int SIZE_CAP = Global.getSettings().getInt("lout_MoonPopCap");
    private static final float POP_PERCENT = -9999f;
    private static final float POP_SUB_PERCENT = -50f;

    @Override
    public void apply(String id) {
        super.apply(id);

        int size = this.market.getSize();
        if (size >= SIZE_CAP) this.market.addImmigrationModifier(new MoonImmigrationModifier(this.market, id, this.getName(), POP_PERCENT));
        else if (size == SIZE_CAP - 1) this.market.addImmigrationModifier(new MoonImmigrationModifier(this.market, id, this.getName(), POP_SUB_PERCENT));
    }

    private static class MoonImmigrationModifier implements MarketImmigrationModifier {
        private final MarketAPI market;
        private final String id;
        private final String name;
        private final float amount;

        public MoonImmigrationModifier(MarketAPI market, String id, String name, float amount) {
            this.market = market;
            this.id = id;
            this.name = name;
            this.amount = amount;
        }

        @Override
        public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
            if (market == this.market) incoming.getWeight().modifyPercent(id, amount, name);
        }
    }
}
