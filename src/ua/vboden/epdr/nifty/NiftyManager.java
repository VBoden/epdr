package ua.vboden.epdr.nifty;

import java.util.ResourceBundle;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import ua.vboden.epdr.AppContext;

public class NiftyManager {

	private Nifty nifty;
	private AppContext context;
	private AssetManager assetManager;
	private InputManager inputManager;
	private AudioRenderer audioRenderer;
	private ViewPort guiViewPort;
	private String popupId;

	public NiftyManager(AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer,
			ViewPort guiViewPort, AppContext context) {
		this.assetManager = assetManager;
		this.inputManager = inputManager;
		this.audioRenderer = audioRenderer;
		this.guiViewPort = guiViewPort;
		this.context = context;
		createScreen();
	}

	private void createScreen() {
		NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager, inputManager, audioRenderer,
				guiViewPort);
		nifty = niftyDisplay.getNifty();
		NiftyScreenController sc = new NiftyScreenController(context);

		nifty.fromXml("Interface/screen.xml", "empty", sc);
		guiViewPort.addProcessor(niftyDisplay);
		nifty.addXml("Interface/popup.xml");
	}

	public void showPopup() {
		String key = context.getBreakedRuleKey();
		if (key != null) {
			showPopupByKey(key);
			context.setBreakedRuleKey(null);
		}
	}

	private void showPopupByKey(String key) {
		ResourceBundle rb = ResourceBundle.getBundle("ua.vboden.epdr.nifty.rules_text");
		String ruleText = rb.getString(key);
		showPopup(ruleText);
	}

	private void showPopup(String message) {
		Element popupElement = nifty.createPopup("popupExit");
		Element text = popupElement.findElementById("text");
		TextRenderer textRenderer = text.getRenderer(TextRenderer.class);
		textRenderer.setText(message);
		nifty.showPopup(nifty.getCurrentScreen(), popupElement.getId(), null);
		popupId = popupElement.getId();
	}

	public String getPopupId() {
		return popupId;
	}

	public void setPopupId(String popupId) {
		this.popupId = null;
	}
}
