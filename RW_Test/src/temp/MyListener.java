package helper;

import net.risingworld.api.Plugin;
import net.risingworld.api.assets.AssetBundle;
import net.risingworld.api.assets.PrefabAsset;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerKeyEvent;
import net.risingworld.api.events.player.PlayerSpawnEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.Key;
import net.risingworld.api.utils.Layer;
import net.risingworld.api.utils.Quaternion;
import net.risingworld.api.utils.RaycastResult;
import net.risingworld.api.utils.Vector3f;
import net.risingworld.api.worldelements.Prefab;

public class MyListener implements Listener {

	private Plugin plugin;
	private AssetBundle bundle;

	public MyListener(Plugin plugin) {
		this.plugin = plugin;
		String path = this.plugin.getPath();
		System.out.println("Path detected : ´" + path + "´");
		bundle = AssetBundle.loadFromFile(path + "/resources/stonewalls.bundle");	
	}

	private Prefab createStoneWallEntanceObject(Vector3f position) {
		Prefab prefab = new Prefab();
		position.y += 1;
		prefab.setPrefab(PrefabAsset.loadFromAssetBundle(bundle, "WallStoneEntrance_V1"));
		prefab.setLocalPosition(position);
		prefab.setLocalRotation(new Quaternion().fromAngles(90, 0, 0));
		float scale = 1F;
		prefab.setLocalScale(scale, scale, scale);
		return prefab;
	}

	@EventMethod
	public void keyInput(PlayerKeyEvent evt) {
		Player player = evt.getPlayer();
		if (evt.isPressed()) {
			if (evt.getKey() == Key.P) {
				player.sendTextMessage("<color=green>Wasili ... ein Ping");
				int layerMask = Layer.getBitmask(Layer.TERRAIN, Layer.OBJECT);

				player.raycast(layerMask, (RaycastResult result) -> {
					if (result != null) {
						player.sendTextMessage("<color=yellow>" + result.toString());
					}
				});
			}

			if (evt.getKey() == Key.U) {
				player.sendTextMessage("<color=blue>Wasilios ... ein Ping");
				int layerMask = Layer.getBitmask(Layer.TERRAIN);

				player.raycast(layerMask, (RaycastResult result) -> {
					if (result != null) {
						Prefab prefab = createStoneWallEntanceObject(result.getCollisionPoint());
						player.addGameObject(prefab);
						player.sendTextMessage("Entance summoned!");
					}
				});
			}
		}
	}

	@EventMethod
	public void onPlayerSpawns (PlayerSpawnEvent evt) {
		Player player = evt.getPlayer();

		player.setListenForKeyInput(true);
		player.registerKeys(Key.P, Key.U);
		player.sendTextMessage("<color=red>Willkommen SraSra");
	}

}