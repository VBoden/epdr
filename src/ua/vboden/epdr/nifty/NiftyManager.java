package ua.vboden.epdr.nifty;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import ua.vboden.epdr.AppContext;
import ua.vboden.epdr.AppStart;

public class NiftyManager {

	private Nifty nifty;
	private Screen screen;
	private AppContext context;
	private AppStart mainApp;

	public NiftyManager(AppContext context) {
		this.context = context;
		this.mainApp = context.getMainApp();
	}

	public void createScreen(AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer,
			ViewPort guiViewPort) {
		NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager, inputManager, audioRenderer,
				guiViewPort);
		nifty = niftyDisplay.getNifty();
		NiftyScreenController sc = new NiftyScreenController(context);

		nifty.fromXml("Interface/screen.xml", "empty", sc);
		guiViewPort.addProcessor(niftyDisplay);

		nifty.addXml("Interface/popup.xml");
		Element popupElement = nifty.createPopup("popupExit");
		Element text = popupElement.findElementById("text");
		TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
//		textRenderer.setText("ffffffffffffffffff");
		nifty.showPopup(nifty.getCurrentScreen(), popupElement.getId(), null);
		context.setPopupId(popupElement.getId());

	}

	public void showPopup(String message) {
		nifty.addXml("Interface/popup.xml");
		Element popupElement = nifty.createPopup("popupExit");
		Element text = popupElement.findElementById("text");
		TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
		textRenderer.setText(message);
		nifty.showPopup(nifty.getCurrentScreen(), popupElement.getId(), null);
		context.setPopupId(popupElement.getId());
	}

}
