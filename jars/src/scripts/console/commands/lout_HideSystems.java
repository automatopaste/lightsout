package scripts.console.commands;

import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;
import plugins.lout_ModPlugin;

public class lout_HideSystems implements BaseCommand {
    @Override
    public CommandResult runCommand(String args, CommandContext context) {
        if (context != CommandContext.CAMPAIGN_MAP)
        {
            Console.showMessage(CommonStrings.ERROR_CAMPAIGN_ONLY);
            return CommandResult.WRONG_CONTEXT;
        }

        lout_ModPlugin.hideStarSystems();
        Console.showMessage("Successfully ran system hiding script.");
        return CommandResult.SUCCESS;
    }
}
