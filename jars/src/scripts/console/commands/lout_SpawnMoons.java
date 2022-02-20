package scripts.console.commands;

import com.fs.starfarer.api.Global;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;
import world.procgen.lout_MoonGenerator;

public class lout_SpawnMoons implements BaseCommand {
    @Override
    public CommandResult runCommand(String args, CommandContext context) {
        if (context != CommandContext.CAMPAIGN_MAP)
        {
            Console.showMessage(CommonStrings.ERROR_CAMPAIGN_ONLY);
            return CommandResult.WRONG_CONTEXT;
        }

        new lout_MoonGenerator().generate(Global.getSector());
        Console.showMessage("Successfully ran moon generation scripts.");
        return CommandResult.SUCCESS;
    }
}
