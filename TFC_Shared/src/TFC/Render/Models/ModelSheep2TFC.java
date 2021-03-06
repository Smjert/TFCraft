package TFC.Render.Models;

import net.minecraft.client.model.ModelSheep2;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

import TFC.API.Entities.IAnimal;
import TFC.Core.TFC_Core;
public class ModelSheep2TFC extends ModelSheep2
{
	public ModelSheep2TFC()
	{
		super();
	}

	@Override
	public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);

		float percent = TFC_Core.getPercentGrown((IAnimal)entity);
		float ageScale = 2.0F-percent;
		float offset = 1.4f - percent;

		if(((IAnimal)entity).isAdult()) {
			offset = 0;
		}

		GL11.glPushMatrix();
		GL11.glTranslatef (0.0F, (0.75f-(0.75f*percent)), 0f);
		GL11.glScalef(1.0F / ageScale, 1.0F / ageScale, 1.0F / ageScale);
		//GL11.glTranslatef(0.0F, offset / ageScale, 0f);
		this.head.render(par7);
		this.body.render(par7);
		this.leg1.render(par7);
		this.leg2.render(par7);
		this.leg3.render(par7);
		this.leg4.render(par7);
		GL11.glPopMatrix();
	}
}
