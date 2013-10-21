package net.acomputerdog.TerrainEdit.undo;

import net.minecraft.src.World;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of tasks to perform when undoing actions.
 */
public class UndoList {
    private static List<UndoItem> tasks = new ArrayList<UndoItem>();
    private static int maxEntries = 5;

    public static void addTask(UndoItem task){
        tasks.add(task);
        if(tasks.size() > maxEntries){
            tasks.remove(0);
        }
    }

    public static boolean hasTask(){
        return tasks.size() > 0;
    }

    public static void undoLastTask(World world){
        if(hasTask()){
            tasks.remove(tasks.size() - 1).undo(world);
        }
    }

    public static int getMaxEntries(){
        return maxEntries;
    }

    public static void setMaxEntries(int maxEntries){
        UndoList.maxEntries = maxEntries;
    }
}
