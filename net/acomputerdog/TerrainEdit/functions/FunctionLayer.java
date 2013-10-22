package net.acomputerdog.TerrainEdit.functions;

import net.acomputerdog.BlazeLoader.api.block.ENotificationType;
import net.acomputerdog.BlazeLoader.api.chat.EChatColor;
import net.acomputerdog.TerrainEdit.config.Config;
import net.acomputerdog.TerrainEdit.cuboid.Cuboid;
import net.acomputerdog.TerrainEdit.cuboid.CuboidTable;
import net.acomputerdog.TerrainEdit.main.CommandTE;
import net.acomputerdog.TerrainEdit.main.ModTerrainEdit;
import net.acomputerdog.TerrainEdit.undo.UndoList;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.World;

/**
 * A function that adds a layer of blocks to the cuboid.
 */
public class FunctionLayer extends Function {
    public FunctionLayer(ModTerrainEdit baseMod, CommandTE baseCommand) {
        super(baseMod, baseCommand);
    }

    /**
     * Gets the name of the function.
     *
     * @return Return the name of the function.
     */
    @Override
    public String getFunctionName() {
        return "layer";
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
        if(args.length < 2){
            sendChatLine(user, EChatColor.COLOR_RED + "Not enough arguments!  Use /te layer <block_id> [metadata]");
        }else{
            Cuboid cuboid = CuboidTable.getCuboidForPlayer(user.getCommandSenderName());
            if(cuboid.getIsSet()){
                try{
                    int id = Integer.parseInt(args[1]);
                    int meta = 0;
                    if(args.length >= 3){
                        meta = Integer.parseInt(args[2]);
                    }
                    UndoList.createUndoTask(user.getEntityWorld(), cuboid);
                    World world = user.getEntityWorld();
                    int maxY = Math.max(cuboid.getYPos1(), cuboid.getYPos2());
                    int minY = Math.min(cuboid.getYPos1(), cuboid.getYPos2());
                    for(int x = Math.min(cuboid.getXPos1(), cuboid.getXPos2()); x <= Math.max(cuboid.getXPos1(), cuboid.getXPos2()); x++){
                        for(int z = Math.min(cuboid.getZPos1(), cuboid.getZPos2()); z <= Math.max(cuboid.getZPos1(), cuboid.getZPos2()); z++){
                            int y = getHighestBlock(world, x, z, maxY, minY - 1) + 1;
                            if(y <= maxY && y >= minY){
                                world.setBlock(x, y, z, id, meta, ENotificationType.NOTIFY_CLIENTS.getType());
                            }
                        }
                    }
                    if(Config.getConfigForPlayer(user.getCommandSenderName()).commandConfirmation){
                        sendChatLine(user, EChatColor.COLOR_YELLOW + "Done.");
                    }
                }catch(NumberFormatException e){
                    sendChatLine(user, EChatColor.COLOR_RED + "Invalid arguments!  Use Use /te layer <block_id> [metadata]");
                }catch(Exception e){
                    sendChatLine(user, EChatColor.COLOR_RED + "" + EChatColor.FORMAT_UNDERLINE + "" + EChatColor.FORMAT_BOLD + "An error occurred while layering blocks!");
                    e.printStackTrace();
                }
            }else{
                sendChatLine(user, EChatColor.COLOR_RED + "You must select a cuboid first!  Use /te p1 and /te p2!");
            }
        }
    }

    /**
     * Gets a concise description of what the function does.
     *
     * @return Return a concise description of what the function does.
     */
    @Override
    public String getFunctionDescription() {
        return "Adds a layer of blocks to the cuboid.";
    }

    public int getHighestBlock(World world, int x, int z, int maxY, int minY){
        if(maxY < minY){
            throw new IllegalArgumentException("maxY must be less than minY!");
        }else{
            for(int y = maxY; y >= minY; y--){
                if(world.getBlockId(x, y, z) != 0){
                    return y;
                }
            }
            return minY;
        }
    }
}
