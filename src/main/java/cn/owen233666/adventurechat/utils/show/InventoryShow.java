package cn.owen233666.adventurechat.utils.show;

import net.minecraft.server.level.ServerPlayer;

public class InventoryShow {
    public String replaceInvKeyString(String str, ServerPlayer player){
        if(str.contains("%i")){
            String newstr = str.replace("%i", "<clickevet:run_command:/test>");
            player.getMainHandItem();
            player.getMainHandItem().getComponents();
            player.getMainHandItem().getHoverName();
            return newstr;
        }else{
            return str;
        }
    }
}
