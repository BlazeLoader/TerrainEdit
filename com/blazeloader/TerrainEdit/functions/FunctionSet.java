package com.blazeloader.TerrainEdit.functions;

import com.blazeloader.TerrainEdit.cuboid.Cuboid;
import com.blazeloader.TerrainEdit.cuboid.CuboidTable;
import com.blazeloader.TerrainEdit.main.BlazeModTerrainEdit;
import com.blazeloader.TerrainEdit.main.BlockAccess;
import com.blazeloader.TerrainEdit.main.CommandTE;
import com.blazeloader.TerrainEdit.undo.UndoList;
import com.blazeloader.api.api.block.ApiBlock;
import com.blazeloader.api.api.block.NotificationType;
import com.blazeloader.api.api.chat.ChatColor;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;

/**
 * Sets all the blocks in the cuboid to a particular type.
 */
public class FunctionSet extends Function {
    public FunctionSet(BlazeModTerrainEdit baseMod, CommandTE baseCommand) {
        super(baseMod, baseCommand);
        register();
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String[] getFunctionNames() {
        return new String[]{"set"};
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
        Cuboid cuboid = CuboidTable.getCuboidForPlayer(user.getName());
        if (cuboid.isSet()) {
            try {
                Block block = ApiBlock.getBlockByNameOrId(args[1]);
                int meta = 0;
                if (args.length >= 3) {
                    meta = Integer.parseInt(args[2]);
                }
                UndoList.createUndoTask(user.getEntityWorld(), cuboid);
                for (int x = Math.min(cuboid.getXPos1(), cuboid.getXPos2()); x <= Math.max(cuboid.getXPos1(), cuboid.getXPos2()); x++) {
                    for (int y = Math.min(cuboid.getYPos1(), cuboid.getYPos2()); y <= Math.max(cuboid.getYPos1(), cuboid.getYPos2()); y++) {
                        for (int z = Math.min(cuboid.getZPos1(), cuboid.getZPos2()); z <= Math.max(cuboid.getZPos1(), cuboid.getZPos2()); z++) {
                            BlockAccess.setBlockAt(user.getEntityWorld(), x, y, z, block, meta, NotificationType.NOTIFY_CLIENTS);
                        }
                    }
                }
                sendChatLine(user, ChatColor.COLOR_YELLOW + "Done.");
            } catch (NumberFormatException e) {
                sendChatLine(user, ChatColor.COLOR_RED + "Invalid arguments!  Use \"/te " + getFunctionUsage() + "\".");
            }
        } else {
            sendChatLine(user, ChatColor.COLOR_RED + "You must select a cuboid first!  Use /te p1 and /te p2!");
        }
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Sets the blocks in the cuboid.";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return PERMISSION_OP;
    }

    @Override
    public int getNumRequiredArgs() {
        return 1;
    }

    @Override
    public String getFunctionUsage() {
        return getFunctionNames()[0] + " <block> [metadata]";
    }
}
