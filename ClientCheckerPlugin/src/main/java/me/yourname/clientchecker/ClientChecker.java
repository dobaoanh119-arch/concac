package me.yourname.clientchecker;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.Map;

public class ClientChecker extends JavaPlugin implements Listener, PluginMessageListener {

    private final Map<Player, String> clientBrands = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "minecraft:brand", this);

        this.getCommand("client").setExecutor((sender, command, label, args) -> {
            if (args.length == 0) {
                sender.sendMessage("§cDùng: /client <tên>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("§cNgười chơi không online.");
                return true;
            }
            String brand = getClient(target);
            sender.sendMessage("§e" + target.getName() + " dùng client: §a" + brand);
            return true;
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        // Client sẽ tự gửi brand khi join
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("minecraft:brand")) return;

        String brand = new String(message).trim();
        clientBrands.put(player, brand);
        Bukkit.getLogger().info(player.getName() + " đang dùng client: " + brand);
    }

    public String getClient(Player p) {
        return clientBrands.getOrDefault(p, "Không rõ");
    }
}
