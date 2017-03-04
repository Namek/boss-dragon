package net.bossdragon.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import net.bossdragon.component.base.Transform;
import net.bossdragon.component.base.Velocity;
import net.bossdragon.component.Player;
import net.bossdragon.enums.Tags;
import net.bossdragon.system.base.collision.messaging.CollisionEnterListener;
import net.bossdragon.system.view.render.RenderSystem;

@Wire
public class PlayerStateSystem extends EntityProcessingSystem implements CollisionEnterListener {
	ComponentMapper<Player> mPlayer;
	ComponentMapper<Transform> mTransform;
	ComponentMapper<Velocity> mVelocity;

	EntityFactorySystem entityFactorySystem;
	InputSystem inputSystem;
	RenderSystem renderSystem;
	TagManager tags;

	Input input;

	public PlayerStateSystem() {
		super(Aspect.all(Player.class));
	}

	@Override
	protected void initialize() {
		inputSystem.enableDebugCamera = false;
		input = Gdx.input;
	}

	@Override
	protected void process(Entity e) {
		Player player = mPlayer.get(e);
		Transform transform = mTransform.get(e);
		Velocity velocity = mVelocity.get(e);


	}

	@Override
	public void onCollisionEnter(int entityId, int otherEntityId) {
		Entity entity = world.getEntity(entityId);
		Entity otherEntity = world.getEntity(otherEntityId);


	}

}
