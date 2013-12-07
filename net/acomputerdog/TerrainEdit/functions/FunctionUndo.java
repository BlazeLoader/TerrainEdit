package net.acomputerdog.TerrainEdit.functions;

import net.acomputerdog.BlazeLoader.api.chat.EChatColor;
import net.acomputerdog.TerrainEdit.config.Config;
import net.acomputerdog.TerrainEdit.main.CommandTE;
import net.acomputerdog.TerrainEdit.main.ModTerrainEdit;
import net.acomputerdog.TerrainEdit.undo.UndoList;
import net.minecraft.command.ICommandSender;

/**
 * Allows the user to undo TE changes.  Not currently user-specific!
 */
public class FunctionUndo extends Function {
    public FunctionUndo(ModTerrainEdit baseMod, CommandTE baseCommand) {
        super(baseMod, baseCommand);
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String getFunctionName() {
        return "undo";
    }

    /**
     * Executes the command.
     *
     * @param user The user executing the command.
     * @param args The arguments passed to the module.
     *             -WARNING: args[0] is always the name of the sub-command!  Skip it!-
     */
    @Override
    public void execute(ICommandSender user, String[] args) {
        try{
            if(UndoList.hasTask()){
                UndoList.undoLastTask(user.getEntityWorld());
                if(Config.getConfigForPlayer(user.getCommandSenderName()).commandConfirmation){
                    sendChatLine(user, EChatColor.COLOR_YELLOW + "Done.");
                }
            }else{
                sendChatLine(user, EChatColor.COLOR_RED + "There is nothing to undo!");
            }
        }catch(Exception e){
            sendChatLine(user, EChatColor.COLOR_RED + "" + EChatColor.FORMAT_UNDERLINE + "" + EChatColor.FORMAT_BOLD + "An error occurred while undoing the previous action!!");
        }
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Undoes a previous TE action.";
    }
}
