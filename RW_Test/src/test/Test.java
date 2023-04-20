package test;

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

public class Test extends Plugin implements Listener {

	private AssetBundle bundle;
	public PrefabAsset StoneEntrance;
	
	@Override
	public void onEnable() {
		System.out.println("Path detected : ´" + getPath() + "´");
		bundle = AssetBundle.loadFromFile(getPath() + "/resources/stonewalls.bundle");	
		StoneEntrance = PrefabAsset.loadFromAssetBundle(bundle, "StoneEntrance");

		registerEventListener(this);
		
		System.out.println("****************************************");
		System.out.println("*** test1 - enabled                  ***");
		System.out.println("****************************************");
	}
	
	@Override
	public void onDisable() {
		// unregisterEventListener(this);
		System.out.println("t1 - disabled");

	}
	
	@EventMethod
	public void onPlayerSpawns (PlayerSpawnEvent evt) {
		Player player = evt.getPlayer();

		player.setListenForKeyInput(true);
		player.registerKeys(Key.P, Key.U);
		player.showSuccessMessageBox("<color=red>Willkommen","Dein´erstes Plugin wurde erfolgreich geladen.");
		
	}
	
	@EventMethod
	public void keyInput(PlayerKeyEvent evt) {
		Player player = evt.getPlayer();
		if (evt.isPressed()) {
			
			if (evt.getKey() == Key.P) {
				player.sendTextMessage("<color=green>Wasili ... ein Ping");
				int layerMask = Layer.getBitmask(Layer.OBJECT, Layer.TERRAIN,Layer.CONSTRUCTION);

				player.raycast(layerMask, (RaycastResult result) -> {
					if (result != null) {
						player.sendTextMessage("<color=yellow>" + result.toString());
						if (result.getLayer()==Layer.CONSTRUCTION)
						{
							player.showStatusMessage("Global ID : "+result.getObjectGlobalID(), 5);
						}
					}
				});
			}

			if (evt.getKey() == Key.U) {
				player.sendTextMessage("<color=blue>Wasilios ... Erzeuge Ein Prefab");
				int layerMask = Layer.getBitmask(Layer.TERRAIN,Layer.CONSTRUCTION);

				player.raycast(layerMask, (RaycastResult result) -> {
					if (result != null) {
						Prefab prefab = createStoneWallEntanceObject(result.getCollisionPoint());
						//GameObject go = new GameObject(Layer.CONSTRUCTION);
						
						player.addGameObject(prefab);
						player.sendTextMessage("added Prefab : "+ prefab.getID());
						//Internals.forceSyncAsset(player, StoneEntrance);
						
						player.sendTextMessage("Entrance summoned!");
					}
				});
			}
		}
	}

	private Prefab createStoneWallEntanceObject(Vector3f position) {
		Prefab prefab = new Prefab(StoneEntrance);
		position.y += 1;
		prefab.setLocalPosition(position);
		prefab.setLocalRotation(new Quaternion().fromAngles(0, 0, 0));
		prefab.setLayer (Layer.CONSTRUCTION);
		float scale = 1F;
		prefab.setLocalScale(scale, scale, scale);
		return prefab;
	}

}
