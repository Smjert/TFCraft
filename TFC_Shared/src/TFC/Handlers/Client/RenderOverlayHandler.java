package TFC.Handlers.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

import TFC.Reference;
import TFC.API.TFCOptions;
import TFC.Core.TFC_Climate;
import TFC.Core.TFC_Core;
import TFC.Core.Player.PlayerInfo;
import TFC.Core.Player.PlayerManagerTFC;
import TFC.Food.FoodStatsTFC;
import TFC.Items.Tools.ItemChisel;
import TFC.Items.Tools.ItemCustomHoe;

public class RenderOverlayHandler 
{
	public static ResourceLocation tfcicons = new ResourceLocation(Reference.ModID, Reference.AssetPathGui + "icons.png");

	@ForgeSubscribe
	public void render(RenderGameOverlayEvent.Pre event)
	{
		ScaledResolution sr = event.resolution;

		int healthRowHeight = sr.getScaledHeight() - 39;
		int armorRowHeight = healthRowHeight - 10;

		//TFC_PlayerClient playerclient = ((TFC.Core.Player.TFC_PlayerClient)Minecraft.getMinecraft().thePlayer.getPlayerBase("TFC Player Client"));
		PlayerInfo playerclient = PlayerManagerTFC.getInstance().getClientPlayer();
		if(playerclient != null)
		{
			//Draw Health
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			Minecraft.getMinecraft().func_110434_K().func_110577_a(tfcicons);
			this.drawTexturedModalRect(sr.getScaledWidth() / 2-91, healthRowHeight, 0, 0, 90, 10);
			float maxHealth = Minecraft.getMinecraft().thePlayer.func_110138_aP();
			float percentHealth = Minecraft.getMinecraft().thePlayer.func_110143_aJ()/maxHealth;
			this.drawTexturedModalRect(sr.getScaledWidth() / 2-91, healthRowHeight, 0, 9, (int) (90*percentHealth), 9);

			//Draw Food and Water
			FoodStatsTFC foodstats = TFC_Core.getPlayerFoodStats(Minecraft.getMinecraft().thePlayer);
			int foodLevel = foodstats.getFoodLevel();
			int preFoodLevel = foodstats.getPrevFoodLevel();

			float waterLevel = foodstats.waterLevel;

			float percentFood = foodLevel/100f;
			float percentWater = waterLevel/foodstats.getMaxWater(Minecraft.getMinecraft().thePlayer);

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(sr.getScaledWidth() / 2, healthRowHeight, 0, 18, 90, 5);
			if(playerclient.guishowFoodRestoreAmount)
			{
				float percentFood2 = Math.min(percentFood + playerclient.guiFoodRestoreAmount/100f, 1);
				GL11.glColor4f(0.0F, 0.6F, 0.0F, 0.3F);
				this.drawTexturedModalRect(sr.getScaledWidth() / 2, healthRowHeight, 0, 23, (int) (90*(percentFood2)), 5);
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(sr.getScaledWidth() / 2, healthRowHeight, 0, 23, (int) (90*percentFood), 5);

			this.drawTexturedModalRect(sr.getScaledWidth() / 2, healthRowHeight+5, 0, 28, 90, 5);
			this.drawTexturedModalRect(sr.getScaledWidth() / 2, healthRowHeight+5, 0, 33, (int) (90*percentWater), 5);

			//Render Tool Mode
			if(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && 
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemCustomHoe)
			{
				int mode = PlayerManagerTFC.getInstance().getClientPlayer().hoeMode;
				this.drawTexturedModalRect(sr.getScaledWidth() / 2 + 95, sr.getScaledHeight() - 21, 0+(20*mode), 38, 20, 20);
			}
			else if(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && 
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemChisel)
			{
				int mode = PlayerManagerTFC.getInstance().getClientPlayer().ChiselMode;
				this.drawTexturedModalRect(sr.getScaledWidth() / 2 + 95, sr.getScaledHeight() - 21, 0+(20*mode), 58, 20, 20);
			}
		}
		Minecraft.getMinecraft().func_110434_K().func_110577_a(new ResourceLocation("minecraft:textures/gui/icons.png"));
	}

	@ForgeSubscribe
	public void render(RenderGameOverlayEvent.Post event)
	{
		if(event.type != ElementType.ALL)
		{
			return;
		}


	}

	@ForgeSubscribe
	public void renderText(RenderGameOverlayEvent.Text event)
	{
		if(Minecraft.getMinecraft().gameSettings.showDebugInfo || TFCOptions.enableDebugMode)
		{
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			int xCoord = (int)player.posX;
			int yCoord = (int)player.posY;
			int zCoord = (int)player.posZ;
			event.left.add(String.format("rain: %.0f, temp: %.2f, evt: %.3f", new Object[] {
					TFC_Climate.getRainfall(xCoord, yCoord, zCoord), 
					TFC_Climate.getHeightAdjustedTemp(xCoord, yCoord, zCoord), 
					TFC_Climate.manager.getEVTLayerAt(xCoord, zCoord).floatdata1}));

			event.left.add("Health: " + player.func_110143_aJ());
		}
	}

	public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
	{
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(par1 + 0, par2 + par6, 0.0, (par3 + 0) * f, (par4 + par6) * f1);
		tessellator.addVertexWithUV(par1 + par5, par2 + par6, 0.0, (par3 + par5) * f, (par4 + par6) * f1);
		tessellator.addVertexWithUV(par1 + par5, par2 + 0, 0.0, (par3 + par5) * f, (par4 + 0) * f1);
		tessellator.addVertexWithUV(par1 + 0, par2 + 0, 0.0, (par3 + 0) * f, (par4 + 0) * f1);
		tessellator.draw();
	}
}
